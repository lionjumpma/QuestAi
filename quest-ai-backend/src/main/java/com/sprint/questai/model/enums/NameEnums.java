package com.sprint.questai.model.enums;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public enum NameEnums {
    BGE_SMALL_ZH("bge-small-zh"),
    ZHIPU_EMBEDDING_2("zhipu-embedding-2"),

    BGE_SMALL_ZH_DIM("512"),
    ZHIPU_EMBEDDING_2_DIM("1024"),

    DEFAULT_EMBEDDING_MODEL("bge-small-zh"),

    CHARACTER_DOCUMENT_SPLITTER("character-document-splitter"),
    SENTENCE_DOCUMENT_SPLITTER("sentence-document-splitter"),
    DEFAULT_DOCUMENT_SPLITTER("character-document-splitter"),

    ELASTIC_SEARCH_STORE("elastic-search-store"),
    DEFAULT_EMBEDDING_STORE("elastic-search-store"),

    // chat language model
    GLM3("glm3"),
    GLM4("glm4"),

    // chat model
    Chat_GLM_3_NAME("GLM_3_TURBO"),
    CHAT_GLM_4_NAME("GLM_4"),

    // reranker model
    BGE_RERANKER_BASE_NAME("bge-reranker-base"),

    
    CASE_RECORD_ZHIPU_INDEX("case_record_embedding_zhipu"),
    CASE_RECORD_BGE_INDEX("case_record_embedding_bge"),
    JUDGE_ACCUSATION_ZHIPU_INDEX("judge_accusation_embedding_zhipu"),
    JUDGE_ACCUSATION_BGE_INDEX("judge_accusation_embedding_bge"),
    JUDGE_REASON_ZHIPU_INDEX("judge_reason_embedding_zhipu"),
    JUDGE_REASON_BGE_INDEX("judge_reason_embedding_bge"),
    JUDGE_RESULT_ZHIPU_INDEX("judge_result_embedding_zhipu"),
    JUDGE_RESULT_BGE_INDEX("judge_result_embedding_bge"),



    CASE_RECORD_NAME("case_record"),
    JUDGE_ACCUSATION_NAME("judge_accusation"),
    JUDGE_REASON_NAME("judge_reason"),
    JUDGE_RESULT_NAME("judge_result"),
    FAKAO_NAME("fakao");
    String text;
    NameEnums(String text) {
        this.text = text;
    }
    String getEmbeddingIndexName(String name,String model){
        return name+"_embedding_"+model;
    }
    public String toString() {
        return text;
    }
    public static NameEnums fromString(String text) {
        for (NameEnums b : NameEnums.values()) {
            if (b.text.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
    //将-替换为_
    public static String replaceDash(String str){
        return str.replace("-","_");
    }
    public static String generateEmbeddingStoreName(String field,String emodel,String edb){
        String str= field+"_"+emodel+"_"+edb;
        log.info("appendEmbeddingStoreName:"+str);
        return str;
    }
    public static String generateEmbeddingIndexName(String field,String emodel){
        String str= field+"_embedding_"+replaceDash(emodel);
        log.info("generateEmbeddingIndexName:"+str);
        return str;
    }
    public static String generateEmbeddingIndexName(String field,String emodel,String uid){
        String str= field+"_embedding_"+replaceDash(emodel);
        log.info("generateEmbeddingIndexName:"+str);
        return str;
    }
    public static int getEmbeddingDimension(NameEnums emodel){
        switch (emodel) {
            case BGE_SMALL_ZH:
                return Integer.parseInt(BGE_SMALL_ZH_DIM.getText());
            case ZHIPU_EMBEDDING_2:
                return Integer.parseInt(ZHIPU_EMBEDDING_2_DIM.getText());
            default:
                return -1;
        }
    }
}
