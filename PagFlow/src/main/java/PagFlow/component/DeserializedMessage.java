package PagFlow.component;

import PagFlow.dto.RequestTopicPayment;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DeserializedMessage {
// This class is responsible for deserializing messages from the Kafka topic into RequestTopicPayment DTOs.


    private static final Logger logger = LoggerFactory.getLogger(DeserializedMessage.class);


    public RequestTopicPayment deserializeRequestTopic(String message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            RequestTopicPayment payment = objectMapper.readValue(message, RequestTopicPayment.class);
            logger.info("Deserialized DTO: {}", payment);
            return payment;
        } catch (Exception e) {
            logger.error("Failed to deserialize message: {}", e.getMessage(), e);
        }
        return null;
    }
}