package PagFlow.entities;

import PagFlow.enumeration.PaymentStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
@Table(name = "TB_PAYMENT_TRANSACTION")
public class PaymentTransaction {

    @Id
    private UUID id;

    private BigDecimal amount;
    private String method;
    private String customerName;
    private String customerEmail;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;


    public PaymentTransaction() {
    }

    public PaymentTransaction(String paymentId, double amount, String paymentMethod, String clientName, String clientEmail) {
        this.id = UUID.fromString(paymentId);
        this.amount = BigDecimal.valueOf(amount);
        this.method = paymentMethod;
        this.customerName = clientName;
        this.status = PaymentStatus.PENDING;
        this.customerEmail = clientEmail;
    }
}