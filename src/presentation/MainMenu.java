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
    private CustomerMenu customerMenu;

    public MainMenu(User user, Scanner sc) {
        this.currentUser = user;
        IUserService userService = AppFactory.getUserService();
        IBookingService bookingService = AppFactory.getBookingService();
        IPaymentService paymentService = AppFactory.getPaymentService();
        IOrderService orderService = AppFactory.getOrderService();

        this.adminMenu = new AdminMenu(sc, userService, bookingService);
        this.staffMenu = new StaffMenu(sc, bookingService, paymentService, orderService);
        this.customerMenu = new CustomerMenu(sc, bookingService, paymentService, orderService);
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
            case "CUSTOMER":
                customerMenu.displayCustomerMenu(currentUser);
                break;
            default:
                ConsoleUtils.printCenter(ConsoleUtils.RED + "Unknown role!" + ConsoleUtils.RESET,
                        ConsoleUtils.DEFAULT_WIDTH);
        }
    }
}
