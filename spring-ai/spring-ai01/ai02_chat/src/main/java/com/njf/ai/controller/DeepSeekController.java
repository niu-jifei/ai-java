package com.njf.ai.controller;

import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class DeepSeekController {

    @Autowired
    private ChatModel chatModel;

    @GetMapping
    public String chat(@RequestParam("msg") String msg) {
        return chatModel.call(msg);
    }

    /**
     * 调用ChatModel接口
     *
     * @param msg
     * @return
     */
    @GetMapping("/openai")
    public String openai(@RequestParam("msg") String msg) {
        ChatResponse call = chatModel.call(
                new Prompt(
                        msg,
                        OpenAiChatOptions.builder()
                                //可以更换成其他大模型，如Anthropic3ChatOptions亚马逊
                                .model("deepseek-chat")
                                .temperature(0.8)
                                .build()
                )
        );
        return call.getResult().getOutput().getContent();
    }


    /**
     * 使用Prompt
     * @param name
     * @param voice
     * @return
     */
    @GetMapping("/prompt")
    public String prompt(@RequestParam("name") String name,
                         @RequestParam("voice") String voice) {
        String userText = """
                给我推荐北京的至少三种美食
                """;
        UserMessage userMessage = new UserMessage(userText);
        String systemText = """
                你是一个美食咨询助手，可以帮助人们查询美食信息。
                你的名字是{name},
                你应该用你的名字和{voice}的饮食习惯回复用户的请求。
                """;
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemText);

        //替换占位符
        Message systemMessage = systemPromptTemplate.createMessage(Map.of("name", name, "voice", voice));
        Prompt prompt = new Prompt(List.of(userMessage, systemMessage));

        List<Generation> results = chatModel.call(prompt).getResults();
        return results.stream().map(x -> x.getOutput().getContent()).collect(Collectors.joining(""));
    }
}
