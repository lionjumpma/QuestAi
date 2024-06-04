package com.sprint.questai.model.enums;


import dev.langchain4j.model.embedding.BgeSmallZhEmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.zhipu.ZhipuAiEmbeddingModel;

public enum EmbeddingModelEnums {
    ZHIPU_EMBEDDING_MODEL(NameEnums.ZHIPU_EMBEDDING_2.getText(), ZhipuAiEmbeddingModel.builder()
            .apiKey(System.getenv("ZHIPU_API_KEY"))
            .model(dev.langchain4j.model.zhipu.embedding.EmbeddingModel.EMBEDDING_2.toString())
            .logRequests(true)
            .logResponses(true)
            .build()),
    BGE_SMALL_ZH(NameEnums.BGE_SMALL_ZH.text, new BgeSmallZhEmbeddingModel());
    private final String text;
    private final EmbeddingModel value;
    public String getText() {
        return text;
    }
    public EmbeddingModel getValue() {
        return value;
    }
    EmbeddingModelEnums(String text, EmbeddingModel value) {
        this.text = text;
        this.value = value;
    }
    public static EmbeddingModel findModel(String text) {
        for (EmbeddingModelEnums e : EmbeddingModelEnums.values()) {
            if (e.text.equals(text)) {
                return e.value;
            }
        }
        return null;
    }
}
