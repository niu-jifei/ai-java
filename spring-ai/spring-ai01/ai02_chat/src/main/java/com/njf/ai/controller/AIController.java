package com.njf.ai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/ai")
public class AIController {

    @Autowired
    private ChatClient chatClient;

    /**
     * 设定系统角色
     * @param message
     * @return
     */
    @GetMapping("/chat")
    public String chat(@RequestParam(value = "msg") String message) {
        return chatClient.prompt().user(message).call().content();
    }


    /**
     * 流式聊天
     * @param message
     * @return
     */
    @GetMapping(value = "/chat/stream",produces="text/html;charset=UTF-8")
    public Flux<String> chatStream(@RequestParam(value = "msg") String message) {
        return chatClient.prompt().user(message).stream().content();
    }
}
