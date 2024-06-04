/**
 * @Author: mayixiang
 * @Date: 2024-05-29
 *
 */

package com.sprint.questai.module.tool;

import dev.langchain4j.code.Judge0JavaScriptExecutionTool;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.zhipu.ZhipuAiChatModel;
import dev.langchain4j.service.AiServices;

import static java.time.Duration.ofSeconds;
/**
 * @author liu
 * @date 2024-05-29
 */
public class Judge0ToolTest {
    interface Assistant {

        String chat(String message);
    }

    public static void main(String[] args) {

        Judge0JavaScriptExecutionTool judge0Tool = new Judge0JavaScriptExecutionTool("6e3b12df0cmsh63856e762e6494bp184935jsn2e0471163cf9");

        ChatLanguageModel chatLanguageModel = ZhipuAiChatModel.builder()
                .apiKey(System.getenv("ZHIPU_API_KEY"))
//                .temperature(0.0)
                .build();

        Assistant assistant = AiServices.builder(Assistant.class)
                .chatLanguageModel(chatLanguageModel)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(20))
                .tools(judge0Tool)
                .build();

        interact(assistant, "What is the square root of 49506838032859?");
//        interact(assistant, "Capitalize every third letter: abcabc");
//        interact(assistant, "What is the number of hours between 17:00 on 21 Feb 1988 and 04:00 on 12 Apr 2014?");
    }

    private static void interact(Assistant assistant, String userMessage) {
        System.out.println("[User]: " + userMessage);
        String answer = assistant.chat(userMessage);
        System.out.println("[Assistant]: " + answer);
        System.out.println();
        System.out.println();
    }
}
