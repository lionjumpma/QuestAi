package com.sprint.questai.model.entity;

import lombok.Data;

@Data
public class ChunkResult {
    private String docId;
    private int chunkId;
    private String content;
}
