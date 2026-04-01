package presentation.handlers;

import dao.interfaces.IBookingDAO;
import dao.interfaces.IUserDAO;
import presentation.ConsoleUtils;
import model.Booking;
import model.User;
import enums.BookingStatus;
import enums.UserStatus;

import java.util.List;
import java.util.Scanner;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static presentation.ConsoleUtils.centerText;

public class SystemReportsHandler {
    private Scanner sc;
    private IUserDAO userDAO;
    private IBookingDAO bookingDAO;

    public SystemReportsHandler(Scanner sc, IUserDAO userDAO, IBookingDAO bookingDAO) {
        this.sc = sc;
        this.userDAO = userDAO;
        this.bookingDAO = bookingDAO;
    }

    public void handleSystemReports() {
        while (true) {
            ConsoleUtils.clearScreen();
            String[] logo = {
                    " ▗▄▄▖▗▖  ▗▖▗▄▄▖▗▄▄▄▖▗▄▄▄▖▗▖  ▗▖    ▗▄▄▖ ▗▄▄▄▖▗▄▄▖  ▗▄▖ ▗▄▄▖▗▄▄▄▖▗▄▄▖",
                    "▐▌    ▝▚▞▘▐▌     █  ▐▌   ▐▛▚▞▜▌    ▐▌ ▐▌▐▌   ▐▌ ▐▌▐▌ ▐▌▐▌ ▐▌ █ ▐▌   ",
                    " ▝▀▚▖  ▐▌  ▝▀▚▖  █  ▐▛▀▀▘▐▌  ▐▌    ▐▛▀▚▖▐▛▀▀▘▐▛▀▘ ▐▌ ▐▌▐▛▀▚▖ █  ▝▀▚▖",
                    "▗▄▄▞▘  ▐▌ ▗▄▄▞▘  █  ▐▙▄▄▖▐▌  ▐▌    ▐▌ ▐▌▐▙▄▄▖▐▌   ▝▚▄▞▘▐▌ ▐▌ █ ▗▄▄▞▘",
            };

            ConsoleUtils.printLogoCentered(logo, ConsoleUtils.DEFAULT_WIDTH, 58);
            ConsoleUtils.printCenter(ConsoleUtils.CYAN + "━━━━━━━━━━ ＳＹＳＴＥＭ ＲＥＰＯＲＴＳ ━━━━━━━━━━" + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH);
            System.out.println();

            String[] options = {
                    "1. Booking statistics",
                    "2. User statistics",
                    "3. Revenue report",
                    "4. Back"
            };

            String[] colors = {
                    ConsoleUtils.GREEN,
                    ConsoleUtils.GREEN,
                    ConsoleUtils.GREEN,
                    ConsoleUtils.RED
            };

            ConsoleUtils.printMenuOptions(options, ConsoleUtils.DEFAULT_WIDTH, colors);
            System.out.println();

            System.out.print(centerText(
                    ConsoleUtils.YELLOW + "Choose an option (1-4): " + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH));

            try {
                int choice = Integer.parseInt(sc.nextLine());
                switch (choice) {
                    case 1:
                        bookingStatistics();
                        break;
                    case 2:
                        userStatistics();
                        break;
                    case 3:
                        revenueReport();
                        break;
                    case 4:
                        return;
                    default:
                        ConsoleUtils.printCenter(
                                ConsoleUtils.RED + "Invalid choice!" + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH);
                        System.out.println(centerText(
                                ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH));
                        sc.nextLine();
                }
            } catch (NumberFormatException e) {
                ConsoleUtils.printCenter(ConsoleUtils.RED + "Invalid input!" + ConsoleUtils.RESET,
                        ConsoleUtils.DEFAULT_WIDTH);
                System.out.println(centerText(
                        ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                        ConsoleUtils.DEFAULT_WIDTH));
                sc.nextLine();
            }
        }
    }

