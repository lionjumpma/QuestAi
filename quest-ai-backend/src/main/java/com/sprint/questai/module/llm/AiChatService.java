// 引入所需的包和类
package com.sprint.questai.module.llm;

import com.sprint.questai.factory.ModelFactory;
import com.sprint.questai.model.dto.request.ChatRequest;
import com.sprint.questai.model.enums.ChatModelEnums;
import com.sprint.questai.module.store.ChatMemoryStore.ChatMemoryStoreInMemory;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.model.output.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
/**
 * @Author: mayixiang
 * @Date: 2024-05-25
 */

@Service  // 标记为Spring服务组件
@Slf4j  // Lombok库提供的日志功能，自动创建log对象
public class AiChatService {
    @Autowired  // Spring自动注入相应的组件
    ModelFactory modelFactory;  // 模型工厂，用于获取不同的模型实例
    @Autowired
    ChatMemoryStoreInMemory chatMemoryStoreInMemory;  // 内存中的聊天历史存储

    // 定义一个公共方法，用于处理聊天功能，输入参数为ChatRequest对象
    public String aiChatWithLocalKeyAndMemory(ChatRequest request){
        String model = request.getModel();  // 获取请求中指定的模型名
        String content = request.getContent();  // 获取聊天内容
        String memoryId = request.getUserId() + request.getConversationId();  // 根据用户ID和对话ID生成唯一的内存ID

        // 向聊天内存中追加用户消息
        chatMemoryStoreInMemory.appendMessages(memoryId, Arrays.asList(new UserMessage(content)));

        // 通过枚举查找对应的聊天模型
        ChatLanguageModel chatModel = ChatModelEnums.findModel(model);

        Response<AiMessage> response = null;  // 初始化响应对象
        List<ChatMessage> historyMessage = chatMemoryStoreInMemory.getMessages(memoryId);  // 获取当前对话ID的历史消息列表

        // 如果找到了聊天模型，就使用该模型生成回复
        if (chatModel != null) {
            response = chatModel.generate(historyMessage);
            log.info("prompt: " + historyMessage);  // 记录日志，输出历史消息
        }else{
            log.error("model not found: " + model);  // 如果没有找到模型，记录错误日志
        }

        // 如果生成了响应，并且终止原因为STOP，那么将AI的回复也追加到聊天内存中
        if (response != null && response.finishReason().equals(FinishReason.STOP)) {
            chatMemoryStoreInMemory.appendMessages(memoryId, Arrays.asList(new AiMessage(response.content().text())));
        }

        // 返回AI的回复内容
        return response.content().text();
    }
}
