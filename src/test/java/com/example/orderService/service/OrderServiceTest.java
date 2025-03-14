package com.example.orderService.service;

import com.example.orderService.models.order.Order;
import com.example.orderService.models.order.OrderStatus;
import com.example.orderService.models.order.PaymentMethod;
import com.example.orderService.notFoundException.notFoundException;
import com.example.orderService.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    private Order order;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        order = new Order();
        order.setId("12345");
        order.setAmount(3000);
        order.setProductName("Laptop");
        order.setPaymentMethod(PaymentMethod.CASH_ON_DELIVERY);
        order.setOrderStatus(OrderStatus.NEW);
    }

    @Test
    public void testCreateOrder() {
        when(orderRepository.save(order)).thenReturn(order);

        Order createdOrder = orderService.createOrder(order);

        assertNotNull(createdOrder);
        assertEquals(order.getId(), createdOrder.getId());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    public void testChangeOrderStatus_Success() throws notFoundException {
        when(orderRepository.findById("12345")).thenReturn(Optional.of(order));

        orderService.changeOrderStatus("12345", OrderStatus.IN_STORE);

        assertEquals(OrderStatus.RETURNED_TO_CLIENT, order.getOrderStatus());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    public void testChangeOrderStatus_OrderNotFound() {
        when(orderRepository.findById("12345")).thenReturn(Optional.empty());

        assertThrows(notFoundException.class, () -> orderService.changeOrderStatus("12345", OrderStatus.DELIVERED));
    }

    @Test
    public void testChangeOrderStatus_OnDelivery() throws notFoundException {
        when(orderRepository.findById("12345")).thenReturn(Optional.of(order));

        orderService.changeOrderStatus("12345", OrderStatus.ON_DELIVERY);

        assertEquals(OrderStatus.DELIVERED, order.getOrderStatus());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    public void testGetAllOrders() {
        when(orderRepository.findAll()).thenReturn(List.of(order));

        var orders = orderService.getAllOrders();

        assertNotNull(orders);
        assertEquals(1, orders.size());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    public void testGetOrdersById_Success() throws notFoundException {
        when(orderRepository.findById("12345")).thenReturn(Optional.of(order));

        Order retrievedOrder = orderService.getOrdersById("12345");

        assertNotNull(retrievedOrder);
        assertEquals("12345", retrievedOrder.getId());
    }

    @Test
    public void testGetOrdersById_NotFound() {
        when(orderRepository.findById("12345")).thenReturn(Optional.empty());

        assertThrows(notFoundException.class, () -> orderService.getOrdersById("12345"));
    }

    @Test
    public void testUpdateOrder_Success() {
        Order updatedOrder = new Order();
        updatedOrder.setAmount(1500.0);
        updatedOrder.setPaymentMethod(PaymentMethod.CARD);

        when(orderRepository.findById("12345")).thenReturn(Optional.of(order));

        orderService.updateOrder("12345", updatedOrder);

        assertEquals(1500.0, order.getAmount());
        assertEquals(PaymentMethod.CARD, order.getPaymentMethod());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    public void testDeleteOrderById() {
        doNothing().when(orderRepository).deleteById("12345");

        orderService.deleteOrderById("12345");

        verify(orderRepository, times(1)).deleteById("12345");
    }
}
