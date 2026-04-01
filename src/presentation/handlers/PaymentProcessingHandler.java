package presentation.handlers;

import dao.interfaces.IBookingDAO;
import dao.interfaces.IPaymentDAO;
import dao.interfaces.IWalletDAO;
import model.Payment;
import model.Booking;
import model.Wallet;
import presentation.ConsoleUtils;
import utils.InputValidator;
import enums.PaymentMethod;
import enums.PaymentStatus;

import java.util.List;
import java.util.Scanner;

import static presentation.ConsoleUtils.centerText;

public class PaymentProcessingHandler {
        private Scanner sc;
        private IPaymentDAO paymentDAO;
        private IBookingDAO bookingDAO;
        private IWalletDAO walletDAO;

        public PaymentProcessingHandler(Scanner sc, IPaymentDAO paymentDAO, IBookingDAO bookingDAO,
                                        IWalletDAO walletDAO) {
                this.sc = sc;
                this.paymentDAO = paymentDAO;
                this.bookingDAO = bookingDAO;
                this.walletDAO = walletDAO;
        }

        public void handlePaymentProcessing() {
                while (true) {
                        ConsoleUtils.clearScreen();
                        String[] logo = {
                                "▗▄▄▖  ▗▄▖▗▖  ▗▖▗▖  ▗▖▗▄▄▄▖▗▖  ▗▖▗▄▄▄▖    ▗▄▄▖ ▗▄▄▖  ▗▄▖  ▗▄▄▖▗▄▄▄▖ ▗▄▄▖ ▗▄▄▖▗▄▄▄▖▗▖  ▗▖ ▗▄▄▖",
                                "▐▌ ▐▌▐▌ ▐▌▝▚▞▘ ▐▛▚▞▜▌▐▌   ▐▛▚▖▐▌  █      ▐▌ ▐▌▐▌ ▐▌▐▌ ▐▌▐▌   ▐▌   ▐▌   ▐▌     █  ▐▛▚▖▐▌▐▌   ",
                                "▐▛▀▘ ▐▛▀▜▌ ▐▌  ▐▌  ▐▌▐▛▀▀▘▐▌ ▝▜▌  █      ▐▛▀▘ ▐▛▀▚▖▐▌ ▐▌▐▌   ▐▛▀▀▘ ▝▀▚▖ ▝▀▚▖  █  ▐▌ ▝▜▌▐▌▝▜▌",
                                "▐▌   ▐▌ ▐▌ ▐▌  ▐▌  ▐▌▐▙▄▄▖▐▌  ▐▌  █      ▐▌   ▐▌ ▐▌▝▚▄▞▘▝▚▄▄▖▐▙▄▄▖▗▄▄▞▘▗▄▄▞▘▗▄█▄▖▐▌  ▐▌▝▚▄▞▘",
                        };

                        ConsoleUtils.printLogoCentered(logo, ConsoleUtils.DEFAULT_WIDTH, 100);
                        ConsoleUtils.printCenter(
                                ConsoleUtils.CYAN + "━━━━━━━━━━ ＰＡＹＭＥＮＴ  ＰＲＯＣＥＳＳＩＮＧ ━━━━━━━━━━"
                                        + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH);
                        System.out.println();

                        String[] options = {
                                "1. Process cash payment",
                                "2. Process wallet payment",
                                "3. View payment history",
                                "4. Update payment status",
                                "5. Back"
                        };

                        String[] colors = {
                                ConsoleUtils.GREEN,
                                ConsoleUtils.GREEN,
                                ConsoleUtils.GREEN,
                                ConsoleUtils.CYAN,
                                ConsoleUtils.RED
                        };

                        ConsoleUtils.printMenuOptions(options, ConsoleUtils.DEFAULT_WIDTH, colors);
                        System.out.println();

                        System.out.print(centerText(
                                ConsoleUtils.YELLOW + "Choose an option (1-5): " + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH));

                        try {
                                int choice = InputValidator.getIntInput(sc, "");
                                switch (choice) {
                                        case 1:
                                                processCashPayment();
                                                break;
                                        case 2:
                                                processWalletPayment();
                                                break;
                                        case 3:
                                                viewPaymentHistory();
                                                break;
                                        case 4:
                                                updatePaymentStatus();
                                                break;
                                        case 5:
                                                return;
                                        default:
                                                ConsoleUtils.printCenter(
                                                        ConsoleUtils.RED + "Invalid choice!"
                                                                + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                                pauseMenu();
                                }
                        } catch (Exception e) {
                                ConsoleUtils.printCenter(ConsoleUtils.RED + "Invalid input!" + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                                pauseMenu();
                        }
                }
        }

        private void processCashPayment() {
                ConsoleUtils.clearScreen();
                ConsoleUtils.printCenter(
                        ConsoleUtils.CYAN + "━━━━━━━━━━ ＰＲＯＣＥＳＳ ＣＡＳＨ ＰＡＹＭＥＮＴ ━━━━━━━━━━" + ConsoleUtils.RESET,
                        ConsoleUtils.DEFAULT_WIDTH);
                System.out.println();

                try {
                        System.out.print(centerText(ConsoleUtils.YELLOW + "Enter booking ID: " + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH));
                        int bookingId = InputValidator.getIntInput(sc, "");

                        Booking booking = bookingDAO.findById(bookingId);
                        if (booking == null) {
                                ConsoleUtils.printCenter(ConsoleUtils.RED + "Booking not found!" + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                                pauseMenu();
                                return;
                        }

                        System.out.print(centerText(ConsoleUtils.YELLOW + "Enter payment amount: " + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH));
                        double amount = Double.parseDouble(sc.nextLine().trim());

                        Payment payment = new Payment();
                        payment.setBookingId(bookingId);
                        payment.setAmount(amount);
                        payment.setMethod(PaymentMethod.CASH);
                        payment.setStatus(PaymentStatus.PAID);

                        paymentDAO.create(payment);

                        ConsoleUtils.printCenter(
                                ConsoleUtils.GREEN + "Cash payment processed successfully!"
                                        + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH);
                        System.out.println(
                                centerText("Booking ID: " + bookingId + " | Amount: " + amount,
                                        ConsoleUtils.DEFAULT_WIDTH));

                        pauseMenu();
                } catch (NumberFormatException e) {
                        ConsoleUtils.printCenter(ConsoleUtils.RED + "Invalid amount format!" + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH);
                        pauseMenu();
                } catch (Exception e) {
                        ConsoleUtils.printCenter(ConsoleUtils.RED + "Error: " + e.getMessage() + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH);
                        pauseMenu();
                }
        }

        private void processWalletPayment() {
                ConsoleUtils.clearScreen();
                ConsoleUtils.printCenter(
                        ConsoleUtils.CYAN + "━━━━━━━━━━ ＰＲＯＣＥＳＳ ＷＡＬＬＥＴ ＰＡＹＭＥＮＴ ━━━━━━━━━━" + ConsoleUtils.RESET,
                        ConsoleUtils.DEFAULT_WIDTH);
                System.out.println();

                try {
                        System.out.print(centerText(ConsoleUtils.YELLOW + "Enter booking ID: " + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH));
                        int bookingId = InputValidator.getIntInput(sc, "");

                        Booking booking = bookingDAO.findById(bookingId);
                        if (booking == null) {
                                ConsoleUtils.printCenter(ConsoleUtils.RED + "Booking not found!" + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                                pauseMenu();
                                return;
                        }

                        System.out.print(centerText(ConsoleUtils.YELLOW + "Enter wallet ID: " + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH));
                        int walletId = InputValidator.getIntInput(sc, "");

                        Wallet wallet = walletDAO.findById(walletId);
                        if (wallet == null) {
                                ConsoleUtils.printCenter(ConsoleUtils.RED + "Wallet not found!" + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                                pauseMenu();
                                return;
                        }

                        System.out.print(centerText(ConsoleUtils.YELLOW + "Enter payment amount: " + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH));
                        double amount = Double.parseDouble(sc.nextLine().trim());

                        if (wallet.getBalance() < amount) {
                                ConsoleUtils.printCenter(
                                        ConsoleUtils.RED + "Insufficient wallet balance!" + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                                System.out.println(centerText("Current balance: " + wallet.getBalance(),
                                        ConsoleUtils.DEFAULT_WIDTH));
                                pauseMenu();
                                return;
                        }

                        Payment payment = new Payment();
                        payment.setBookingId(bookingId);
                        payment.setWalletId(walletId);
                        payment.setAmount(amount);
                        payment.setMethod(PaymentMethod.WALLET);
                        payment.setStatus(PaymentStatus.PAID);

                        paymentDAO.create(payment);


                        walletDAO.deductBalance(walletId, amount);

                        ConsoleUtils.printCenter(
                                ConsoleUtils.GREEN + "Wallet payment processed successfully!"
                                        + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH);
                        System.out.println(
                                centerText("Booking ID: " + bookingId + " | Amount: " + amount,
                                        ConsoleUtils.DEFAULT_WIDTH));
                        System.out.println(centerText("Remaining balance: " + wallet.getBalance(),
                                ConsoleUtils.DEFAULT_WIDTH));

                        pauseMenu();
                } catch (NumberFormatException e) {
                        ConsoleUtils.printCenter(ConsoleUtils.RED + "Invalid amount format!" + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH);
                        pauseMenu();
                } catch (Exception e) {
                        ConsoleUtils.printCenter(ConsoleUtils.RED + "Error: " + e.getMessage() + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH);
                        pauseMenu();
                }
        }

        private void viewPaymentHistory() {
                ConsoleUtils.clearScreen();
                ConsoleUtils.printCenter(
                        ConsoleUtils.CYAN + "━━━━━━━━━━ ＰＡＹＭＥＮＴ ＨＩＳＴＯＲＹ ━━━━━━━━━━" + ConsoleUtils.RESET,
                        ConsoleUtils.DEFAULT_WIDTH);
                System.out.println();

                try {
                        List<Payment> payments = paymentDAO.findAll();
                        if (payments.isEmpty()) {
                                ConsoleUtils.printCenter(
                                        ConsoleUtils.ORANGE + "No payment records found" + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                        } else {
                                System.out.println(centerText(
                                        String.format("%-10s %-12s %-12s %-15s %-15s", "ID", "Booking ID",
                                                "Amount", "Method",
                                                "Status"),
                                        ConsoleUtils.DEFAULT_WIDTH));
                                System.out.println(centerText("─".repeat(65), ConsoleUtils.DEFAULT_WIDTH));

                                for (Payment payment : payments) {
                                        System.out.println(centerText(
                                                String.format("%-10d %-12d %-12.2f %-15s %-15s",
                                                        payment.getPaymentId(),
                                                        payment.getBookingId(),
                                                        payment.getAmount(),
                                                        payment.getMethod(),
                                                        payment.getStatus()),
                                                ConsoleUtils.DEFAULT_WIDTH));
                                }
                        }

                        pauseMenu();
                } catch (Exception e) {
                        ConsoleUtils.printCenter(ConsoleUtils.RED + "Error: " + e.getMessage() + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH);
                        pauseMenu();
                }
        }

        private void updatePaymentStatus() {
                ConsoleUtils.clearScreen();
                ConsoleUtils.printCenter(
                        ConsoleUtils.CYAN + "━━━━━━━━━━ ＵＰＤＡＴＥ ＰＡＹＭＥＮＴ ＳＴＡＴＵＳ ━━━━━━━━━━" + ConsoleUtils.RESET,
                        ConsoleUtils.DEFAULT_WIDTH);
                System.out.println();

                try {
                        System.out.print(centerText(ConsoleUtils.YELLOW + "Enter payment ID: " + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH));
                        int paymentId = InputValidator.getIntInput(sc, "");

                        Payment payment = paymentDAO.findById(paymentId);
                        if (payment == null) {
                                ConsoleUtils.printCenter(ConsoleUtils.RED + "Payment not found!" + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                                pauseMenu();
                                return;
                        }

                        System.out.println(centerText("Current Status: " + payment.getStatus(),
                                ConsoleUtils.DEFAULT_WIDTH));
                        System.out.println();
                        System.out.println(centerText("1. PENDING", ConsoleUtils.DEFAULT_WIDTH));
                        System.out.println(centerText("2. PAID", ConsoleUtils.DEFAULT_WIDTH));
                        System.out.println(centerText("3. FAILED", ConsoleUtils.DEFAULT_WIDTH));
                        System.out.println();

                        System.out.print(centerText(
                                ConsoleUtils.YELLOW + "Choose new status (1-3): " + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH));
                        int statusChoice = InputValidator.getIntInput(sc, "");

                        PaymentStatus newStatus = switch (statusChoice) {
                                case 1 -> PaymentStatus.PENDING;
                                case 2 -> PaymentStatus.PAID;
                                case 3 -> PaymentStatus.FAILED;
                                default -> null;
                        };

                        if (newStatus == null) {
                                ConsoleUtils.printCenter(
                                        ConsoleUtils.RED + "Invalid status choice!" + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                                pauseMenu();
                                return;
                        }

                        paymentDAO.updateStatus(paymentId, newStatus.name());

                        ConsoleUtils.printCenter(ConsoleUtils.GREEN + "Payment status updated!" + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH);
                        System.out.println(
                                centerText("Payment ID: " + paymentId + " | New Status: " + newStatus,
                                        ConsoleUtils.DEFAULT_WIDTH));

                        pauseMenu();
                } catch (Exception e) {
                        ConsoleUtils.printCenter(ConsoleUtils.RED + "Error: " + e.getMessage() + ConsoleUtils.RESET,
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
