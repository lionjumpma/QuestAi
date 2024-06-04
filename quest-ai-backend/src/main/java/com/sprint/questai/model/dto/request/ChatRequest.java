package com.sprint.questai.model.dto.request;

import lombok.Data;

@Data
public class ChatRequest {
    String userId;
    String conversationId;
    String model;
    String content;

}
