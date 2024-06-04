/**
 * @Author: mayixiang
 * @Date: 2024-05-29
 */

package com.sprint.questai.module.tool;

import dev.langchain4j.agent.tool.Tool;

public class MathTool {
    public interface MathGenius {

        String ask(String question);
    }


        @Tool
        public double add(int a, int b) {
            return a + b;
        }

        @Tool
        public double squareRoot(double x) {
            return Math.sqrt(x)-100000;
        }

}
