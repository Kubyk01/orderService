package com.example.orderService;

import java.util.*;

import com.example.orderService.models.order.CustomerType;
import com.example.orderService.models.order.Order;
import com.example.orderService.models.order.OrderStatus;
import com.example.orderService.models.order.PaymentMethod;
import com.example.orderService.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class OrderServiceApplication implements CommandLineRunner {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ApplicationContext context;

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        String gesture;

        while (true) {
            System.out.print("Waiting for command: "); gesture = scanner.next();

            switch (gesture) {
                case "1":
                    System.out.println("Tworzenie zamowienia...");
                    Order newOrder = new Order();

                    System.out.print("Wprowadz typ customera (FIRM, PERSON): "); gesture = scanner.next();
                    try {
                        newOrder.setCustomerType(CustomerType.valueOf(gesture));
                    } catch (Exception e){
                        System.out.println("Blad! Wprowadz prawidlowe dane");
                        break;
                    }

                    System.out.print("Wprowadz ilosc: "); gesture = scanner.next();
                    try {
                        newOrder.setAmount(Double.parseDouble(gesture));
                    } catch (Exception e){
                        System.out.println("Blad! Wprowadz prawidlowe dane");
                        break;
                    }

                    System.out.print("Wprowadz nazwe produktu: "); gesture = scanner.next();
                    newOrder.setProductName(gesture);

                    System.out.print("Wprowadz adress: "); gesture = scanner.next();
                    if (Objects.equals(gesture, "")){
                        System.out.println("Blad! Wprowadz prawidlowe dane");
                        break;
                    }
                    newOrder.setDeliveryAddress(gesture);

                    newOrder.setOrderDate(new Date(System.currentTimeMillis()));

                    System.out.print("Wprowadz metode (CARD, CASH_ON_DELIVERY) : "); gesture = scanner.next();
                    try {
                        newOrder.setPaymentMethod(PaymentMethod.valueOf(gesture));
                    } catch (Exception e){
                        System.out.println("Blad! Wprowadz prawidlowe dane");
                        break;
                    }

                    newOrder.setOrderStatus(OrderStatus.NEW);

                    orderService.createOrder(newOrder);
                    System.out.println("Zamowienie utworzono");

                    break;
                case "2":
                    System.out.println("Zmiana statusu zamówienia...");
                    System.out.print("Wprowadz id zamowienia: "); gesture = scanner.next();
                    System.out.print("Wprowadz nowy status (NEW, IN_STORE, ON_DELIVERY, DELIVERED, CLOSED, RETURNED_TO_CLIENT, ERROR): "); String gestureStatus = scanner.next();
                    try {
                        orderService.changeOrderStatus(gesture, OrderStatus.valueOf(gestureStatus));
                    } catch (Exception e){
                        System.out.println(e);
                        break;
                    }

                    break;
                case "3":
                    System.out.println("Przegląd wszystich zamowień");
                    long amountOrders = orderService.countOrders();
                    List<Order> orders = orderService.getAllOrders();

                    System.out.println("All orders: " + amountOrders);
                    for (Order order : orders) {
                        System.out.println("____________________________________");
                        System.out.println("Id: " + order.getId());
                        System.out.println("CustomerType: " + order.getCustomerType());
                        System.out.println("Amount: " + order.getAmount());
                        System.out.println("ProductName: " + order.getProductName());
                        System.out.println("DeliveryAddress: " + order.getDeliveryAddress());
                        System.out.println("PaymentMethod: " + order.getPaymentMethod());
                        System.out.println("OrderStatus: " + order.getOrderStatus());
                        System.out.println("____________________________________");
                    }
                    break;
                case "4":
                    System.out.println("Przegląd zamówień");
                    System.out.print("Wprowadz id zamowienia: "); gesture = scanner.next();
                    try{
                        Order order = orderService.getOrdersById(gesture);
                        System.out.println("____________________________________");
                        System.out.println("Id: " + order.getId());
                        System.out.println("CustomerType: " + order.getCustomerType());
                        System.out.println("Amount: " + order.getAmount());
                        System.out.println("ProductName: " + order.getProductName());
                        System.out.println("DeliveryAddress: " + order.getDeliveryAddress());
                        System.out.println("PaymentMethod: " + order.getPaymentMethod());
                        System.out.println("OrderStatus: " + order.getOrderStatus());
                        System.out.println("____________________________________");

                    }catch (Exception e){
                        System.out.println(e);
                        break;
                    }

                    break;
                case "5":
                    System.out.println("Wyjscie");
                    return;
                case "6":
                    System.out.println("Zmiana kwoty, nazwy klienta, typu klienta, adresu czy sposobem platności");
                    // Implementacja zmiany danych zamówienia

                    break;
                case "7":
                    System.out.println("Usuwanie zamowienia");
                    System.out.print("Wprowadz id zamowienia: "); gesture = scanner.next();
                    orderService.deleteOrderById(gesture);
                    System.out.println("Zamowienie usuniente");

                    break;
                case "8":
                    System.out.println("Wypisanie rabatu");

                    break;
                case "9" :

                    break;
                case "10":
                    System.out.println("Zamykanie aplikacji...");
                    SpringApplication.exit(context, () -> 0);
                    return;
                case "help":
                    System.out.println("1. Tworzenie zamowienia");
                    System.out.println("2. Zmiana statusu zamówienia");
                    System.out.println("3. Przegląd wszystich zamowień");
                    System.out.println("3. Przegląd zamowień");
                    System.out.println("4. Zmiana kwoty, nazwy klienta, typu klienta, adresu czy sposobu platności");
                    System.out.println("5. Zebranie statystyki za określony czas (month, 3 months, 6 months, year, 2 years) dla typu, kwoty, nazwy produktu lub sposobu platnosci");
                    System.out.println("6. Wypisanie rabatu (np. Zamowienia powyzej 5000 zl mogą otrzymać rabat 10%, Klienci firmowi mogą otrzymać 5% rabatu na każde zamówienie. Rabat sezonowy: 20% na zamówienia złożone w określonych miesiącach (np. styczeń, luty))");
                    System.out.println("7. Usuwanie zamowienia");
                    System.out.println("10. Wyjsce");

                    break;
                default:
                    System.out.println("Nieprawidlowa komenda. Uzyj help dla listy komend");
            }
        }
    }
}