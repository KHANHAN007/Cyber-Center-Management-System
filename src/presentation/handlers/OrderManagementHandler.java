package presentation.handlers;

import dao.interfaces.IFBOrderDAO;
import dao.interfaces.IBookingDAO;
import model.FBOrder;
import model.Booking;
import presentation.ConsoleUtils;
import utils.InputValidator;
import enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import java.math.BigDecimal;

import static presentation.ConsoleUtils.centerText;

public class OrderManagementHandler {
    private Scanner sc;
    private IFBOrderDAO fbOrderDAO;
    private IBookingDAO bookingDAO;

    public OrderManagementHandler(Scanner sc, IFBOrderDAO fbOrderDAO, IBookingDAO bookingDAO) {
        this.sc = sc;
        this.fbOrderDAO = fbOrderDAO;
        this.bookingDAO = bookingDAO;
    }

    public void handleOrderManagement() {
        while (true) {
            ConsoleUtils.clearScreen();
            String[] logo ={" " +
                    "`▗▄▖ ▗▄▄▖ ▗▄▄▄  ▗▄▄▄▖▗▄▄▖     ▗▖  ▗▖ ▗▄▖ ▗▖  ▗▖ ▗▄▖  ▗▄▄▖▗▄▄▄▖▗▄▄▖ \n" +
                    "▐▌ ▐▌▐▌ ▐▌▐▌  █ ▐▌   ▐▌ ▐▌    ▐▛▚▞▜▌▐▌ ▐▌▐▛▚▖▐▌▐▌ ▐▌▐▌   ▐▌   ▐▌ ▐▌\n" +
                    "▐▌ ▐▌▐▛▀▚▖▐▌  █ ▐▛▀▀▘▐▛▀▚▖    ▐▌  ▐▌▐▛▀▜▌▐▌ ▝▜▌▐▛▀▜▌▐▌▝▜▌▐▛▀▀▘▐▛▀▚▖\n" +
                    "▝▚▄▞▘▐▌ ▐▌▐▙▄▄▀ ▐▙▄▄▖▐▌ ▐▌    ▐▌  ▐▌▐▌ ▐▌▐▌  ▐▌▐▌ ▐▌▝▚▄▞▘▐▙▄▄▖▐▌ ▐▌\n"};

            ConsoleUtils.printLogoCentered(logo, ConsoleUtils.DEFAULT_WIDTH, 60);
            ConsoleUtils.printCenter(ConsoleUtils.CYAN + "━━━━━━━━━━ ＰＲＯＣＥＳＳ ＯＲＤＥＲＳ ━━━━━━━━━━" + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH);
            System.out.println();

            String[] options = {
                    "1. Create new order",
                    "2. View pending orders",
                    "3. Update order status",
                    "4. View order details",
                    "5. Cancel order",
                    "6. Back"
            };

            String[] colors = {
                    ConsoleUtils.GREEN,
                    ConsoleUtils.GREEN,
                    ConsoleUtils.CYAN,
                    ConsoleUtils.GREEN,
                    ConsoleUtils.ORANGE,
                    ConsoleUtils.RED
            };

            ConsoleUtils.printMenuOptions(options, ConsoleUtils.DEFAULT_WIDTH, colors);
            System.out.println();

            System.out.print(centerText(
                    ConsoleUtils.YELLOW + "Choose an option (1-6): " + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH));

            try {
                int choice = InputValidator.getIntInput(sc, "");
                switch (choice) {
                    case 1:
                        createNewOrder();
                        break;
                    case 2:
                        viewPendingOrders();
                        break;
                    case 3:
                        updateOrderStatus();
                        break;
                    case 4:
                        viewOrderDetails();
                        break;
                    case 5:
                        cancelOrder();
                        break;
                    case 6:
                        return;
                    default:
                        ConsoleUtils.printCenter(
                                ConsoleUtils.RED + " Invalid choice!" + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH);
                        pauseMenu();
                }
            } catch (Exception e) {
                ConsoleUtils.printCenter(ConsoleUtils.RED + " Invalid input!" + ConsoleUtils.RESET,
                        ConsoleUtils.DEFAULT_WIDTH);
                pauseMenu();
            }
        }
    }

    private void createNewOrder() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printCenter(ConsoleUtils.CYAN + "━━━━━━━━━━ ＣＲＥＡＴＥ ＯＲＤＥＲ ━━━━━━━━━━" + ConsoleUtils.RESET,
                ConsoleUtils.DEFAULT_WIDTH);
        System.out.println();

