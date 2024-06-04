/**
 * @Author: chenzehao
 * @Date: 2024-05-29
 *
 */
package com.sprint.questai.memoryStore;

import dev.langchain4j.store.memory.chat.InMemoryChatMemoryStore;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MemoryStoreTest {
    @Test
    public void memoryStoreTest() {
        InMemoryChatMemoryStore memoryStore = new InMemoryChatMemoryStore();
//        memoryStore.updateMessages("id1", );

    }

}
