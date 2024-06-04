/**
 * @Author: mayixiang
 * @Date: 2024-05-29
 */

package com.sprint.questai.module.tool;

import com.sprint.questai.model.enums.ChatModelEnums;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.UserMessage;

public class se {
    enum Sentiment {
        POSITIVE, NEUTRAL, NEGATIVE;
        public String toString() {
            return name().toLowerCase();
        }
    }

    interface SentimentAnalyzer {

        @UserMessage("Analyze sentiment of {{it}}")
        Sentiment analyzeSentimentOf(String text);

        @UserMessage("Does {{it}} has a positive sentiment?")
        boolean isPositive(String text);
    }
    static ChatLanguageModel model= ChatModelEnums.GLM3.getValue();

    static SentimentAnalyzer sentimentAnalyzer = AiServices.create(SentimentAnalyzer.class, model);

    public static void main(String[] args) {
        Sentiment sentiment = sentimentAnalyzer.analyzeSentimentOf("This is great!");
        System.out.println(sentiment);
// POSITIVE

        boolean positive = sentimentAnalyzer.isPositive("It's awful!");
        System.out.println(positive);
    }

// false
}
