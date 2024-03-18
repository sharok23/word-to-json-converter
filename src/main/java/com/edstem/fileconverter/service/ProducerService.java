package com.edstem.fileconverter.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.Producer;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;


@Service
public class ProducerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(Producer.class);
    private KafkaTemplate<String, String> kafkaTemplate;

    public ProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    public void upload(MultipartFile file) throws Exception {
        XWPFDocument document = new XWPFDocument(file.getInputStream());
        XWPFWordExtractor extractor = new XWPFWordExtractor(document);

        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("text", extractor.getText());

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(jsonMap);
        LOGGER.info("JSON message to be sent -> {}", json);

        Message<String> message = MessageBuilder
                .withPayload(json)
                .setHeader(KafkaHeaders.TOPIC, "file-converter")
                .build();

        kafkaTemplate.send(message);
    }

}
