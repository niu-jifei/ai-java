package com.njf.ai01;

import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author niujifei
 * @create 2026/1/7
 * @desc
 **/
@RestController
public class ChatDeepSeekController {

    @Autowired
    private OpenAiChatModel openAiChatModel;

    @GetMapping("/v1/ai/generate")
    public String generate(@RequestParam(value = "message", defaultValue = "hello") String message) {
        try {
            String response = this.openAiChatModel.call(message);
            System.out.println("response:" + response);
            return response;
        } catch (Exception e) {
            System.err.println("API 调用失败: " + e.getMessage());
            return "请求失败: " + e.getMessage();
        }
    }
}
