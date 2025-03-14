package com.example.orderService.service;

import com.example.orderService.models.order.Order;
import com.example.orderService.models.order.OrderStatus;
import com.example.orderService.notFoundException.notFoundException;
import com.example.orderService.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    public void changeOrderStatus(String orderId, OrderStatus orderStatus) throws notFoundException {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isEmpty()){
            throw new notFoundException("Order doesnt exist");
        }
        order.ifPresent(o -> {
            o.setOrderStatus(orderStatus);
            orderRepository.save(o);
        });
        if (orderStatus == OrderStatus.ON_DELIVERY){
            order.ifPresent(o -> {
                o.setOrderStatus(OrderStatus.DELIVERED);
                orderRepository.save(o);
            });
        }
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrdersById(String orderId) throws notFoundException {
        Optional<Order> orders = orderRepository.findById(orderId);
        if (orders.isEmpty()){
            throw new notFoundException("Orders doesnt exist");
        }
        return orders.stream().toList().get(0);
    }
    public void updateOrder(String orderId, Order updatedOrder) {
        Optional<Order> order = orderRepository.findById(orderId);
        order.ifPresent(o -> {
            o.setAmount(updatedOrder.getAmount());
            o.setPaymentMethod(updatedOrder.getPaymentMethod());
            o.setDeliveryAddress(updatedOrder.getDeliveryAddress());
            orderRepository.save(o);
        });
    }

    public long countOrders() {
        return orderRepository.count();
    }

    public void deleteOrderById(String orderId){
        orderRepository.deleteById(orderId);
    }
}