package com.edstem.fileconverter.controller;

import com.edstem.fileconverter.service.ProducerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/word")
public class MessageController {
    private ProducerService producer;

    public MessageController(ProducerService producer) {
        this.producer = producer;
    }

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) throws Exception {
        producer.upload(file);
        return "File upload successfully";
    }

}
