package presentation.handlers;

import dao.interfaces.ICheckInOutDAO;
import dao.interfaces.IBookingDAO;
import model.CheckInOut;
import model.Booking;
import presentation.ConsoleUtils;
import utils.InputValidator;

import java.time.LocalDateTime;
import java.util.Scanner;

import static presentation.ConsoleUtils.centerText;

public class CheckInOutHandler {
    private Scanner sc;
    private ICheckInOutDAO checkInOutDAO;
    private IBookingDAO bookingDAO;

    public CheckInOutHandler(Scanner sc, ICheckInOutDAO checkInOutDAO, IBookingDAO bookingDAO) {
        this.sc = sc;
        this.checkInOutDAO = checkInOutDAO;
        this.bookingDAO = bookingDAO;
    }

    public void handleCheckInOut() {
        while (true) {
            ConsoleUtils.clearScreen();
            String[] logo = {
                    " ▗▄▖ ▗▄▄▖▗▖ ▗▖ ▗▄▖  ▗▄▄▖ ▗▖ ▗▖    ▗▄▄▖▗▖  ▗▖▗▄▄▄▖ ▗▄▖ ▗▖ ▗▖▗▄▄▄▖",
                    "▐▌ ▐▌▐▌ ▐▌▐▛▚▞▜▌▐▌ ▐▌▐▌▐▌    ▐▌   ▐▛▚▖▐▌  █  ▐▌ ▐▌▐▌ ▐▌  █",
                    "▐▌ ▐▌▐▛▀▘ ▐▌  ▐▌▐▛▀▜▌ ▐▌▞▘    ▐▌▝▜▌▐▌ ▐▌  █  ▐▛▀▜▌▐▌ ▐▌  █",
                    " ▝▚▄▞▘▐▌  ▐▌  ▐▌▐▌ ▐▌▗▄▞▘     ▝▚▄▞▘▐▌  ▐▌  █  ▐▌ ▐▌▝▚▄▞▘▗▄█▄▖",
            };

            ConsoleUtils.printLogoCentered(logo, ConsoleUtils.DEFAULT_WIDTH, 62);
            ConsoleUtils.printCenter(ConsoleUtils.CYAN + "━━━━━━━ ＣＨＥＣＫ-ＩＮ / ＣＨＥＣＫ-ＯＵＴ ━━━━━━━" + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH);
            System.out.println();

            String[] options = {
                    "1. Check-in to PC",
                    "2. Check-out from PC",
                    "3. View active sessions",
                    "4. Check-in/check-out history",
                    "5. Back"
            };

            String[] colors = {
                    ConsoleUtils.GREEN,
                    ConsoleUtils.ORANGE,
                    ConsoleUtils.GREEN,
                    ConsoleUtils.GREEN,
                    ConsoleUtils.RED
            };

            ConsoleUtils.printMenuOptions(options, ConsoleUtils.DEFAULT_WIDTH, colors);
            System.out.println();

            System.out.print(centerText(
                    ConsoleUtils.YELLOW + "Choose an option (1-5): " + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH));

            try {
                int choice = InputValidator.getIntInput(sc, "");
                switch (choice) {
                    case 1:
                        handleCheckIn();
                        break;
                    case 2:
                        handleCheckOut();
                        break;
                    case 3:
                        viewActiveSessions();
                        break;
                    case 4:
                        viewAllCheckInOutHistory();
                        break;
                    case 5:
                        return;
                    default:
                        ConsoleUtils.printCenter(
                                ConsoleUtils.RED + "Invalid choice!" + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH);
                        pauseMenu();
                }
            } catch (Exception e) {
                ConsoleUtils.printCenter(ConsoleUtils.RED + "Invalid input!" + ConsoleUtils.RESET,
                        ConsoleUtils.DEFAULT_WIDTH);
                pauseMenu();
            }
        }
    }

    private void handleCheckIn() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printCenter(ConsoleUtils.CYAN + "━━━━━━━━━━ ＣＨＥＣＫ-ＩＮ ━━━━━━━━━━" + ConsoleUtils.RESET,
                ConsoleUtils.DEFAULT_WIDTH);
        System.out.println();

        System.out.print(centerText(ConsoleUtils.YELLOW + "Enter booking ID: " + ConsoleUtils.RESET,
                ConsoleUtils.DEFAULT_WIDTH));
        int bookingId = InputValidator.getIntInput(sc, "");

