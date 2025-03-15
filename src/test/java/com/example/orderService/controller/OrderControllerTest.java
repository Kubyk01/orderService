package com.example.orderService.controller;

import com.example.orderService.contoller.OrderController;
import com.example.orderService.models.order.Order;
import com.example.orderService.models.order.OrderStatus;
import com.example.orderService.notFoundException.notFoundException;
import com.example.orderService.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private MockMvc mockMvc;

    private Order order;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
        order = new Order();
        order.setId("12345");
        order.setAmount(100.0);
        order.setProductName("Laptop");
        order.setDeliveryAddress("Warsaw");
        order.setOrderStatus(OrderStatus.NEW);
        order.setDeliveryAddress("address");
    }

    @Test
    public void testCreateOrder() throws Exception {
        when(orderService.createOrder(any(Order.class))).thenReturn(order);

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerType\":\"PERSON\",\"amount\":100.0,\"productName\":\"Laptop\",\"deliveryAddress\":\"Warsaw\",\"orderStatus\":\"NEW\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("12345"))
                .andExpect(jsonPath("$.amount").value(100.0))
                .andExpect(jsonPath("$.productName").value("Laptop"))
                .andExpect(jsonPath("$.orderStatus").value("NEW"));

        verify(orderService, times(1)).createOrder(any(Order.class));
    }

    @Test
    public void testGetOrderById_Success() throws Exception {
        when(orderService.getOrdersById("12345")).thenReturn(order);

        mockMvc.perform(get("/orders/12345"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("12345"))
                .andExpect(jsonPath("$.amount").value(100.0));

        verify(orderService, times(1)).getOrdersById("12345");
    }

    @Test
    public void testUpdateOrderStatus_Success() throws Exception {
        doNothing().when(orderService).changeOrderStatus("12345", OrderStatus.DELIVERED);

        mockMvc.perform(put("/orders/12345/status?status=DELIVERED"))
                .andExpect(status().isOk())
                .andExpect(content().string("Order status updated successfully."));

        verify(orderService, times(1)).changeOrderStatus("12345", OrderStatus.DELIVERED);
    }

    @Test
    public void testUpdateOrderStatus_Failure() throws Exception {
        doThrow(new notFoundException("Order doesn't exist")).when(orderService).changeOrderStatus("12345", OrderStatus.DELIVERED);

        mockMvc.perform(put("/orders/12345/status?status=DELIVERED"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error: Order doesn't exist"));

        verify(orderService, times(1)).changeOrderStatus("12345", OrderStatus.DELIVERED);
    }

    @Test
    public void testUpdateOrder_Success() throws Exception {
        Order updatedOrder = new Order();
        updatedOrder.setAmount(150.0);
        updatedOrder.setProductName("Updated Laptop");

        doNothing().when(orderService).updateOrder(eq("12345"), any(Order.class));

        mockMvc.perform(put("/orders/12345")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":150.0, \"productName\":\"Updated Laptop\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Order updated successfully."));

        verify(orderService, times(1)).updateOrder(eq("12345"), any(Order.class));
    }


    @Test
    public void testDeleteOrder_Success() throws Exception {
        doNothing().when(orderService).deleteOrderById("12345");

        mockMvc.perform(delete("/orders/12345"))
                .andExpect(status().isOk())
                .andExpect(content().string("Order deleted successfully."));

        verify(orderService, times(1)).deleteOrderById("12345");
    }

    @Test
    public void testGetAllOrders() throws Exception {
        List<Order> orders = List.of(order);
        when(orderService.getAllOrders()).thenReturn(orders);

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(orderService, times(1)).getAllOrders();
    }

    @Test
    public void testGetOrderStatistics() throws Exception {
        when(orderService.countOrders()).thenReturn(5L);

        mockMvc.perform(get("/orders/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(5));

        verify(orderService, times(1)).countOrders();
    }
}