    private void bookingStatistics() {
        try {
            ConsoleUtils.clearScreen();
            String[] logo = {
                    " ▗▄▄▖▗▖  ▗▖▗▄▄▖▗▄▄▄▖▗▄▄▄▖▗▖  ▗▖    ▗▄▄▖ ▗▄▄▄▖▗▄▄▖  ▗▄▖ ▗▄▄▖▗▄▄▄▖▗▄▄▖",
                    "▐▌    ▝▚▞▘▐▌     █  ▐▌   ▐▛▚▞▜▌    ▐▌ ▐▌▐▌   ▐▌ ▐▌▐▌ ▐▌▐▌ ▐▌ █ ▐▌   ",
                    " ▝▀▚▖  ▐▌  ▝▀▚▖  █  ▐▛▀▀▘▐▌  ▐▌    ▐▛▀▚▖▐▛▀▀▘▐▛▀▘ ▐▌ ▐▌▐▛▀▚▖ █  ▝▀▚▖",
                    "▗▄▄▞▘  ▐▌ ▗▄▄▞▘  █  ▐▙▄▄▖▐▌  ▐▌    ▐▌ ▐▌▐▙▄▄▖▐▌   ▝▚▄▞▘▐▌ ▐▌ █ ▗▄▄▞▘",
            };

            ConsoleUtils.printLogoCentered(logo, ConsoleUtils.DEFAULT_WIDTH, 58);
            ConsoleUtils.printCenter(ConsoleUtils.CYAN + "━━━━━━━━━ ＢＯＯＫＩＮＧ ＳＴＡＴＩＳＴＩＣＳ ━━━━━━━━━" + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH);
            System.out.println();

            List<Booking> allBookings = bookingDAO.findAll();

            int totalBookings = allBookings.size();
            int pending = 0, confirmed = 0, inProgress = 0, completed = 0, cancelled = 0;
            BigDecimal totalRevenue = BigDecimal.ZERO;
            BigDecimal maxBooking = BigDecimal.ZERO;
            BigDecimal minBooking = null;

            for (Booking booking : allBookings) {
                BookingStatus status = booking.getStatus();
                if (status == BookingStatus.PENDING)
                    pending++;
                else if (status == BookingStatus.CONFIRMED)
                    confirmed++;
                else if (status == BookingStatus.IN_PROGRESS)
                    inProgress++;
                else if (status == BookingStatus.COMPLETED) {
                    completed++;
                    double priceValue = booking.getTotalPrice();
                    BigDecimal price = BigDecimal.valueOf(priceValue);
                    if (price.compareTo(BigDecimal.ZERO) > 0) {
                        totalRevenue = totalRevenue.add(price);
                        if (price.compareTo(maxBooking) > 0)
                            maxBooking = price;
                        if (minBooking == null || price.compareTo(minBooking) < 0)
                            minBooking = price;
                    }
                } else if (status == BookingStatus.CANCELLED)
                    cancelled++;
            }

            ConsoleUtils.printCenter(ConsoleUtils.CYAN + "Overview Statistics" + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH);
            System.out.println();

            String[] headers = { "Metric", "Value" };
            int[] columnWidths = { 30, 20 };

            ConsoleUtils.printTableHeader(headers, columnWidths, 140);

            String[][] stats = {
                    { "Total Bookings", String.valueOf(totalBookings) },
                    { "Pending", String.valueOf(pending) },
                    { "Confirmed", String.valueOf(confirmed) },
                    { "In Progress", String.valueOf(inProgress) },
                    { "Completed", String.valueOf(completed) },
                    { "Cancelled", String.valueOf(cancelled) },
                    { "Total Revenue", "₫" + totalRevenue },
                    { "Average Booking Price",
                            completed > 0
                                    ? "₫" + totalRevenue.divide(BigDecimal.valueOf(completed), RoundingMode.HALF_UP)
                                    : "N/A" },
                    { "Max Booking Price", "₫" + (maxBooking.compareTo(BigDecimal.ZERO) > 0 ? maxBooking : 0) },
                    { "Min Booking Price", minBooking != null ? "₫" + minBooking : "N/A" }
            };

            for (int i = 0; i < stats.length; i++) {
                ConsoleUtils.printTableRow(stats[i], columnWidths, 140);
                if (i < stats.length - 1) {
                    ConsoleUtils.printTableRowSeparator(columnWidths, 140);
                }
            }

            ConsoleUtils.printTableFooter(columnWidths, 140);

            System.out.println();
            System.out.print(centerText(
                    ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH));
            sc.nextLine();

        } catch (Exception e) {
            ConsoleUtils.printCenter(ConsoleUtils.RED + "Error: " + e.getMessage() + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH);
            System.out.print(centerText(
                    ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH));
            sc.nextLine();
        }
    }

