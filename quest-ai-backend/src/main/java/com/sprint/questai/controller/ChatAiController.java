package com.sprint.questai.controller;

import com.sprint.questai.common.BaseResponse;
import com.sprint.questai.common.ResultUtils;
import com.sprint.questai.model.dto.request.ChatRequest;

import com.sprint.questai.model.dto.response.ChatResponse;
import com.sprint.questai.module.llm.AiChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/chat")
@Slf4j
public class ChatAiController {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    @Resource
    private AiChatService aiService;
//    @GetMapping(value = "/stream", produces = "text/event-stream")
//    public SseEmitter chatStream(@RequestParam String model, @RequestParam String content) {
//        ChatRequest request = new ChatRequest();
//        request.setContent(content);
//        request.setModel(model);
//        return aiService.StreamChat(request);
//    }
//
//    @PostMapping(value = "/history")
//    public void chatHistory(@RequestBody ChatRequest request) {
//        //aiService.ChatHistory(request);
//    }
//
    @GetMapping(value = "/nokey")
    public BaseResponse<ChatResponse> chat(HttpServletRequest request,
                                           @RequestParam String model,
                                           @RequestParam String content,
                                           @RequestParam(defaultValue = "11111") String conversationId) {
        log.info(request.getSession().getAttribute("userId").toString());
        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setContent(content);
        chatRequest.setModel(model);
        chatRequest.setUserId(request.getSession().getAttribute("userId").toString());
        chatRequest.setConversationId(conversationId);
        String res = aiService.aiChatWithLocalKeyAndMemory(chatRequest);

        ChatResponse chatResponse = new ChatResponse();
        chatResponse.setMessage(res);
        return ResultUtils.success(chatResponse);
    }
}
