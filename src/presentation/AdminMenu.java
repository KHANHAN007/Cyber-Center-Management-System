package presentation;

import dao.interfaces.*;
import model.User;
import presentation.handlers.*;
import service.interfaces.IBookingService;
import service.interfaces.IUserService;
import utils.AppFactory;

import java.util.Scanner;

public class AdminMenu {
        private Scanner sc;
        private IUserDAO userDAO;
        private ILoyaltyPointsDAO loyaltyPointsDAO;
        private IBookingDAO bookingDAO;
        private IPCDAO pcDAO;
        private IPCConfigDAO pcConfigDAO;
        private IFoodItemDAO foodItemDAO;
        private IComboDAO comboDAO;
        private IVoucherCodeDAO voucherDAO;

        private UserManagementHandler userManagementHandler;
        private BookingManagementHandler bookingManagementHandler;
        private PCManagementHandler pcManagementHandler;
        private FoodManagementHandler foodManagementHandler;
        private VoucherManagementHandler voucherManagementHandler;
        private SystemReportsHandler systemReportsHandler;

        public AdminMenu(Scanner sc, IUserService userService, IBookingService bookingService) {
                this.sc = sc;
                this.userDAO = AppFactory.getUserDAO();
                this.loyaltyPointsDAO = AppFactory.getLoyaltyPointsDAO();
                this.bookingDAO = AppFactory.getBookingDAO();
                this.pcDAO = AppFactory.getPCDAO();
                this.pcConfigDAO = AppFactory.getPCConfigDAO();
                this.foodItemDAO = AppFactory.getFoodItemDAO();
                this.comboDAO = AppFactory.getComboDAO();
                this.voucherDAO = AppFactory.getVoucherCodeDAO();

                this.userManagementHandler = new UserManagementHandler(sc, userService, userDAO, loyaltyPointsDAO);
                this.bookingManagementHandler = new BookingManagementHandler(sc, bookingService, bookingDAO);
                this.pcManagementHandler = new PCManagementHandler(sc, pcDAO, pcConfigDAO);
                this.foodManagementHandler = new FoodManagementHandler(sc, foodItemDAO, comboDAO);
                this.voucherManagementHandler = new VoucherManagementHandler(sc, voucherDAO);
                this.systemReportsHandler = new SystemReportsHandler(sc, userDAO, bookingDAO);

        }

        public int displayAdminMenu(User currentUser) {
                while (true) {
                        ConsoleUtils.clearScreen();

                        String[] logo = {
                                        "____ ___  _ ____  _____ ____    ____  ____  _____ _      ____",
                                        "/   _\\\\  \\///  _ \\/  __//  __\\  /  _ \\/  __\\/  __// \\  /|/  _ \\",
                                        "|  /   \\  / | | //|  \\  |  \\/|  | / \\||  \\/||  \\  | |\\ ||| / \\|",
                                        "|  \\__ / /  | |_\\\\|  /_ |    /  | |-|||    /|  /_ | | \\||| |-||",
                                        "\\____//_/   \\____/\\____\\_/\\_\\  \\_/ \\||\\_/\\_\\\\____\\_/  \\||\\_/ \\|"
                        };

                        ConsoleUtils.printLogoCentered(logo, ConsoleUtils.DEFAULT_WIDTH, 58);

                        System.out.println();

                        String[] options = {
                                        "1. User Management",
                                        "2. Booking Management",
                                        "3. PC Management",
                                        "4. Food Management",
                                        "5. Voucher Management",
                                        "6. System Reports",
                                        "7. Logout"
                        };

                        String[] colors = {
                                        ConsoleUtils.CYAN,
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
                                        ConsoleUtils.centerText(
                                                        ConsoleUtils.YELLOW + "Enter your choice (1-7): "
                                                                        + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH));

                        try {
                                int choice = Integer.parseInt(sc.nextLine());

                                if (choice == 7) {
                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.RED + "Logging out..." + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                        return -1;
                                }

                                handleAdminMenuChoice(choice);
                        } catch (NumberFormatException e) {
                                ConsoleUtils.printCenter(
                                                ConsoleUtils.RED + "Invalid input! Please enter a number."
                                                                + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH);
                        }
                }
        }

        private void handleAdminMenuChoice(int choice) {
                switch (choice) {
                        case 1:
                                userManagementHandler.handleUserManagement();
                                break;
                        case 2:
                                bookingManagementHandler.handleBookingManagement();
                                break;
                        case 3:
                                pcManagementHandler.handlePCManagement();
                                break;
                        case 4:
                                foodManagementHandler.handleFoodManagement();
                                break;
                        case 5:
                                voucherManagementHandler.handleVoucherManagement();
                                break;
                        case 6:
                                systemReportsHandler.handleSystemReports();
                                break;
                        default:
                                ConsoleUtils.printCenter(ConsoleUtils.RED
                                                + "Invalid choice! Please enter a number between 1 and 7."
                                                + ConsoleUtils.RESET, ConsoleUtils.DEFAULT_WIDTH);
                }
        }

        private void handleVoucherManagement() {
                ConsoleUtils.clearScreen();
                ConsoleUtils.printBox("🎟️  VOUCHER MANAGEMENT", 35, ConsoleUtils.DEFAULT_WIDTH);
                System.out.println();

                String[] options = {
                                "1. View all vouchers",
                                "2. Create voucher",
                                "3. Delete voucher",
                                "4. Back"
                };

                ConsoleUtils.printMenuOptions(options, ConsoleUtils.DEFAULT_WIDTH);
                System.out.println();

                System.out.print(
                                ConsoleUtils.centerText(
                                                ConsoleUtils.YELLOW + "Choose an option (1-4): " + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH));

                try {
                        int choice = Integer.parseInt(sc.nextLine());
                        switch (choice) {
                                case 1:
                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.CYAN + "View all vouchers" + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                        break;
                                case 2:
                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.CYAN + "Create voucher" + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                        break;
                                case 3:
                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.CYAN + "Delete voucher" + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                        break;
                                case 4:
                                        return;
                                default:
                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.RED + "Invalid choice!" + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH);
                        }
                } catch (NumberFormatException e) {
                        ConsoleUtils.printCenter(ConsoleUtils.RED + "Invalid input!" + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                }
        }

        private void handleSystemReports() {
                ConsoleUtils.clearScreen();
                ConsoleUtils.printBox("📊 SYSTEM REPORTS", 35, ConsoleUtils.DEFAULT_WIDTH);
                System.out.println();

                String[] options = {
                                "1. Revenue report",
                                "2. Booking statistics",
                                "3. User statistics",
                                "4. Back"
                };

                ConsoleUtils.printMenuOptions(options, ConsoleUtils.DEFAULT_WIDTH);
                System.out.println();

                System.out.print(
                                ConsoleUtils.centerText(
                                                ConsoleUtils.YELLOW + "Choose an option (1-4): " + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH));

                try {
                        int choice = Integer.parseInt(sc.nextLine());
                        switch (choice) {
                                case 1:
                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.CYAN + "Revenue report" + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                        break;
                                case 2:
                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.CYAN + "Booking statistics" + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                        break;
                                case 3:
                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.CYAN + "User statistics" + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                        break;
                                case 4:
                                        return;
                                default:
                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.RED + "Invalid choice!" + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH);
                        }
                } catch (NumberFormatException e) {
                        ConsoleUtils.printCenter(ConsoleUtils.RED + "Invalid input!" + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                }
        }
}
