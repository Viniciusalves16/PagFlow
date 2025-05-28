package com.example.PagFlow.entities;

import com.example.PagFlow.enumeration.PaymentStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "TB_PAYMENT_TRANSACTION")
public class PaymentTransaction {

    @Id
    private Long id;

    private BigDecimal amount;
    private String method;
    private String customerName;
    private String customerEmail;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    public PaymentTransaction(String paymentId, double amount, String paymentMethod, String clientName) {
        this.id = Long.parseLong(paymentId);
        this.amount = BigDecimal.valueOf(amount);
        this.method = paymentMethod;
        this.customerName = clientName;
        this.status = PaymentStatus.PENDING;
    }

public PaymentTransaction() {
}
}

