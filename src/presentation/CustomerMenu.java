package presentation;

import dao.interfaces.ILoyaltyPointsDAO;
import dao.interfaces.IWalletDAO;
import dao.interfaces.IBookingDAO;
import dao.interfaces.IPCDAO;
import dao.interfaces.IVoucherCodeDAO;
import dao.interfaces.IFoodItemDAO;
import dao.interfaces.IComboDAO;
import enums.BookingStatus;
import enums.PCStatus;
import model.User;
import model.Wallet;
import model.LoyaltyPoints;
import model.Booking;
import model.PC;
import model.FBOrder;
import model.VoucherCode;
import model.FoodItem;
import model.Combo;
import service.interfaces.IBookingService;
import service.interfaces.IOrderService;
import service.interfaces.IPaymentService;
import utils.AppFactory;
import utils.InputValidator;

import java.util.List;
import java.util.Scanner;

import static presentation.ConsoleUtils.centerText;

public class CustomerMenu {
        private Scanner sc;
        private IBookingService bookingService;
        private IPaymentService paymentService;
        private IOrderService orderService;
        private IWalletDAO walletDAO;
        private ILoyaltyPointsDAO loyaltyDAO;
        private IBookingDAO bookingDAO;
        private IPCDAO pcDAO;
        private IVoucherCodeDAO voucherDAO;
        private IFoodItemDAO foodItemDAO;
        private IComboDAO comboDAO;
        private dao.interfaces.IFBOrderDAO fbOrderDAO;
        private dao.interfaces.IPCConfigDAO pcConfigDAO;

        public CustomerMenu(Scanner sc, IBookingService bookingService, IPaymentService paymentService,
                        IOrderService orderService) {
                this.sc = sc;
                this.bookingService = bookingService;
                this.paymentService = paymentService;
                this.orderService = orderService;
                this.walletDAO = AppFactory.getWalletDAO();
                this.loyaltyDAO = AppFactory.getLoyaltyPointsDAO();
                this.bookingDAO = AppFactory.getBookingDAO();
                this.pcDAO = AppFactory.getPCDAO();
                this.voucherDAO = AppFactory.getVoucherCodeDAO();
                this.foodItemDAO = AppFactory.getFoodItemDAO();
                this.comboDAO = AppFactory.getComboDAO();
                this.fbOrderDAO = AppFactory.getFBOrderDAO();
                this.pcConfigDAO = AppFactory.getPCConfigDAO();
        }

