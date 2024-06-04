/**
 * @Author: mayixiang
 * @Date: 2024-05-29
 *
 */

package com.sprint.questai.config;

import com.sprint.questai.model.enums.NameEnums;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "app")
public class AppConfig {
    private EmbeddingModel embeddingModel=new EmbeddingModel();
    private ChatModel chatModel=new ChatModel();
    private String documentSplitter=NameEnums.DEFAULT_DOCUMENT_SPLITTER.getText();
    private String embeddingStore=NameEnums.DEFAULT_EMBEDDING_STORE.getText();

    @Data
    public static class EmbeddingModel {
        private String name= NameEnums.BGE_SMALL_ZH.getText();
        private String apiKey=System.getenv("ZHIPU_API_KEY");
    }
    @Data
    public static class ChatModel {
        private String name= NameEnums.Chat_GLM_3_NAME.getText();
        private String apiKey=System.getenv("ZHIPU_API_KEY");
    }
}
