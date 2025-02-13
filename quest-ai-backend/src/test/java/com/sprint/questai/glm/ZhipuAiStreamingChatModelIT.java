//package com.sprint.questai.glm;
//
//import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.agent.tool.ToolSpecification;
//import dev.langchain4j.data.message.AiMessage;
//import dev.langchain4j.data.message.ChatMessage;
//import dev.langchain4j.data.message.ToolExecutionResultMessage;
//import dev.langchain4j.data.message.UserMessage;
//import dev.langchain4j.model.chat.TestStreamingResponseHandler;
//import dev.langchain4j.model.output.Response;
//import dev.langchain4j.model.output.TokenUsage;
//import dev.langchain4j.model.zhipu.ZhipuAiStreamingChatModel;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
//
//import java.util.List;
//
//import static dev.langchain4j.agent.tool.JsonSchemaProperty.INTEGER;
//import static dev.langchain4j.data.message.ToolExecutionResultMessage.from;
//import static dev.langchain4j.data.message.UserMessage.userMessage;
//import static dev.langchain4j.model.output.FinishReason.STOP;
//import static dev.langchain4j.model.output.FinishReason.TOOL_EXECUTION;
//import static java.util.Arrays.asList;
//import static java.util.Collections.singletonList;
//import static org.assertj.core.api.Assertions.assertThat;
//
//@EnabledIfEnvironmentVariable(named = "ZHIPU_API_KEY", matches = ".+")
//public class ZhipuAiStreamingChatModelIT {
//    private static final String apiKey = System.getenv("ZHIPU_API_KEY");
//
//    private ZhipuAiStreamingChatModel model = ZhipuAiStreamingChatModel.builder()
//            .apiKey(apiKey)
//            .logRequests(true)
//            .logResponses(true)
//            .build();
//
//    ToolSpecification calculator = ToolSpecification.builder()
//            .name("calculator")
//            .description("returns a sum of two numbers")
//            .addParameter("first", INTEGER)
//            .addParameter("second", INTEGER)
//            .build();
//
//    @Test
//    void should_stream_answer() {
//
//
//        TestStreamingResponseHandler<AiMessage> handler = new TestStreamingResponseHandler<>();
//
//        model.generate("Where is the capital of China? Please answer in English,describe it with 800 words", handler);
//
//        Response<AiMessage> response = handler.get();
//
//        assertThat(response.content().text()).containsIgnoringCase("Beijing");
//        TokenUsage tokenUsage = response.tokenUsage();
//        assertThat(tokenUsage.totalTokenCount())
//                .isEqualTo(tokenUsage.inputTokenCount() + tokenUsage.outputTokenCount());
//
//        assertThat(response.finishReason()).isEqualTo(STOP);
//    }
//
//    @Test
//    void should_execute_a_tool_then_stream_answer() {
//
//        // given
//        UserMessage userMessage = userMessage("2+2=?");
//        List<ToolSpecification> toolSpecifications = singletonList(calculator);
//
//        // when
//        TestStreamingResponseHandler<AiMessage> handler = new TestStreamingResponseHandler<>();
//
//        model.generate(singletonList(userMessage), toolSpecifications, handler);
//
//        Response<AiMessage> response = handler.get();
//        AiMessage aiMessage = response.content();
//
//        // then
//        assertThat(aiMessage.text()).isNull();
//
//        List<ToolExecutionRequest> toolExecutionRequests = aiMessage.toolExecutionRequests();
//        assertThat(toolExecutionRequests).hasSize(1);
//
//        ToolExecutionRequest toolExecutionRequest = toolExecutionRequests.get(0);
//        assertThat(toolExecutionRequest.name()).isEqualTo("calculator");
//        assertThat(toolExecutionRequest.arguments()).isEqualToIgnoringWhitespace("{\"first\": 2, \"second\": 2}");
//
//        TokenUsage tokenUsage = response.tokenUsage();
//        assertThat(tokenUsage.totalTokenCount())
//                .isEqualTo(tokenUsage.inputTokenCount() + tokenUsage.outputTokenCount());
//
//        assertThat(response.finishReason()).isEqualTo(TOOL_EXECUTION);
//
//        // given
//        ToolExecutionResultMessage toolExecutionResultMessage = from(toolExecutionRequest, "4");
//
//        List<ChatMessage> messages = asList(userMessage, aiMessage, toolExecutionResultMessage);
//
//        // when
//        TestStreamingResponseHandler<AiMessage> secondHandler = new TestStreamingResponseHandler<>();
//
//        model.generate(messages, secondHandler);
//
//        Response<AiMessage> secondResponse = secondHandler.get();
//        AiMessage secondAiMessage = secondResponse.content();
//
//        // then
//        assertThat(secondAiMessage.text()).contains("4");
//        assertThat(secondAiMessage.toolExecutionRequests()).isNull();
//
//        TokenUsage secondTokenUsage = secondResponse.tokenUsage();
//        assertThat(secondTokenUsage.totalTokenCount())
//                .isEqualTo(secondTokenUsage.inputTokenCount() + secondTokenUsage.outputTokenCount());
//
//        assertThat(secondResponse.finishReason()).isEqualTo(STOP);
//    }
//}
