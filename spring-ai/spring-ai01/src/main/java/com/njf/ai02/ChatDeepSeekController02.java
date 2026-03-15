package com.njf.ai02;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author niujifei
 * @create 2026/1/7
 * @desc
 **/
@RestController
public class ChatDeepSeekController02 {

    private final ChatClient chatClient;

    public ChatDeepSeekController02(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/v1/chat")
    public String chat(@RequestParam(value = "message", defaultValue = "讲个笑话") String message) {
        try {
            return this.chatClient.prompt()
                    // 设置用户输入
                    .user(message)
                    // 调用模型
                    .call()
                    // 获取结果
                    .content();
        } catch (Exception e) {
            System.err.println("API 调用失败: " + e.getMessage());
            e.printStackTrace();
            return "请求失败: " + e.getMessage();
        }

    }
}