        try {
            Booking booking = bookingDAO.findById(bookingId);
            if (booking == null) {
                ConsoleUtils.printCenter(ConsoleUtils.RED + "Booking not found!" + ConsoleUtils.RESET,
                        ConsoleUtils.DEFAULT_WIDTH);
                pauseMenu();
                return;
            }

            CheckInOut existing = checkInOutDAO.findByBookingId(bookingId);
            if (existing != null) {
                ConsoleUtils.printCenter(
                        ConsoleUtils.ORANGE + "This booking already has a check-in record!" + ConsoleUtils.RESET,
                        ConsoleUtils.DEFAULT_WIDTH);
                System.out.println();

                java.util.Map<String, Object> sessionDetail = checkInOutDAO.findSessionDetailByBookingId(bookingId);
                if (sessionDetail != null) {
                    String[] headers = { "Booking ID", "Full Name", "Email", "PC Name", "Zone", "Check-in Time" };
                    int[] columnWidths = { 12, 18, 22, 15, 10, 18 };

                    ConsoleUtils.printTableHeader(headers, columnWidths, 110);

                    int id = (Integer) sessionDetail.get("bookingId");
                    String fullName = (String) sessionDetail.get("fullName");
                    String email = (String) sessionDetail.get("email");
                    String pcName = (String) sessionDetail.get("pcName");
                    String zoneName = (String) sessionDetail.get("zoneName");
                    LocalDateTime checkInTime = (LocalDateTime) sessionDetail.get("checkInTime");

                    String[] rowData = {
                            String.valueOf(id),
                            fullName != null ? fullName : "N/A",
                            email != null ? email : "N/A",
                            pcName != null ? pcName : "N/A",
                            zoneName != null ? zoneName : "N/A",
                            checkInTime != null ? checkInTime.toString() : "N/A"
                    };

                    ConsoleUtils.printTableRow(rowData, columnWidths, 110);
                    ConsoleUtils.printTableFooter(columnWidths, 110);
                }

                pauseMenu();
                return;
            }

            CheckInOut checkInOut = new CheckInOut();
            checkInOut.setBookingId(bookingId);
            checkInOut.setCheckInTime(LocalDateTime.now());

            checkInOutDAO.createCheckIn(checkInOut);

            ConsoleUtils.printCenter(ConsoleUtils.GREEN + "Check-in successful!" + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH);
            System.out.println();

            java.util.Map<String, Object> sessionDetail = checkInOutDAO.findSessionDetailByBookingId(bookingId);
            if (sessionDetail != null) {
                String[] headers = { "Booking ID", "Full Name", "Email", "PC Name", "Zone", "Check-in Time" };
                int[] columnWidths = { 12, 18, 22, 15, 10, 18 };

                ConsoleUtils.printTableHeader(headers, columnWidths, 110);

                int id = (Integer) sessionDetail.get("bookingId");
                String fullName = (String) sessionDetail.get("fullName");
                String email = (String) sessionDetail.get("email");
                String pcName = (String) sessionDetail.get("pcName");
                String zoneName = (String) sessionDetail.get("zoneName");
                LocalDateTime checkInTime = (LocalDateTime) sessionDetail.get("checkInTime");

                String[] rowData = {
                        String.valueOf(id),
                        fullName != null ? fullName : "N/A",
                        email != null ? email : "N/A",
                        pcName != null ? pcName : "N/A",
                        zoneName != null ? zoneName : "N/A",
                        checkInTime != null ? checkInTime.toString() : "N/A"
                };

                ConsoleUtils.printTableRow(rowData, columnWidths, 110);
                ConsoleUtils.printTableFooter(columnWidths, 110);
            }

            pauseMenu();
        } catch (Exception e) {
            ConsoleUtils.printCenter(ConsoleUtils.RED + "Error: " + e.getMessage() + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH);
            pauseMenu();
        }
    }

    private void handleCheckOut() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printCenter(ConsoleUtils.CYAN + "━━━━━━━━━━ ＣＨＥＣＫ-ＯＵＴ ━━━━━━━━━━" + ConsoleUtils.RESET,
                ConsoleUtils.DEFAULT_WIDTH);
        System.out.println();

        System.out.print(centerText(ConsoleUtils.YELLOW + "Enter booking ID: " + ConsoleUtils.RESET,
                ConsoleUtils.DEFAULT_WIDTH));
        int bookingId = InputValidator.getIntInput(sc, "");

        try {
            CheckInOut checkInOut = checkInOutDAO.findByBookingId(bookingId);
            if (checkInOut == null) {
                ConsoleUtils.printCenter(ConsoleUtils.RED + "Check-in record not found!" + ConsoleUtils.RESET,
                        ConsoleUtils.DEFAULT_WIDTH);
                pauseMenu();
                return;
            }

            if (checkInOut.getCheckOutTime() != null) {
                ConsoleUtils.printCenter(
                        ConsoleUtils.ORANGE + "This session already has a check-out!" + ConsoleUtils.RESET,
                        ConsoleUtils.DEFAULT_WIDTH);
                System.out.println();

                String[] headers = { "Property", "Value" };
                int[] columnWidths = { 25, 40 };
                ConsoleUtils.printTableHeader(headers, columnWidths, 70);

                String[][] data = {
                        { "Check-in Time", checkInOut.getCheckInTime().toString() },
                        { "Check-out Time", checkInOut.getCheckOutTime().toString() }
                };

                for (int i = 0; i < data.length; i++) {
                    ConsoleUtils.printTableRow(data[i], columnWidths, 70);
                    if (i < data.length - 1) {
                        ConsoleUtils.printTableRowSeparator(columnWidths, 70);
                    }
                }
                ConsoleUtils.printTableFooter(columnWidths, 70);

                pauseMenu();
                return;
            }

            checkInOutDAO.updateCheckOut(bookingId);

            LocalDateTime checkInTime = checkInOut.getCheckInTime();
            LocalDateTime checkOutTime = LocalDateTime.now();
            long sessionMinutes = java.time.temporal.ChronoUnit.MINUTES.between(checkInTime, checkOutTime);

            ConsoleUtils.printCenter(ConsoleUtils.GREEN + "Check-out successful!" + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH);
            System.out.println();

            String[] headers = { "Property", "Value" };
            int[] columnWidths = { 25, 40 };
            ConsoleUtils.printTableHeader(headers, columnWidths, 70);

            String[][] data = {
                    { "Booking ID", String.valueOf(bookingId) },
                    { "Check-in Time", checkInTime.toString() },
                    { "Check-out Time", checkOutTime.toString() },
                    { "Duration", sessionMinutes + " minutes" }
            };

            for (int i = 0; i < data.length; i++) {
                ConsoleUtils.printTableRow(data[i], columnWidths, 70);
                if (i < data.length - 1) {
                    ConsoleUtils.printTableRowSeparator(columnWidths, 70);
                }
            }
            ConsoleUtils.printTableFooter(columnWidths, 70);

            pauseMenu();
        } catch (Exception e) {
            ConsoleUtils.printCenter(ConsoleUtils.RED + "Error: " + e.getMessage() + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH);
            pauseMenu();
        }
    }

    private void viewActiveSessions() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printCenter(ConsoleUtils.CYAN + "━━━━━━━━━━ ＡＣＴＩＶＥ ＳＥＳＳＩＯＮＳ ━━━━━━━━━━" + ConsoleUtils.RESET,
                ConsoleUtils.DEFAULT_WIDTH);
        System.out.println();

        try {
            java.util.List<java.util.Map<String, Object>> sessions = checkInOutDAO.findAllActiveSessions();

            if (sessions.isEmpty()) {
                ConsoleUtils.printCenter(
                        ConsoleUtils.ORANGE + "No active sessions found" + ConsoleUtils.RESET,
                        ConsoleUtils.DEFAULT_WIDTH);
            } else {
                String[] headers = { "Booking ID", "Full Name", "Email", "PC Name", "Zone", "Check-in Time" };
                int[] columnWidths = { 12, 18, 22, 15, 10, 18 };

                ConsoleUtils.printTableHeader(headers, columnWidths, 110);

                for (int idx = 0; idx < sessions.size(); idx++) {
                    java.util.Map<String, Object> session = sessions.get(idx);

                    int bookingId = (Integer) session.get("bookingId");
                    String fullName = (String) session.get("fullName");
                    String email = (String) session.get("email");
                    String pcName = (String) session.get("pcName");
                    String zoneName = (String) session.get("zoneName");
                    LocalDateTime checkInTime = (LocalDateTime) session.get("checkInTime");

                    String[] rowData = {
                            String.valueOf(bookingId),
                            fullName != null ? fullName : "N/A",
                            email != null ? email : "N/A",
                            pcName != null ? pcName : "N/A",
                            zoneName != null ? zoneName : "N/A",
                            checkInTime != null ? checkInTime.toString() : "N/A"
                    };

                    ConsoleUtils.printTableRow(rowData, columnWidths, 110);
                    if (idx < sessions.size() - 1) {
                        ConsoleUtils.printTableRowSeparator(columnWidths, 110);
                    }
                }

                ConsoleUtils.printTableFooter(columnWidths, 110);

                System.out.println();
                System.out.print(centerText(
                        ConsoleUtils.YELLOW + "Enter booking ID to view details (or 0 to go back): "
                                + ConsoleUtils.RESET,
                        ConsoleUtils.DEFAULT_WIDTH));
                int bookingId = InputValidator.getIntInput(sc, "");

                if (bookingId > 0) {
                    viewSessionDetail(bookingId);
                }
            }

            pauseMenu();
        } catch (Exception e) {
            ConsoleUtils.printCenter(ConsoleUtils.RED + "Error: " + e.getMessage() + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH);
            pauseMenu();
        }
    }

    private void viewSessionDetail(int bookingId) {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printCenter(ConsoleUtils.CYAN + "━━━━━━━━━━ ＳＥＳＳＩＯＮ ＤＥＴＡＩＬ ━━━━━━━━━━" + ConsoleUtils.RESET,
                ConsoleUtils.DEFAULT_WIDTH);
        System.out.println();

        try {
            java.util.Map<String, Object> session = checkInOutDAO.findSessionDetailByBookingId(bookingId);

            if (session == null) {
                ConsoleUtils.printCenter(
                        ConsoleUtils.RED + "Session not found!" + ConsoleUtils.RESET,
                        ConsoleUtils.DEFAULT_WIDTH);
                return;
            }

            String[] headers = { "Property", "Value" };
            int[] columnWidths = { 30, 50 };

            ConsoleUtils.printCenter(ConsoleUtils.YELLOW + "User Information" + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH);
            ConsoleUtils.printTableHeader(headers, columnWidths, 85);

            String[][] userInfo = {
                    { "Username", (String) session.get("username") },
                    { "Full Name", (String) session.get("fullName") },
                    { "Email", (String) session.get("email") },
                    { "Phone", (String) session.get("phone") },
                    { "Address", (String) session.get("address") }
            };

            for (int i = 0; i < userInfo.length; i++) {
                ConsoleUtils.printTableRow(userInfo[i], columnWidths, 85);
                if (i < userInfo.length - 1) {
                    ConsoleUtils.printTableRowSeparator(columnWidths, 85);
                }
            }
            ConsoleUtils.printTableFooter(columnWidths, 85);

            System.out.println();

            ConsoleUtils.printCenter(ConsoleUtils.YELLOW + "Booking Information" + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH);
            ConsoleUtils.printTableHeader(headers, columnWidths, 85);

            String[][] bookingInfo = {
                    { "Booking Code", (String) session.get("bookingCode") },
                    { "Booking Status", (String) session.get("bookingStatus") },
                    { "Total Price", "₫" + String.format("%.2f", session.get("totalPrice")) }
            };

            for (int i = 0; i < bookingInfo.length; i++) {
                ConsoleUtils.printTableRow(bookingInfo[i], columnWidths, 85);
                if (i < bookingInfo.length - 1) {
                    ConsoleUtils.printTableRowSeparator(columnWidths, 85);
                }
            }
            ConsoleUtils.printTableFooter(columnWidths, 85);

            System.out.println();

            ConsoleUtils.printCenter(ConsoleUtils.YELLOW + "PC Information" + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH);
            ConsoleUtils.printTableHeader(headers, columnWidths, 85);

            String[][] pcInfo = {
                    { "PC Name", (String) session.get("pcName") },
                    { "Zone", (String) session.get("zoneName") },
                    { "CPU", (String) session.get("cpu") },
                    { "RAM", session.get("ram") + " GB" },
                    { "GPU", (String) session.get("gpu") },
                    { "Price per Hour", "₫" + String.format("%.2f", session.get("pricePerHour")) }
            };

            for (int i = 0; i < pcInfo.length; i++) {
                ConsoleUtils.printTableRow(pcInfo[i], columnWidths, 85);
                if (i < pcInfo.length - 1) {
                    ConsoleUtils.printTableRowSeparator(columnWidths, 85);
                }
            }
            ConsoleUtils.printTableFooter(columnWidths, 85);

            System.out.println();

            ConsoleUtils.printCenter(ConsoleUtils.YELLOW + "Session Information" + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH);
            ConsoleUtils.printTableHeader(headers, columnWidths, 85);

            LocalDateTime checkInTime = (LocalDateTime) session.get("checkInTime");
            LocalDateTime checkOutTime = (LocalDateTime) session.get("checkOutTime");

            java.util.List<String[]> sessionInfo = new java.util.ArrayList<>();
            sessionInfo.add(new String[] { "Check-in Time", checkInTime != null ? checkInTime.toString() : "N/A" });
            sessionInfo.add(new String[] { "Check-out Time",
                    checkOutTime != null ? checkOutTime.toString() : "Still in session" });

            if (checkInTime != null) {
                LocalDateTime endTime = checkOutTime != null ? checkOutTime : LocalDateTime.now();
                long minutes = java.time.temporal.ChronoUnit.MINUTES.between(checkInTime, endTime);
                sessionInfo.add(new String[] { "Duration", minutes + " minutes" });
            }

            for (int i = 0; i < sessionInfo.size(); i++) {
                ConsoleUtils.printTableRow(sessionInfo.get(i), columnWidths, 85);
                if (i < sessionInfo.size() - 1) {
                    ConsoleUtils.printTableRowSeparator(columnWidths, 85);
                }
            }
            ConsoleUtils.printTableFooter(columnWidths, 85);

            pauseMenu();
        } catch (Exception e) {
            ConsoleUtils.printCenter(ConsoleUtils.RED + "Error: " + e.getMessage() + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH);
            pauseMenu();
        }
    }

    private void pauseMenu() {
        System.out.println();
        System.out.print(centerText(
                ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                ConsoleUtils.DEFAULT_WIDTH));
        sc.nextLine();
    }

    private void viewAllCheckInOutHistory() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printCenter(ConsoleUtils.CYAN + "━━━━━━ ＣＨＥＣＫ-ＩＮ / ＣＨＥＣＫ-ＯＵＴ ＨＩＳＴＯＲＹ ━━━━━━" + ConsoleUtils.RESET,
                ConsoleUtils.DEFAULT_WIDTH);
        System.out.println();

        try {
            java.util.List<java.util.Map<String, Object>> records = checkInOutDAO.findAllCheckInOut();

            if (records.isEmpty()) {
                ConsoleUtils.printCenter(
                        ConsoleUtils.ORANGE + "No check-in/check-out records found" + ConsoleUtils.RESET,
                        ConsoleUtils.DEFAULT_WIDTH);
            } else {
                String[] headers = { "ID", "Full Name", "Email", "PC Name", "Zone", "Check-in Time",
                        "Check-out Time" };
                int[] columnWidths = { 6, 25, 30, 25, 15, 18, 18 };

                ConsoleUtils.printTableHeader(headers, columnWidths, 115);

                for (int idx = 0; idx < records.size(); idx++) {
                    java.util.Map<String, Object> record = records.get(idx);

                    int bookingId = (Integer) record.get("bookingId");
                    String fullName = (String) record.get("fullName");
                    String email = (String) record.get("email");
                    String pcName = (String) record.get("pcName");
                    String zoneName = (String) record.get("zoneName");
                    LocalDateTime checkInTime = (LocalDateTime) record.get("checkInTime");
                    LocalDateTime checkOutTime = (LocalDateTime) record.get("checkOutTime");

                    String[] rowData = {
                            String.valueOf(bookingId),
                            fullName != null ? fullName : "N/A",
                            email != null ? email : "N/A",
                            pcName != null ? pcName : "N/A",
                            zoneName != null ? zoneName : "N/A",
                            checkInTime != null ? checkInTime.toString() : "N/A",
                            checkOutTime != null ? checkOutTime.toString() : "Still in session"
                    };

                    ConsoleUtils.printTableRow(rowData, columnWidths, 115);
                    if (idx < records.size() - 1) {
                        ConsoleUtils.printTableRowSeparator(columnWidths, 115);
                    }
                }

                ConsoleUtils.printTableFooter(columnWidths, 115);

                System.out.println();
                System.out.print(centerText(
                        ConsoleUtils.YELLOW + "Enter booking ID to view details (or 0 to go back): "
                                + ConsoleUtils.RESET,
                        ConsoleUtils.DEFAULT_WIDTH));
                int bookingId = InputValidator.getIntInput(sc, "");

                if (bookingId > 0) {
                    viewSessionDetail(bookingId);
                }
            }

            pauseMenu();
        } catch (Exception e) {
            ConsoleUtils.printCenter(ConsoleUtils.RED + "Error: " + e.getMessage() + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH);
            pauseMenu();
        }
    }
}
