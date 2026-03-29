package presentation;

import model.User;
import service.interfaces.IBookingService;
import service.interfaces.IOrderService;
import service.interfaces.IPaymentService;
import service.interfaces.IUserService;
import utils.AppFactory;

import java.util.Scanner;

public class MainMenu {
    private User currentUser;
    private AdminMenu adminMenu;
    private StaffMenu staffMenu;
    private Scanner sc;

    public MainMenu(User user, Scanner sc) {
        this.currentUser = user;
        this.sc = sc;
        IUserService userService = AppFactory.getUserService();
        IBookingService bookingService = AppFactory.getBookingService();
        IPaymentService paymentService = AppFactory.getPaymentService();
        IOrderService orderService = AppFactory.getOrderService();

        this.adminMenu = new AdminMenu(sc, userService, bookingService);
        this.staffMenu = new StaffMenu(sc, bookingService, paymentService, orderService);
    }

    public void displayMainMenu() {
        String roleName = currentUser.getRole() != null ? currentUser.getRole().getRoleName() : "UNKNOWN";

        switch (roleName) {
            case "ADMIN":
                adminMenu.displayAdminMenu(currentUser);
                break;
            case "STAFF":
                staffMenu.displayStaffMenu(currentUser);
                break;
            case "USER":
                // TODO: Implement user menu
                break;
            default:
                ConsoleUtils.printCenter(ConsoleUtils.RED + "Unknown role!" + ConsoleUtils.RESET,
                        ConsoleUtils.DEFAULT_WIDTH);
        }
    }

    private void handleAdminMenu() {
        boolean running = true;
        while (running) {
            int choice = adminMenu.displayAdminMenu(currentUser);

            if (choice == -1) {
                running = false;
                break;
            }

            switch (choice) {
                case 1:
                    // User Management
                    ConsoleUtils.printCenter(ConsoleUtils.YELLOW + "User Management - Coming Soon" + ConsoleUtils.RESET,
                            ConsoleUtils.DEFAULT_WIDTH);
                    pause();
                    break;
                case 2:
                    // PC Management
                    ConsoleUtils.printCenter(ConsoleUtils.YELLOW + "PC Management - Coming Soon" + ConsoleUtils.RESET,
                            ConsoleUtils.DEFAULT_WIDTH);
                    pause();
                    break;
                case 3:
                    // Food Management
                    ConsoleUtils.printCenter(ConsoleUtils.YELLOW + "Food Management - Coming Soon" + ConsoleUtils.RESET,
                            ConsoleUtils.DEFAULT_WIDTH);
                    pause();
                    break;
                case 4:
                    // Voucher Management
                    ConsoleUtils.printCenter(
                            ConsoleUtils.YELLOW + "Voucher Management - Coming Soon" + ConsoleUtils.RESET,
                            ConsoleUtils.DEFAULT_WIDTH);
                    pause();
                    break;
                case 5:
                    // System Reports
                    ConsoleUtils.printCenter(ConsoleUtils.YELLOW + "System Reports - Coming Soon" + ConsoleUtils.RESET,
                            ConsoleUtils.DEFAULT_WIDTH);
                    pause();
                    break;
                default:
                    ConsoleUtils.printCenter(
                            ConsoleUtils.RED + "Invalid choice! Please try again." + ConsoleUtils.RESET,
                            ConsoleUtils.DEFAULT_WIDTH);
                    pause();
            }
        }
    }


    private void pause() {
        System.out.print(ConsoleUtils.centerText(ConsoleUtils.CYAN + "Press Enter to continue..." + ConsoleUtils.RESET,
                ConsoleUtils.DEFAULT_WIDTH));
        try {
            sc.nextLine();
        } catch (Exception e) {
            // Ignore
        }
    }


}
