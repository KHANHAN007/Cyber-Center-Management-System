package presentation;

import dao.interfaces.ILoyaltyPointsDAO;
import dao.interfaces.IOTPDAO;
import dao.interfaces.IUserDAO;
import dao.interfaces.IWalletDAO;
import exception.BusinessException;
import model.User;
import service.interfaces.IBookingService;
import service.interfaces.IOrderService;
import service.interfaces.IPaymentService;
import service.interfaces.IUserService;
import utils.AppFactory;
import utils.InputValidator;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static presentation.ConsoleUtils.centerText;

public class LoginMenu {
    private IUserService userService;
    private IUserDAO userDAO;
    private Scanner scanner;
    private static final Map<String, Long> lockedAccounts = new HashMap<>();
    private static final long LOCK_DURATION = 5 * 60 * 1000;

    public LoginMenu(Scanner scanner, IUserService userService, IUserDAO userDAO) {
        this.scanner = scanner;
        this.userService = userService;
        this.userDAO = userDAO;
    }

    public User displayLoginMenu() {
        int width = ConsoleUtils.DEFAULT_WIDTH;

        while (true) {
            ConsoleUtils.clearScreen();

            String[] logo = {
                    "░█▀▀░█░█░█▀▄░█▀▀░█▀▄░░░█▀█░█▀▄░█▀▀░█▀█░█▀█",
                    "░█░░░░█░░█▀▄░█▀▀░█▀▄░░░█▀█░█▀▄░█▀▀░█░█░█▀█",
                    "░▀▀▀░░▀░░▀▀░░▀▀▀░▀░▀░░░▀░▀░▀░▀░▀▀▀░▀░▀░▀░▀"
            };

            ConsoleUtils.printLogoCentered(logo, ConsoleUtils.DEFAULT_WIDTH, 37);

            System.out.println();

            ConsoleUtils.printMenuRow("1. LOGIN", "2. REGISTER", ConsoleUtils.DEFAULT_WIDTH, ConsoleUtils.CYAN,
                    ConsoleUtils.CYAN);

            ConsoleUtils.printAlignedBox("3. EXIT", 64, ConsoleUtils.DEFAULT_WIDTH);

            System.out.println();

            System.out.print(
                    ConsoleUtils.centerText(ConsoleUtils.YELLOW + "Choose option (1-3): " + ConsoleUtils.RESET, width));

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                ConsoleUtils.printCenter(ConsoleUtils.RED + "Please enter a valid number!" + ConsoleUtils.RESET, width);
                continue;
            }

            switch (choice) {
                case 1:
                    User user = handleLogin();
                    if (user != null)
                        return user;
                    break;

                case 2:
                    handleRegister();
                    break;

                case 3:
                    ConsoleUtils.printCenter(ConsoleUtils.RED + "Exiting program..." + ConsoleUtils.RESET, width);
                    System.exit(0);
                    break;

                default:
                    ConsoleUtils.printCenter(ConsoleUtils.RED + "Invalid choice!" + ConsoleUtils.RESET, width);
            }
        }
    }

    private User handleLogin() {
        int width = ConsoleUtils.DEFAULT_WIDTH;
        int maxAttempts = 5;
        int attempts = 0;

        while (attempts < maxAttempts) {
            ConsoleUtils.clearScreen();

            String[] logo = {
                    "░█▀▀░█░█░█▀▄░█▀▀░█▀▄░░░█▀█░█▀▄░█▀▀░█▀█░█▀█",
                    "░█░░░░█░░█▀▄░█▀▀░█▀▄░░░█▀█░█▀▄░█▀▀░█░█░█▀█",
                    "░▀▀▀░░▀░░▀▀░░▀▀▀░▀░▀░░░▀░▀░▀░▀░▀▀▀░▀░▀░▀░▀"
            };

            ConsoleUtils.printLogoCentered(logo, ConsoleUtils.DEFAULT_WIDTH, 41);

            System.out.print(ConsoleUtils.centerText(ConsoleUtils.GREEN + "Username: " + ConsoleUtils.RESET, width));
            String username = scanner.nextLine();

            if (isAccountLocked(username)) {
                displayLockMessage(username, width);
                return null;
            }

            String password = InputValidator.readPasswordInput(scanner, width);

            try {
                User user = userService.login(username, password);

                System.out.println();
                ConsoleUtils.printCenter(ConsoleUtils.GREEN + "Login successful!" + ConsoleUtils.RESET, width);
                ConsoleUtils.printCenter(ConsoleUtils.CYAN + "Welcome, " + username +
                        " (" + user.getRole().getRoleName() + ")" + ConsoleUtils.RESET, width);

                return user;
            } catch (BusinessException e) {
                attempts++;
                int remainingAttempts = maxAttempts - attempts;

                System.out.println();
                ConsoleUtils.printCenter(ConsoleUtils.RED + e.getMessage() + ConsoleUtils.RESET, width);

                if (remainingAttempts > 0) {
                    ConsoleUtils.printCenter(
                            ConsoleUtils.YELLOW + "Remaining attempts: " + remainingAttempts + ConsoleUtils.RESET,
                            width);
                    System.out.print(ConsoleUtils.centerText(
                            ConsoleUtils.YELLOW + "Press Enter to try again..." + ConsoleUtils.RESET, width));
                    scanner.nextLine();
                } else {
                    lockedAccounts.put(username, System.currentTimeMillis());

                    System.out.println();
                    ConsoleUtils.printCenter(
                            ConsoleUtils.RED + "Maximum login attempts exceeded!" + ConsoleUtils.RESET, width);
                    ConsoleUtils.printCenter(
                            ConsoleUtils.YELLOW + "Account locked for 5 minutes." + ConsoleUtils.RESET, width);
                    System.out.print(ConsoleUtils.centerText(
                            ConsoleUtils.YELLOW + "Press Enter to return to login menu..." + ConsoleUtils.RESET,
                            width));
                    scanner.nextLine();
                    return null;
                }
            }
        }

        return null;
    }

    private boolean isAccountLocked(String username) {
        if (lockedAccounts.containsKey(username)) {
            long lockTime = lockedAccounts.get(username);
            long currentTime = System.currentTimeMillis();
            long timePassed = currentTime - lockTime;

            if (timePassed < LOCK_DURATION) {
                return true;
            } else {
                lockedAccounts.remove(username);
                return false;
            }
        }
        return false;
    }

    private void displayLockMessage(String username, int width) {
        long lockTime = lockedAccounts.get(username);
        long currentTime = System.currentTimeMillis();
        long timePassed = currentTime - lockTime;
        long remainingTime = (LOCK_DURATION - timePassed) / 1000;

        System.out.println();
        ConsoleUtils.printCenter(
                ConsoleUtils.RED + "Account locked due to too many failed login attempts!" +
                        ConsoleUtils.RESET,
                width);
        ConsoleUtils.printCenter(
                ConsoleUtils.YELLOW + "Please try again after " + remainingTime + " seconds." +
                        ConsoleUtils.RESET,
                width);
        System.out.print(ConsoleUtils.centerText(
                ConsoleUtils.YELLOW + "Press Enter to return to login menu..." + ConsoleUtils.RESET,
                width));
        scanner.nextLine();
    }

    private void handleRegister() {
        int width = ConsoleUtils.DEFAULT_WIDTH;

        ConsoleUtils.clearScreen();

        String[] logo = {
                "░█▀▀░█░█░█▀▄░█▀▀░█▀▄░░░█▀█░█▀▄░█▀▀░█▀█░█▀█",
                "░█░░░░█░░█▀▄░█▀▀░█▀▄░░░█▀█░█▀▄░█▀▀░█░█░█▀█",
                "░▀▀▀░░▀░░▀▀░░▀▀▀░▀░▀░░░▀░▀░▀░▀░▀▀▀░▀░▀░▀░▀"
        };

        ConsoleUtils.printLogoCentered(logo, ConsoleUtils.DEFAULT_WIDTH, 41);

        String username;
        while (true) {
            System.out.print(ConsoleUtils.centerText(ConsoleUtils.GREEN + "Username: " + ConsoleUtils.RESET, width));
            username = scanner.nextLine();

            if (!InputValidator.isValidUsername(username)) {
                ConsoleUtils.printCenter(
                        ConsoleUtils.RED + InputValidator.getUsernameErrorMessage(username) + ConsoleUtils.RESET,
                        width);
                continue;
            }

            if (userService.usernameExists(username)) {
                ConsoleUtils.printCenter(ConsoleUtils.RED + "Username already exists!" + ConsoleUtils.RESET, width);
                continue;
            }
            break;
        }

        String password = InputValidator.readPasswordInput(scanner, width);

        String fullName;
        while (true) {
            ConsoleUtils.centerText(
                    ConsoleUtils.GREEN + "(This will be displayed on your profile)" + ConsoleUtils.RESET, width);
            System.out.print(ConsoleUtils.centerText(ConsoleUtils.GREEN + "Full Name: " + ConsoleUtils.RESET, width));
            fullName = scanner.nextLine();
            if (InputValidator.isValidFullName(fullName)) {
                break;
            }
            ConsoleUtils.printCenter(
                    ConsoleUtils.RED + InputValidator.getFullNameErrorMessage(fullName) + ConsoleUtils.RESET, width);
        }

        String email;
        while (true) {
            System.out.print(ConsoleUtils.centerText(ConsoleUtils.GREEN + "Email: " + ConsoleUtils.RESET, width));
            email = scanner.nextLine();

            if (!InputValidator.isValidEmail(email)) {
                ConsoleUtils.printCenter(ConsoleUtils.RED + "Invalid email!" + ConsoleUtils.RESET, width);
                continue;
            }

            if (userService.emailExists(email)) {
                ConsoleUtils.printCenter(ConsoleUtils.RED + "Email already exists!" + ConsoleUtils.RESET, width);
                continue;
            }
            break;
        }

        String phone;
        while (true) {
            System.out.print(ConsoleUtils.centerText(ConsoleUtils.GREEN + "Phone: " + ConsoleUtils.RESET, width));
            phone = scanner.nextLine();

            if (InputValidator.isValidPhoneNumber(phone))
                break;

            ConsoleUtils.printCenter(ConsoleUtils.RED + "Invalid phone number!" + ConsoleUtils.RESET, width);
        }

        String address;
        while (true) {
            System.out.print(ConsoleUtils.centerText(ConsoleUtils.GREEN + "Address: " + ConsoleUtils.RESET, width));
            address = scanner.nextLine();

            if (address != null && !address.trim().isEmpty() && address.length() <= 255)
                break;

            ConsoleUtils.printCenter(
                    ConsoleUtils.RED + "Address must not be empty and not exceed 255 characters!" + ConsoleUtils.RESET,
                    width);
        }

        String referralPhone = "";
        while (true) {
            System.out.println();
            System.out.print(ConsoleUtils.centerText(
                    ConsoleUtils.CYAN + "Enter referral phone (optional, press Enter to skip, get 200 bonus points): "
                            + ConsoleUtils.RESET,
                    width));
            referralPhone = scanner.nextLine();

            if (referralPhone == null || referralPhone.trim().isEmpty()) {
                ConsoleUtils.printCenter(ConsoleUtils.YELLOW + "Referral phone skipped." + ConsoleUtils.RESET, width);
                break;
            }

            User referrerUser = userDAO.findUserByPhone(referralPhone.trim());
            if (referrerUser != null) {
                ConsoleUtils.printCenter(ConsoleUtils.GREEN + "Referral phone verified! +200 bonus points for both."
                        + ConsoleUtils.RESET, width);
                break;
            } else {
                System.out.println();
                ConsoleUtils.printCenter(ConsoleUtils.RED + "Phone number not found in system!" + ConsoleUtils.RESET,
                        width);
                System.out.print(ConsoleUtils.centerText(
                        ConsoleUtils.YELLOW + "Try again? (Y/N): " + ConsoleUtils.RESET, width));
                String choice = scanner.nextLine();
                if (!choice.equalsIgnoreCase("Y")) {
                    referralPhone = "";
                    ConsoleUtils.printCenter(ConsoleUtils.YELLOW + "Referral phone skipped." + ConsoleUtils.RESET,
                            width);
                    break;
                }
            }
        }

        try {
            userService.register(username, password, email, fullName, phone, address, referralPhone.trim());

            System.out.println();
            ConsoleUtils.printCenter(ConsoleUtils.GREEN + "Registration successful!" + ConsoleUtils.RESET, width);
            System.out.print(centerText(
                    ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH));
        } catch (BusinessException e) {
            System.out.println();
            ConsoleUtils.printCenter(ConsoleUtils.RED + e.getMessage() + ConsoleUtils.RESET, width);
            System.out.print(centerText(
                    ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH));
            scanner.nextLine();
        }
    }

    public static class CustomerMenu {
        private Scanner sc;
        private IBookingService bookingService;
        private IPaymentService paymentService;
        private IOrderService orderService;
        private IWalletDAO walletDAO;
        private ILoyaltyPointsDAO loyaltyDAO;
        private IOTPDAO otpDAO;

        public CustomerMenu(Scanner sc, IBookingService bookingService, IPaymentService paymentService,
                            IOrderService orderService) {
            this.sc = sc;
            this.bookingService = bookingService;
            this.paymentService = paymentService;
            this.orderService = orderService;
            this.walletDAO = AppFactory.getWalletDAO();
            this.loyaltyDAO = AppFactory.getLoyaltyPointsDAO();
            this.otpDAO = AppFactory.getOTPDAO();
        }
    }
}
