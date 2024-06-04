/**
 * @Author: mayixiang
 * @Date: 2024-05-29
 */

package com.sprint.questai.module.tool;

import com.sprint.questai.model.enums.LegalCaseType;
import com.sprint.questai.model.enums.LegalCategory;
import dev.langchain4j.service.UserMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface LegalCaseClassificier {
    @UserMessage("下面是用户要问的法律问题：{{it}},该问题属于哪个大类别，只要返回类别名称")
    LegalCategory analyzeLegalCategoryTypeOf(String text);
    @UserMessage("分析案件的具体类别{{it}}，用中文回答")
    LegalCaseType analyzeLegalCaseTypeOf(String text);

}
