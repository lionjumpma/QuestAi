package com.sprint.questai.util;


import com.sprint.questai.factory.ModelFactory;
import com.sprint.questai.model.enums.ChatModelEnums;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.spi.prompt.PromptTemplateFactory;

import javax.annotation.Resource;
/**
 * @Author: mayixiang
 * @Date: 2024-05-30
 */

public class PromptUtil {
    @Resource
    private ModelFactory factory;
    private ChatLanguageModel chatModel= ChatModelEnums.GLM4.getValue();
    public static String getHydeAnswer(String question) {
        //根据问题通过生成假设性回答
        return "Hyde Answer";
    }
}
