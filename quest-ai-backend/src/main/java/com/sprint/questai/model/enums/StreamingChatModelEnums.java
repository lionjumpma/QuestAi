package com.sprint.questai.model.enums;

import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.zhipu.ZhipuAiStreamingChatModel;
import dev.langchain4j.model.zhipu.chat.ChatCompletionModel;

public enum StreamingChatModelEnums {

    //from test to model
    GLM3("GLM3", ZhipuAiStreamingChatModel.builder()
            .apiKey(System.getenv("ZHIPU_API_KEY"))
            .logRequests(true)
            .logResponses(true)
            .build()),
    GLM4("GLM4", ZhipuAiStreamingChatModel.builder()
            .apiKey(System.getenv("ZHIPU_API_KEY"))
            .model(ChatCompletionModel.GLM_4.toString())
            .logRequests(true)
            .logResponses(true)
            .build());



    private final String text;

    private final StreamingChatLanguageModel value;

    StreamingChatModelEnums(String text, StreamingChatLanguageModel value) {
        this.text = text;
        this.value = value;
    }
    //get the value of the enum
    public static StreamingChatLanguageModel findValue(String text) {
        for (StreamingChatModelEnums e : StreamingChatModelEnums.values()) {
            if (e.text.equals(text)) {
                return e.value;
            }
        }
        return null;
    }


}