        public void displayCustomerMenu(User currentUser) {
                boolean running = true;
                while (running) {
                        ConsoleUtils.clearScreen();
                        String[] logo = {
                                        " ▗▄▄▖▗▖ ▗▖ ▗▄▄▖▗▄▄▄▖▗▄▖ ▗▖  ▗▖▗▄▄▄▖▗▄▄▖     ▗▖  ▗▖▗▄▄▄▖▗▖  ▗▖▗▖ ▗▖",
                                        "▐▌   ▐▌ ▐▌▐▌     █ ▐▌ ▐▌▐▛▚▞▜▌▐▌   ▐▌ ▐▌    ▐▛▚▞▜▌▐▌   ▐▛▚▖▐▌▐▌ ▐▌",
                                        "▐▌   ▐▌ ▐▌ ▝▀▚▖  █ ▐▌ ▐▌▐▌  ▐▌▐▛▀▀▘▐▛▀▚▖    ▐▌  ▐▌▐▛▀▀▘▐▌ ▝▜▌▐▌ ▐▌",
                                        "▝▚▄▄▖▝▚▄▞▘▗▄▄▞▘  █ ▝▚▄▞▘▐▌  ▐▌▐▙▄▄▖▐▌ ▐▌    ▐▌  ▐▌▐▙▄▄▖▐▌  ▐▌▝▚▄▞▘",
                        };

                        ConsoleUtils.printLogoCentered(logo, ConsoleUtils.DEFAULT_WIDTH, 72);
                        ConsoleUtils.printCenter(
                                        ConsoleUtils.CYAN + "━━━━━━━━━━ ＣＵＳＴＯＭＥＲ ＰＯＲＴＡＬ ━━━━━━━━━━"
                                                        + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                        System.out.println();
                        ConsoleUtils.printCenter(
                                        ConsoleUtils.YELLOW + "Welcome, " + currentUser.getUsername() + " ("
                                                        + currentUser.getEmail() + ")"
                                                        + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                        System.out.println();

                        String[] options = {
                                        "1. Book PC",
                                        "2. Order Food & Beverages",
                                        "3. View My Bookings",
                                        "4. Transaction History",
                                        "5. View Loyalty Points",
                                        "6. Logout"
                        };

                        String[] colors = {
                                        ConsoleUtils.CYAN,
                                        ConsoleUtils.CYAN,
                                        ConsoleUtils.CYAN,
                                        ConsoleUtils.CYAN,
                                        ConsoleUtils.CYAN,
                                        ConsoleUtils.RED
                        };

                        ConsoleUtils.printMenuOptions(options, ConsoleUtils.DEFAULT_WIDTH, colors);
                        System.out.println();

                        System.out.print(centerText(
                                        ConsoleUtils.YELLOW + "Choose an option (1-6): " + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH));

                        try {
                                int choice = InputValidator.getIntInput(sc, "");
                                switch (choice) {
                                        case 1:
                                                handleBooking(currentUser);
                                                break;
                                        case 2:
                                                handleOrderFoodMenu(currentUser);
                                                break;
                                        case 3:
                                                viewMyBookings(currentUser);
                                                break;
                                        case 4:
                                                viewTransactionHistory(currentUser);
                                                break;
                                        case 5:
                                                viewLoyaltyPoints(currentUser);
                                                break;
                                        case 6:
                                                ConsoleUtils.printCenter(
                                                                ConsoleUtils.GREEN
                                                                                + "Thank you for using Cyber Hub. Goodbye!"
                                                                                + ConsoleUtils.RESET,
                                                                ConsoleUtils.DEFAULT_WIDTH);
                                                running = false;
                                                break;
                                        default:
                                                ConsoleUtils.printCenter(
                                                                ConsoleUtils.RED + "Invalid choice!"
                                                                                + ConsoleUtils.RESET,
                                                                ConsoleUtils.DEFAULT_WIDTH);
                                                pauseMenu();
                                }
                        } catch (Exception e) {
                                ConsoleUtils.printCenter(
                                                ConsoleUtils.RED + "Invalid input!" + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH);
                                pauseMenu();
                        }
                }
        }

        private void handleBooking(User currentUser) {
                ConsoleUtils.clearScreen();
                ConsoleUtils.printCenter(ConsoleUtils.CYAN + "━━━━━━━━━━ ＢＯＯＫ Ａ ΡС ━━━━━━━━━━" + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH);
                System.out.println();

                try {
                        List<PC> pcs = pcDAO.findAll();

                        if (pcs.isEmpty()) {
                                ConsoleUtils.printCenter(
                                                ConsoleUtils.ORANGE + "No PCs available" + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH);
                                pauseMenu();
                                return;
                        }

                        String[] headers = { "PC ID", "PC Name", "Zone", "Status", "Price/Hour" };
                        int[] columnWidths = { 10, 15, 15, 12, 15 };

                        ConsoleUtils.printTableHeader(headers, columnWidths, ConsoleUtils.DEFAULT_WIDTH);

                        for (int idx = 0; idx < pcs.size(); idx++) {
                                PC pc = pcs.get(idx);
                                String statusStr = pc.getStatus() != null ? pc.getStatus().toString() : "N/A";
                                String priceStr = pc.getConfig() != null
                                                ? ConsoleUtils.formatVND(pc.getConfig().getPricePerHour())
                                                : "N/A";

                                String rowColor = "AVAILABLE".equals(statusStr) ? ConsoleUtils.GREEN : ConsoleUtils.RED;

                                String[] rowData = {
                                                rowColor + String.valueOf(pc.getPcId()) + ConsoleUtils.RESET,
                                                rowColor + pc.getPcName() + ConsoleUtils.RESET,
                                                rowColor + (pc.getZone() != null ? pc.getZone().getZoneName() : "N/A")
                                                                + ConsoleUtils.RESET,
                                                rowColor + statusStr + ConsoleUtils.RESET,
                                                rowColor + priceStr + ConsoleUtils.RESET
                                };

                                ConsoleUtils.printTableRow(rowData, columnWidths, ConsoleUtils.DEFAULT_WIDTH);
                                if (idx < pcs.size() - 1) {
                                        ConsoleUtils.printTableRowSeparator(columnWidths, ConsoleUtils.DEFAULT_WIDTH);
                                }
                        }

                        ConsoleUtils.printTableFooter(columnWidths, ConsoleUtils.DEFAULT_WIDTH);

                        boolean bookingSuccess = false;
                        while (!bookingSuccess) {
                                System.out.println();
                                System.out.print(
                                                centerText(ConsoleUtils.YELLOW
                                                                + "Enter PC ID to book (or 0 to cancel): "
                                                                + ConsoleUtils.RESET,
                                                                ConsoleUtils.DEFAULT_WIDTH));
                                int pcId = InputValidator.getIntInput(sc, "");

                                if (pcId == 0) {
                                        return;
                                }

                                PC selectedPC = null;
                                for (PC pc : pcs) {
                                        if (pc.getPcId() == pcId) {
                                                selectedPC = pc;
                                                break;
                                        }
                                }

                                if (selectedPC == null) {
                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.RED + "PC not found!" + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                        continue;
                                }

                                if (selectedPC.getStatus() != null
                                                && !"AVAILABLE".equals(selectedPC.getStatus().toString())) {
                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.RED + "This PC is not available for booking!"
                                                                        + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.YELLOW + "Status: " + selectedPC.getStatus()
                                                                        + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                        continue;
                                }

                                System.out.println();
                                System.out.print(
                                                centerText(ConsoleUtils.YELLOW
                                                                + "Enter booking duration in hours (1-8): "
                                                                + ConsoleUtils.RESET,
                                                                ConsoleUtils.DEFAULT_WIDTH));
                                int hours = InputValidator.getIntInput(sc, "");

                                if (hours < 1 || hours > 8) {
                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.RED + "Invalid duration! Must be between 1 and 8 hours."
                                                                        + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                        continue;
                                }


                                System.out.println();
                                String pcName = selectedPC.getPcName();
                                String zoneName = selectedPC.getZone() != null ? selectedPC.getZone().getZoneName()
                                                : "N/A";
                                double pricePerHour = selectedPC.getConfig() != null
                                                ? selectedPC.getConfig().getPricePerHour()
                                                : 0;
                                double totalPrice = pricePerHour * hours;

                                String[] bookingHeaders = { "PC", "Zone", "Duration", "Price/Hour", "Subtotal" };
                                int[] bookingColumnWidths = { 18, 15, 12, 15, 15};

                                ConsoleUtils.printCenter(
                                                ConsoleUtils.CYAN + "─── BOOKING PREVIEW ───" + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH);
                                System.out.println();

                                ConsoleUtils.printTableHeader(bookingHeaders, bookingColumnWidths,
                                                ConsoleUtils.DEFAULT_WIDTH);

                                String[] bookingData = {
                                                ConsoleUtils.YELLOW + pcName + ConsoleUtils.RESET,
                                                ConsoleUtils.YELLOW + zoneName + ConsoleUtils.RESET,
                                                ConsoleUtils.YELLOW + hours + " hours" + ConsoleUtils.RESET,
                                                ConsoleUtils.YELLOW + ConsoleUtils.formatVND(pricePerHour)
                                                                + ConsoleUtils.RESET,
                                                ConsoleUtils.GREEN + ConsoleUtils.formatVND(totalPrice)
                                                                + ConsoleUtils.RESET,
                                };

                                ConsoleUtils.printTableRow(bookingData, bookingColumnWidths,
                                                ConsoleUtils.DEFAULT_WIDTH);
                                ConsoleUtils.printTableFooter(bookingColumnWidths, ConsoleUtils.DEFAULT_WIDTH);
                                System.out.println();
                                System.out.println();

                                System.out.println();
                                ConsoleUtils.printCenter(ConsoleUtils.CYAN + "─── NEXT ACTION ───" + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH);
                                System.out.println();

                                String[] nextActionOptions = {
                                                ConsoleUtils.GREEN + "1. Order Food & Beverage" + ConsoleUtils.RESET,
                                                ConsoleUtils.YELLOW + "2. Go to Payment" + ConsoleUtils.RESET,
                                                ConsoleUtils.RED + "3. Cancel" + ConsoleUtils.RESET
                                };

                                ConsoleUtils.printMenuOptions(nextActionOptions, ConsoleUtils.DEFAULT_WIDTH);
                                System.out.println();

                                System.out.print(
                                                centerText(ConsoleUtils.YELLOW + "Choose next action (1-3): "
                                                                + ConsoleUtils.RESET,
                                                                ConsoleUtils.DEFAULT_WIDTH));
                                int nextAction = InputValidator.getIntInput(sc, "");

                                if (nextAction == 1) {


                                        java.time.LocalDateTime startTime = java.time.LocalDateTime.now();
                                        java.time.LocalDateTime endTime = startTime.plusHours(hours);

                                        handleOrderForBooking(currentUser, pcId, currentUser.getUserId(), startTime, endTime);
                                        bookingSuccess = true;
                                } else if (nextAction == 2) {


                                        java.time.LocalDateTime startTime = java.time.LocalDateTime.now();
                                        java.time.LocalDateTime endTime = startTime.plusHours(hours);

                                        handlePaymentForBooking(currentUser, pcId, currentUser.getUserId(), startTime, endTime, new java.util.ArrayList<>());
                                        bookingSuccess = true;
                                } else if (nextAction == 3) {
                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.YELLOW + "Booking cancelled." + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                        bookingSuccess = true;
                                } else {
                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.RED + "Invalid choice!" + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                        continue;
                                }
                        }
                        pauseMenu();
                } catch (Exception e) {
                        ConsoleUtils.printCenter(
                                        ConsoleUtils.RED + "Error: " + e.getMessage() + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                        pauseMenu();
                }
        }

        private void handleOrderFoodMenu(User currentUser) {
                ConsoleUtils.clearScreen();
                System.out.println();

                String[] logo = {
                                "▗▄▖ ▗▖ ▗▖ ▗▄▄▖    ▗▖  ▗▖ ▗▄▖ ▗▖  ▗▖ ▗▄▖",
                                "▐▌ ▐▌▐▌ ▐▌▐▌     ▐▛▚▞▜▌▐▌ ▐▌▐▛▚▖▐▌▐▌ ▐▌",
                                "▐▌ ▐▌▐▌ ▐▌ ▝▀▚▖  ▐▌  ▐▌▐▛▀▜▌▐▌ ▝▜▌▐▛▀▜▌",
                                "▝▚▄▞▘▝▚▄▞▘▗▄▄▞▘  ▐▌  ▐▌▐▌ ▐▌▐▌  ▐▌▐▌ ▐▌"
                };

                ConsoleUtils.printLogoCentered(logo, ConsoleUtils.DEFAULT_WIDTH, 50);
                ConsoleUtils.printCenter(
                                ConsoleUtils.CYAN + "━━━━━━━━━━ ＭＥＮＵ ━━━━━━━━━━" + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH);
                System.out.println();

                try {

                        List<FoodItem> foodItems = foodItemDAO.findAll();
                        List<Combo> combos = comboDAO.findAll(false);


                        displayFoodItemsSection(foodItems);

                        System.out.println();
                        System.out.println();

                        displayCombosSection(combos);

                        System.out.println();
                        System.out.println();

                        String[] orderOptions = {
                                        ConsoleUtils.GREEN + "1. Place an Order" + ConsoleUtils.RESET,
                                        ConsoleUtils.RED + "2. Back to Menu" + ConsoleUtils.RESET
                        };

                        ConsoleUtils.printMenuOptions(orderOptions, ConsoleUtils.DEFAULT_WIDTH);
                        System.out.println();

                        System.out.print(centerText(
                                        ConsoleUtils.YELLOW + "Choose an option (1-2): " + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH));
                        int choice = InputValidator.getIntInput(sc, "");

                        if (choice == 1) {
                                FBOrder order = orderService.createOrderInMemory(0);
                                if (order == null) {
                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.RED + "Failed to create order!" + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                        pauseMenu();
                                        return;
                                }

                                ConsoleUtils.clearScreen();
                                ConsoleUtils.printCenter(
                                                ConsoleUtils.CYAN + "━━━━━━━━━━ ADD ITEMS TO ORDER ━━━━━━━━━━" + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH);


                                List<model.OrderDetail> orderDetails = collectOrderItemsFromUser();


                                displayOrderSummary(orderDetails);


                                System.out.print(centerText(
                                                ConsoleUtils.YELLOW + "Save this order? (y/n): " + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH));
                                String confirm = sc.nextLine().trim().toLowerCase();


                                if (confirm.startsWith("y")) {
                                        try {
                                                orderService.saveOrder(order, orderDetails);
                                                ConsoleUtils.printCenter(
                                                                ConsoleUtils.GREEN + "Order saved successfully!" + ConsoleUtils.RESET,
                                                                ConsoleUtils.DEFAULT_WIDTH);
                                        } catch (Exception e) {
                                                ConsoleUtils.printCenter(
                                                                ConsoleUtils.RED + "Error saving order: " + e.getMessage() + ConsoleUtils.RESET,
                                                                ConsoleUtils.DEFAULT_WIDTH);
                                        }
                                } else {
                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.ORANGE + "Order not saved." + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                }

                                pauseMenu();
                        } else if (choice == 2) {

                                return;
                        } else {
                                ConsoleUtils.printCenter(
                                                ConsoleUtils.RED + "Invalid choice!" + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH);
                                pauseMenu();
                        }

                } catch (Exception e) {
                        ConsoleUtils.printCenter(
                                        ConsoleUtils.RED + "Error: " + e.getMessage() + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                        pauseMenu();
                }
        }


        private void handleOrderForBooking(User currentUser, int pcId, int userId, java.time.LocalDateTime startTime, java.time.LocalDateTime endTime) {
                ConsoleUtils.clearScreen();
                ConsoleUtils.printCenter(
                                ConsoleUtils.CYAN + "━━━━━━━━━━ Ｏｒｄｅｒ  Ｆｏｏｄ  ＆  Ｂｅｖｅｒａｇｅ ━━━━━━━━━━"
                                                + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH);
                System.out.println();

                try {

                        List<model.OrderDetail> orderDetails = collectOrderItemsFromUser();


                        java.util.List<java.util.Map<String, Object>> orderedItems =
                                convertOrderDetailsToMaps(orderDetails);


                        System.out.println();
                        System.out.println();
                        ConsoleUtils.printCenter(ConsoleUtils.CYAN + "─── NEXT ACTION ───" + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                        System.out.println();

                        String[] nextStepsOptions = {
                                        ConsoleUtils.GREEN + "1. Order Food & Beverage" + ConsoleUtils.RESET,
                                        ConsoleUtils.YELLOW + "2. Go to Payment" + ConsoleUtils.RESET,
                                        ConsoleUtils.RED + "3. Cancel" + ConsoleUtils.RESET
                        };

                        ConsoleUtils.printMenuOptions(nextStepsOptions, ConsoleUtils.DEFAULT_WIDTH);
                        System.out.println();

                        System.out.print(
                                        centerText(ConsoleUtils.YELLOW + "Choose an option (1-3): "
                                                        + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH));

                        try {
                                int nextChoice = InputValidator.getIntInput(sc, "");
                                switch (nextChoice) {
                                        case 1:

                                                System.out.println();
                                                ConsoleUtils.printCenter(
                                                                ConsoleUtils.YELLOW + "Returning to food menu..." + ConsoleUtils.RESET,
                                                                ConsoleUtils.DEFAULT_WIDTH);
                                                System.out.println();
                                                pauseMenu();

                                                handleOrderForBooking(currentUser, pcId, userId, startTime, endTime);
                                                return;
                                        case 2:
                                                handlePaymentForBooking(currentUser, pcId, userId, startTime, endTime, orderedItems);
                                                return;
                                        case 3:

                                                ConsoleUtils.printCenter(
                                                                ConsoleUtils.YELLOW + "Booking cancelled." + ConsoleUtils.RESET,
                                                                ConsoleUtils.DEFAULT_WIDTH);
                                                pauseMenu();
                                                return;
                                        default:
                                                ConsoleUtils.printCenter(
                                                                ConsoleUtils.RED + "Lựa chọn không hợp lệ!"
                                                                                + ConsoleUtils.RESET,
                                                                ConsoleUtils.DEFAULT_WIDTH);
                                                System.out.println();
                                                pauseMenu();
                                                handleOrderForBooking(currentUser, pcId, userId, startTime, endTime);
                                                return;
                                }
                        } catch (NumberFormatException e) {
                                ConsoleUtils.printCenter(
                                                ConsoleUtils.RED + "Vui lòng nhập số từ 1-3!" + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH);
                                System.out.println();
                                pauseMenu();
                        }
                } catch (Exception e) {
                        ConsoleUtils.printCenter(
                                        ConsoleUtils.RED + "Error: " + e.getMessage() + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                        pauseMenu();
                }
        }

        private void handlePaymentForBooking(User currentUser, int pcId, int userId, java.time.LocalDateTime startTime, java.time.LocalDateTime endTime, java.util.List<java.util.Map<String, Object>> orderedItems) {
                ConsoleUtils.clearScreen();
                ConsoleUtils.printCenter(ConsoleUtils.CYAN + "━━━━━━━━━━ ＭＡＫＥ  ＰＡＹＭＥＮＴ ━━━━━━━━━━" + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH);
                System.out.println();

                try {



                        VoucherCode selectedVoucher = null;
                        Integer selectedVoucherId = null;


                        System.out.println();
                        ConsoleUtils.printWideMenuBox(
                                        ConsoleUtils.CYAN + "APPLY VOUCHER (Optional)" + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                        System.out.println();


                        displayVouchers();
                        System.out.println();


                        String[] voucherOptions = {
                                        ConsoleUtils.GREEN + "1. Enter Voucher Code" + ConsoleUtils.RESET,
                                        ConsoleUtils.RED + "2. Skip Voucher" + ConsoleUtils.RESET
                        };
                        ConsoleUtils.printMenuOptions(voucherOptions, ConsoleUtils.DEFAULT_WIDTH);

                        System.out.print(
                                        centerText(ConsoleUtils.YELLOW + "Choose action (1-2): "
                                                        + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH));
                        int voucherAction = InputValidator.getIntInput(sc, "");

                        if (voucherAction == 1) {

                                System.out.println();
                                System.out.print(
                                                centerText(ConsoleUtils.YELLOW + "Enter voucher code: "
                                                                + ConsoleUtils.RESET,
                                                                ConsoleUtils.DEFAULT_WIDTH));
                                String code = sc.nextLine().trim();
                                selectedVoucher = voucherDAO.findByCode(code);
                        }

                        if (selectedVoucher != null && !isVoucherValid(selectedVoucher, userId)) {
                                System.out.println();
                                selectedVoucher = null;
                        }

                        if (selectedVoucher != null) {
                                selectedVoucherId = selectedVoucher.getVoucherId();
                                System.out.println();
                                ConsoleUtils.printCenter(
                                                ConsoleUtils.GREEN + "VOUCHER APPLIED!" + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH);
                                System.out.println();
                                System.out.println(centerText(
                                                "Code: " + ConsoleUtils.YELLOW + selectedVoucher.getCode()
                                                                + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH));
                                System.out.println(centerText(
                                                "Discount: " + ConsoleUtils.YELLOW
                                                                + selectedVoucher.getDiscountValue()
                                                                + (selectedVoucher.getDiscountType().name()
                                                                                .equals("PERCENT") ? "%"
                                                                                                : " VND")
                                                                + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH));
                                System.out.println();
                        }


                        System.out.println();
                        ConsoleUtils.printWideMenuBox(
                                        ConsoleUtils.CYAN + "INVOICE" + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                        System.out.println();


                        PC pc = pcDAO.findById(pcId);


                        if (pc != null && pc.getConfigId() > 0) {
                                model.PCConfig config = pcConfigDAO.findById(pc.getConfigId());
                                pc.setConfig(config);
                        }

                        String pcName = pc != null ? pc.getPcName() : "N/A";
                        String zoneName = pc != null && pc.getZone() != null ? pc.getZone().getZoneName() : "N/A";


                        long hours = java.time.temporal.ChronoUnit.HOURS.between(startTime, endTime);


                        double pricePerHour = 0;
                        double pcTotal = 0;

                        if (pc != null && pc.getConfig() != null) {
                                pricePerHour = pc.getConfig().getPricePerHour();
                                pcTotal = pricePerHour * hours;
                        } else {

                                pcTotal = 0;
                                pricePerHour = 0;
                        }


                        System.out.println(centerText(ConsoleUtils.YELLOW + "─── PC BOOKING ───" + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH));
                        System.out.println(centerText("PC: " + pcName, ConsoleUtils.DEFAULT_WIDTH));
                        System.out.println(centerText("Zone: " + zoneName, ConsoleUtils.DEFAULT_WIDTH));
                        System.out.println(centerText("Duration: " + hours + " hours @ "
                                        + ConsoleUtils.formatVND(pricePerHour) + "/hour", ConsoleUtils.DEFAULT_WIDTH));
                        System.out.println(centerText("PC Subtotal: " + ConsoleUtils.formatVND(pcTotal),
                                        ConsoleUtils.DEFAULT_WIDTH));
                        System.out.println();


                        double foodTotal = 0;
                        java.util.List<java.util.Map<String, Object>> foodForInvoice = new java.util.ArrayList<>();

                        if (orderedItems != null && !orderedItems.isEmpty()) {
                                System.out.println(centerText(
                                                ConsoleUtils.YELLOW + "─── FOOD & BEVERAGE ───" + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH));
                                for (java.util.Map<String, Object> itemInfo : orderedItems) {
                                        Integer itemId = (Integer) itemInfo.get("itemId");
                                        Integer quantity = (Integer) itemInfo.get("quantity");
                                        Double price = (Double) itemInfo.get("price");

                                        if (itemId != null && quantity != null && price != null) {
                                                String itemName = "Unknown";
                                                Boolean isCombo = (Boolean) itemInfo.get("isCombo");

                                                if (isCombo != null && isCombo) {
                                                        Combo combo = comboDAO.findById(itemId);
                                                        itemName = combo != null ? combo.getName() : "Combo #" + itemId;
                                                } else {
                                                        FoodItem item = foodItemDAO.findById(itemId);
                                                        itemName = item != null ? item.getName() : "Food Item #" + itemId;
                                                }

                                                double itemTotal = price * quantity;
                                                foodTotal += itemTotal;
                                                System.out.println(centerText(
                                                                itemName + " x" + quantity + " = "
                                                                                + ConsoleUtils.formatVND(itemTotal),
                                                                ConsoleUtils.DEFAULT_WIDTH));

                                                foodForInvoice.add(itemInfo);
                                        }
                                }
                                System.out.println(centerText("Food Subtotal: " + ConsoleUtils.formatVND(foodTotal),
                                                ConsoleUtils.DEFAULT_WIDTH));
                                System.out.println();
                        }


                        double totalAmount = pcTotal + foodTotal;


                        double discountAmount = 0;
                        double finalAmount = totalAmount;

                        if (selectedVoucher != null && isVoucherValid(selectedVoucher, userId)) {
                                if ("PERCENT".equals(selectedVoucher.getDiscountType().name())) {
                                        discountAmount = totalAmount
                                                        * (selectedVoucher.getDiscountValue().doubleValue() / 100);
                                } else {
                                        discountAmount = selectedVoucher.getDiscountValue().doubleValue();
                                }
                                finalAmount = totalAmount - discountAmount;
                        }


                        System.out.println(
                                        centerText(ConsoleUtils.YELLOW + "─── PAYMENT SUMMARY ───" + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH));
                        System.out.println();

                        String[] summaryHeaders = { "Item Type", "Name", "Description", "Amount" };
                        int[] summaryWidths = { 12, 15, 81, 20 };

                        ConsoleUtils.printTableHeader(summaryHeaders, summaryWidths, ConsoleUtils.DEFAULT_WIDTH);


                        String[] pcRow = {
                                        "PC Booking",
                                        pcName,
                                        hours + " hour(s)",
                                        ConsoleUtils.formatVND(pcTotal)
                        };
                        ConsoleUtils.printTableRow(pcRow, summaryWidths, ConsoleUtils.DEFAULT_WIDTH);
                        ConsoleUtils.printTableRowSeparator(summaryWidths, ConsoleUtils.DEFAULT_WIDTH);


                        if (foodTotal > 0) {
                                StringBuilder foodDesc = new StringBuilder();
                                for (int i = 0; i < foodForInvoice.size(); i++) {
                                        java.util.Map<String, Object> itemInfo = foodForInvoice.get(i);
                                        Integer itemId = (Integer) itemInfo.get("itemId");
                                        Integer quantity = (Integer) itemInfo.get("quantity");

                                        String itemName = "Unknown";
                                        Boolean isCombo = (Boolean) itemInfo.get("isCombo");

                                        if (isCombo != null && isCombo) {
                                                Combo combo = comboDAO.findById(itemId);
                                                itemName = combo != null ? combo.getName() : "Combo #" + itemId;
                                        } else {
                                                FoodItem item = foodItemDAO.findById(itemId);
                                                itemName = item != null ? item.getName() : "Food Item #" + itemId;
                                        }

                                        foodDesc.append(itemName).append(" x").append(quantity);
                                        if (i < foodForInvoice.size() - 1) {
                                                foodDesc.append(", ");
                                        }
                                }
                                String[] foodRow = {
                                                "Food & Beverage",
                                                foodForInvoice.size() + " item(s)",
                                                foodDesc.toString(),
                                                ConsoleUtils.formatVND(foodTotal)
                                };
                                ConsoleUtils.printTableRow(foodRow, summaryWidths, ConsoleUtils.DEFAULT_WIDTH);
                                ConsoleUtils.printTableRowSeparator(summaryWidths, ConsoleUtils.DEFAULT_WIDTH);
                        }


                        if (pcTotal > 0 && foodTotal > 0) {
                                String[] subtotalRow = {
                                                "Subtotal",
                                                "-",
                                                "-",
                                                ConsoleUtils.formatVND(totalAmount)
                                };
                                ConsoleUtils.printTableRow(subtotalRow, summaryWidths, ConsoleUtils.DEFAULT_WIDTH);
                                ConsoleUtils.printTableRowSeparator(summaryWidths, ConsoleUtils.DEFAULT_WIDTH);
                        }


                        if (discountAmount > 0 && selectedVoucher != null) {
                                String voucherDesc = selectedVoucher.getCode() + " ("
                                                        + selectedVoucher.getDiscountValue()
                                                        + ("PERCENT".equals(selectedVoucher.getDiscountType().name()) ? "%" : " VND") + ")";
                                String[] voucherRow = {
                                                ConsoleUtils.GREEN + "Voucher" + ConsoleUtils.RESET,
                                                "-",
                                                voucherDesc,
                                                ConsoleUtils.GREEN + "-" + ConsoleUtils.formatVND(discountAmount) + ConsoleUtils.RESET
                                };
                                ConsoleUtils.printTableRow(voucherRow, summaryWidths, ConsoleUtils.DEFAULT_WIDTH);
                                ConsoleUtils.printTableRowSeparator(summaryWidths, ConsoleUtils.DEFAULT_WIDTH);
                        }


                        String[] finalRow = {
                                        ConsoleUtils.CYAN + "FINAL TOTAL" + ConsoleUtils.RESET,
                                        "-",
                                        "-",
                                        ConsoleUtils.CYAN + ConsoleUtils.formatVND(finalAmount) + ConsoleUtils.RESET
                        };
                        ConsoleUtils.printTableRow(finalRow, summaryWidths, ConsoleUtils.DEFAULT_WIDTH);

                        ConsoleUtils.printTableFooter(summaryWidths, ConsoleUtils.DEFAULT_WIDTH);
                        System.out.println();

                        String[] paymentOptions = {
                                        ConsoleUtils.GREEN + "1. Pay by Cash" + ConsoleUtils.RESET,
                                        ConsoleUtils.YELLOW + "2. Pay by Wallet" + ConsoleUtils.RESET,
                                        ConsoleUtils.ORANGE + "3. Pay by QR Code (Transfer)" + ConsoleUtils.RESET
                        };

                        ConsoleUtils.printMenuOptions(paymentOptions, ConsoleUtils.DEFAULT_WIDTH);
                        System.out.println();

                        boolean paymentSuccessful = false;
                        while (!paymentSuccessful) {
                                System.out.print(
                                                centerText(ConsoleUtils.YELLOW + "Choose payment method (1-3): "
                                                                + ConsoleUtils.RESET,
                                                                ConsoleUtils.DEFAULT_WIDTH));
                                int paymentMethod = InputValidator.getIntInput(sc, "");

                                if (paymentMethod == 1) {

                                        System.out.println();
                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.YELLOW + "PAYMENT PENDING - AWAITING STAFF CONFIRMATION"
                                                                        + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                        System.out.println();
                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.CYAN + "Please pay at the counter: "
                                                                        + ConsoleUtils.formatVND(finalAmount)
                                                                        + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                        System.out.println();
                                        System.out.print(
                                                        centerText(ConsoleUtils.GRAY + "[Staff Confirm - y/n]: "
                                                                        + ConsoleUtils.RESET,
                                                                        ConsoleUtils.DEFAULT_WIDTH));
                                        String staffConfirm = sc.nextLine().trim().toLowerCase();

                                        if (staffConfirm.equals("y")) {

                                                Booking booking = bookingService.createBooking(userId, pcId, startTime, endTime);
                                                if (booking == null) {
                                                        ConsoleUtils.printCenter(
                                                                        ConsoleUtils.RED + "Failed to create booking!" + ConsoleUtils.RESET,
                                                                        ConsoleUtils.DEFAULT_WIDTH);
                                                        paymentSuccessful = true;
                                                        pauseMenu();
                                                        return;
                                                }


                                                booking.setDiscountAmount(discountAmount);
                                                booking.setTotalPrice(finalAmount);
                                                booking.setPaid(true);
                                                booking.setPaymentMethod("CASH");
                                                booking.setStatus(BookingStatus.CONFIRMED);
                                                booking.setPaidAt(java.time.LocalDateTime.now());
                                                if (selectedVoucherId != null) {
                                                        booking.setVoucherId(selectedVoucherId);
                                                }

                                                ConsoleUtils.printCenter(
                                                                ConsoleUtils.GREEN + "BOOKING CONFIRMED SUCCESSFULLY!"
                                                                                + ConsoleUtils.RESET,
                                                                ConsoleUtils.DEFAULT_WIDTH);
                                                ConsoleUtils.printCenter(
                                                                ConsoleUtils.CYAN + "Payment confirmed. You can check in now!"
                                                                                + ConsoleUtils.RESET,
                                                                ConsoleUtils.DEFAULT_WIDTH);
                                                paymentSuccessful = true;


                                                bookingDAO.update(booking);


                                                if (selectedVoucherId != null) {
                                                        voucherDAO.incrementUsageCount(selectedVoucherId);
                                                }


                                                if (foodForInvoice != null && !foodForInvoice.isEmpty()) {
                                                        FBOrder fbOrder = orderService.createOrder(booking.getBookingId());
                                                        if (fbOrder != null) {

                                                                for (java.util.Map<String, Object> itemInfo : foodForInvoice) {
                                                                        Integer itemId = (Integer) itemInfo.get("itemId");
                                                                        Integer quantity = (Integer) itemInfo.get("quantity");
                                                                        Double price = (Double) itemInfo.get("price");

                                                                        if (itemId != null && quantity != null && price != null) {
                                                                                orderService.addItemToOrder(fbOrder.getOrderId(), itemId, 1, quantity, price);
                                                                        }
                                                                }
                                                        }
                                                }


                                                PC pcToBook = pcDAO.findById(pcId);
                                                if (pcToBook != null) {
                                                        pcToBook.setStatus(PCStatus.BOOKED);
                                                        pcDAO.update(pcToBook);
                                                }
                                        } else {
                                                System.out.println();
                                                ConsoleUtils.printCenter(
                                                                ConsoleUtils.ORANGE + "Payment NOT confirmed by staff"
                                                                                + ConsoleUtils.RESET,
                                                                ConsoleUtils.DEFAULT_WIDTH);
                                                System.out.println();
                                                System.out.print(
                                                                centerText(ConsoleUtils.YELLOW + "Try another payment method? (y/n): "
                                                                                + ConsoleUtils.RESET,
                                                                                ConsoleUtils.DEFAULT_WIDTH));
                                                String retryChoice = sc.nextLine().trim().toLowerCase();
                                                if (!retryChoice.equals("y")) {

                                                        paymentSuccessful = true;
                                                }
                                                System.out.println();
                                        }
                                } else if (paymentMethod == 2) {

                                        Wallet wallet = walletDAO.findByUserId(currentUser.getUserId());
                                        if (wallet == null) {
                                                ConsoleUtils.printCenter(
                                                                ConsoleUtils.RED + "Wallet not found!" + ConsoleUtils.RESET,
                                                                ConsoleUtils.DEFAULT_WIDTH);
                                                System.out.println();
                                                continue;
                                        }

                                        if (wallet.getBalance() < finalAmount) {
                                                ConsoleUtils.printCenter(
                                                                ConsoleUtils.RED + "Insufficient wallet balance!"
                                                                                + ConsoleUtils.RESET,
                                                                ConsoleUtils.DEFAULT_WIDTH);
                                                ConsoleUtils.printCenter(
                                                                ConsoleUtils.YELLOW + "Current balance: "
                                                                                + ConsoleUtils.formatVND(wallet.getBalance())
                                                                                + ConsoleUtils.RESET,
                                                                ConsoleUtils.DEFAULT_WIDTH);
                                                System.out.println();
                                                continue;
                                        }


                                        Booking booking = bookingService.createBooking(userId, pcId, startTime, endTime);
                                        if (booking == null) {
                                                ConsoleUtils.printCenter(
                                                                ConsoleUtils.RED + "Failed to create booking!" + ConsoleUtils.RESET,
                                                                ConsoleUtils.DEFAULT_WIDTH);
                                                paymentSuccessful = true;
                                                pauseMenu();
                                                return;
                                        }


                                        booking.setDiscountAmount(discountAmount);
                                        booking.setTotalPrice(finalAmount);
                                        booking.setPaid(true);
                                        booking.setPaymentMethod("WALLET");
                                        booking.setStatus(BookingStatus.CONFIRMED);
                                        booking.setPaidAt(java.time.LocalDateTime.now());
                                        if (selectedVoucherId != null) {
                                                booking.setVoucherId(selectedVoucherId);
                                        }


                                        paymentService.payByWallet(booking, wallet.getWalletId(), finalAmount);

                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.GREEN + "BOOKING CONFIRMED SUCCESSFULLY!"
                                                                        + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.CYAN
                                                                        + "Payment completed via Wallet. You can check in now!"
                                                                        + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                        paymentSuccessful = true;


                                        bookingDAO.update(booking);


                                        if (selectedVoucherId != null) {
                                                voucherDAO.incrementUsageCount(selectedVoucherId);
                                        }


                                        if (foodForInvoice != null && !foodForInvoice.isEmpty()) {
                                                FBOrder fbOrder = orderService.createOrder(booking.getBookingId());
                                                if (fbOrder != null) {

                                                        for (java.util.Map<String, Object> itemInfo : foodForInvoice) {
                                                                Integer itemId = (Integer) itemInfo.get("itemId");
                                                                Integer quantity = (Integer) itemInfo.get("quantity");
                                                                Double price = (Double) itemInfo.get("price");

                                                                if (itemId != null && quantity != null && price != null) {
                                                                        orderService.addItemToOrder(fbOrder.getOrderId(), itemId, 1, quantity, price);
                                                                }
                                                        }
                                                }
                                        }


                                        PC pcToBook = pcDAO.findById(pcId);
                                        if (pcToBook != null) {
                                                pcToBook.setStatus(PCStatus.BOOKED);
                                                pcDAO.update(pcToBook);
                                        }
                                } else if (paymentMethod == 3) {

                                        System.out.println();
                                        System.out.print(
                                                        centerText(ConsoleUtils.YELLOW + "Enter transfer key (for testing): "
                                                                        + ConsoleUtils.RESET,
                                                                        ConsoleUtils.DEFAULT_WIDTH));
                                        String transferKey = sc.nextLine().trim();

                                        String SECRET_KEY = "cybercenter2026";

                                        if (transferKey.equals(SECRET_KEY)) {

                                                Booking booking = bookingService.createBooking(userId, pcId, startTime, endTime);
                                                if (booking == null) {
                                                        ConsoleUtils.printCenter(
                                                                        ConsoleUtils.RED + "Failed to create booking!" + ConsoleUtils.RESET,
                                                                        ConsoleUtils.DEFAULT_WIDTH);
                                                        paymentSuccessful = true;
                                                        pauseMenu();
                                                        return;
                                                }


                                                booking.setDiscountAmount(discountAmount);
                                                booking.setTotalPrice(finalAmount);
                                                booking.setPaid(true);
                                                booking.setPaymentMethod("QR_CODE");
                                                booking.setStatus(BookingStatus.CONFIRMED);
                                                booking.setPaidAt(java.time.LocalDateTime.now());
                                                if (selectedVoucherId != null) {
                                                        booking.setVoucherId(selectedVoucherId);
                                                }


                                                paymentService.payByCash(booking, finalAmount);

                                                ConsoleUtils.printCenter(
                                                                ConsoleUtils.GREEN + "BOOKING CONFIRMED SUCCESSFULLY!"
                                                                                + ConsoleUtils.RESET,
                                                                ConsoleUtils.DEFAULT_WIDTH);
                                                ConsoleUtils.printCenter(
                                                                ConsoleUtils.CYAN
                                                                                + "Payment received via QR transfer. You can check in now!"
                                                                                + ConsoleUtils.RESET,
                                                                ConsoleUtils.DEFAULT_WIDTH);
                                                paymentSuccessful = true;


                                                bookingDAO.update(booking);


                                                if (selectedVoucherId != null) {
                                                        voucherDAO.incrementUsageCount(selectedVoucherId);
                                                }


                                                if (foodForInvoice != null && !foodForInvoice.isEmpty()) {
                                                        FBOrder fbOrder = orderService.createOrder(booking.getBookingId());
                                                        if (fbOrder != null) {

                                                                for (java.util.Map<String, Object> itemInfo : foodForInvoice) {
                                                                        Integer itemId = (Integer) itemInfo.get("itemId");
                                                                        Integer quantity = (Integer) itemInfo.get("quantity");
                                                                        Double price = (Double) itemInfo.get("price");

                                                                        if (itemId != null && quantity != null && price != null) {
                                                                                orderService.addItemToOrder(fbOrder.getOrderId(), itemId, 1, quantity, price);
                                                                        }
                                                        }
                                                }


                                                PC pcToBook = pcDAO.findById(pcId);
                                                if (pcToBook != null) {
                                                        pcToBook.setStatus(PCStatus.BOOKED);
                                                        pcDAO.update(pcToBook);
                                                }

                                        } else {
                                                ConsoleUtils.printCenter(
                                                                ConsoleUtils.RED + "Invalid transfer key!"
                                                                                + ConsoleUtils.RESET,
                                                                ConsoleUtils.DEFAULT_WIDTH);
                                                System.out.println();
                                                System.out.print(
                                                                centerText(ConsoleUtils.YELLOW + "Retry payment? (y/n): "
                                                                                + ConsoleUtils.RESET,
                                                                                ConsoleUtils.DEFAULT_WIDTH));
                                                String retryChoice = sc.nextLine().trim().toLowerCase();
                                                if (!retryChoice.equals("y")) {
                                                        paymentSuccessful = true;
                                                }
                                                System.out.println();
                                        }
                                } else {
                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.RED + "Invalid payment method!" + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                        System.out.println();
                                }
                        }

                        if (paymentSuccessful) {
                                pauseMenu();
                                return;
                        }
                        }
                } catch (Exception e) {
                        ConsoleUtils.printCenter(
                                        ConsoleUtils.RED + "Error: " + e.getMessage() + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                        pauseMenu();
                }
        }

        private void viewMyBookings(User currentUser) {
                ConsoleUtils.clearScreen();
                ConsoleUtils.printCenter(ConsoleUtils.CYAN + "━━━━━━━━━━ ΜΥ ΒΟΟΚΙΝΓΣ ━━━━━━━━━━" + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH);
                System.out.println();

                try {
                        List<Booking> bookings = bookingDAO.findByUserId(currentUser.getUserId());

                        if (bookings.isEmpty()) {
                                ConsoleUtils.printCenter(
                                                ConsoleUtils.ORANGE + "No bookings found" + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH);
                        } else {
                                String[] headers = { "Booking Code", "PC Name", "Status", "Payment", "Start Time", "End Time", "Voucher", "Total", "Method" };
                                int[] columnWidths = { 14, 16, 12, 10, 16, 16, 10, 15, 12 };

                                ConsoleUtils.printTableHeader(headers, columnWidths, ConsoleUtils.DEFAULT_WIDTH);

                                for (int idx = 0; idx < bookings.size(); idx++) {
                                        Booking booking = bookings.get(idx);


                                        PC pc = pcDAO.findById(booking.getPcId());
                                        String pcName = pc != null ? pc.getPcName() : "N/A";


                                        String voucherCode = "N/A";
                                        if (booking.getVoucherId() != null && booking.getVoucherId() > 0) {
                                                VoucherCode voucher = voucherDAO.findById(booking.getVoucherId());
                                                voucherCode = voucher != null ? voucher.getCode() : "N/A";
                                        }


                                        String paymentStatus = booking.isPaid() ? "PAID" : "UNPAID";
                                        String paymentStatusColor = booking.isPaid() ? ConsoleUtils.GREEN : ConsoleUtils.ORANGE;


                                        String startTime = booking.getStartTime() != null
                                                        ? booking.getStartTime().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm dd/MM"))
                                                        : "N/A";
                                        String endTime = booking.getEndTime() != null
                                                        ? booking.getEndTime().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm dd/MM"))
                                                        : "N/A";

                                        String[] rowData = {
                                                        booking.getBookingCode() != null ? booking.getBookingCode() : "N/A",
                                                        pcName,
                                                        booking.getStatus() != null ? booking.getStatus().toString() : "N/A",
                                                        paymentStatusColor + paymentStatus + ConsoleUtils.RESET,
                                                        startTime,
                                                        endTime,
                                                        voucherCode,
                                                        ConsoleUtils.formatVND(booking.getTotalPrice()),
                                                        booking.getPaymentMethod() != null ? booking.getPaymentMethod() : "N/A"
                                        };

                                        ConsoleUtils.printTableRow(rowData, columnWidths, ConsoleUtils.DEFAULT_WIDTH);
                                        if (idx < bookings.size() - 1) {
                                                ConsoleUtils.printTableRowSeparator(columnWidths,
                                                                ConsoleUtils.DEFAULT_WIDTH);
                                        }
                                }

                                ConsoleUtils.printTableFooter(columnWidths, ConsoleUtils.DEFAULT_WIDTH);
                        }

                        pauseMenu();
                } catch (Exception e) {
                        ConsoleUtils.printCenter(
                                        ConsoleUtils.RED + "Error: " + e.getMessage() + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                        pauseMenu();
                }
        }

        private void viewTransactionHistory(User currentUser) {
                ConsoleUtils.clearScreen();
                ConsoleUtils.printCenter(
                                ConsoleUtils.CYAN + "━━━━━━━━━━ TRANSACTION HISTORY ━━━━━━━━━━" + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH);
                System.out.println();

                try {
                        List<Booking> bookings = bookingDAO.findByUserId(currentUser.getUserId());
                        Wallet wallet = walletDAO.findByUserId(currentUser.getUserId());

                        if (bookings.isEmpty() && wallet == null) {
                                ConsoleUtils.printCenter(
                                                ConsoleUtils.ORANGE + "No transaction history available"
                                                                + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH);
                        } else {
                                System.out.println();
                                ConsoleUtils.printCenter(
                                                ConsoleUtils.CYAN + "─── BOOKING PAYMENTS ───" + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH);
                                System.out.println();

                                if (bookings.isEmpty()) {
                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.YELLOW + "No bookings" + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                } else {
                                        String[] headers = { "Booking ID", "Price", "Payment Method", "Status",
                                                        "Date" };
                                        int[] columnWidths = { 12, 12, 15, 10, 19 };

                                        ConsoleUtils.printTableHeader(headers, columnWidths,
                                                        ConsoleUtils.DEFAULT_WIDTH);

                                        for (int idx = 0; idx < bookings.size(); idx++) {
                                                Booking booking = bookings.get(idx);
                                                String statusStr = booking.isPaid()
                                                                ? ConsoleUtils.GREEN + "PAID" + ConsoleUtils.RESET
                                                                : ConsoleUtils.ORANGE + "PENDING" + ConsoleUtils.RESET;
                                                String paymentMethod = booking.getPaymentMethod() != null
                                                                ? booking.getPaymentMethod()
                                                                : "CASH";
                                                String dateStr = booking.getPaidAt() != null
                                                                ? booking.getPaidAt().toString()
                                                                : booking.getCreatedAt().toString();

                                                String[] rowData = {
                                                                String.valueOf(booking.getBookingId()),
                                                                ConsoleUtils.formatVND(booking.getTotalPrice()),
                                                                paymentMethod,
                                                                statusStr,
                                                                dateStr
                                                };

                                                ConsoleUtils.printTableRow(rowData, columnWidths,
                                                                ConsoleUtils.DEFAULT_WIDTH);
                                                if (idx < bookings.size() - 1) {
                                                        ConsoleUtils.printTableRowSeparator(columnWidths,
                                                                        ConsoleUtils.DEFAULT_WIDTH);
                                                }
                                        }

                                        ConsoleUtils.printTableFooter(columnWidths, ConsoleUtils.DEFAULT_WIDTH);
                                }

                                System.out.println();
                                ConsoleUtils.printCenter(ConsoleUtils.CYAN + "─── WALLET INFO ───" + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH);
                                System.out.println();

                                if (wallet != null) {
                                        System.out.println(centerText("Wallet ID: " + wallet.getWalletId(),
                                                        ConsoleUtils.DEFAULT_WIDTH));
                                        System.out.println(centerText(
                                                        "Current Balance: " + ConsoleUtils.GREEN
                                                                        + ConsoleUtils.formatVND(wallet.getBalance())
                                                                        + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH));
                                } else {
                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.ORANGE + "Wallet not found" + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                }
                        }

                        pauseMenu();
                } catch (Exception e) {
                        ConsoleUtils.printCenter(
                                        ConsoleUtils.RED + "Error: " + e.getMessage() + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                        pauseMenu();
                }
        }

        private void viewLoyaltyPoints(User currentUser) {
                ConsoleUtils.clearScreen();
                ConsoleUtils.printCenter(
                                ConsoleUtils.CYAN + "━━━━━━━━━━ ＬＯＹＡＬＴＹ  ＰＯＩＮＴＳ ━━━━━━━━━━" + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH);
                System.out.println();

                try {
                        LoyaltyPoints loyaltyPoints = loyaltyDAO.findByUserId(currentUser.getUserId());

                        if (loyaltyPoints == null) {
                                ConsoleUtils.printCenter(
                                                ConsoleUtils.ORANGE + "Loyalty points record not found"
                                                                + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH);
                        } else {
                                System.out
                                                .println(centerText("Total Points: " + loyaltyPoints.getPoints(),
                                                                ConsoleUtils.DEFAULT_WIDTH));
                                System.out.println();

                                System.out.println(centerText("1. Enter voucher code to redeem",
                                                ConsoleUtils.DEFAULT_WIDTH));
                                System.out.println(centerText("2. Back to menu", ConsoleUtils.DEFAULT_WIDTH));
                                System.out.println();

                                System.out.print(
                                                centerText(ConsoleUtils.YELLOW + "Choose option (1-2): "
                                                                + ConsoleUtils.RESET,
                                                                ConsoleUtils.DEFAULT_WIDTH));
                                int choice = InputValidator.getIntInput(sc, "");

                                if (choice == 1) {
                                        System.out.println();
                                        System.out.print(centerText(
                                                        ConsoleUtils.YELLOW + "Enter voucher code: "
                                                                        + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH));
                                        String voucherCode = sc.nextLine().trim();

                                        VoucherCode voucher = voucherDAO.findByCode(voucherCode);
                                        if (voucher == null) {
                                                ConsoleUtils.printCenter(
                                                                ConsoleUtils.RED + "Voucher not found!"
                                                                                + ConsoleUtils.RESET,
                                                                ConsoleUtils.DEFAULT_WIDTH);
                                        } else if (!isVoucherValid(voucher, currentUser.getUserId())) {
                                                System.out.println();
                                        } else {
                                                System.out.println();
                                                System.out.println(centerText("Voucher: " + voucher.getCode(),
                                                                ConsoleUtils.DEFAULT_WIDTH));
                                                System.out.println(
                                                                centerText("Description: " + voucher.getDescription(),
                                                                                ConsoleUtils.DEFAULT_WIDTH));
                                                System.out.println(centerText(
                                                                "Discount: "
                                                                                + (voucher.getDiscountType().name()
                                                                                                .equals("FIXED") ? "%"
                                                                                                                : "Fixed amount"),
                                                                ConsoleUtils.DEFAULT_WIDTH));
                                                System.out.println(centerText(
                                                                "Value: " + voucher.getDiscountValue(),
                                                                ConsoleUtils.DEFAULT_WIDTH));
                                        }
                                }
                        }

                        pauseMenu();
                } catch (Exception e) {
                        ConsoleUtils.printCenter(
                                        ConsoleUtils.RED + "Error: " + e.getMessage() + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                        pauseMenu();
                }
        }

        private boolean isVoucherValid(VoucherCode voucher, int userId) {
                if (voucher == null)
                        return false;
                if (!"ACTIVE".equals(voucher.getStatus().toString())) {
                        ConsoleUtils.printCenter(
                                ConsoleUtils.RED + "Voucher: Inactive!" + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH);
                        return false;
                }
                if (voucher.getExpiresAt() != null && voucher.getExpiresAt().isBefore(java.time.LocalDateTime.now())) {
                        ConsoleUtils.printCenter(
                                ConsoleUtils.RED + "Voucher: Expired!" + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH);
                        return false;
                }
                if (voucher.getMaxUses() > 0 && voucher.getUsedCount() >= voucher.getMaxUses()) {
                        ConsoleUtils.printCenter(
                                ConsoleUtils.RED + "Voucher: Exceeded maximum usage limit!" + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH);
                        return false;
                }

                if (checkIfUserUsedVoucher(userId, voucher.getVoucherId())) {
                        ConsoleUtils.printCenter(
                                ConsoleUtils.RED + "Voucher: You already used this voucher!" + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH);
                        return false;
                }
                return true;
        }


        private boolean checkIfUserUsedVoucher(int userId, int voucherId) {
                try {
                        List<Booking> userBookings = bookingDAO.findByUserId(userId);
                        if (userBookings != null && !userBookings.isEmpty()) {
                                for (Booking b : userBookings) {
                                        if (b.getVoucherId() != null && b.getVoucherId() == voucherId && b.isPaid()) {
                                                return true;
                                        }
                                }
                        }
                        return false;
                } catch (Exception e) {
                        System.err.println("Error checking voucher usage: " + e.getMessage());
                        return false;
                }
        }

        private void displayVouchers() {
                try {
                        List<VoucherCode> vouchers = voucherDAO.findAllActive();

                        if (vouchers.isEmpty()) {
                                ConsoleUtils.printCenter(
                                                ConsoleUtils.ORANGE + "No active vouchers available"
                                                                + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH);
                                return;
                        }

                        String[] headers = { "Code", "Description", "Type", "Value" };
                        int[] columnWidths = { 12, 25, 10, 10 };

                        ConsoleUtils.printTableHeader(headers, columnWidths, ConsoleUtils.DEFAULT_WIDTH);

                        for (int idx = 0; idx < vouchers.size(); idx++) {
                                VoucherCode voucher = vouchers.get(idx);
                                String typeStr = voucher.getDiscountType().name().equals("FIXED") ? "%" : "Amount";
                                String[] rowData = {
                                                voucher.getCode(),
                                                voucher.getDescription(),
                                                typeStr,
                                                String.valueOf(voucher.getDiscountValue())
                                };

                                ConsoleUtils.printTableRow(rowData, columnWidths, ConsoleUtils.DEFAULT_WIDTH);
                                if (idx < vouchers.size() - 1) {
                                        ConsoleUtils.printTableRowSeparator(columnWidths, ConsoleUtils.DEFAULT_WIDTH);
                                }
                        }

                        ConsoleUtils.printTableFooter(columnWidths, ConsoleUtils.DEFAULT_WIDTH);
                } catch (Exception e) {
                        ConsoleUtils.printCenter(
                                        ConsoleUtils.RED + "Error loading vouchers: " + e.getMessage()
                                                        + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                }
        }

        private void pauseMenu() {
                System.out.println();
                System.out.print(centerText(
                                ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH));
                sc.nextLine();
        }

        private void displayFoodMenu() {
                System.out.println();
                ConsoleUtils.printCenter(ConsoleUtils.CYAN + "━━━━━━━━━━ AVAILABLE FOOD & BEVERAGE ━━━━━━━━━━"
                                + ConsoleUtils.RESET, ConsoleUtils.DEFAULT_WIDTH);
                System.out.println();
                showFoodMenuTable();
        }

        private void displayItemsForOrder(List<FoodItem> foodItems, List<Combo> combos) {
                ConsoleUtils.printCenter(
                                ConsoleUtils.YELLOW + "Danh sách item có sẵn:" + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH);
                System.out.println();


                if (foodItems != null && !foodItems.isEmpty()) {
                        displayFoodItemsSection(foodItems);
                        System.out.println();
                        System.out.println();
                }


                if (combos != null && !combos.isEmpty()) {
                        displayCombosSection(combos);
                }
        }

        private void showFoodMenuTable() {
                try {

                        displayFoodItemsSection(foodItemDAO.findAll());

                        System.out.println();
                        System.out.println();


                        displayCombosSection(comboDAO.findAll(false));

                        System.out.println();
                } catch (Exception e) {
                        ConsoleUtils.printCenter(
                                        ConsoleUtils.RED + "Error loading menu: " + e.getMessage()
                                                        + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                }
        }

        private void displayFoodItemsSection(List<FoodItem> items) {
                if (items == null || items.isEmpty()) {
                        ConsoleUtils.printCenter(ConsoleUtils.YELLOW + "No food items available!" + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                        return;
                }

                ConsoleUtils.printCenter(
                                ConsoleUtils.GREEN + "━━━━━━━━━━━━━━━━━━━━━━━ ＦＯＯＤ  ＩＴＥＭＳ ━━━━━━━━━━━━━━━━━━━━━━━"
                                                + ConsoleUtils.RESET,
                                180);
                System.out.println();

                String[] headers = { "ID", "Name", "Category", "Description", "Price", "Status", "Sizes", "Toppings" };
                int[] columnWidths = { 4, 25, 12, 30, 25, 15, 20, 60 };

                ConsoleUtils.printTableHeader(headers, columnWidths, 180);

                for (int i = 0; i < items.size(); i++) {
                        FoodItem item = items.get(i);

                        String priceRange = "";
                        if (item.getMinPrice() != null && item.getMaxPrice() != null) {
                                priceRange = ConsoleUtils.formatPriceRange(item.getMinPrice(), item.getMaxPrice());
                        } else {
                                priceRange = "N/A";
                        }

                        String status = item.isDeleted() ? "DELETED" : item.getStatus().getDescription();
                        String sizes = item.getAvailableSizes() != null ? item.getAvailableSizes() : "N/A";
                        String toppings = item.getAvailableToppings() != null ? item.getAvailableToppings() : "None";
                        String description = item.getDescription() != null ? item.getDescription() : "";

                        String[] rowData = {
                                        String.valueOf(item.getItemId()),
                                        item.getName() != null ? item.getName() : "N/A",
                                        item.getCategoryName() != null ? item.getCategoryName() : "N/A",
                                        description,
                                        priceRange,
                                        status,
                                        sizes,
                                        toppings
                        };

                        ConsoleUtils.printTableRow(rowData, columnWidths, 180);

                        if (i < items.size() - 1) {
                                ConsoleUtils.printTableRowSeparator(columnWidths, 180);
                        }
                }

                ConsoleUtils.printTableFooter(columnWidths, 180);
        }

        private void displayCombosSection(List<Combo> combos) {
                if (combos == null || combos.isEmpty()) {
                        ConsoleUtils.printCenter(ConsoleUtils.YELLOW + "No combos available!" + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                        return;
                }

                ConsoleUtils.printCenter(
                                ConsoleUtils.ORANGE + "━━━━━━━━━━━━━━━━━━━━━━━ ＳＰＥＣＩＡＬ  ＣＯＭＢＯＳ ━━━━━━━━━━━━━━━━━━━━━━━"
                                                + ConsoleUtils.RESET,
                                180);
                System.out.println();

                String[] headers = { "ID", "Combo Name", "Price", "Status", "Contents", "Details" };
                int[] columnWidths = { 4, 40, 15, 12, 55, 65 };

                ConsoleUtils.printTableHeader(headers, columnWidths, 180);

                for (int i = 0; i < combos.size(); i++) {
                        Combo combo = combos.get(i);

                        String status = combo.isDeleted() ? "DELETED" : combo.getStatus().getDescription();
                        String contents = combo.getContainedItems() != null ? combo.getContainedItems() : "N/A";
                        String details = combo.getComboItems() != null ? combo.getComboItems() : "N/A";
                        String price = combo.getPrice() != null ? ConsoleUtils.formatVND(combo.getPrice()) : "N/A";

                        String[] rowData = {
                                        String.valueOf(combo.getComboId()),
                                        combo.getName() != null ? combo.getName() : "N/A",
                                        price,
                                        status,
                                        contents,
                                        details
                        };

                        ConsoleUtils.printTableRow(rowData, columnWidths, 180);

                        if (i < combos.size() - 1) {
                                ConsoleUtils.printTableRowSeparator(columnWidths, 180);
                        }
                }

                ConsoleUtils.printTableFooter(columnWidths, 180);
        }


        private List<model.OrderDetail> collectOrderItemsFromUser() {
                List<model.OrderDetail> orderDetails = new java.util.ArrayList<>();


                List<FoodItem> allFoodItems = foodItemDAO.findAll();
                List<Combo> allCombos = comboDAO.findAll(false);

                System.out.println();
                ConsoleUtils.printCenter(ConsoleUtils.CYAN + "─── ADD ITEMS TO ORDER ───" + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH);
                ConsoleUtils.printCenter(
                                ConsoleUtils.YELLOW + "Nhập: Tên món x số lượng  (ví dụ: Pizza x2, Cola x1)"
                                                + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH);
                ConsoleUtils.printCenter(ConsoleUtils.YELLOW + "Nhấn 0 để xong" + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH);
                System.out.println();

                boolean addingItems = true;
                while (addingItems) {
                        System.out.print(
                                        centerText(ConsoleUtils.CYAN + "Nhập món ăn (0 để xong): " + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH));
                        String itemInput = sc.nextLine().trim();

                        if (itemInput.equals("0")) {
                                addingItems = false;
                                break;
                        }

                        if (itemInput.isEmpty()) {
                                ConsoleUtils.printCenter(
                                                ConsoleUtils.RED + "Item không được để trống!" + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH);
                                continue;
                        }


                        String[] parts = itemInput.split("x");
                        if (parts.length != 2) {
                                ConsoleUtils.printCenter(
                                                ConsoleUtils.RED + "Format sai! Dùng: 'ItemName x quantity' (ví dụ: Pizza x2)"
                                                                + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH);
                                continue;
                        }

                        String itemName = parts[0].trim();
                        String quantityStr = parts[1].trim();

                        int quantity;
                        try {
                                quantity = Integer.parseInt(quantityStr);
                                if (quantity <= 0) {
                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.RED + "Số lượng phải > 0!" + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                        continue;
                                }
                        } catch (NumberFormatException e) {
                                ConsoleUtils.printCenter(
                                                ConsoleUtils.RED + "Số lượng phải là số!" + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH);
                                continue;
                        }


                        FoodItem foundItem = null;
                        for (FoodItem fi : allFoodItems) {
                                if (fi.getName().toLowerCase().contains(itemName.toLowerCase())) {
                                        foundItem = fi;
                                        break;
                                }
                        }


                        Combo foundCombo = null;
                        if (foundItem == null) {
                                for (Combo combo : allCombos) {
                                        if (combo.getName().toLowerCase().contains(itemName.toLowerCase())) {
                                                foundCombo = combo;
                                                break;
                                        }
                                }
                        }

                        if (foundItem == null && foundCombo == null) {
                                ConsoleUtils.printCenter(
                                                ConsoleUtils.RED + "Không tìm thấy item: '" + itemName + "'" + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH);
                                System.out.println();
                                continue;
                        }


                        try {
                                if (foundItem != null) {

                                        int sizeId = 1;
                                        String availableSizes = foundItem.getAvailableSizes();
                                        if (availableSizes != null && !availableSizes.trim().isEmpty()) {
                                                String[] sizes = availableSizes.split(",");
                                                System.out.println();
                                                System.out.println(centerText(
                                                                ConsoleUtils.YELLOW + foundItem.getName() + " - Available sizes:" + ConsoleUtils.RESET,
                                                                ConsoleUtils.DEFAULT_WIDTH));

                                                for (int i = 0; i < sizes.length; i++) {
                                                        String sizeName = sizes[i].trim();
                                                        System.out.println(centerText(
                                                                        (i + 1) + ". " + sizeName,
                                                                        ConsoleUtils.DEFAULT_WIDTH));
                                                }

                                                System.out.print(centerText(
                                                                ConsoleUtils.YELLOW + "Select size (1-" + sizes.length + "): " + ConsoleUtils.RESET,
                                                                ConsoleUtils.DEFAULT_WIDTH));
                                                int sizeChoice = InputValidator.getIntInput(sc, "");
                                                if (sizeChoice >= 1 && sizeChoice <= sizes.length) {
                                                        sizeId = sizeChoice;
                                                }
                                        }


                                        double price = foundItem.getMinPrice();
                                        String[] sizes = availableSizes != null ? availableSizes.split(",\\s*") : new String[]{};
                                        if (sizes.length > 1) {
                                                double sizeRatio = (sizeId - 1) / (double)(sizes.length - 1);
                                                price = foundItem.getMinPrice() + (foundItem.getMaxPrice() - foundItem.getMinPrice()) * sizeRatio;
                                        }

                                        model.OrderDetail detail = new model.OrderDetail();
                                        detail.setItemId(foundItem.getItemId());
                                        detail.setSizeId(sizeId);
                                        detail.setQuantity(quantity);
                                        detail.setPrice(price);
                                        orderDetails.add(detail);

                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.GREEN + foundItem.getName() + " x" + quantity + " added successfully!"
                                                                        + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                } else if (foundCombo != null) {

                                        double price = foundCombo.getPrice() != null ?
                                                foundCombo.getPrice().doubleValue() : 0.0;

                                        model.OrderDetail detail = new model.OrderDetail();
                                        detail.setComboId(foundCombo.getComboId());
                                        detail.setQuantity(quantity);
                                        detail.setPrice(price);
                                        orderDetails.add(detail);

                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.GREEN + foundCombo.getName() + " x" + quantity + " added successfully!"
                                                                        + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                }
                        } catch (Exception e) {
                                ConsoleUtils.printCenter(
                                                ConsoleUtils.RED + "Error adding item: " + e.getMessage() + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH);
                        }
                }

                return orderDetails;
        }


        private double displayOrderSummary(List<model.OrderDetail> orderDetails) {
                System.out.println();
                ConsoleUtils.printWideMenuBox(
                                ConsoleUtils.CYAN + "ORDER SUMMARY" + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH);
                System.out.println();

                double totalAmount = 0;
                for (model.OrderDetail detail : orderDetails) {
                        Integer itemId = detail.getItemId();
                        Integer comboId = detail.getComboId();
                        Integer quantity = detail.getQuantity();
                        Double itemPrice = detail.getPrice();

                        String name = "Unknown";
                        if (itemId != null && itemId > 0) {
                                FoodItem foodItem = foodItemDAO.findById(itemId);
                                name = foodItem != null ? foodItem.getName() : "Unknown Food";
                        } else if (comboId != null && comboId > 0) {
                                Combo combo = comboDAO.findById(comboId);
                                name = combo != null ? combo.getName() : "Unknown Combo";
                        }

                        double itemTotal = itemPrice * quantity;
                        totalAmount += itemTotal;

                        System.out.println(centerText(
                                        name + " x" + quantity + " @ " + ConsoleUtils.formatVND(itemPrice)
                                        + " = " + ConsoleUtils.formatVND(itemTotal),
                                        ConsoleUtils.DEFAULT_WIDTH));
                }

                System.out.println();
                System.out.println(centerText(
                                ConsoleUtils.CYAN + "TOTAL: " + ConsoleUtils.formatVND(totalAmount) + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH));
                System.out.println();

                return totalAmount;
        }


        private java.util.List<java.util.Map<String, Object>> convertOrderDetailsToMaps(List<model.OrderDetail> orderDetails) {
                java.util.List<java.util.Map<String, Object>> itemMaps = new java.util.ArrayList<>();
                for (model.OrderDetail detail : orderDetails) {
                        java.util.Map<String, Object> itemInfo = new java.util.HashMap<>();

                        Integer itemId = detail.getItemId();
                        Integer comboId = detail.getComboId();
                        Integer quantity = detail.getQuantity();
                        Double price = detail.getPrice();

                        if (itemId != null && itemId > 0) {

                                itemInfo.put("itemId", itemId);
                                itemInfo.put("isCombo", false);
                        } else if (comboId != null && comboId > 0) {

                                itemInfo.put("itemId", -comboId);
                                itemInfo.put("isCombo", true);
                        }

                        itemInfo.put("quantity", quantity);
                        itemInfo.put("price", price * quantity);
                        itemMaps.add(itemInfo);
                }
                return itemMaps;
        }

}