    private void userStatistics() {
        try {
            ConsoleUtils.clearScreen();
            String[] logo = {
                    " ▗▄▄▖▗▖  ▗▖▗▄▄▖▗▄▄▄▖▗▄▄▄▖▗▖  ▗▖    ▗▄▄▖ ▗▄▄▄▖▗▄▄▖  ▗▄▖ ▗▄▄▖▗▄▄▄▖▗▄▄▖",
                    "▐▌    ▝▚▞▘▐▌     █  ▐▌   ▐▛▚▞▜▌    ▐▌ ▐▌▐▌   ▐▌ ▐▌▐▌ ▐▌▐▌ ▐▌ █ ▐▌   ",
                    " ▝▀▚▖  ▐▌  ▝▀▚▖  █  ▐▛▀▀▘▐▌  ▐▌    ▐▛▀▚▖▐▛▀▀▘▐▛▀▘ ▐▌ ▐▌▐▛▀▚▖ █  ▝▀▚▖",
                    "▗▄▄▞▘  ▐▌ ▗▄▄▞▘  █  ▐▙▄▄▖▐▌  ▐▌    ▐▌ ▐▌▐▙▄▄▖▐▌   ▝▚▄▞▘▐▌ ▐▌ █ ▗▄▄▞▘",
            };

            ConsoleUtils.printLogoCentered(logo, ConsoleUtils.DEFAULT_WIDTH, 58);
            ConsoleUtils.printCenter(ConsoleUtils.CYAN + "━━━━━━━━━━ ＵＳＥＲ ＳＴＡＴＩＳＴＩＣＳ ━━━━━━━━━━" + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH);
            System.out.println();

            List<User> allUsers = userDAO.findAll();

            int totalUsers = allUsers.size();
            int active = 0, inactive = 0, blocked = 0;
            int admin = 0, staff = 0, customer = 0;

            for (User user : allUsers) {
                UserStatus status = user.getStatus();
                if (status == UserStatus.ACTIVE)
                    active++;
                else if (status == UserStatus.INACTIVE)
                    inactive++;
                else if (status == UserStatus.BLOCKED)
                    blocked++;

                if (user.getRoleId() == 1)
                    admin++;
                else if (user.getRoleId() == 2)
                    staff++;
                else if (user.getRoleId() == 3)
                    customer++;
            }

            ConsoleUtils.printCenter(ConsoleUtils.CYAN + "Overview Statistics" + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH);
            System.out.println();

            String[] headers = { "Metric", "Value" };
            int[] columnWidths = { 30, 20 };

            ConsoleUtils.printTableHeader(headers, columnWidths, 140);

            String[][] stats = {
                    { "Total Users", String.valueOf(totalUsers) },
                    { "Active Users", String.valueOf(active) },
                    { "Inactive Users", String.valueOf(inactive) },
                    { "Blocked Users", String.valueOf(blocked) },
                    { "Admins", String.valueOf(admin) },
                    { "Staff", String.valueOf(staff) },
                    { "Customers", String.valueOf(customer) }
            };

            for (int i = 0; i < stats.length; i++) {
                ConsoleUtils.printTableRow(stats[i], columnWidths, 140);
                if (i < stats.length - 1) {
                    ConsoleUtils.printTableRowSeparator(columnWidths, 140);
                }
            }

            ConsoleUtils.printTableFooter(columnWidths, 140);

            System.out.println();
            System.out.print(centerText(
                    ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH));
            sc.nextLine();

        } catch (Exception e) {
            ConsoleUtils.printCenter(ConsoleUtils.RED + "Error: " + e.getMessage() + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH);
            System.out.print(centerText(
                    ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH));
            sc.nextLine();
        }
    }

