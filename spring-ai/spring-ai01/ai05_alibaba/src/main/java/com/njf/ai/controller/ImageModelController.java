package com.njf.ai.controller;

import com.alibaba.cloud.ai.dashscope.api.DashScopeImageApi;
import com.alibaba.cloud.ai.dashscope.image.DashScopeImageModel;
import com.alibaba.cloud.ai.dashscope.image.DashScopeImageOptions;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;

@RestController
public class ImageModelController {

    @Autowired
    private DashScopeImageModel imageModel;

    @GetMapping("/image")
    public void getImage(@RequestParam(value = "msg",defaultValue = "生成一直小猫")
                             String msg, HttpServletResponse res) {
        ImageResponse response = imageModel.call(
                new ImagePrompt(
                        msg,
                        DashScopeImageOptions.builder()
                                .withModel(DashScopeImageApi.DEFAULT_IMAGE_MODEL)
                                .withN(1)//要生成的图像数。必须介于 1 和 10 之间。
                                .withHeight(1024)//生成的图像的高宽度。
                                .withWidth(1024).build())
        );
        //获取生成图像地址
        String imageUrl = response.getResult().getOutput().getUrl();
        try {
            //使用输出流在浏览器输出
            URL url = URI.create(imageUrl).toURL();
            InputStream in = url.openStream();
            res.setHeader("Content-Type", MediaType.IMAGE_PNG_VALUE);
            res.getOutputStream().write(in.readAllBytes());
            res.getOutputStream().flush();
        }catch (Exception e) {
        }
    }
}
