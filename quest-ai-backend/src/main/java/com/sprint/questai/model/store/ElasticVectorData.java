package com.sprint.questai.model.store;

import lombok.Data;

@Data
public class ElasticVectorData {
    private String chunkId;
    private String content;
    private String docId;
    private double[] vector;
}
