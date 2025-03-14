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
            System.out.print("Waiting for command: ");
            gesture = scanner.next();

            switch (gesture) {
                case "1" -> {
                    System.out.println("Tworzenie zamowienia...");
                    Order newOrder = new Order();
                    System.out.print("Wprowadz typ customera (FIRM, PERSON): ");
                    gesture = scanner.next();
                    try {
                        newOrder.setCustomerType(CustomerType.valueOf(gesture));
                    } catch (Exception e) {
                        System.out.println("Blad! Wprowadz prawidlowe dane");
                        break;
                    }
                    System.out.print("Wprowadz ilosc: ");
                    gesture = scanner.next();
                    try {
                        newOrder.setAmount(Double.parseDouble(gesture));
                    } catch (Exception e) {
                        System.out.println("Blad! Wprowadz prawidlowe dane");
                        break;
                    }
                    System.out.print("Wprowadz nazwe produktu: ");
                    gesture = scanner.next();
                    newOrder.setProductName(gesture);
                    System.out.print("Wprowadz adress: ");
                    gesture = scanner.next();
                    if (Objects.equals(gesture, "")) {
                        System.out.println("Blad! Wprowadz prawidlowe dane");
                        break;
                    }
                    newOrder.setDeliveryAddress(gesture);
                    newOrder.setOrderDate(new Date(System.currentTimeMillis()));
                    System.out.print("Wprowadz metode (CARD, CASH_ON_DELIVERY) : ");
                    gesture = scanner.next();
                    try {
                        newOrder.setPaymentMethod(PaymentMethod.valueOf(gesture));
                    } catch (Exception e) {
                        System.out.println("Blad! Wprowadz prawidlowe dane");
                        break;
                    }
                    newOrder.setOrderStatus(OrderStatus.NEW);
                    orderService.createOrder(newOrder);
                    System.out.println("Zamowienie utworzono");
                }
                case "2" -> {
                    System.out.println("Zmiana statusu zamowienia...");
                    System.out.print("Wprowadz id zamowienia: ");
                    gesture = scanner.next();
                    System.out.print("Wprowadz nowy status (NEW, IN_STORE, ON_DELIVERY, DELIVERED, CLOSED, RETURNED_TO_CLIENT, ERROR): ");
                    String gestureStatus = scanner.next();
                    try {
                        orderService.changeOrderStatus(gesture, OrderStatus.valueOf(gestureStatus));
                    } catch (Exception e) {
                        System.out.println(e);
                    }

                }
                case "3" -> {
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
                }
                case "4" -> {
                    System.out.println("Przegląd zamówień");
                    System.out.print("Wprowadz id zamowienia: ");
                    gesture = scanner.next();
                    try {
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
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
                case "5" -> {
                    System.out.println("Zmiana kwoty, adresu i sposobu platnosci");
                    Order changedOrder = new Order();
                    System.out.print("Wprowadz id zamowienia: ");
                    gesture = scanner.next();
                    try {
                        Order order = orderService.getOrdersById(gesture);
                        System.out.print("Wprowadz nowa kwote(stara wartosc:" + order.getAmount() + "): ");
                        changedOrder.setAmount(Double.parseDouble(scanner.next()));
                        System.out.print("Wprowadz nowy adress(stara wartosc:" + order.getDeliveryAddress() + "): ");
                        changedOrder.setDeliveryAddress(scanner.next());
                        System.out.print("Wprowadz nowy sposob platnosci(stara wartosc:" + order.getPaymentMethod() + "): ");
                        changedOrder.setPaymentMethod(PaymentMethod.valueOf(scanner.next()));

                        orderService.updateOrder(gesture, changedOrder);
                    } catch (Exception e) {
                        System.out.print(e);
                        break;
                    }
                    System.out.println("Zmiana zostala zapisana do bazy dannych");
                }
                case "6" -> {
                    System.out.println("Statystyka:");
                    long totalOrders = orderService.countOrders();
                    System.out.println("Laczna liczba zamowien: " + totalOrders);
                    Map<OrderStatus, Long> orderStatusCount = new HashMap<>();
                    for (OrderStatus status : OrderStatus.values()) {
                        orderStatusCount.put(status, orderService.getAllOrders()
                                .stream()
                                .filter(order -> order.getOrderStatus() == status)
                                .count());
                    }
                    System.out.println("Liczba zamowien wedlug statusu:");
                    for (Map.Entry<OrderStatus, Long> entry : orderStatusCount.entrySet()) {
                        System.out.println(entry.getKey() + ": " + entry.getValue());
                    }
                    double totalRevenue = orderService.getAllOrders().stream()
                            .mapToDouble(Order::getAmount)
                            .sum();
                    System.out.println("Laczna kwota zamowien: " + totalRevenue + " PLN");
                    double avgOrderAmount = totalOrders > 0 ? totalRevenue / totalOrders : 0;
                    System.out.println("Srednia wartosc zamowienia: " + avgOrderAmount + " PLN");
                    System.out.println("\nStatystyka za czas:");
                    Map<String, Long> ordersByTimePeriod = new LinkedHashMap<>();
                    Map<String, Double> revenueByTimePeriod = new LinkedHashMap<>();
                    Date now = new Date();
                    Map<String, Long> timePeriods = Map.of(
                            "Ostatni miesiac", 30L,
                            "Ostatnie 3 miesiace", 90L,
                            "Ostatnie 6 miesiecy", 180L,
                            "Ostatni rok", 365L,
                            "Ostatnie 2 lata", 730L
                    );
                    for (Map.Entry<String, Long> entry : timePeriods.entrySet()) {
                        String label = entry.getKey();
                        long days = entry.getValue();

                        Date thresholdDate = new Date(now.getTime() - (days * 24L * 60L * 60L * 1000L));

                        List<Order> filteredOrders = orderService.getAllOrders().stream()
                                .filter(order -> order.getOrderDate().after(thresholdDate))
                                .toList();

                        long count = filteredOrders.size();
                        double revenue = filteredOrders.stream().mapToDouble(Order::getAmount).sum();

                        ordersByTimePeriod.put(label, count);
                        revenueByTimePeriod.put(label, revenue);
                    }
                    for (String label : ordersByTimePeriod.keySet()) {
                        System.out.println(label + ": " + ordersByTimePeriod.get(label) + " zamowien, " +
                                "Przychod: " + revenueByTimePeriod.get(label) + " PLN");
                    }
                }
                case "7" -> {
                    System.out.println("Wypisanie rabatu");
                    System.out.print("Wprowadz wysokosc rabatu (np. 10 dla 10%): ");
                    final double discountPercentage;
                    try {
                        discountPercentage = Double.parseDouble(scanner.next());
                    } catch (Exception e) {
                        System.out.println("Blad! Wprowadz prawidlowa liczbe.");
                        break;
                    }
                    System.out.print("Wybierz kategorie klienta, ktora otrzyma rabat (FIRM, PERSON, ALL): ");
                    final String customerCategoryInput = scanner.next();
                    final CustomerType selectedCustomerType;
                    if (customerCategoryInput.equalsIgnoreCase("ALL")) {
                        selectedCustomerType = null; // Wszystkie typy klientów
                    } else {
                        try {
                            selectedCustomerType = CustomerType.valueOf(customerCategoryInput);
                        } catch (Exception e) {
                            System.out.println("Blad! Wprowadz prawidlowa kategorie klienta.");
                            break;
                        }
                    }
                    System.out.print("Podaj numer miesiaca (1-12) dla zamowien, ktore maja dostac rabat (lub 0 dla wszystkich miesiecy): ");
                    final int selectedMonth;
                    try {
                        selectedMonth = Integer.parseInt(scanner.next());
                        if (selectedMonth < 0 || selectedMonth > 12) {
                            System.out.println("Blad! Wprowadz wartosc od 0 do 12.");
                            break;
                        }
                    } catch (Exception e) {
                        System.out.println("Blad! Wprowadz prawidlowa liczbe.");
                        break;
                    }
                    List<Order> eligibleOrders = orderService.getAllOrders().stream()
                            .filter(order ->
                                    (selectedCustomerType == null || order.getCustomerType() == selectedCustomerType) &&
                                            (selectedMonth == 0 || order.getOrderDate().toInstant().atZone(java.time.ZoneId.systemDefault()).getMonthValue() == selectedMonth)
                            )
                            .toList();
                    if (eligibleOrders.isEmpty()) {
                        System.out.println("Brak zamowien spelniajacych kryteria.");
                        break;
                    }
                    System.out.println("\nLista zamowien z rabatem:");
                    for (Order order : eligibleOrders) {
                        double discountAmount = order.getAmount() * (discountPercentage / 100);
                        double newAmount = order.getAmount() - discountAmount;
                        System.out.println("____________________________________");
                        System.out.println("Id: " + order.getId());
                        System.out.println("Klient: " + order.getCustomerType());
                        System.out.println("Kwota przed rabatem: " + order.getAmount() + " PLN");
                        System.out.println("Kwota po rabacie (" + discountPercentage + "%): " + newAmount + " PLN");
                        System.out.println("____________________________________");
                    }
                }
                case "8" -> {
                    System.out.println("Usuwanie zamowienia");
                    System.out.print("Wprowadz id zamowienia: ");
                    gesture = scanner.next();
                    orderService.deleteOrderById(gesture);
                    System.out.println("Zamowienie usuniente");
                }
                case "9" -> {
                    System.out.println("Zamykanie aplikacji...");
                    SpringApplication.exit(context, () -> 0);
                    return;
                }
                case "help" -> {
                    System.out.println("1. Tworzenie zamowienia");
                    System.out.println("2. Zmiana statusu zamowienia");
                    System.out.println("3. Przeglad wszystich zamowien");
                    System.out.println("4. Przeglad zamowień za id");
                    System.out.println("5. Zmiana kwoty, nazwy klienta, typu klienta, adresu czy sposobu platnosci");
                    System.out.println("6. Statystyka");
                    System.out.println("7. Wypisanie rabatu");
                    System.out.println("8. Usuwanie zamowienia");
                    System.out.println("9. Wyjsce");
                }
                default -> System.out.println("Nieprawidlowa komenda. Uzyj help dla listy komend");
            }
        }
    }
}