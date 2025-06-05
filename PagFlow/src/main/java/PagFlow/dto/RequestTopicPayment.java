package PagFlow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestTopicPayment {
    @JsonProperty("paymentId")
    private String paymentId;

    @JsonProperty("amount")
    private double amount;

    @JsonProperty("paymentMethod")
    private String paymentMethod;

    @JsonProperty("clientName")
    private String clientName;

    @JsonProperty("email")
    private String clientEmail;

    public RequestTopicPayment() {}

    public RequestTopicPayment(String paymentId, double amount, String paymentMethod, String clientName) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.clientName = clientName;
    }
}