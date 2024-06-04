package com.sprint.questai.module.llm;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.StreamingResponseHandler;
import dev.langchain4j.model.output.Response;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import static dev.langchain4j.internal.Exceptions.illegalArgument;
import static java.util.concurrent.TimeUnit.SECONDS;
/**
 * @Author: mayixiang
 * @Date: 2024-05-30
 */

@Data
public class StreamResponseHandler<T> implements StreamingResponseHandler<T> {


    private final CompletableFuture<Response<T>> futureResponse = new CompletableFuture<>();

    private final StringBuffer textContentBuilder = new StringBuffer();
    private SseEmitter emitter;
    public StreamResponseHandler() {
        this.emitter = new SseEmitter(Long.MAX_VALUE);
    }

    @Override
    public void onNext(String token) {
        System.out.println("onNext: '" + token + "'");
        textContentBuilder.append(token);
        try {
            emitter.send(token);
        } catch (Exception e) {
            emitter.completeWithError(e);
        }
    }

    @Override
    public void onComplete(Response<T> response) {
        System.out.println("onComplete: '" + response + "'");

        String expectedTextContent = textContentBuilder.toString();
        try {
            emitter.send(SseEmitter.event().name("[complete]").data("Chat stream complete"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        emitter.complete();
        if (response.content() instanceof AiMessage) {
            AiMessage aiMessage = (AiMessage) response.content();
            if (aiMessage.hasToolExecutionRequests()){
                //assertThat (aiMessage.toolExecutionRequests().size()).isGreaterThan(0);

            } else {
                //assertThat(aiMessage.text()).isEqualTo(expectedTextContent);
            }
        } else if (response.content() instanceof String) {
            //assertThat(response.content()).isEqualTo(expectedTextContent);
        } else {
            throw illegalArgument("Unknown response content: " + response.content());
        }
        futureResponse.complete(response);
    }

    @Override
    public void onError(Throwable error) {
        futureResponse.completeExceptionally(error);
    }

    @SneakyThrows
    public Response<T> get() {
        return futureResponse.get(30, SECONDS);
    }
}

