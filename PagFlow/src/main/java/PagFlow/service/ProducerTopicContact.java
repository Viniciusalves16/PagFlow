package PagFlow.service;

import PagFlow.dto.RequestTopicPayment;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProducerTopicContact {
// This service is responsible for sending payment requests to the "Contact_Topic" Kafka topic.
    private final KafkaTemplate<String, RequestTopicPayment> kafkaTemplate;

    public ProducerTopicContact(KafkaTemplate<String, RequestTopicPayment> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    public void sendPaymentToTopic(RequestTopicPayment request) {
        kafkaTemplate.send("Contact_Topic", request);
    }
}
