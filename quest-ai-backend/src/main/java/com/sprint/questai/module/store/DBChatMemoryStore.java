package com.sprint.questai.module.store;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;

import java.util.List;
/**
 * @Author: mayixiang
 * @Date: 2024-05-20
 */

public class DBChatMemoryStore implements ChatMemoryStore {

    @Override
    public List<ChatMessage> getMessages(Object o) {

        return null;
    }

    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> list) {

    }

    @Override
    public void deleteMessages(Object memoryId) {

    }
}
