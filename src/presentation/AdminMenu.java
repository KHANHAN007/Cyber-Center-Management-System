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
                                        "/   _\\\\  \\
                                        "|  /   \\  / | |
                                        "|  \\__ / /  | |_\\\\|  /_ |    /  | |-|||    /|  /_ | | \\||| |-||",
                                        "\\____
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
}
