package PagFlow.service;


import PagFlow.component.DeserializedMessage;
import PagFlow.dto.RequestTopicPayment;
import PagFlow.entities.PaymentTransaction;
import PagFlow.repository.PaymentTransactionRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ListenerPayment {
// This service listens to the Kafka topic "Payment_Topic" and processes incoming payment messages.

    private final PaymentTransactionRepository paymentRepository;
    private final DeserializedMessage deserializedMessage;

    public ListenerPayment(DeserializedMessage deserializedMessage, PaymentTransactionRepository paymentRepository) {
        this.deserializedMessage = deserializedMessage;
        this.paymentRepository = paymentRepository;
    }

    @KafkaListener(topics = "Payment_Topic", groupId = "Consumer_PagFlow_Group")
    public void listenTopic(String message) {
        System.out.println("Message received from topic Payment_Topic: " + message);

        if (message == null || message.isEmpty()) {
            System.out.println("Empty message received. Ignoring.");
            return;
        }

        RequestTopicPayment requestTopicPayment = deserializedMessage.deserializeRequestTopic(message);

        PaymentTransaction paymentTransaction = new PaymentTransaction(requestTopicPayment.getPaymentId(),
                requestTopicPayment.getAmount(),
                requestTopicPayment.getPaymentMethod(),
                requestTopicPayment.getClientName());

        paymentRepository.save(paymentTransaction);

        System.out.println("Payment transaction saved: " + paymentTransaction);


    }
}
