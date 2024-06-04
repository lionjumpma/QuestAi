/**
 * @Author: mayixiang
 * @Date: 2024-05-29
 *
 */
package com.sprint.questai.constant;

public enum PromptConstants {
    ANSWER_WITH_SAME_LANGUAGE("用相同语言回答"),
    DECLARE_UNKNOWN("如果你不知道，你需要说不知道");

    private String prompt;
    PromptConstants(String prompt) {
        this.prompt = prompt;
    }
}
