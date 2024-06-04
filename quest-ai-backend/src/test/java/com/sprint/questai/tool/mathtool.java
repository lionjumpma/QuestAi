package com.sprint.questai.tool;

import cn.hutool.core.math.Calculator;
import com.sprint.questai.model.enums.ChatModelEnums;
import com.sprint.questai.module.tool.MathTool;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.code.Judge0JavaScriptExecutionTool;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import org.junit.jupiter.api.Test;

public class mathtool {
    ChatLanguageModel model= ChatModelEnums.GLM4.getValue();


    MathTool.MathGenius mathGenius = AiServices.builder(MathTool.MathGenius.class)
            .chatLanguageModel(model)
            .tools(new MathTool())
            .build();
    @Test
    public void test() {
        String answer = mathGenius.ask("What is the square root of 475695037565?");
        System.out.println(answer); // The square root of 475695037565 is 689706.486532.
    }
    @Test
    public void judgeTest() {
    }
}

