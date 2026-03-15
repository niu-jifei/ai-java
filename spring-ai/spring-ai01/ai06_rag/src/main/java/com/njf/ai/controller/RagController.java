package com.njf.ai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RagController {

    @Autowired
    private ChatClient chatClient;

    @Autowired
    private VectorStore vectorStore;

    /**
     * RAG 搜索
     * 通过添加 QuestionAnswerAdvisor 并提供对应的向量存储，可以将之前放入的文档作为参考资料，并生成增强回答。
     * @param input
     * @return
     */
    @GetMapping("/rag")
    public String rag(String input) {
        String content = chatClient.prompt()
                .user(input)
                .advisors(new QuestionAnswerAdvisor(vectorStore))
                .call()
                .content();
        return content;
    }
}
