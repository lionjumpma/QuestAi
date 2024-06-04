/**
 * @Author: mayixiang
 * @Date: 2024-05-29
 */

package com.sprint.questai.module.tool;

import com.sprint.questai.model.enums.ChatModelEnums;
import com.sprint.questai.model.enums.LegalCaseType;
import com.sprint.questai.model.enums.LegalCategory;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.UserMessage;

import java.util.ArrayList;
import java.util.List;

public class LegalCaseTool {
    final String typeList = LegalCaseType.types;

//    @Tool(
//            name = "分析法律问题",
//            value = "中文法律问题或者需要法律分析"
//    )
//    public LegalCategory analyzeLegalCase(@P("用户的法律问题的类型,中文") LegalCategory type) {
//
//        System.out.println("analyzeLegalCase: " + type);
//        return type;
//    }
@Tool(
        name = "分析法律问题",
        value = "中文法律问题或者需要法律分析"
)
public LegalCaseType analyzeLegalCase(@P("用户的法律问题的类型列表，使用中文") LegalCaseType type) {

    System.out.println("analyzeLegalCase: " + type);
    return type;
}
interface  LegalCaseTypeAnalyzer{
//@UserMessage("下面是用户要问的法律问题：{{it}},分析传入案件的大类，用中文回答")
//LegalCategory analyzeLegalCategoryTypeOf(String text);
@UserMessage("下面是用户要问的法律问题：{{it}},分析传入案件的大类，用中文回答")
LegalCategory analyzeLegalCategoryTypeOf(String text);
    @UserMessage("分析案件的具体类别{{it}}，用中文回答")
    LegalCaseType analyzeLegalCaseTypeOf(String text);

}



    public static void main(String[] args) {
        LegalCaseTypeAnalyzer analyzer = AiServices.builder(LegalCaseTypeAnalyzer.class)
                .chatLanguageModel(ChatModelEnums.GLM3.getValue())
                .build();
        System.out.println(analyzer.analyzeLegalCategoryTypeOf("我的车被撞了，这是什么类型的案件"));
    }
}
