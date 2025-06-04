package PagFlow.service;

import PagFlow.entities.PaymentTransaction;
import PagFlow.enumeration.PaymentStatus;
import PagFlow.repository.PaymentTransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Service
public class PaymentProcessorService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentProcessorService.class);
    private final PaymentTransactionRepository paymentRepository;

    public PaymentProcessorService(PaymentTransactionRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Async
    public CompletableFuture<PaymentStatus> processPayment(UUID paymentId) {
        try {
            // Simulate processing delay
            TimeUnit.SECONDS.sleep(10);

            Optional<PaymentTransaction> optionalTransaction = paymentRepository.findById(paymentId);

            if (optionalTransaction.isPresent()) {
                PaymentTransaction transaction = optionalTransaction.get();

                // Simulate payment success or failure
                boolean isPaymentSuccessful = ThreadLocalRandom.current().nextBoolean();
                PaymentStatus status = isPaymentSuccessful ? PaymentStatus.COMPLETED : PaymentStatus.FAILED;
                transaction.setStatus(status);
                paymentRepository.save(transaction);

                logger.info("Payment transaction {} processed with status: {}.", paymentId, status);
                return CompletableFuture.completedFuture(status);
            } else {
                logger.warn("Payment transaction with ID {} not found.", paymentId);
            }
        } catch (InterruptedException e) {
            logger.error("Error while processing payment transaction {}.", paymentId, e);
            Thread.currentThread().interrupt();
        }
        return CompletableFuture.completedFuture(null);
    }
}