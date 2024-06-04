package com.sprint.questai.model.enums;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.zhipu.ZhipuAiChatModel;
import dev.langchain4j.model.zhipu.chat.ChatCompletionModel;

public enum ChatModelEnums {
    GLM3("GLM3", ZhipuAiChatModel.builder()
            .apiKey(System.getenv("ZHIPU_API_KEY"))
            .model(ChatCompletionModel.GLM_3_TURBO.toString())
            .logRequests(true)
            .logResponses(true)
            .build()),
    GLM4("GLM4", ZhipuAiChatModel.builder()
            .apiKey(System.getenv("ZHIPU_API_KEY"))
            .model(ChatCompletionModel.GLM_4.toString())
            .logRequests(true)
            .logResponses(true)
            .build()),
    glm3("glm3", ZhipuAiChatModel.builder()
            .apiKey(System.getenv("ZHIPU_API_KEY"))
            .model(ChatCompletionModel.GLM_3_TURBO.toString())
            .logRequests(true)
            .logResponses(true)
            .build()),
    glm4("glm4", ZhipuAiChatModel.builder()
            .apiKey(System.getenv("ZHIPU_API_KEY"))
            .model(ChatCompletionModel.GLM_4.toString())
            .logRequests(true)
            .logResponses(true)
            .build());
    private String text;
    private ChatLanguageModel value;

    ChatModelEnums(String text, ChatLanguageModel value) {
        this.text = text;
        this.value = value;
    }
    public static ChatLanguageModel findModel(String text) {
        for (ChatModelEnums e : ChatModelEnums.values()) {
            if (e.text.equals(text)) {
                return e.value;
            }
        }
        return null;
    }
    public String getText() {
        return text;
    }
    public ChatLanguageModel getValue() {
        return value;
    }
}
