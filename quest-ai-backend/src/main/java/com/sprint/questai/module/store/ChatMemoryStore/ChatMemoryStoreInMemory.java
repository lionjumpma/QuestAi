/**
 * @Author: mayixiang
 * @Date: 2024-05-29
 */

package com.sprint.questai.module.store.ChatMemoryStore;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/**
 * @Author: mayixiang
 * @Date: 2024-05-30
 */

@Component
public class ChatMemoryStoreInMemory implements ChatMemoryStore{
        private final Map<Object, List<ChatMessage>> messagesByMemoryId = new ConcurrentHashMap();

        public ChatMemoryStoreInMemory() {
        }

        public List<ChatMessage> getMessages(Object memoryId) {
            return (List)this.messagesByMemoryId.computeIfAbsent(memoryId, (ignored) -> {
                return new ArrayList();
            });
        }

        public void updateMessages(Object memoryId, List<ChatMessage> messages) {
            this.messagesByMemoryId.put(memoryId, messages);
        }
        public void appendMessages(Object memoryId, List<ChatMessage> messages) {
            List<ChatMessage> existingMessages = getMessages(memoryId);
            existingMessages.addAll(messages);
            this.messagesByMemoryId.put(memoryId, existingMessages);
        }
        public void deleteMessages(Object memoryId) {
            this.messagesByMemoryId.remove(memoryId);
        }


}
