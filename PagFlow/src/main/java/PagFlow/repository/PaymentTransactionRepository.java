package PagFlow.repository;

import PagFlow.entities.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {
    boolean existsById(UUID id);

    Optional<PaymentTransaction> findById(UUID paymentId);
}
