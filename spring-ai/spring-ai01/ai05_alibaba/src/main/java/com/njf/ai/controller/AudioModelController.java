package com.njf.ai.controller;

import com.alibaba.cloud.ai.dashscope.audio.DashScopeSpeechSynthesisModel;
import com.alibaba.cloud.ai.dashscope.audio.DashScopeSpeechSynthesisOptions;
import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisPrompt;
import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

@RestController
public class AudioModelController {

    @Autowired
    private DashScopeSpeechSynthesisModel speechSynthesisModel;

    private static final String TEXT = "床前明月光， 疑是地上霜。 举头望明月， 低头思故乡。";

    @GetMapping("/tts")
    public void tts() throws IOException {
        // 使用构建器模式创建 DashScopeSpeechSynthesisOptions 实例并设置参数
        DashScopeSpeechSynthesisOptions options = DashScopeSpeechSynthesisOptions.builder()
                .withSpeed(1.0)        // 设置语速
                .withPitch(0.9)         // 设置音调
                .withVolume(60)         // 设置音量
                .build();
        SpeechSynthesisResponse response = speechSynthesisModel.call(new SpeechSynthesisPrompt(TEXT,options));
        
        // 获取当前类所在模块的根目录，创建 src/main/resources/tts 目录
        String classPath = AudioModelController.class.getResource("/").getPath();
        File moduleDir = new File(classPath).getParentFile().getParentFile();
        File ttsDir = new File(moduleDir, "src/main/resources/tts");
        if (!ttsDir.exists()) {
            ttsDir.mkdirs();
        }
        
        File file = new File(ttsDir, "output.mp3");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            ByteBuffer byteBuffer = response.getResult().getOutput().getAudio();
            fos.write(byteBuffer.array());
            System.out.println("文件保存路径：" + file.getAbsolutePath());
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }
}
