package com.example.orderService.contoller;

import com.example.orderService.models.order.Order;
import com.example.orderService.models.order.OrderStatus;
import com.example.orderService.service.OrderService;
import com.example.orderService.notFoundException.notFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        Order savedOrder = orderService.createOrder(order);
        return ResponseEntity.ok(savedOrder);
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable String id) throws notFoundException {
        Order order = orderService.getOrdersById(id);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateOrderStatus(@PathVariable String id, @RequestParam OrderStatus status) {
        try {
            orderService.changeOrderStatus(id, status);
            return ResponseEntity.ok("Order status updated successfully.");
        } catch (notFoundException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateOrder(@PathVariable String id, @RequestBody Order updatedOrder) {
        orderService.updateOrder(id, updatedOrder);
        return ResponseEntity.ok("Order updated successfully.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable String id) {
        orderService.deleteOrderById(id);
        return ResponseEntity.ok("Order deleted successfully.");
    }

    @GetMapping("/stats")
    public ResponseEntity<Long> getOrderCount() {
        long orderCount = orderService.countOrders();
        return ResponseEntity.ok(orderCount);
    }
}

