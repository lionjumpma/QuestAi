package com.sprint.questai.factory;

import com.sprint.questai.config.AppConfig;
import com.sprint.questai.model.enums.ChatModelEnums;
import com.sprint.questai.model.enums.EmbeddingModelEnums;
import com.sprint.questai.model.enums.NameEnums;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.splitter.DocumentByCharacterSplitter;
import dev.langchain4j.data.document.splitter.DocumentBySentenceSplitter;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.BgeSmallZhEmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.zhipu.ZhipuAiChatModel;
import dev.langchain4j.model.zhipu.ZhipuAiEmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.elasticsearch.ElasticsearchEmbeddingStore;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Data
@Slf4j
public class ModelFactory {
    @Resource
    private final AppConfig appConfig;

    public EmbeddingModel getEmbeddingModelFromEnums(String model){
        return EmbeddingModelEnums.findModel(model);
    }
    public ChatLanguageModel getChatModelFromEnums(String model){
        return ChatModelEnums.findModel(model);
    }

    public EmbeddingModel createEmbeddingModelByConfig() {
        AppConfig.EmbeddingModel embeddingModelConfig = appConfig.getEmbeddingModel();
        return createEmbeddingModel(embeddingModelConfig.getName(), embeddingModelConfig.getApiKey());
    }
    public ChatLanguageModel createChatLanguageModelByConfig() {
        AppConfig.EmbeddingModel embeddingModelConfig = appConfig.getEmbeddingModel();
        return createChatLanguageModel(embeddingModelConfig.getName(), embeddingModelConfig.getApiKey());
    }


    public DocumentSplitter createDocumentSplitter(String splitter) {
        if (splitter != null) {
            if (splitter.equals(NameEnums.DEFAULT_DOCUMENT_SPLITTER.getText())) {
                log.info("Using default document splitter--DocumentBySentenceSplitter");
                return new DocumentBySentenceSplitter(500, 30);
            } else if (splitter.equals(NameEnums.CHARACTER_DOCUMENT_SPLITTER.getText())) {
                log.info("Using character document splitter");
                return new DocumentByCharacterSplitter(500, 15);
            } else if (splitter.equals(NameEnums.SENTENCE_DOCUMENT_SPLITTER.getText())) {
                log.info("Using sentence document splitter");
                return new DocumentBySentenceSplitter(500, 30);
            }
        }
        return new DocumentByCharacterSplitter(500, 15);
    }
    public DocumentSplitter createDocumentSplitterByConfig() {
        return createDocumentSplitter(appConfig.getDocumentSplitter());
    }


    public EmbeddingModel createEmbeddingModel(String modelName, String apiKey) {
        NameEnums emodel = NameEnums.fromString(modelName);
        switch (emodel) {
            case ZHIPU_EMBEDDING_2:
                log.info("use Zhipu embedding-2 model");
                return ZhipuAiEmbeddingModel.builder().model(emodel.toString())
                        .apiKey(apiKey)
                        .logRequests(true)
                        .logResponses(true)
                        .build();
            case BGE_SMALL_ZH:
                log.info("use BGE_SMALL_ZH model");
                return EmbeddingModelEnums.BGE_SMALL_ZH.getValue();
            default:
                log.info("Invalid model name" + modelName + ". Using default model");
                return new BgeSmallZhEmbeddingModel();
        }
    }

    public EmbeddingStore createEmbeddingStore(String field, String modelName, String storeName) {
        NameEnums emodel = NameEnums.fromString(modelName);
        NameEnums estore = NameEnums.fromString(storeName);

        if (emodel == null || estore == null) {
            log.error("Invalid model name or store name");

            throw new IllegalArgumentException("Invalid model name or store name");
        }
        int dim =NameEnums.getEmbeddingDimension(emodel);
        String indexName = NameEnums.generateEmbeddingIndexName(field, emodel.getText());
        switch (estore) {
            case ELASTIC_SEARCH_STORE:
            default:
                return ElasticsearchEmbeddingStore.builder()
                        .serverUrl("http://localhost:9200")
                        .dimension(dim)
                        .indexName(indexName)
                        .build();
        }


    }

    public ChatLanguageModel createChatLanguageModel(String modelName, String apiKey) {
        NameEnums chatModel = NameEnums.fromString(modelName);
        if (chatModel == null) {
            log.error("Invalid chat model name");
            throw new IllegalArgumentException("Invalid chat model name");
        }
        switch (chatModel) {
            case GLM3:
                log.info("use GLM3 model");
                return ZhipuAiChatModel.builder()
                        .apiKey(apiKey)
                        .logRequests(true)
                        .logResponses(true)
                        .build();
            case GLM4:
                log.info("use GLM4 model");
                return ZhipuAiChatModel.builder()
                        .apiKey(apiKey)
                        .logRequests(true)
                        .logResponses(true)
                        .build();
            default:
                log.error("Invalid chat model name");
                throw new IllegalArgumentException("Invalid chat model name");


        }
    }

    public ChatLanguageModel createChatLanguageModel(String modelName) {
        NameEnums chatModel = NameEnums.fromString(modelName);
        String apiKey = System.getenv("ZHIPU_API_KEY");
        if (chatModel == null) {
            log.error("Invalid chat model name");
            throw new IllegalArgumentException("Invalid chat model name");
        }
        switch (chatModel) {
            case GLM3:
                log.info("use GLM3 model");
                return ZhipuAiChatModel.builder()
                        .model(ChatModelEnums.GLM3.getText())
                        .apiKey(apiKey)
                        .logRequests(true)
                        .logResponses(true)
                        .build();
            case GLM4:
                log.info("use GLM4 model");
                return ZhipuAiChatModel.builder()
                        .apiKey(apiKey)
                        .logRequests(true)
                        .logResponses(true)
                        .build();
            default:
                log.error("Invalid chat model name");
                throw new IllegalArgumentException("Invalid chat model name");
        }
    }

    public EmbeddingStoreIngestor createEmbeddingStoreIngestor(String field, String emodelName, String storeName,String splitter) {
        EmbeddingStore store = createEmbeddingStore(field, emodelName, storeName);
        DocumentSplitter documentSplitter = createDocumentSplitter(splitter);
        EmbeddingModel embeddingModel = getEmbeddingModelFromEnums(emodelName);
        return EmbeddingStoreIngestor.builder()
                .embeddingStore(store)
                .documentSplitter(documentSplitter)
                .embeddingModel(embeddingModel)
                .build();
    }
}
