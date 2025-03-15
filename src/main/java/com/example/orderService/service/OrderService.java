package com.example.orderService.service;

import com.example.orderService.models.order.Order;
import com.example.orderService.models.order.OrderStatus;
import com.example.orderService.models.order.PaymentMethod;
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
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new notFoundException("Order doesn't exist"));

        if (orderStatus == OrderStatus.IN_STORE) {
            if (order.getAmount() >= 2500 && order.getPaymentMethod() == PaymentMethod.CASH_ON_DELIVERY) {
                order.setOrderStatus(OrderStatus.RETURNED_TO_CLIENT);
                orderRepository.save(order);
                return;
            }
        }

        if (orderStatus == OrderStatus.ON_DELIVERY) {
            order.setOrderStatus(OrderStatus.DELIVERED);
            orderRepository.save(order);
            return;
        }

        order.setOrderStatus(orderStatus);
        orderRepository.save(order);
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