    private void revenueReport() {
        try {
            ConsoleUtils.clearScreen();
            String[] logo = {
                    " ▗▄▄▖▗▖  ▗▖▗▄▄▖▗▄▄▄▖▗▄▄▄▖▗▖  ▗▖    ▗▄▄▖ ▗▄▄▄▖▗▄▄▖  ▗▄▖ ▗▄▄▖▗▄▄▄▖▗▄▄▖",
                    "▐▌    ▝▚▞▘▐▌     █  ▐▌   ▐▛▚▞▜▌    ▐▌ ▐▌▐▌   ▐▌ ▐▌▐▌ ▐▌▐▌ ▐▌ █ ▐▌   ",
                    " ▝▀▚▖  ▐▌  ▝▀▚▖  █  ▐▛▀▀▘▐▌  ▐▌    ▐▛▀▚▖▐▛▀▀▘▐▛▀▘ ▐▌ ▐▌▐▛▀▚▖ █  ▝▀▚▖",
                    "▗▄▄▞▘  ▐▌ ▗▄▄▞▘  █  ▐▙▄▄▖▐▌  ▐▌    ▐▌ ▐▌▐▙▄▄▖▐▌   ▝▚▄▞▘▐▌ ▐▌ █ ▗▄▄▞▘",
            };

            ConsoleUtils.printLogoCentered(logo, ConsoleUtils.DEFAULT_WIDTH, 58);
            ConsoleUtils.printCenter(
                    ConsoleUtils.CYAN + "━━━━━━━━━━━ ＲＥＶＥＮＵＥ ＲＥＰＯＲＴ ━━━━━━━━━━━" + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH);
            System.out.println();

            List<Booking> allBookings = bookingDAO.findAll();

            BigDecimal totalRevenue = BigDecimal.ZERO;
            BigDecimal totalFromCompleted = BigDecimal.ZERO;
            int completedCount = 0, cancelledCount = 0;

            for (Booking booking : allBookings) {
                if (booking.getStatus() == BookingStatus.COMPLETED) {
                    completedCount++;
                    double priceValue = booking.getTotalPrice();
                    BigDecimal price = BigDecimal.valueOf(priceValue);
                    totalFromCompleted = totalFromCompleted.add(price);
                    totalRevenue = totalRevenue.add(price);
                } else if (booking.getStatus() == BookingStatus.CANCELLED) {
                    cancelledCount++;
                }
            }

            ConsoleUtils.printCenter(ConsoleUtils.CYAN + "Revenue Summary" + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH);
            System.out.println();

            String[] headers = { "Category", "Amount" };
            int[] columnWidths = { 30, 20 };

            ConsoleUtils.printTableHeader(headers, columnWidths, 140);

            String[][] stats = {
                    { "Total Revenue", "₫" + totalRevenue },
                    { "Revenue from Completed Bookings", "₫" + totalFromCompleted },
                    { "Completed Bookings Count", String.valueOf(completedCount) },
                    { "Average per Booking",
                            completedCount > 0 ? "₫" + totalFromCompleted.divide(BigDecimal.valueOf(completedCount),
                                    RoundingMode.HALF_UP) : "N/A" },
                    { "Cancelled Bookings (Lost)", String.valueOf(cancelledCount) }
            };

            for (int i = 0; i < stats.length; i++) {
                ConsoleUtils.printTableRow(stats[i], columnWidths, 140);
                if (i < stats.length - 1) {
                    ConsoleUtils.printTableRowSeparator(columnWidths, 140);
                }
            }

            ConsoleUtils.printTableFooter(columnWidths, 140);

            System.out.println();
            System.out.print(centerText(
                    ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH));
            sc.nextLine();

        } catch (Exception e) {
            ConsoleUtils.printCenter(ConsoleUtils.RED + "Error: " + e.getMessage() + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH);
            System.out.print(centerText(
                    ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH));
            sc.nextLine();
        }
    }
}
