package presentation;

import dao.interfaces.IBookingDAO;
import dao.interfaces.ICheckInOutDAO;
import dao.interfaces.IFBOrderDAO;
import dao.interfaces.IPaymentDAO;
import dao.interfaces.IWalletDAO;
import dao.interfaces.IUserDAO;
import model.User;
import presentation.handlers.BookingManagementHandler;
import presentation.handlers.CheckInOutHandler;
import presentation.handlers.OrderManagementHandler;
import presentation.handlers.PaymentProcessingHandler;
import presentation.handlers.SystemReportsHandler;
import service.interfaces.IBookingService;
import service.interfaces.IOrderService;
import service.interfaces.IPaymentService;
import utils.AppFactory;

import java.util.Scanner;

import static presentation.ConsoleUtils.centerText;

public class StaffMenu {
        private Scanner sc;
        private BookingManagementHandler bookingManagementHandler;
        private CheckInOutHandler checkInOutHandler;
        private OrderManagementHandler orderManagementHandler;
        private PaymentProcessingHandler paymentProcessingHandler;
        private SystemReportsHandler systemReportsHandler;

        public StaffMenu(Scanner sc, IBookingService bookingService, IPaymentService paymentService,
                        IOrderService orderService) {
                this.sc = sc;
                IBookingDAO bookingDAO = AppFactory.getBookingDAO();
                ICheckInOutDAO checkInOutDAO = AppFactory.getCheckInOutDAO();
                IFBOrderDAO fbOrderDAO = AppFactory.getFBOrderDAO();
                IPaymentDAO paymentDAO = AppFactory.getPaymentDAO();
                IWalletDAO walletDAO = AppFactory.getWalletDAO();
                IUserDAO userDAO = AppFactory.getUserDAO();

                this.bookingManagementHandler = new BookingManagementHandler(sc, bookingService, bookingDAO);
                this.checkInOutHandler = new CheckInOutHandler(sc, checkInOutDAO, bookingDAO);
                this.orderManagementHandler = new OrderManagementHandler(sc, fbOrderDAO, bookingDAO);
                this.paymentProcessingHandler = new PaymentProcessingHandler(sc, paymentDAO, bookingDAO, walletDAO);
                this.systemReportsHandler = new SystemReportsHandler(sc, userDAO, bookingDAO);
        }

        public void displayStaffMenu(User currentUser) {
                boolean running = true;

                while (running) {
                        ConsoleUtils.clearScreen();
                        String[] logo = {
                                        "____ ___  _ ____  _____ ____    ____  ____  _____ _      ____",
                                        "/   _\\\\  \\
                                        "|  /   \\  / | |
                                        "|  \\__ / /  | |_\\\\|  /_ |    /  | |-|||    /|  /_ | | \\||| |-||",
                                        "\\____
                        };

                        ConsoleUtils.printLogoCentered(logo, ConsoleUtils.DEFAULT_WIDTH, 58);

                        String staffName = currentUser.getUsername() != null ? currentUser.getUsername() : "Staff";
                        ConsoleUtils.printCenter(
                                        ConsoleUtils.CYAN + "Welcome, " + staffName.toUpperCase() + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                        System.out.println();

                        String[] options = {
                                        "1. Manage Bookings",
                                        "2. Check-in/Check-out",
                                        "3. Process Orders",
                                        "4. Process Payments",
                                        "5. View Reports",
                                        "6. Logout"
                        };
                        String[] colors = {
                                        ConsoleUtils.CYAN,
                                        ConsoleUtils.CYAN,
                                        ConsoleUtils.CYAN,
                                        ConsoleUtils.CYAN,
                                        ConsoleUtils.CYAN,
                                        ConsoleUtils.RED
                        };
                        ConsoleUtils.printMenuOptions(options, ConsoleUtils.DEFAULT_WIDTH, colors);
                        System.out.println();

                        System.out.print(
                                        centerText(
                                                        ConsoleUtils.YELLOW + "Choose an option (1-6): "
                                                                        + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH));

                        try {
                                int choice = Integer.parseInt(sc.nextLine());
                                switch (choice) {
                                        case 1:
                                                bookingManagementHandler.handleBookingManagement();
                                                break;
                                        case 2:
                                                checkInOutHandler.handleCheckInOut();
                                                break;
                                        case 3:
                                                orderManagementHandler.handleOrderManagement();
                                                break;
                                        case 4:
                                                paymentProcessingHandler.handlePaymentProcessing();
                                                break;
                                        case 5:
                                                systemReportsHandler.handleSystemReports();
                                                break;
                                        case 6:
                                                System.out.println();
                                                ConsoleUtils.printCenter(
                                                                ConsoleUtils.GREEN + "Logging out..."
                                                                                + ConsoleUtils.RESET,
                                                                ConsoleUtils.DEFAULT_WIDTH);
                                                System.out.print(centerText(
                                                                ConsoleUtils.YELLOW + "Press Enter to continue..."
                                                                                + ConsoleUtils.RESET,
                                                                ConsoleUtils.DEFAULT_WIDTH));
                                                sc.nextLine();
                                                running = false;
                                                break;
                                        default:
                                                ConsoleUtils.printCenter(
                                                                ConsoleUtils.RED + "Invalid choice!"
                                                                                + ConsoleUtils.RESET,
                                                                ConsoleUtils.DEFAULT_WIDTH);
                                                System.out.print(centerText(
                                                                ConsoleUtils.YELLOW + "Press Enter to continue..."
                                                                                + ConsoleUtils.RESET,
                                                                ConsoleUtils.DEFAULT_WIDTH));
                                                sc.nextLine();
                                }
                        } catch (NumberFormatException e) {
                                ConsoleUtils.printCenter(ConsoleUtils.RED + "Invalid input!" + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH);
                                System.out.print(centerText(
                                                ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH));
                                sc.nextLine();
                        }
                }
        }
}