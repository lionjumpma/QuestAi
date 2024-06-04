/**
 * @Author: mayixiang
 * @Date: 2024-05-29
 *
 */

package com.sprint.questai.common;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.output.TokenUsage;
import lombok.Data;

import java.io.Serializable;

//将Response对象转换为可转换的对象
@Data
public class ConvertableResponse implements Serializable {
    //通过Response对象构造可转换的对象
    private final AiMessage content;
    private final TokenUsage tokenUsage;
    private final FinishReason finishReason;
    public ConvertableResponse(Response<AiMessage> response) {
        this.content = response.content();
        this.tokenUsage = response.tokenUsage();
        this.finishReason = response.finishReason();
    }


}
