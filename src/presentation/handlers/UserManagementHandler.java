package presentation.handlers;

import dao.interfaces.IUserDAO;
import dao.interfaces.ILoyaltyPointsDAO;
import model.Role;
import model.User;
import model.UserProfile;
import presentation.ConsoleUtils;
import service.interfaces.IUserService;
import utils.InputValidator;

import java.util.List;
import java.util.Scanner;

import static presentation.ConsoleUtils.centerText;

public class UserManagementHandler {
    private Scanner sc;
    private IUserService userService;
    private IUserDAO userDAO;
    private ILoyaltyPointsDAO loyaltyPointsDAO;

    public static final String CYAN = "\033[96m";

    public UserManagementHandler(Scanner sc, IUserService userService, IUserDAO userDAO,
            ILoyaltyPointsDAO loyaltyPointsDAO) {
        this.sc = sc;
        this.userService = userService;
        this.userDAO = userDAO;
        this.loyaltyPointsDAO = loyaltyPointsDAO;
    }

    public void handleUserManagement() {
        while (true) {
            ConsoleUtils.clearScreen();
            String[] logo = {
                    "▗▖ ▗▖ ▗▄▄▖▗▄▄▄▖▗▄▄▖     ▗▖  ▗▖ ▗▄▖ ▗▖  ▗▖ ▗▄▖  ▗▄▄▖▗▄▄▄▖▗▖  ▗▖▗▄▄▄▖▗▖  ▗▖▗▄▄▄▖",
                    "▐▌ ▐▌▐▌   ▐▌   ▐▌ ▐▌    ▐▛▚▞▜▌▐▌ ▐▌▐▛▚▖▐▌▐▌ ▐▌▐▌   ▐▌   ▐▛▚▞▜▌▐▌   ▐▛▚▖▐▌  █",
                    "▐▌ ▐▌ ▝▀▚▖▐▛▀▀▘▐▛▀▚▖    ▐▌  ▐▌▐▛▀▜▌▐▌ ▝▜▌▐▛▀▜▌▐▌▝▜▌▐▛▀▀▘▐▌  ▐▌▐▛▀▀▘▐▌ ▝▜▌  █",
                    "▝▚▄▞▘▗▄▄▞▘▐▙▄▄▖▐▌ ▐▌    ▐▌  ▐▌▐▌ ▐▌▐▌  ▐▌▐▌ ▐▌▝▚▄▞▘▐▙▄▄▖▐▌  ▐▌▐▙▄▄▖▐▌  ▐▌  █",
                    ""
            };

            ConsoleUtils.printLogoCentered(logo, ConsoleUtils.DEFAULT_WIDTH, 72);

            String[] options = {
                    "1. View all users",
                    "2. Block/Unblock user",
                    "3. View user details",
                    "4. Add loyalty points",
                    "5. Back"
            };

            ConsoleUtils.printMenuOptions(options, ConsoleUtils.DEFAULT_WIDTH);
            System.out.println();

            System.out.print(
                    centerText(
                            ConsoleUtils.YELLOW + "Choose an option (1-5): " + ConsoleUtils.RESET,
                            ConsoleUtils.DEFAULT_WIDTH));

            try {
                int choice = Integer.parseInt(sc.nextLine());
                switch (choice) {
                    case 1:
                        viewAllUsers();
                        break;
                    case 2:
                        blockUnblockUser();
                        break;
                    case 3:
                        viewUserDetails();
                        break;
                    case 4:
                        addLoyaltyPoints();
                        break;
                    case 5:
                        return;
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

    private void viewAllUsers() {
        try {
            List<User> users = userDAO.findAll();

            if (users.isEmpty()) {
                ConsoleUtils.printCenter(ConsoleUtils.YELLOW + "No users found!" + ConsoleUtils.RESET,
                        ConsoleUtils.DEFAULT_WIDTH);
                System.out.print(centerText(
                        ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                        ConsoleUtils.DEFAULT_WIDTH));
                sc.nextLine();
                return;
            }

            ConsoleUtils.clearScreen();
            System.out.println();
            String[] logo = {
                    "▗▖ ▗▖ ▗▄▄▖▗▄▄▄▖▗▄▄▖     ▗▖  ▗▖ ▗▄▖ ▗▖  ▗▖ ▗▄▖  ▗▄▄▖▗▄▄▄▖▗▖  ▗▖▗▄▄▄▖▗▖  ▗▖▗▄▄▄▖",
                    "▐▌ ▐▌▐▌   ▐▌   ▐▌ ▐▌    ▐▛▚▞▜▌▐▌ ▐▌▐▛▚▖▐▌▐▌ ▐▌▐▌   ▐▌   ▐▛▚▞▜▌▐▌   ▐▛▚▖▐▌  █",
                    "▐▌ ▐▌ ▝▀▚▖▐▛▀▀▘▐▛▀▚▖    ▐▌  ▐▌▐▛▀▜▌▐▌ ▝▜▌▐▛▀▜▌▐▌▝▜▌▐▛▀▀▘▐▌  ▐▌▐▛▀▀▘▐▌ ▝▜▌  █",
                    "▝▚▄▞▘▗▄▄▞▘▐▙▄▄▖▐▌ ▐▌    ▐▌  ▐▌▐▌ ▐▌▐▌  ▐▌▐▌ ▐▌▝▚▄▞▘▐▙▄▄▖▐▌  ▐▌▐▙▄▄▖▐▌  ▐▌  █",
                    ""
            };

            ConsoleUtils.printLogoCentered(logo, ConsoleUtils.DEFAULT_WIDTH, 25);
            ConsoleUtils.printCenter(ConsoleUtils.CYAN + "USER LIST" + ConsoleUtils.RESET, 190);
            String[] headers = { "Id", "Code", "Username", "Password", "Email", "Role", "Status", "Deleted",
                    "Created At",
                    "Updated At" };
            int[] columnWidths = { 5, 8, 12, 70, 20, 15, 11, 7, 11, 11 };

            ConsoleUtils.printTableHeader(headers, columnWidths, ConsoleUtils.DEFAULT_WIDTH);

            for (int i = 0; i < users.size(); i++) {
                User user = users.get(i);
                String status = user.getStatus() != null ? user.getStatus().getDescription() : "N/A";
                String roleName = user.getRole() != null ? user.getRole().getRoleName() : "N/A";
                String userCode = generateUserCode(user.getUserId());

                String createdAt = user.getCreatedAt() != null ? user.getCreatedAt().toString().substring(0, 10)
                        : "N/A";
                String updatedAt = user.getUpdatedAt() != null ? user.getUpdatedAt().toString().substring(0, 10)
                        : "N/A";
                String isDeleted = user.isDeleted() ? "Yes" : "No";

                String[] rowData = {
                        user.getUserId() > 0 ? String.valueOf(user.getUserId()) : "N/A",
                        userCode,
                        user.getUsername(),
                        user.getPassword(),
                        user.getEmail(),
                        roleName,
                        status,
                        isDeleted,
                        createdAt,
                        updatedAt
                };

                ConsoleUtils.printTableRow(rowData, columnWidths, ConsoleUtils.DEFAULT_WIDTH);

                if (i < users.size() - 1) {
                    ConsoleUtils.printTableRowSeparator(columnWidths, ConsoleUtils.DEFAULT_WIDTH);
                }
            }

            ConsoleUtils.printTableFooter(columnWidths, ConsoleUtils.DEFAULT_WIDTH);

            System.out.println();
            System.out.print(centerText(
                    ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                    190));
            sc.nextLine();
        } catch (Exception e) {
            ConsoleUtils.printCenter(ConsoleUtils.RED + "Error fetching users: " + e.getMessage() + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH);
        }
    }

    private void blockUnblockUser() {
        try {
            ConsoleUtils.clearScreen();
            System.out.println();
            String[] logo = {
                    "▗▖ ▗▖ ▗▄▄▖▗▄▄▄▖▗▄▄▖     ▗▖  ▗▖ ▗▄▖ ▗▖  ▗▖ ▗▄▖  ▗▄▄▖▗▄▄▄▖▗▖  ▗▖▗▄▄▄▖▗▖  ▗▖▗▄▄▄▖",
                    "▐▌ ▐▌▐▌   ▐▌   ▐▌ ▐▌    ▐▛▚▞▜▌▐▌ ▐▌▐▛▚▖▐▌▐▌ ▐▌▐▌   ▐▌   ▐▛▚▞▜▌▐▌   ▐▛▚▖▐▌  █",
                    "▐▌ ▐▌ ▝▀▚▖▐▛▀▀▘▐▛▀▚▖    ▐▌  ▐▌▐▛▀▜▌▐▌ ▝▜▌▐▛▀▜▌▐▌▝▜▌▐▛▀▀▘▐▌  ▐▌▐▛▀▀▘▐▌ ▝▜▌  █",
                    "▝▚▄▞▘▗▄▄▞▘▐▙▄▄▖▐▌ ▐▌    ▐▌  ▐▌▐▌ ▐▌▐▌  ▐▌▐▌ ▐▌▝▚▄▞▘▐▙▄▄▖▐▌  ▐▌▐▙▄▄▖▐▌  ▐▌  █",
                    ""
            };

            ConsoleUtils.printLogoCentered(logo, ConsoleUtils.DEFAULT_WIDTH, 72);
            ConsoleUtils.printCenter(ConsoleUtils.CYAN + "ＢＬＯＣＫ ／ ＵＮＬＯＣＫ  ＵＳＥＲ" + ConsoleUtils.RESET, 134);
            System.out.print(
                    centerText(
                            ConsoleUtils.YELLOW + "Enter user ID: " + ConsoleUtils.RESET,
                            143));
            try {
                int userId = Integer.parseInt(sc.nextLine());
                User user = userDAO.findById(userId);
                if (user == null) {
                    ConsoleUtils.printCenter(ConsoleUtils.RED + "User not found!" + ConsoleUtils.RESET,
                            ConsoleUtils.DEFAULT_WIDTH);
                    return;
                }

                String userCode = generateUserCode(user.getUserId());
                String fullInfo = "User Code: " + userCode + " | Username: " + user.getUsername()
                        + " | Status: " + user.getStatus().getDescription();
                ConsoleUtils.printCenter(ConsoleUtils.CYAN + fullInfo + ConsoleUtils.RESET,
                        ConsoleUtils.DEFAULT_WIDTH);

                String statusDescription = user.getStatus() == enums.UserStatus.ACTIVE
                        ? "ACTIVE - User account is active and can login"
                        : "BLOCKED - User account is blocked and cannot login";
                ConsoleUtils.printCenter(ConsoleUtils.GREEN + statusDescription + ConsoleUtils.RESET,
                        ConsoleUtils.DEFAULT_WIDTH);
                System.out.println();

                String[] blockUnblockOptions = {
                        "1. Block user",
                        "2. Unblock user",
                        "3. Cancel"
                };

                String[] optionColors = user.getStatus() == enums.UserStatus.ACTIVE
                        ? new String[] { ConsoleUtils.GREEN, ConsoleUtils.GRAY, ConsoleUtils.RED }
                        : new String[] { ConsoleUtils.GRAY, ConsoleUtils.GREEN, ConsoleUtils.RED };

                ConsoleUtils.printMenuOptions(blockUnblockOptions, ConsoleUtils.DEFAULT_WIDTH, optionColors);
                System.out.print(centerText(
                        ConsoleUtils.YELLOW + "Enter your choice (1-3): " + ConsoleUtils.RESET,
                        ConsoleUtils.DEFAULT_WIDTH));
                int choice = InputValidator.getIntInput(sc, "");

                switch (choice) {
                    case 1:
                        user.setStatus(enums.UserStatus.BLOCKED);
                        userService.updateUser(user);
                        System.out.println();
                        ConsoleUtils.printCenter(
                                ConsoleUtils.GREEN + "User blocked successfully" + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH);
                        break;
                    case 2:
                        user.setStatus(enums.UserStatus.ACTIVE);
                        userService.updateUser(user);
                        System.out.println();
                        ConsoleUtils.printCenter(
                                ConsoleUtils.GREEN + "User unblocked successfully" + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH);
                        break;
                    case 3:
                        System.out.println();
                        ConsoleUtils.printCenter(ConsoleUtils.CYAN + "Operation cancelled" + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH);
                        break;
                    default:
                        System.out.println();
                        ConsoleUtils.printCenter(ConsoleUtils.RED + "Invalid choice" + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH);
                }

                System.out.println();
                System.out.print(centerText(
                        ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                        ConsoleUtils.DEFAULT_WIDTH));
                sc.nextLine();
            } catch (NumberFormatException e) {
                ConsoleUtils.printCenter(ConsoleUtils.RED + "Invalid user ID!" + ConsoleUtils.RESET,
                        ConsoleUtils.DEFAULT_WIDTH);
            }
        } catch (Exception e) {
            ConsoleUtils.printCenter(
                    ConsoleUtils.RED + "Error blocking/unblocking user: " + e.getMessage() + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH);
        }
    }

    private void viewUserDetails() {
        try {
            ConsoleUtils.clearScreen();
            System.out.println();
            String[] logo = {
                    "▗▖ ▗▖ ▗▄▄▖▗▄▄▄▖▗▄▄▖     ▗▖  ▗▖ ▗▄▖ ▗▖  ▗▖ ▗▄▖  ▗▄▄▖▗▄▄▄▖▗▖  ▗▖▗▄▄▄▖▗▖  ▗▖▗▄▄▄▖",
                    "▐▌ ▐▌▐▌   ▐▌   ▐▌ ▐▌    ▐▛▚▞▜▌▐▌ ▐▌▐▛▚▖▐▌▐▌ ▐▌▐▌   ▐▌   ▐▛▚▞▜▌▐▌   ▐▛▚▖▐▌  █",
                    "▐▌ ▐▌ ▝▀▚▖▐▛▀▀▘▐▛▀▚▖    ▐▌  ▐▌▐▛▀▜▌▐▌ ▝▜▌▐▛▀▜▌▐▌▝▜▌▐▛▀▀▘▐▌  ▐▌▐▛▀▀▘▐▌ ▝▜▌  █",
                    "▝▚▄▞▘▗▄▄▞▘▐▙▄▄▖▐▌ ▐▌    ▐▌  ▐▌▐▌ ▐▌▐▌  ▐▌▐▌ ▐▌▝▚▄▞▘▐▙▄▄▖▐▌  ▐▌▐▙▄▄▖▐▌  ▐▌  █",
                    ""
            };

            ConsoleUtils.printLogoCentered(logo, ConsoleUtils.DEFAULT_WIDTH, 72);
            ConsoleUtils.printCenter(ConsoleUtils.CYAN + "ＶＩＥＷ  ＵＳＥＲ  ＤＥＴＡＩＬＳ" + ConsoleUtils.RESET, 134);
            System.out.println(
                    centerText(
                            ConsoleUtils.YELLOW + "Enter user ID: " + ConsoleUtils.RESET,
                            143));
            try {
                int userId = Integer.parseInt(sc.nextLine());
                UserProfile userProfile = userDAO.getUserWithProfile(userId);
                if (userProfile == null || userProfile.getUserId() == 0) {
                    ConsoleUtils.printCenter(ConsoleUtils.RED + "User not found!" + ConsoleUtils.RESET,
                            ConsoleUtils.DEFAULT_WIDTH);
                    System.out.print(centerText(
                            ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                            ConsoleUtils.DEFAULT_WIDTH));
                    sc.nextLine();
                    return;
                }
                User user = userDAO.findById(userId);
                if (user != null && user.getRoleId() > 0) {
                    Role role = new Role();
                    role.setRoleId(user.getRoleId());
                    List<User> allUsers = userDAO.findAll();
                    for (User u : allUsers) {
                        if (u.getUserId() == userId && u.getRole() != null) {
                            role = u.getRole();
                            break;
                        }
                    }
                    user.setRole(role);
                }

                System.out.println();
                String userCode = generateUserCode(userId);
                String roleName = user != null && user.getRole() != null ? user.getRole().getRoleName() : "N/A";
                String status = user != null ? user.getStatus().getDescription() : "N/A";
                String fullName = userProfile.getFullName() != null ? userProfile.getFullName() : "N/A";
                String phone = userProfile.getPhone() != null ? userProfile.getPhone() : "N/A";
                String address = userProfile.getAddress() != null ? userProfile.getAddress() : "N/A";
                int loyaltyPoints = userProfile.getLoyaltyPoints();

                String[] headers = { "Field", "Value" };
                int[] columnWidths = { 20, 70 };

                ConsoleUtils.printTableHeader(headers, columnWidths, 146);

                String[][] tableData = {
                        { "User ID", String.valueOf(userId) },
                        { "User Code", userCode },
                        { "Username", user != null ? user.getUsername() : "N/A" },
                        { "Email", user != null ? user.getEmail() : "N/A" },
                        { "Full Name", fullName },
                        { "Phone", phone },
                        { "Address", address },
                        { "Loyalty Points", String.valueOf(loyaltyPoints) },
                        { "Status", status },
                        { "Role", roleName }
                };

                for (int i = 0; i < tableData.length; i++) {
                    ConsoleUtils.printTableRow(tableData[i], columnWidths, 146);
                    if (i < tableData.length - 1) {
                        ConsoleUtils.printTableRowSeparator(columnWidths, 146);
                    }
                }

                ConsoleUtils.printTableFooter(columnWidths, 146);
                System.out.println();
                System.out.println(centerText(
                        ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                        ConsoleUtils.DEFAULT_WIDTH));
                sc.nextLine();
            } catch (NumberFormatException e) {
                ConsoleUtils.printCenter(ConsoleUtils.RED + "Invalid user ID!" + ConsoleUtils.RESET,
                        ConsoleUtils.DEFAULT_WIDTH);
                System.out.println(centerText(
                        ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                        ConsoleUtils.DEFAULT_WIDTH));
                sc.nextLine();
            } catch (Exception e) {
                ConsoleUtils.printCenter(
                        ConsoleUtils.RED + "Error fetching user details: " + e.getMessage() + ConsoleUtils.RESET,
                        ConsoleUtils.DEFAULT_WIDTH);
                System.out.println(centerText(
                        ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                        ConsoleUtils.DEFAULT_WIDTH));
                sc.nextLine();
            }
        } catch (Exception e) {
            ConsoleUtils.printCenter(
                    ConsoleUtils.RED + "Error fetching user details: " + e.getMessage() + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH);
        }
    }

    private String generateUserCode(int userId) {
        return String.format("USR%05d", userId);
    }

    private void addLoyaltyPoints() {
        try {
            ConsoleUtils.clearScreen();
            System.out.println();
            String[] logo = {
                    "▗▖ ▗▖ ▗▄▄▖▗▄▄▄▖▗▄▄▖     ▗▖  ▗▖ ▗▄▖ ▗▖  ▗▖ ▗▄▖  ▗▄▄▖▗▄▄▄▖▗▖  ▗▖▗▄▄▄▖▗▖  ▗▖▗▄▄▄▖",
                    "▐▌ ▐▌▐▌   ▐▌   ▐▌ ▐▌    ▐▛▚▞▜▌▐▌ ▐▌▐▛▚▖▐▌▐▌ ▐▌▐▌   ▐▌   ▐▛▚▞▜▌▐▌   ▐▛▚▖▐▌  █",
                    "▐▌ ▐▌ ▝▀▚▖▐▛▀▀▘▐▛▀▚▖    ▐▌  ▐▌▐▛▀▜▌▐▌ ▝▜▌▐▛▀▜▌▐▌▝▜▌▐▛▀▀▘▐▌  ▐▌▐▛▀▀▘▐▌ ▝▜▌  █",
                    "▝▚▄▞▘▗▄▄▞▘▐▙▄▄▖▐▌ ▐▌    ▐▌  ▐▌▐▌ ▐▌▐▌  ▐▌▐▌ ▐▌▝▚▄▞▘▐▙▄▄▖▐▌  ▐▌▐▙▄▄▖▐▌  ▐▌  █",
                    ""
            };

            ConsoleUtils.printLogoCentered(logo, ConsoleUtils.DEFAULT_WIDTH, 72);
            ConsoleUtils.printCenter(ConsoleUtils.CYAN + "ＡＤＤ  ＬＯＹＡＬＴＹ  ＰＯＩＮＴＳ" + ConsoleUtils.RESET, 134);

            System.out.print(
                    centerText(
                            ConsoleUtils.YELLOW + "Enter user ID: " + ConsoleUtils.RESET,
                            143));
            try {
                int userId = Integer.parseInt(sc.nextLine());
                User user = userDAO.findById(userId);
                if (user == null) {
                    ConsoleUtils.printCenter(ConsoleUtils.RED + "User not found!" + ConsoleUtils.RESET,
                            ConsoleUtils.DEFAULT_WIDTH);
                    System.out.print(centerText(
                            ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                            ConsoleUtils.DEFAULT_WIDTH));
                    sc.nextLine();
                    return;
                }

                String userCode = generateUserCode(user.getUserId());
                String fullInfo = "User Code: " + userCode + " | Username: " + user.getUsername();
                ConsoleUtils.printCenter(ConsoleUtils.CYAN + fullInfo + ConsoleUtils.RESET,
                        ConsoleUtils.DEFAULT_WIDTH);
                int currentPoints = loyaltyPointsDAO.getPoints(userId);
                if (currentPoints == 0) {
                    model.LoyaltyPoints existingPoints = loyaltyPointsDAO.findByUserId(userId);
                    if (existingPoints == null) {
                        model.LoyaltyPoints newLoyaltyPoints = new model.LoyaltyPoints(userId);
                        loyaltyPointsDAO.create(newLoyaltyPoints);
                        ConsoleUtils.printCenter(
                                ConsoleUtils.YELLOW + "Created loyalty points record for user." + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH);
                    }
                }

                System.out.println();
                ConsoleUtils.printCenter(
                        ConsoleUtils.CYAN + "Current Loyalty Points: " + ConsoleUtils.GREEN + currentPoints
                                + ConsoleUtils.RESET,
                        ConsoleUtils.DEFAULT_WIDTH);
                System.out.println();

                System.out.print(
                        centerText(
                                ConsoleUtils.YELLOW + "Enter points to add: " + ConsoleUtils.RESET,
                                143));
                int points = Integer.parseInt(sc.nextLine());

                if (points <= 0) {
                    System.out.println();
                    ConsoleUtils.printCenter(ConsoleUtils.RED + "Points must be greater than 0!" + ConsoleUtils.RESET,
                            ConsoleUtils.DEFAULT_WIDTH);
                    System.out.print(centerText(
                            ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                            ConsoleUtils.DEFAULT_WIDTH));
                    sc.nextLine();
                    return;
                }

                loyaltyPointsDAO.addPoints(userId, points);
                int newPoints = loyaltyPointsDAO.getPoints(userId);

                System.out.println();
                ConsoleUtils.printCenter(
                        ConsoleUtils.GREEN + "Successfully added " + points + " points!" + ConsoleUtils.RESET,
                        ConsoleUtils.DEFAULT_WIDTH);
                System.out.println();
                ConsoleUtils.printCenter(
                        ConsoleUtils.CYAN + "Old Points: " + ConsoleUtils.YELLOW + currentPoints + ConsoleUtils.CYAN +
                                " → New Points: " + ConsoleUtils.GREEN + newPoints + ConsoleUtils.RESET,
                        ConsoleUtils.DEFAULT_WIDTH);

                System.out.println();
                System.out.print(centerText(
                        ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                        ConsoleUtils.DEFAULT_WIDTH));
                sc.nextLine();
            } catch (NumberFormatException e) {
                ConsoleUtils.printCenter(ConsoleUtils.RED + "Invalid input!" + ConsoleUtils.RESET,
                        ConsoleUtils.DEFAULT_WIDTH);
                System.out.print(centerText(
                        ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                        ConsoleUtils.DEFAULT_WIDTH));
                sc.nextLine();
            }
        } catch (Exception e) {
            ConsoleUtils.printCenter(
                    ConsoleUtils.RED + "Error adding loyalty points: " + e.getMessage() + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH);
            System.out.print(centerText(
                    ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH));
            sc.nextLine();
        }
    }
}
