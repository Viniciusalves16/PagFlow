package PagFlow.service;


import PagFlow.component.DeserializedMessage;
import PagFlow.dto.RequestTopicPayment;
import PagFlow.entities.PaymentTransaction;
import PagFlow.enumeration.PaymentStatus;
import PagFlow.repository.PaymentTransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class ListenerPayment {
// This service listens to the Kafka topic "Payment_Topic" and processes incoming payment messages.

    private static final Logger logger = LoggerFactory.getLogger(ListenerPayment.class);
    private final PaymentTransactionRepository paymentRepository;
    private final DeserializedMessage deserializedMessage;
    private final PaymentProcessorService paymentProcessorService;

    private final ProducerTopicContact producerTopicContact;

    public ListenerPayment(DeserializedMessage deserializedMessage,
                           PaymentTransactionRepository paymentRepository,
                           PaymentProcessorService paymentProcessorService,
                           ProducerTopicContact producerTopicContact) {
        this.deserializedMessage = deserializedMessage;
        this.paymentRepository = paymentRepository;
        this.paymentProcessorService = paymentProcessorService;
        this.producerTopicContact = producerTopicContact;
    }

    @KafkaListener(topics = "Payment_Topic", groupId = "Consumer_PagFlow_Group")
    public void listenTopic(String message) {
        logger.info("Message received from topic Payment_Topic: {}", message);

        if (message == null || message.isEmpty()) {
            logger.warn("Empty message received. Ignoring.");
            return;
        }

        RequestTopicPayment requestTopicPayment = deserializedMessage.deserializeRequestTopic(message);

        // Check if the payment transaction already exists
        if (paymentRepository.existsById(UUID.fromString(requestTopicPayment.getPaymentId()))) {
            logger.info("Payment transaction with ID {} already exists in the database.", requestTopicPayment.getPaymentId());
            return;
        }

        PaymentTransaction paymentTransaction = new PaymentTransaction(
                requestTopicPayment.getPaymentId(),
                requestTopicPayment.getAmount(),
                requestTopicPayment.getPaymentMethod(),
                requestTopicPayment.getClientName(),
                requestTopicPayment.getClientEmail()
        );

        paymentRepository.save(paymentTransaction);

        // Process the payment asynchronously and send a notification upon completion
        paymentProcessorService.processPayment(paymentTransaction.getId())
                .thenAccept(status -> {
                    if (status != null) {
                        producerTopicContact.sendPaymentToTopic(requestTopicPayment);
                        logger.info("Message sent to Contact_Topic for payment ID {} with status {}.",
                                paymentTransaction.getId(), status);
                    } else {
                        logger.warn("Failed to process payment transaction with ID {}.", paymentTransaction.getId());
                    }
                });
    }}