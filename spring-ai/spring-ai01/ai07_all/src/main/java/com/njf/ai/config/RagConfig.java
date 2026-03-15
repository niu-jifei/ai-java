package com.njf.ai.config;

import com.njf.ai.func.RecruitServiceFunction;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.List;
import java.util.function.Function;

@Configuration
public class RagConfig {

    /**
     * 创建向量存储
     *
     * @param embeddingModel
     * @return
     */
    @Bean
    VectorStore vectorStore(EmbeddingModel embeddingModel) {
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(embeddingModel)
                .build();
        //1 提取文本内容
        String filePath = "张三简历.txt";
        TextReader textReader = new TextReader(filePath);
        textReader.getCustomMetadata().put("filePath", filePath);
        List<Document> documents = textReader.get();
        //2 文本切分段落
        TokenTextSplitter splitter =
                new TokenTextSplitter(1200,
                        350, 5,
                        100, true);
        splitter.apply(documents);
        //3 添加
        simpleVectorStore.add(documents);
        return simpleVectorStore;
    }

    @Bean
    @Description("某某是否有资格面试")
    public Function<RecruitServiceFunction.Request, RecruitServiceFunction.Response> recruitServiceFunction() {
        return new RecruitServiceFunction();
    }
}
