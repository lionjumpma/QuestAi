package com.sprint.questai.service;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.output.Response;

public interface Assistant {

        String  chat(String message);
        Response<AiMessage> chat(AiMessage message);
}
