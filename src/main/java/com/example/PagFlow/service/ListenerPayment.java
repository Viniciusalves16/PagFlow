package com.example.PagFlow.service;

import com.example.PagFlow.component.DeserializedMessage;
import com.example.PagFlow.dto.RequestTopicPayment;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ListenerPayment {
// This service listens to the Kafka topic "Payment_Topic" and processes incoming payment messages.


    private DeserializedMessage deserializedMessage;

    public ListenerPayment(DeserializedMessage deserializedMessage) {
        this.deserializedMessage = deserializedMessage;
    }

    @KafkaListener(topics = "Payment_Topic", groupId = "Consumer_PagFlow_Group")
    public void listenTopic(String message) {
        System.out.println("Message received from topic Payment_Topic: " + message);

        if (message == null || message.isEmpty()) {
            System.out.println("Empty message received. Ignoring.");
            return;
        }

        RequestTopicPayment requestTopicPayment = deserializedMessage.deserializeRequestTopic(message);


    }
}
