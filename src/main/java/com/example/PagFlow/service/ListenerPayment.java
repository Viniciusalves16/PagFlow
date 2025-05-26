package com.example.PagFlow.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ListenerPayment {

    @KafkaListener(topics = "Payment_Topic", groupId = "Consumer_PagFlow_Group")
    public void listenTopic(String message) {
        System.out.println("Mensagem recebida do tópico Payment_Topic: " + message);



    }
}