        try {
            System.out.print(centerText(ConsoleUtils.YELLOW + "Enter booking ID: " + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH));
            int bookingId = InputValidator.getIntInput(sc, "");

            Booking booking = bookingDAO.findById(bookingId);
            if (booking == null) {
                ConsoleUtils.printCenter(ConsoleUtils.RED + " Booking not found!" + ConsoleUtils.RESET,
                        ConsoleUtils.DEFAULT_WIDTH);
                pauseMenu();
                return;
            }

            System.out.print(centerText(ConsoleUtils.YELLOW + "Enter total price: " + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH));
            BigDecimal totalPrice = new BigDecimal(sc.nextLine().trim());

            FBOrder order = new FBOrder();
            order.setBookingId(bookingId);
            order.setCreatedAt(LocalDateTime.now());
            order.setTotalPrice(totalPrice.doubleValue());
            order.setStatus(OrderStatus.PENDING);

            fbOrderDAO.create(order);

            ConsoleUtils.printCenter(ConsoleUtils.GREEN + " Order created successfully!" + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH);
            System.out.println(
                    centerText("Booking ID: " + bookingId + " | Total: " + totalPrice, ConsoleUtils.DEFAULT_WIDTH));

            pauseMenu();
        } catch (NumberFormatException e) {
            ConsoleUtils.printCenter(ConsoleUtils.RED + " Invalid price format!" + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH);
            pauseMenu();
        } catch (Exception e) {
            ConsoleUtils.printCenter(ConsoleUtils.RED + " Error: " + e.getMessage() + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH);
            pauseMenu();
        }
    }

    private void viewPendingOrders() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printCenter(ConsoleUtils.CYAN + "━━━━━━━━━━ ＰＥＮＤＩＮＧ ＯＲＤＥＲＳ ━━━━━━━━━━" + ConsoleUtils.RESET,
                ConsoleUtils.DEFAULT_WIDTH);
        System.out.println();

        try {
            List<FBOrder> orders = fbOrderDAO.findAll();
            if (orders.isEmpty()) {
                ConsoleUtils.printCenter(ConsoleUtils.ORANGE + " No orders found" + ConsoleUtils.RESET,
                        ConsoleUtils.DEFAULT_WIDTH);
            } else {
                System.out.println(centerText(
                        String.format("%-10s %-12s %-15s %-15s %-15s", "ID", "Booking ID", "Total Price", "Status",
                                "Order Time"),
                        ConsoleUtils.DEFAULT_WIDTH));
                System.out.println(centerText("─".repeat(70), ConsoleUtils.DEFAULT_WIDTH));

                for (FBOrder order : orders) {
                    if (order.getStatus().equals(OrderStatus.PENDING)) {
                        System.out.println(centerText(
                                String.format("%-10d %-12d %-15s %-15s %-15s",
                                        order.getOrderId(),
                                        order.getBookingId(),
                                        order.getTotalPrice(),
                                        order.getStatus(),
                                        order.getCreatedAt().toString().substring(0, 16)),
                                ConsoleUtils.DEFAULT_WIDTH));
                    }
                }
            }

            pauseMenu();
        } catch (Exception e) {
            ConsoleUtils.printCenter(ConsoleUtils.RED + " Error: " + e.getMessage() + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH);
            pauseMenu();
        }
    }

    private void updateOrderStatus() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printCenter(ConsoleUtils.CYAN + "━━━━━━━━━━ ＵＰＤＡＴＥ ＯＲＤＥＲ ＳＴＡＴＵＳ ━━━━━━━━━━" + ConsoleUtils.RESET,
                ConsoleUtils.DEFAULT_WIDTH);
        System.out.println();

        try {
            System.out.print(centerText(ConsoleUtils.YELLOW + "Enter order ID: " + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH));
            int orderId = InputValidator.getIntInput(sc, "");

            FBOrder order = fbOrderDAO.findById(orderId);
            if (order == null) {
                ConsoleUtils.printCenter(ConsoleUtils.RED + " Order not found!" + ConsoleUtils.RESET,
                        ConsoleUtils.DEFAULT_WIDTH);
                pauseMenu();
                return;
            }

            System.out.println(centerText("Current Status: " + order.getStatus(), ConsoleUtils.DEFAULT_WIDTH));
            System.out.println();
            System.out.println(centerText("1. PENDING", ConsoleUtils.DEFAULT_WIDTH));
            System.out.println(centerText("2. PREPARING", ConsoleUtils.DEFAULT_WIDTH));
            System.out.println(centerText("3. READY", ConsoleUtils.DEFAULT_WIDTH));
            System.out.println(centerText("4. COMPLETED", ConsoleUtils.DEFAULT_WIDTH));
            System.out.println(centerText("5. CANCELLED", ConsoleUtils.DEFAULT_WIDTH));
            System.out.println();

            System.out.print(centerText(ConsoleUtils.YELLOW + "Choose new status (1-5): " + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH));
            int statusChoice = InputValidator.getIntInput(sc, "");

            OrderStatus newStatus = switch (statusChoice) {
                case 1 -> OrderStatus.PENDING;
                case 2 -> OrderStatus.PREPARING;
                case 3 -> OrderStatus.READY;
                case 4 -> OrderStatus.COMPLETED;
                case 5 -> OrderStatus.CANCELLED;
                default -> null;
            };

            if (newStatus == null) {
                ConsoleUtils.printCenter(ConsoleUtils.RED + " Invalid status choice!" + ConsoleUtils.RESET,
                        ConsoleUtils.DEFAULT_WIDTH);
                pauseMenu();
                return;
            }

            fbOrderDAO.updateStatus(orderId, newStatus.getValue());

            ConsoleUtils.printCenter(ConsoleUtils.GREEN + " Order status updated!" + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH);
            System.out.println(
                    centerText("Order ID: " + orderId + " | New Status: " + newStatus, ConsoleUtils.DEFAULT_WIDTH));

            pauseMenu();
        } catch (Exception e) {
            ConsoleUtils.printCenter(ConsoleUtils.RED + " Error: " + e.getMessage() + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH);
            pauseMenu();
        }
    }

    private void viewOrderDetails() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printCenter(ConsoleUtils.CYAN + "━━━━━━━━━━ ＶＩＥＷ ＯＲＤＥＲ ＤＥＴＡＩＬＳ ━━━━━━━━━━" + ConsoleUtils.RESET,
                ConsoleUtils.DEFAULT_WIDTH);
        System.out.println();

        try {
            System.out.print(centerText(ConsoleUtils.YELLOW + "Enter order ID: " + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH));
            int orderId = InputValidator.getIntInput(sc, "");

            FBOrder order = fbOrderDAO.findById(orderId);
            if (order == null) {
                ConsoleUtils.printCenter(ConsoleUtils.RED + " Order not found!" + ConsoleUtils.RESET,
                        ConsoleUtils.DEFAULT_WIDTH);
                pauseMenu();
                return;
            }

            System.out.println();
            System.out.println(centerText("Order ID: " + order.getOrderId(), ConsoleUtils.DEFAULT_WIDTH));
            System.out.println(centerText("Booking ID: " + order.getBookingId(), ConsoleUtils.DEFAULT_WIDTH));
            System.out.println(centerText("Total Price: " + order.getTotalPrice(), ConsoleUtils.DEFAULT_WIDTH));
            System.out.println(centerText("Status: " + order.getStatus(), ConsoleUtils.DEFAULT_WIDTH));
            System.out.println(centerText("Order Time: " + order.getCreatedAt(), ConsoleUtils.DEFAULT_WIDTH));

            pauseMenu();
        } catch (Exception e) {
            ConsoleUtils.printCenter(ConsoleUtils.RED + " Error: " + e.getMessage() + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH);
            pauseMenu();
        }
    }

    private void cancelOrder() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printCenter(ConsoleUtils.CYAN + "━━━━━━━━━━ ＣＡＮＣＥＬ ＯＲＤＥＲ ━━━━━━━━━━" + ConsoleUtils.RESET,
                ConsoleUtils.DEFAULT_WIDTH);
        System.out.println();

        try {
            System.out.print(centerText(ConsoleUtils.YELLOW + "Enter order ID: " + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH));
            int orderId = InputValidator.getIntInput(sc, "");

            FBOrder order = fbOrderDAO.findById(orderId);
            if (order == null) {
                ConsoleUtils.printCenter(ConsoleUtils.RED + " Order not found!" + ConsoleUtils.RESET,
                        ConsoleUtils.DEFAULT_WIDTH);
                pauseMenu();
                return;
            }

            if (order.getStatus().equals(OrderStatus.COMPLETED)) {
                ConsoleUtils.printCenter(
                        ConsoleUtils.ORANGE + "  Cannot cancel completed order!" + ConsoleUtils.RESET,
                        ConsoleUtils.DEFAULT_WIDTH);
                pauseMenu();
                return;
            }

            fbOrderDAO.updateStatus(orderId, OrderStatus.CANCELLED.getValue());

            ConsoleUtils.printCenter(ConsoleUtils.GREEN + " Order cancelled successfully!" + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH);
            System.out.println(centerText("Order ID: " + orderId, ConsoleUtils.DEFAULT_WIDTH));

            pauseMenu();
        } catch (Exception e) {
            ConsoleUtils.printCenter(ConsoleUtils.RED + " Error: " + e.getMessage() + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH);
            pauseMenu();
        }
    }

    private void pauseMenu() {
        System.out.println();
        System.out.print(centerText(
                ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                ConsoleUtils.DEFAULT_WIDTH));
        sc.nextLine();
    }
}
