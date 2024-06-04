package com.sprint.questai.model.dto.response;

import lombok.Data;

/**
 * @Author: mayixiang
 * @Date: 2024/5/30 22:53
 * @Description: 用于xinference的reranker模型传递score分数
 */
@Data
public class XScoreDto {
    private int index;
    private double relevanceScore;
    private String document;
}
