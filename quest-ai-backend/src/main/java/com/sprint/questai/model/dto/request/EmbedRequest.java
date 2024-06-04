package com.sprint.questai.model.dto.request;

import com.zhipu.oapi.service.v4.embedding.EmbeddingRequest;
import com.zhipu.oapi.service.v4.embedding.EmbeddingResult;
import lombok.Data;

@Data
public class EmbedRequest {
    EmbeddingResult embeddingResult;

}
