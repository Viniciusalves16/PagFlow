package com.example.PagFlow.component;

import com.example.PagFlow.dto.RequestTopicPayment;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class DeserializedMessage {
// This class is responsible for deserializing messages from the Kafka topic into RequestTopicPayment DTOs.
    public RequestTopicPayment deserializeRequestTopic(String message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            RequestTopicPayment payment = objectMapper.readValue(message, RequestTopicPayment.class);
            System.out.println("Deserialized DTO: " + payment);
            return payment;
        } catch (Exception e) {
            System.out.println("Failed to deserialize message: " + e.getMessage());
        }
        return null;
    }
}