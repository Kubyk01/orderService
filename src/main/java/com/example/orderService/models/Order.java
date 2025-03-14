package com.example.orderService.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "orders")
public class Order {
    @Id
    private String id;
    private double amount;
    private String productName;
    private CustomerType customerType;
    private String deliveryAddress;
    private Date orderDate;
    private PaymentMethod paymentMethod;
}

enum PaymentMethod {
    CARD, CASH_ON_DELIVERY
}

enum CustomerType {
    FIRM, PERSON
}
