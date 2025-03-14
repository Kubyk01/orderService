package com.example.orderService.service;

import com.example.orderService.models.Order;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {
    Order save(Order user);
    Order findbyid(String id);
    List<Order> getAll();
    void delete(Order order);
}
