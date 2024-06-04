package com.sprint.questai.tool;

import com.sprint.questai.model.enums.ChatModelEnums;
import com.sprint.questai.model.enums.LegalCaseType;
import com.sprint.questai.model.enums.LegalCategory;
import com.sprint.questai.module.tool.LegalCaseClassificier;
import com.sprint.questai.module.tool.LegalCaseTool;
import com.sprint.questai.service.Assistant;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import org.junit.jupiter.api.Test;

import java.util.List;

public class LegalCaseToolTest {
    LegalCaseTool tool = new LegalCaseTool();
    ChatLanguageModel model = ChatModelEnums.glm3.getValue();
    Assistant assistant= AiServices.builder(Assistant.class)
            .chatLanguageModel(model)
            .tools(tool)
            .build();
    @Test
    public void test() {
        LegalCaseClassificier legalCaseClassificier = AiServices.create(LegalCaseClassificier.class, model);
        //LegalCaseType legalCaseType = legalCaseClassificier.analyzeLegalCaseTypeOf("我的车被撞了，这是什么类型的案件");
        LegalCategory legalCategory = legalCaseClassificier.analyzeLegalCategoryTypeOf("我的车被撞了，这是什么类型的案件");
        System.out.println(legalCategory);
        //String chat = assistant.chat("我的车被撞了，这是什么类型的案件");
        //System.out.println(chat);
    }
}
