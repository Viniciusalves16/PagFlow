package PagFlow.service;


import PagFlow.component.DeserializedMessage;
import PagFlow.dto.RequestTopicPayment;
import PagFlow.entities.PaymentTransaction;
import PagFlow.repository.PaymentTransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ListenerPayment {
// This service listens to the Kafka topic "Payment_Topic" and processes incoming payment messages.

    private static final Logger logger = LoggerFactory.getLogger(ListenerPayment.class);
    private final PaymentTransactionRepository paymentRepository;
    private final DeserializedMessage deserializedMessage;
    private final PaymentProcessorService paymentProcessorService;

    public ListenerPayment(DeserializedMessage deserializedMessage,
                           PaymentTransactionRepository paymentRepository,
                           PaymentProcessorService paymentProcessorService) {
        this.deserializedMessage = deserializedMessage;
        this.paymentRepository = paymentRepository;
        this.paymentProcessorService = paymentProcessorService;
    }

    @KafkaListener(topics = "Payment_Topic", groupId = "Consumer_PagFlow_Group")
    public void listenTopic(String message) {
        logger.info("Message received from topic Payment_Topic: {}", message);


        if (message == null || message.isEmpty()) {
            logger.warn("Empty message received. Ignoring.");
            return;
        }

        RequestTopicPayment requestTopicPayment = deserializedMessage.deserializeRequestTopic(message);

        PaymentTransaction paymentTransaction = new PaymentTransaction(requestTopicPayment.getPaymentId(),
                requestTopicPayment.getAmount(),
                requestTopicPayment.getPaymentMethod(),
                requestTopicPayment.getClientName());

        paymentRepository.save(paymentTransaction);


        if (paymentRepository.existsById(paymentTransaction.getId())) {
            logger.info("Processing payment transaction with ID {}.", paymentTransaction.getId());
            paymentProcessorService.processPayment(paymentTransaction.getId());
        } else {
            logger.info("Payment transaction with ID {} already exists in the database.", paymentTransaction.getId());
        }





    }
}
