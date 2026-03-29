package presentation.handlers;

import dao.interfaces.IBookingDAO;
import model.Booking;
import presentation.ConsoleUtils;
import service.interfaces.IBookingService;

import java.util.List;
import java.util.Scanner;

import static presentation.ConsoleUtils.centerText;

public class BookingManagementHandler {
    private Scanner sc;
    private IBookingService bookingService;
    private IBookingDAO bookingDAO;

    public BookingManagementHandler(Scanner sc, IBookingService bookingService, IBookingDAO bookingDAO) {
        this.sc = sc;
        this.bookingService = bookingService;
        this.bookingDAO = bookingDAO;
    }

    public void handleBookingManagement() {
        while (true) {
            ConsoleUtils.clearScreen();
            String[] logo = {
                    "▗▄▄▖  ▗▄▖  ▗▄▖ ▗▖ ▗▖▗▄▄▄▖▗▖  ▗▖ ▗▄▄▖    ▗▖  ▗▖ ▗▄▖ ▗▖  ▗▖ ▗▄▖  ▗▄▄▖▗▄▄▄▖▗▖  ▗▖▗▄▄▄▖▗▖  ▗▖▗▄▄▄▖",
                    "▐▌ ▐▌▐▌ ▐▌▐▌ ▐▌▐▌▗▞▘  █  ▐▛▚▖▐▌▐▌       ▐▛▚▞▜▌▐▌ ▐▌▐▛▚▖▐▌▐▌ ▐▌▐▌   ▐▌   ▐▛▚▞▜▌▐▌   ▐▛▚▖▐▌  █",
                    "▐▛▀▚▖▐▌ ▐▌▐▌ ▐▌▐▛▚▖   █  ▐▌ ▝▜▌▐▌▝▜▌    ▐▌  ▐▌▐▛▀▜▌▐▌ ▝▜▌▐▛▀▜▌▐▌▝▜▌▐▛▀▀▘▐▌  ▐▌▐▛▀▀▘▐▌ ▝▜▌  █",
                    "▐▙▄▞▘▝▚▄▞▘▝▚▄▞▘▐▌ ▐▌▗▄█▄▖▐▌  ▐▌▝▚▄▞▘    ▐▌  ▐▌▐▌ ▐▌▐▌  ▐▌▐▌ ▐▌▝▚▄▞▘▐▙▄▄▖▐▌  ▐▌▐▙▄▄▖▐▌  ▐▌  █",
                    ""
            };

            ConsoleUtils.printLogoCentered(logo, ConsoleUtils.DEFAULT_WIDTH, 88);

            String[] options = {
                    "1. View pending bookings",
                    "2. Confirm booking",
                    "3. Cancel booking",
                    "4. View booking details",
                    "5. View all bookings",
                    "6. Back"
            };

            String[] colors = {
                    ConsoleUtils.GREEN,
                    ConsoleUtils.GREEN,
                    ConsoleUtils.ORANGE,
                    ConsoleUtils.GREEN,
                    ConsoleUtils.GREEN,
                    ConsoleUtils.RED
            };

            ConsoleUtils.printMenuOptions(options, ConsoleUtils.DEFAULT_WIDTH, colors);
            System.out.println();

            System.out.print(
                    centerText(
                            ConsoleUtils.YELLOW + "Choose an option (1-6): " + ConsoleUtils.RESET,
                            ConsoleUtils.DEFAULT_WIDTH));

            try {
                int choice = Integer.parseInt(sc.nextLine());
                switch (choice) {
                    case 1:
                        viewPendingBookings();
                        break;
                    case 2:
                        confirmBooking();
                        break;
                    case 3:
                        cancelBooking();
                        break;
                    case 4:
                        viewBookingDetail();
                        break;
                    case 5:
                        viewAllBookings();
                        break;
                    case 6:
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

    private void viewPendingBookings() {
        try {
            ConsoleUtils.clearScreen();
            System.out.println();
            String[] logo = {
                    "▗▄▄▖  ▗▄▖  ▗▄▖ ▗▖ ▗▖▗▄▄▄▖▗▖  ▗▖ ▗▄▄▖    ▗▖  ▗▖ ▗▄▖ ▗▖  ▗▖ ▗▄▖  ▗▄▄▖▗▄▄▄▖▗▖  ▗▖▗▄▄▄▖▗▖  ▗▖▗▄▄▄▖",
                    "▐▌ ▐▌▐▌ ▐▌▐▌ ▐▌▐▌▗▞▘  █  ▐▛▚▖▐▌▐▌       ▐▛▚▞▜▌▐▌ ▐▌▐▛▚▖▐▌▐▌ ▐▌▐▌   ▐▌   ▐▛▚▞▜▌▐▌   ▐▛▚▖▐▌  █",
                    "▐▛▀▚▖▐▌ ▐▌▐▌ ▐▌▐▛▚▖   █  ▐▌ ▝▜▌▐▌▝▜▌    ▐▌  ▐▌▐▛▀▜▌▐▌ ▝▜▌▐▛▀▜▌▐▌▝▜▌▐▛▀▀▘▐▌  ▐▌▐▛▀▀▘▐▌ ▝▜▌  █",
                    "▐▙▄▞▘▝▚▄▞▘▝▚▄▞▘▐▌ ▐▌▗▄█▄▖▐▌  ▐▌▝▚▄▞▘    ▐▌  ▐▌▐▌ ▐▌▐▌  ▐▌▐▌ ▐▌▝▚▄▞▘▐▙▄▄▖▐▌  ▐▌▐▙▄▄▖▐▌  ▐▌  █",
                    ""
            };

            ConsoleUtils.printLogoCentered(logo, ConsoleUtils.DEFAULT_WIDTH, 88);
            ConsoleUtils.printCenter(ConsoleUtils.CYAN + "ＰＥＮＤＩＮＧ  ＢＯＯＫＩＮＧＳ" + ConsoleUtils.RESET, 135);

            List<Booking> bookings = bookingDAO.findPendingBookings();

            if (bookings == null || bookings.isEmpty()) {
                ConsoleUtils.printCenter(ConsoleUtils.YELLOW + "No bookings found!" + ConsoleUtils.RESET,
                        ConsoleUtils.DEFAULT_WIDTH);
                System.out.print(centerText(
                        ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                        ConsoleUtils.DEFAULT_WIDTH));
                sc.nextLine();
                return;
            }

            String[] headers = { "ID", "Code", "User ID", "Slot ID", "Status", "Created At" };
            int[] columnWidths = { 5, 12, 8, 8, 20, 15 };

            ConsoleUtils.printTableHeader(headers, columnWidths, 145);

            for (int i = 0; i < bookings.size(); i++) {
                Booking booking = bookings.get(i);
                String status = booking.getStatus() != null ? booking.getStatus().getDescription() : "N/A";
                String createdAt = booking.getCreatedAt() != null ? booking.getCreatedAt().toString().substring(0, 10)
                        : "N/A";

                String[] rowData = {
                        String.valueOf(booking.getBookingId()),
                        booking.getBookingCode() != null ? booking.getBookingCode() : "N/A",
                        String.valueOf(booking.getUserId()),
                        String.valueOf(booking.getSlotId()),
                        status,
                        createdAt
                };

                ConsoleUtils.printTableRow(rowData, columnWidths, 145);

                if (i < bookings.size() - 1) {
                    ConsoleUtils.printTableRowSeparator(columnWidths, 145);
                }
            }

            ConsoleUtils.printTableFooter(columnWidths, 145);
            System.out.println();
            System.out.print(centerText(
                    ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH));
            sc.nextLine();

        } catch (Exception e) {
            ConsoleUtils.printCenter(ConsoleUtils.RED + "Error: " + e.getMessage() + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH);
        }
    }

    private void confirmBooking() {
        try {
            ConsoleUtils.clearScreen();
            System.out.println();
            String[] logo = {
                    "▗▄▄▖  ▗▄▖  ▗▄▖ ▗▖ ▗▖▗▄▄▄▖▗▖  ▗▖ ▗▄▄▖    ▗▖  ▗▖ ▗▄▖ ▗▖  ▗▖ ▗▄▖  ▗▄▄▖▗▄▄▄▖▗▖  ▗▖▗▄▄▄▖▗▖  ▗▖▗▄▄▄▖",
                    "▐▌ ▐▌▐▌ ▐▌▐▌ ▐▌▐▌▗▞▘  █  ▐▛▚▖▐▌▐▌       ▐▛▚▞▜▌▐▌ ▐▌▐▛▚▖▐▌▐▌ ▐▌▐▌   ▐▌   ▐▛▚▞▜▌▐▌   ▐▛▚▖▐▌  █",
                    "▐▛▀▚▖▐▌ ▐▌▐▌ ▐▌▐▛▚▖   █  ▐▌ ▝▜▌▐▌▝▜▌    ▐▌  ▐▌▐▛▀▜▌▐▌ ▝▜▌▐▛▀▜▌▐▌▝▜▌▐▛▀▀▘▐▌  ▐▌▐▛▀▀▘▐▌ ▝▜▌  █",
                    "▐▙▄▞▘▝▚▄▞▘▝▚▄▞▘▐▌ ▐▌▗▄█▄▖▐▌  ▐▌▝▚▄▞▘    ▐▌  ▐▌▐▌ ▐▌▐▌  ▐▌▐▌ ▐▌▝▚▄▞▘▐▙▄▄▖▐▌  ▐▌▐▙▄▄▖▐▌  ▐▌  █",
                    ""
            };

            ConsoleUtils.printLogoCentered(logo, ConsoleUtils.DEFAULT_WIDTH, 88);
            ConsoleUtils.printCenter(ConsoleUtils.CYAN + "ＣＡＮＣＥＬ  ＢＯＯＫＩＮＧ" + ConsoleUtils.RESET, 145);

            System.out.print(centerText(
                    ConsoleUtils.YELLOW + "Enter booking ID to confirm: " + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH));

            try {
                int bookingId = Integer.parseInt(sc.nextLine());
                Booking booking = bookingDAO.findById(bookingId);

                if (booking == null) {
                    ConsoleUtils.printCenter(ConsoleUtils.RED + "Booking not found!" + ConsoleUtils.RESET,
                            ConsoleUtils.DEFAULT_WIDTH);
                } else if (booking.getStatus().toString().equals("PENDING")) {
                    booking.setStatus(enums.BookingStatus.CONFIRMED);
                    bookingDAO.update(booking);
                    System.out.println();
                    ConsoleUtils.printCenter(
                            ConsoleUtils.GREEN + "Booking confirmed successfully!" + ConsoleUtils.RESET,
                            ConsoleUtils.DEFAULT_WIDTH);
                    ConsoleUtils.printCenter(
                            ConsoleUtils.GREEN + "Booking ID: " + booking.getBookingId() + " | Status: CONFIRMED"
                                    + ConsoleUtils.RESET,
                            ConsoleUtils.DEFAULT_WIDTH);
                } else {
                    System.out.println();
                    ConsoleUtils.printCenter(
                            ConsoleUtils.YELLOW + "Booking is already " + booking.getStatus().getDescription()
                                    + ConsoleUtils.RESET,
                            ConsoleUtils.DEFAULT_WIDTH);
                }
            } catch (NumberFormatException e) {
                ConsoleUtils.printCenter(ConsoleUtils.RED + "Invalid booking ID!" + ConsoleUtils.RESET,
                        ConsoleUtils.DEFAULT_WIDTH);
            }

            System.out.print(centerText(
                    ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH));
            sc.nextLine();

        } catch (Exception e) {
            ConsoleUtils.printCenter(ConsoleUtils.RED + "Error: " + e.getMessage() + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH);
        }
    }

    private void cancelBooking() {
        try {
            ConsoleUtils.clearScreen();
            System.out.println();
            String[] logo = {
                    "▗▄▄▖  ▗▄▖  ▗▄▖ ▗▖ ▗▖▗▄▄▄▖▗▖  ▗▖ ▗▄▄▖    ▗▖  ▗▖ ▗▄▖ ▗▖  ▗▖ ▗▄▖  ▗▄▄▖▗▄▄▄▖▗▖  ▗▖▗▄▄▄▖▗▖  ▗▖▗▄▄▄▖",
                    "▐▌ ▐▌▐▌ ▐▌▐▌ ▐▌▐▌▗▞▘  █  ▐▛▚▖▐▌▐▌       ▐▛▚▞▜▌▐▌ ▐▌▐▛▚▖▐▌▐▌ ▐▌▐▌   ▐▌   ▐▛▚▞▜▌▐▌   ▐▛▚▖▐▌  █",
                    "▐▛▀▚▖▐▌ ▐▌▐▌ ▐▌▐▛▚▖   █  ▐▌ ▝▜▌▐▌▝▜▌    ▐▌  ▐▌▐▛▀▜▌▐▌ ▝▜▌▐▛▀▜▌▐▌▝▜▌▐▛▀▀▘▐▌  ▐▌▐▛▀▀▘▐▌ ▝▜▌  █",
                    "▐▙▄▞▘▝▚▄▞▘▝▚▄▞▘▐▌ ▐▌▗▄█▄▖▐▌  ▐▌▝▚▄▞▘    ▐▌  ▐▌▐▌ ▐▌▐▌  ▐▌▐▌ ▐▌▝▚▄▞▘▐▙▄▄▖▐▌  ▐▌▐▙▄▄▖▐▌  ▐▌  █",
                    ""
            };

            ConsoleUtils.printLogoCentered(logo, ConsoleUtils.DEFAULT_WIDTH, 88);
            ConsoleUtils.printCenter(ConsoleUtils.ORANGE + "CANCEL BOOKING" + ConsoleUtils.RESET, 190);

            System.out.print(centerText(
                    ConsoleUtils.YELLOW + "Enter booking ID to cancel: " + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH));

            try {
                int bookingId = Integer.parseInt(sc.nextLine());
                Booking booking = bookingDAO.findById(bookingId);

                if (booking == null) {
                    ConsoleUtils.printCenter(ConsoleUtils.RED + "Booking not found!" + ConsoleUtils.RESET,
                            ConsoleUtils.DEFAULT_WIDTH);
                } else if (!booking.getStatus().toString().equals("PENDING")) {
                    System.out.println();
                    ConsoleUtils.printCenter(
                            ConsoleUtils.YELLOW + "⚠️ Only PENDING bookings can be cancelled!" + ConsoleUtils.RESET,
                            ConsoleUtils.DEFAULT_WIDTH);
                    ConsoleUtils.printCenter(
                            ConsoleUtils.YELLOW + "Current status: " + booking.getStatus().getDescription()
                                    + ConsoleUtils.RESET,
                            ConsoleUtils.DEFAULT_WIDTH);
                } else {
                    booking.setStatus(enums.BookingStatus.CANCELLED);
                    bookingDAO.update(booking);
                    System.out.println();
                    ConsoleUtils.printCenter(
                            ConsoleUtils.RED + "Booking cancelled successfully!" + ConsoleUtils.RESET,
                            ConsoleUtils.DEFAULT_WIDTH);
                    ConsoleUtils.printCenter(
                            ConsoleUtils.RED + "Booking ID: " + booking.getBookingId() + " | Status: CANCELLED"
                                    + ConsoleUtils.RESET,
                            ConsoleUtils.DEFAULT_WIDTH);
                }
            } catch (NumberFormatException e) {
                ConsoleUtils.printCenter(ConsoleUtils.RED + "Invalid booking ID!" + ConsoleUtils.RESET,
                        ConsoleUtils.DEFAULT_WIDTH);
            }

            System.out.print(centerText(
                    ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH));
            sc.nextLine();

        } catch (Exception e) {
            ConsoleUtils.printCenter(ConsoleUtils.RED + "Error: " + e.getMessage() + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH);
        }
    }

    private void viewBookingDetail() {
        try {
            ConsoleUtils.clearScreen();
            System.out.println();
            String[] logo = {
                    "▗▄▄▖  ▗▄▖  ▗▄▖ ▗▖ ▗▖▗▄▄▄▖▗▖  ▗▖ ▗▄▄▖    ▗▖  ▗▖ ▗▄▖ ▗▖  ▗▖ ▗▄▖  ▗▄▄▖▗▄▄▄▖▗▖  ▗▖▗▄▄▄▖▗▖  ▗▖▗▄▄▄▖",
                    "▐▌ ▐▌▐▌ ▐▌▐▌ ▐▌▐▌▗▞▘  █  ▐▛▚▖▐▌▐▌       ▐▛▚▞▜▌▐▌ ▐▌▐▛▚▖▐▌▐▌ ▐▌▐▌   ▐▌   ▐▛▚▞▜▌▐▌   ▐▛▚▖▐▌  █",
                    "▐▛▀▚▖▐▌ ▐▌▐▌ ▐▌▐▛▚▖   █  ▐▌ ▝▜▌▐▌▝▜▌    ▐▌  ▐▌▐▛▀▜▌▐▌ ▝▜▌▐▛▀▜▌▐▌▝▜▌▐▛▀▀▘▐▌  ▐▌▐▛▀▀▘▐▌ ▝▜▌  █",
                    "▐▙▄▞▘▝▚▄▞▘▝▚▄▞▘▐▌ ▐▌▗▄█▄▖▐▌  ▐▌▝▚▄▞▘    ▐▌  ▐▌▐▌ ▐▌▐▌  ▐▌▐▌ ▐▌▝▚▄▞▘▐▙▄▄▖▐▌  ▐▌▐▙▄▄▖▐▌  ▐▌  █",
                    ""
            };

            ConsoleUtils.printLogoCentered(logo, ConsoleUtils.DEFAULT_WIDTH, 88);
            ConsoleUtils.printCenter(ConsoleUtils.YELLOW + "ＢＯＯＫＩＮＧ  ＤＥＴＡＩＬＳ" + ConsoleUtils.RESET, 145);

            System.out.print(centerText(
                    ConsoleUtils.YELLOW + "Enter booking ID: " + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH));

            try {
                int bookingId = Integer.parseInt(sc.nextLine().trim());
                Booking booking = bookingDAO.findById(bookingId);

                if (booking == null) {
                    ConsoleUtils.printCenter(ConsoleUtils.RED + "Booking not found!" + ConsoleUtils.RESET,
                            ConsoleUtils.DEFAULT_WIDTH);
                } else {
                    System.out.println();
                    displayBookingDetailGrouped(booking);
                }
            } catch (NumberFormatException e) {
                ConsoleUtils.printCenter(ConsoleUtils.RED + "Invalid booking ID!" + ConsoleUtils.RESET,
                        ConsoleUtils.DEFAULT_WIDTH);
            }

            System.out.println(centerText(
                    ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH));
            sc.nextLine();

        } catch (Exception e) {
            ConsoleUtils.printCenter(ConsoleUtils.RED + "Error: " + e.getMessage() + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH);
        }
    }

    private void displayBookingDetailGrouped(Booking booking) {
        System.out.println();

        String[] headers = { "Attribute", "Value" };
        int[] columnWidths = { 20, 70 };

        ConsoleUtils.printTableHeader(headers, columnWidths, 145);

        String[][] data = {
                { "Booking ID", String.valueOf(booking.getBookingId()) },
                { "Booking Code", booking.getBookingCode() != null ? booking.getBookingCode() : "N/A" },
                { "User ID", String.valueOf(booking.getUserId()) },
                { "PC ID", String.valueOf(booking.getPcId()) },
                { "Slot ID", String.valueOf(booking.getSlotId()) },
                { "Status", booking.getStatus() != null ? booking.getStatus().getDescription() : "N/A" },
                { "Total Price",
                        booking.getTotalPrice() > 0 ? ConsoleUtils.formatVND(booking.getTotalPrice()) : "N/A" },
                { "Voucher ID",
                        booking.getVoucherId() != null && booking.getVoucherId() != 0
                                ? String.valueOf(booking.getVoucherId())
                                : "N/A" },
                { "Discount Amount",
                        booking.getDiscountAmount() != null && booking.getDiscountAmount() > 0
                                ? "$" + booking.getDiscountAmount()
                                : "N/A" },
                { "Start Time", booking.getStartTime() != null ? booking.getStartTime().toString() : "N/A" },
                { "End Time", booking.getEndTime() != null ? booking.getEndTime().toString() : "N/A" },
                { "Created At", booking.getCreatedAt() != null ? booking.getCreatedAt().toString() : "N/A" }
        };

        for (int i = 0; i < data.length; i++) {
            ConsoleUtils.printTableRow(data[i], columnWidths, 145);
            if (i < data.length - 1) {
                ConsoleUtils.printTableRowSeparator(columnWidths, 145);
            }
        }

        ConsoleUtils.printTableFooter(columnWidths, 145);
    }

    private void viewAllBookings() {
        try {
            ConsoleUtils.clearScreen();
            System.out.println();
            String[] logo = {
                    "▗▄▄▖  ▗▄▖  ▗▄▖ ▗▖ ▗▖▗▄▄▄▖▗▖  ▗▖ ▗▄▄▖    ▗▖  ▗▖ ▗▄▖ ▗▖  ▗▖ ▗▄▖  ▗▄▄▖▗▄▄▄▖▗▖  ▗▖▗▄▄▄▖▗▖  ▗▖▗▄▄▄▖",
                    "▐▌ ▐▌▐▌ ▐▌▐▌ ▐▌▐▌▗▞▘  █  ▐▛▚▖▐▌▐▌       ▐▛▚▞▜▌▐▌ ▐▌▐▛▚▖▐▌▐▌ ▐▌▐▌   ▐▌   ▐▛▚▞▜▌▐▌   ▐▛▚▖▐▌  █",
                    "▐▛▀▚▖▐▌ ▐▌▐▌ ▐▌▐▛▚▖   █  ▐▌ ▝▜▌▐▌▝▜▌    ▐▌  ▐▌▐▛▀▜▌▐▌ ▝▜▌▐▛▀▜▌▐▌▝▜▌▐▛▀▀▘▐▌  ▐▌▐▛▀▀▘▐▌ ▝▜▌  █",
                    "▐▙▄▞▘▝▚▄▞▘▝▚▄▞▘▐▌ ▐▌▗▄█▄▖▐▌  ▐▌▝▚▄▞▘    ▐▌  ▐▌▐▌ ▐▌▐▌  ▐▌▐▌ ▐▌▝▚▄▞▘▐▙▄▄▖▐▌  ▐▌▐▙▄▄▖▐▌  ▐▌  █",
                    ""
            };

            ConsoleUtils.printLogoCentered(logo, ConsoleUtils.DEFAULT_WIDTH, 88);
            ConsoleUtils.printCenter(ConsoleUtils.GRAY + "ＡＬＬ  ＢＯＯＫＩＮＧＳ" + ConsoleUtils.RESET, 135);

            List<Booking> bookings = bookingDAO.findAll();

            if (bookings == null || bookings.isEmpty()) {
                ConsoleUtils.printCenter(ConsoleUtils.YELLOW + "No bookings found!" + ConsoleUtils.RESET,
                        ConsoleUtils.DEFAULT_WIDTH);
                System.out.println(centerText(
                        ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                        ConsoleUtils.DEFAULT_WIDTH));
                sc.nextLine();
                return;
            }

            String[] headers = { "ID", "Code", "User ID", "PC ID", "Status", "Total Price", "Created At" };
            int[] columnWidths = { 5, 12, 8, 6, 15, 12, 15 };

            ConsoleUtils.printTableHeader(headers, columnWidths, 145);

            for (int i = 0; i < bookings.size(); i++) {
                Booking booking = bookings.get(i);
                String status = booking.getStatus() != null ? booking.getStatus().getDescription() : "N/A";
                String createdAt = booking.getCreatedAt() != null ? booking.getCreatedAt().toString().substring(0, 10)
                        : "N/A";
                String totalPrice = booking.getTotalPrice() > 0 ? ConsoleUtils.formatVND(booking.getTotalPrice())
                        : "N/A";

                String[] rowData = {
                        String.valueOf(booking.getBookingId()),
                        booking.getBookingCode() != null ? booking.getBookingCode() : "N/A",
                        String.valueOf(booking.getUserId()),
                        String.valueOf(booking.getPcId()),
                        status,
                        totalPrice,
                        createdAt
                };

                ConsoleUtils.printTableRow(rowData, columnWidths, 145);

                if (i < bookings.size() - 1) {
                    ConsoleUtils.printTableRowSeparator(columnWidths, 145);
                }
            }

            ConsoleUtils.printTableFooter(columnWidths, 145);
            System.out.println();
            System.out.println(centerText(
                    ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH));
            sc.nextLine();

        } catch (Exception e) {
            ConsoleUtils.printCenter(ConsoleUtils.RED + "Error: " + e.getMessage() + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH);
        }
    }
}
