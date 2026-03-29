package presentation;

import dao.interfaces.IBookingDAO;
import dao.interfaces.IFBOrderDAO;
import dao.interfaces.IPCDAO;
import dao.interfaces.IPaymentDAO;
import model.User;
import presentation.handlers.BookingManagementHandler;
import service.interfaces.IBookingService;
import service.interfaces.IOrderService;
import service.interfaces.IPaymentService;
import utils.AppFactory;
import utils.InputValidator;

import java.util.Scanner;

import static presentation.ConsoleUtils.centerText;

public class StaffMenu {
    private Scanner sc;
    private BookingManagementHandler bookingManagementHandler;


    public StaffMenu(Scanner sc, IBookingService bookingService, IPaymentService paymentService,
                     IOrderService orderService) {
        this.sc = sc;
        IBookingDAO bookingDAO = AppFactory.getBookingDAO();
//        this.paymentDAO = AppFactory.getPaymentDAO();
//        this.fbOrderDAO = AppFactory.getFBOrderDAO();

        this.bookingManagementHandler = new BookingManagementHandler(sc, bookingService, bookingDAO);

    }

    public void displayStaffMenu(User currentUser) {
        boolean running = true;

        while (running) {
            ConsoleUtils.clearScreen();
            String[] logo = {
                    "____ ___  _ ____  _____ ____    ____  ____  _____ _      ____",
                    "/   _\\\\  \\///  _ \\/  __//  __\\  /  _ \\/  __\\/  __// \\  /|/  _ \\",
                    "|  /   \\  / | | //|  \\  |  \\/|  | / \\||  \\/||  \\  | |\\ ||| / \\|",
                    "|  \\__ / /  | |_\\\\|  /_ |    /  | |-|||    /|  /_ | | \\||| |-||",
                    "\\____//_/   \\____/\\____\\_/\\_\\  \\_/ \\||\\_/\\_\\\\____\\_/  \\||\\_/ \\|"
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

            ConsoleUtils.printMenuOptions(options, ConsoleUtils.DEFAULT_WIDTH);
            System.out.println();

            System.out.print(
                    centerText(
                            ConsoleUtils.YELLOW + "Choose an option (1-6): " + ConsoleUtils.RESET,
                            ConsoleUtils.DEFAULT_WIDTH));

            try {
                int choice = Integer.parseInt(sc.nextLine());
                switch (choice) {
                    case 1:
                        bookingManagementHandler.handleBookingManagement();
                        System.out.print(centerText(
                                ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH));
                        sc.nextLine();
                        break;
                    case 2:
                        ConsoleUtils.printCenter(
                                ConsoleUtils.CYAN + "Check-in/Check-out - Coming Soon" + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH);
                        System.out.print(centerText(
                                ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH));
                        sc.nextLine();
                        break;
                    case 3:
                        ConsoleUtils.printCenter(
                                ConsoleUtils.CYAN + "Process Orders - Coming Soon" + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH);
                        System.out.print(centerText(
                                ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH));
                        sc.nextLine();
                        break;
                    case 4:
                        ConsoleUtils.printCenter(
                                ConsoleUtils.CYAN + "Process Payments - Coming Soon" + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH);
                        System.out.print(centerText(
                                ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH));
                        sc.nextLine();
                        break;
                    case 5:
                        ConsoleUtils.printCenter(ConsoleUtils.CYAN + "View Reports - Coming Soon" + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH);
                        System.out.print(centerText(
                                ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH));
                        sc.nextLine();
                        break;
                    case 6:
                        System.out.println();
                        ConsoleUtils.printCenter(ConsoleUtils.GREEN + "Logging out..." + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH);
                        System.out.print(centerText(
                                ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH));
                        sc.nextLine();
                        running = false;
                        break;
                    default:
                        ConsoleUtils.printCenter(
                                ConsoleUtils.RED + "Invalid choice!" + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH);
                        System.out.print(centerText(
                                ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
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