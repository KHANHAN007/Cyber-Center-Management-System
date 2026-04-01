package presentation.handlers;

import dao.interfaces.IVoucherCodeDAO;
import model.VoucherCode;
import presentation.ConsoleUtils;

import java.util.List;
import java.util.Scanner;

import static presentation.ConsoleUtils.centerText;

public class VoucherManagementHandler {
        private Scanner sc;
        @SuppressWarnings("unused")
        private IVoucherCodeDAO voucherDAO;

        public VoucherManagementHandler(Scanner sc, IVoucherCodeDAO voucherDAO) {
                this.sc = sc;
                this.voucherDAO = voucherDAO;
        }

        public void handleVoucherManagement() {
                while (true) {
                        ConsoleUtils.clearScreen();
                        String[] logo = {
                                "▗▖  ▗▖ ▗▄▖ ▗▖ ▗▖ ▗▄▄▖▗▖ ▗▖▗▄▄▄▖▗▄▄▖     ▗▖  ▗▖ ▗▄▖ ▗▖  ▗▖ ▗▄▖  ▗▄▄▖▗▄▄▄▖▗▖  ▗▖▗▄▄▄▖▗▖  ▗▖▗▄▄▄▖",
                                "▐▌  ▐▌▐▌ ▐▌▐▌ ▐▌▐▌   ▐▌ ▐▌▐▌   ▐▌ ▐▌    ▐▛▚▞▜▌▐▌ ▐▌▐▛▚▖▐▌▐▌ ▐▌▐▌   ▐▌   ▐▛▚▞▜▌▐▌   ▐▛▚▖▐▌  █",
                                "▐▌  ▐▌▐▌ ▐▌▐▌ ▐▌▐▌   ▐▛▀▜▌▐▛▀▀▘▐▛▀▚▖    ▐▌  ▐▌▐▛▀▜▌▐▌ ▝▜▌▐▛▀▜▌▐▌▝▜▌▐▛▀▀▘▐▌  ▐▌▐▛▀▀▘▐▌ ▝▜▌  █",
                                " ▝▚▞▘ ▝▚▄▞▘▝▚▄▞▘▝▚▄▄▖▐▌ ▐▌▐▙▄▄▖▐▌ ▐▌    ▐▌  ▐▌▐▌ ▐▌▐▌  ▐▌▐▌ ▐▌▝▚▄▞▘▐▙▄▄▖▐▌  ▐▌▐▙▄▄▖▐▌  ▐▌  █",
                        };

                        ConsoleUtils.printLogoCentered(logo, ConsoleUtils.DEFAULT_WIDTH, 88);
                        ConsoleUtils.printCenter(
                                ConsoleUtils.CYAN + "━━━━━━━━ ＶＯＵＣＨＥＲ ＭＡＮＡＧＥＭＥＮＴ ━━━━━━━━" + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH);
                        System.out.println();

                        String[] options = {
                                "1. View all vouchers",
                                "2. Create voucher",
                                "3. Update voucher",
                                "4. Deactivate voucher",
                                "5. Back"
                        };

                        String[] colors = {
                                ConsoleUtils.GREEN,
                                ConsoleUtils.GREEN,
                                ConsoleUtils.GREEN,
                                ConsoleUtils.ORANGE,
                                ConsoleUtils.RED
                        };

                        ConsoleUtils.printMenuOptions(options, ConsoleUtils.DEFAULT_WIDTH, colors);
                        System.out.println();

                        System.out.print(centerText(
                                ConsoleUtils.YELLOW + "Choose an option (1-5): " + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH));

                        try {
                                int choice = Integer.parseInt(sc.nextLine());
                                switch (choice) {
                                        case 1:
                                                viewAllVouchers();
                                                break;
                                        case 2:
                                                createVoucher();
                                                break;
                                        case 3:
                                                updateVoucher();
                                                break;
                                        case 4:
                                                deactivateVoucher();
                                                break;
                                        case 5:
                                                return;
                                        default:
                                                ConsoleUtils.printCenter(
                                                        ConsoleUtils.RED + " Invalid choice!"
                                                                + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                                System.out.println(centerText(
                                                        ConsoleUtils.YELLOW + "Press Enter to continue..."
                                                                + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH));
                                                sc.nextLine();
                                }
                        } catch (NumberFormatException e) {
                                ConsoleUtils.printCenter(ConsoleUtils.RED + " Invalid input!" + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                                System.out.println(centerText(
                                        ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH));
                                sc.nextLine();
                        }
                }
        }

        private void viewAllVouchers() {
                try {
                        ConsoleUtils.clearScreen();
                        String[] logo = {
                                "▗▖  ▗▖ ▗▄▖ ▗▖ ▗▖ ▗▄▄▖▗▖ ▗▖▗▄▄▄▖▗▄▄▖     ▗▖  ▗▖ ▗▄▖ ▗▖  ▗▖ ▗▄▖  ▗▄▄▖▗▄▄▄▖▗▖  ▗▖▗▄▄▄▖▗▖  ▗▖▗▄▄▄▖",
                                "▐▌  ▐▌▐▌ ▐▌▐▌ ▐▌▐▌   ▐▌ ▐▌▐▌   ▐▌ ▐▌    ▐▛▚▞▜▌▐▌ ▐▌▐▛▚▖▐▌▐▌ ▐▌▐▌   ▐▌   ▐▛▚▞▜▌▐▌   ▐▛▚▖▐▌  █",
                                "▐▌  ▐▌▐▌ ▐▌▐▌ ▐▌▐▌   ▐▛▀▜▌▐▛▀▀▘▐▛▀▚▖    ▐▌  ▐▌▐▛▀▜▌▐▌ ▝▜▌▐▛▀▜▌▐▌▝▜▌▐▛▀▀▘▐▌  ▐▌▐▛▀▀▘▐▌ ▝▜▌  █",
                                " ▝▚▞▘ ▝▚▄▞▘▝▚▄▞▘▝▚▄▄▖▐▌ ▐▌▐▙▄▄▖▐▌ ▐▌    ▐▌  ▐▌▐▌ ▐▌▐▌  ▐▌▐▌ ▐▌▝▚▄▞▘▐▙▄▄▖▐▌  ▐▌▐▙▄▄▖▐▌  ▐▌  █",
                        };

                        ConsoleUtils.printLogoCentered(logo, ConsoleUtils.DEFAULT_WIDTH, 88);
                        ConsoleUtils.printCenter(
                                ConsoleUtils.GREEN + "━━━━━━━━━━━━━ ＡＬＬ ＶＯＵＣＨＥＲＳ ━━━━━━━━━━━━━"
                                        + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH);
                        System.out.println();

                        List<VoucherCode> vouchers = voucherDAO.findAll();
                        if (vouchers == null || vouchers.isEmpty()) {
                                ConsoleUtils.printCenter(
                                        ConsoleUtils.YELLOW + "No vouchers available!" + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                        } else {
                                String[] headers = { "ID", "Code", "Discount", "Max Uses", "Used", "Remaining",
                                        "Status", "Expires" };
                                int[] columnWidths = { 4, 12, 15, 10, 8, 12, 10, 15 };

                                ConsoleUtils.printTableHeader(headers, columnWidths, 140);

                                for (int i = 0; i < vouchers.size(); i++) {
                                        VoucherCode voucher = vouchers.get(i);
                                        String discount = voucher.getDiscountType() != null
                                                ? (voucher.getDiscountType().name().equals("PERCENT")
                                                ? voucher.getDiscountValue() + "%"
                                                : "₫" + voucher.getDiscountValue())
                                                : "N/A";

                                        String maxUsesStr = voucher.isUnlimited() ? "Unlimited"
                                                : String.valueOf(voucher.getMaxUses());
                                        String remainingStr = voucher.getRemainingUsesDisplay();
                                        String status = voucher.getStatus() != null ? voucher.getStatus().name()
                                                : "INACTIVE";

                                        String expiresStr = voucher.getExpiresAt() != null
                                                ? voucher.getExpiresAt().toLocalDate().toString()
                                                : "N/A";

                                        String[] rowData = {
                                                String.valueOf(voucher.getVoucherId()),
                                                voucher.getCode(),
                                                discount,
                                                maxUsesStr,
                                                String.valueOf(voucher.getUsedCount()),
                                                remainingStr,
                                                status,
                                                expiresStr
                                        };

                                        ConsoleUtils.printTableRow(rowData, columnWidths, 140);

                                        if (i < vouchers.size() - 1) {
                                                ConsoleUtils.printTableRowSeparator(columnWidths, 140);
                                        }
                                }

                                ConsoleUtils.printTableFooter(columnWidths, 140);
                        }

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

        private void createVoucher() {
                try {
                        ConsoleUtils.clearScreen();
                        String[] logo = {
                                "▗▖  ▗▖ ▗▄▖ ▗▖ ▗▖ ▗▄▄▖▗▖ ▗▖▗▄▄▄▖▗▄▄▖     ▗▖  ▗▖ ▗▄▖ ▗▖  ▗▖ ▗▄▖  ▗▄▄▖▗▄▄▄▖▗▖  ▗▖▗▄▄▄▖▗▖  ▗▖▗▄▄▄▖",
                                "▐▌  ▐▌▐▌ ▐▌▐▌ ▐▌▐▌   ▐▌ ▐▌▐▌   ▐▌ ▐▌    ▐▛▚▞▜▌▐▌ ▐▌▐▛▚▖▐▌▐▌ ▐▌▐▌   ▐▌   ▐▛▚▞▜▌▐▌   ▐▛▚▖▐▌  █",
                                "▐▌  ▐▌▐▌ ▐▌▐▌ ▐▌▐▌   ▐▛▀▜▌▐▛▀▀▘▐▛▀▚▖    ▐▌  ▐▌▐▛▀▜▌▐▌ ▝▜▌▐▛▀▜▌▐▌▝▜▌▐▛▀▀▘▐▌  ▐▌▐▛▀▀▘▐▌ ▝▜▌  █",
                                " ▝▚▞▘ ▝▚▄▞▘▝▚▄▞▘▝▚▄▄▖▐▌ ▐▌▐▙▄▄▖▐▌ ▐▌    ▐▌  ▐▌▐▌ ▐▌▐▌  ▐▌▐▌ ▐▌▝▚▄▞▘▐▙▄▄▖▐▌  ▐▌▐▙▄▄▖▐▌  ▐▌  █",
                        };

                        ConsoleUtils.printLogoCentered(logo, ConsoleUtils.DEFAULT_WIDTH, 88);
                        ConsoleUtils.printCenter(
                                ConsoleUtils.GREEN + "━━━━━━━━ ＣＲＥＡＴＥ ＮＥＷ ＶＯＵＣＨＥＲ ━━━━━━━━"
                                        + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH);
                        System.out.println();

                        System.out.print(centerText(ConsoleUtils.CYAN + "Enter voucher code: " + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH));
                        String code = sc.nextLine().trim().toUpperCase();
                        if (code.isEmpty()) {
                                ConsoleUtils.printCenter(
                                        ConsoleUtils.RED + "Voucher code cannot be empty!" + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                                System.out.print(centerText(
                                        ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH));
                                sc.nextLine();
                                return;
                        }

                        System.out.println();
                        ConsoleUtils.printCenter(ConsoleUtils.CYAN + "Select discount type:" + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH);
                        System.out.println();
                        String[] discountOptions = { "1. Percentage (%)", "2. Fixed amount (₫)" };
                        String[] discountColors = { ConsoleUtils.GREEN, ConsoleUtils.GREEN };
                        ConsoleUtils.printMenuOptions(discountOptions, ConsoleUtils.DEFAULT_WIDTH, discountColors);
                        System.out.println();

                        System.out.print(centerText(ConsoleUtils.YELLOW + "Choose (1 or 2): " + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH));
                        int discountTypeChoice = Integer.parseInt(sc.nextLine());
                        enums.DiscountType discountType;
                        if (discountTypeChoice == 1) {
                                discountType = enums.DiscountType.PERCENT;
                        } else if (discountTypeChoice == 2) {
                                discountType = enums.DiscountType.FIXED;
                        } else {
                                ConsoleUtils.printCenter(ConsoleUtils.RED + "Invalid choice!" + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                                System.out.print(centerText(
                                        ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH));
                                sc.nextLine();
                                return;
                        }

                        System.out.print(centerText(ConsoleUtils.CYAN + "Enter discount value: " + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH));
                        java.math.BigDecimal discountValue = new java.math.BigDecimal(sc.nextLine().trim());

                        if (discountType == enums.DiscountType.PERCENT
                                && discountValue.compareTo(java.math.BigDecimal.valueOf(100)) > 0) {
                                ConsoleUtils.printCenter(
                                        ConsoleUtils.RED + "Percentage discount cannot exceed 100%!"
                                                + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                                System.out.print(centerText(
                                        ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH));
                                sc.nextLine();
                                return;
                        }

                        System.out.print(centerText(
                                ConsoleUtils.CYAN + "Enter max uses (0 = unlimited): " + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH));
                        int maxUses = Integer.parseInt(sc.nextLine().trim());

                        System.out.print(centerText(
                                ConsoleUtils.CYAN + "Enter description (optional, press Enter to skip): "
                                        + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH));
                        String description = sc.nextLine().trim();

                        System.out.print(
                                centerText(ConsoleUtils.CYAN
                                        + "Enter expiry date (yyyy-MM-dd, or press Enter for no expiry): "
                                        + ConsoleUtils.RESET, ConsoleUtils.DEFAULT_WIDTH));
                        String expiryInput = sc.nextLine().trim();
                        java.time.LocalDateTime expiresAt = null;
                        if (!expiryInput.isEmpty()) {
                                try {
                                        java.time.LocalDate expiryDate = java.time.LocalDate.parse(expiryInput);
                                        expiresAt = expiryDate.atTime(23, 59, 59);
                                } catch (Exception e) {
                                        ConsoleUtils.printCenter(
                                                ConsoleUtils.RED + "Invalid date format!" + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH);
                                        System.out.print(centerText(
                                                ConsoleUtils.YELLOW + "Press Enter to continue..."
                                                        + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH));
                                        sc.nextLine();
                                        return;
                                }
                        }

                        VoucherCode voucher = new VoucherCode();
                        voucher.setCode(code);
                        voucher.setDiscountType(discountType);
                        voucher.setDiscountValue(discountValue);
                        voucher.setMaxUses(maxUses);
                        voucher.setUsedCount(0);
                        voucher.setDescription(description);
                        voucher.setStatus(enums.VoucherStatus.ACTIVE);
                        voucher.setCreatedAt(java.time.LocalDateTime.now());
                        voucher.setExpiresAt(expiresAt);


                        voucherDAO.create(voucher);

                        System.out.println();
                        ConsoleUtils.printCenter(
                                ConsoleUtils.GREEN + " Voucher created successfully!" + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH);
                        System.out.println();
                        System.out.print(centerText(
                                ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH));
                        sc.nextLine();

                } catch (NumberFormatException e) {
                        ConsoleUtils.printCenter(ConsoleUtils.RED + "Invalid number format!" + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH);
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

        private void updateVoucher() {
                try {
                        ConsoleUtils.clearScreen();
                        String[] logo = {
                                "▗▖  ▗▖ ▗▄▖ ▗▖ ▗▖ ▗▄▄▖▗▖ ▗▖▗▄▄▄▖▗▄▄▖     ▗▖  ▗▖ ▗▄▖ ▗▖  ▗▖ ▗▄▖  ▗▄▄▖▗▄▄▄▖▗▖  ▗▖▗▄▄▄▖▗▖  ▗▖▗▄▄▄▖",
                                "▐▌  ▐▌▐▌ ▐▌▐▌ ▐▌▐▌   ▐▌ ▐▌▐▌   ▐▌ ▐▌    ▐▛▚▞▜▌▐▌ ▐▌▐▛▚▖▐▌▐▌ ▐▌▐▌   ▐▌   ▐▛▚▞▜▌▐▌   ▐▛▚▖▐▌  █",
                                "▐▌  ▐▌▐▌ ▐▌▐▌ ▐▌▐▌   ▐▛▀▜▌▐▛▀▀▘▐▛▀▚▖    ▐▌  ▐▌▐▛▀▜▌▐▌ ▝▜▌▐▛▀▜▌▐▌▝▜▌▐▛▀▀▘▐▌  ▐▌▐▛▀▀▘▐▌ ▝▜▌  █",
                                " ▝▚▞▘ ▝▚▄▞▘▝▚▄▞▘▝▚▄▄▖▐▌ ▐▌▐▙▄▄▖▐▌ ▐▌    ▐▌  ▐▌▐▌ ▐▌▐▌  ▐▌▐▌ ▐▌▝▚▄▞▘▐▙▄▄▖▐▌  ▐▌▐▙▄▄▖▐▌  ▐▌  █",
                        };

                        ConsoleUtils.printLogoCentered(logo, ConsoleUtils.DEFAULT_WIDTH, 88);
                        ConsoleUtils.printCenter(
                                ConsoleUtils.YELLOW + "━━━━━━━━━ ＵＰＤＡＴＥ ＶＯＵＣＨＥＲ ━━━━━━━━━" + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH);
                        System.out.println();

                        System.out.print(centerText(ConsoleUtils.CYAN + "Enter voucher ID: " + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH));
                        int voucherId = Integer.parseInt(sc.nextLine().trim());

                        VoucherCode voucher = voucherDAO.findById(voucherId);
                        if (voucher == null) {
                                ConsoleUtils.printCenter(ConsoleUtils.RED + "Voucher not found!" + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                                System.out.print(centerText(
                                        ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH));
                                sc.nextLine();
                                return;
                        }

                        System.out.println();
                        String[] headers = { "Field", "Current Value" };
                        int[] columnWidths = { 20, 40 };
                        ConsoleUtils.printTableHeader(headers, columnWidths, 140);

                        String[] fields = { "Code", "Discount Type", "Discount Value", "Max Uses", "Used Count",
                                "Status",
                                "Expires At" };
                        String[] values = {
                                voucher.getCode(),
                                voucher.getDiscountType() != null ? voucher.getDiscountType().getDescription()
                                        : "N/A",
                                voucher.getDiscountValue() != null ? voucher.getDiscountValue().toString()
                                        : "N/A",
                                voucher.isUnlimited() ? "Unlimited" : String.valueOf(voucher.getMaxUses()),
                                String.valueOf(voucher.getUsedCount()),
                                voucher.getStatus() != null ? voucher.getStatus().getDescription() : "N/A",
                                voucher.getExpiresAt() != null ? voucher.getExpiresAt().toLocalDate().toString()
                                        : "No expiry"
                        };

                        for (int i = 0; i < fields.length; i++) {
                                ConsoleUtils.printTableRow(new String[] { fields[i], values[i] }, columnWidths, 140);
                                if (i < fields.length - 1) {
                                        ConsoleUtils.printTableRowSeparator(columnWidths, 140);
                                }
                        }
                        ConsoleUtils.printTableFooter(columnWidths, 140);

                        System.out.println();
                        ConsoleUtils.printCenter(ConsoleUtils.CYAN + "Select fields to update:" + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH);
                        System.out.println();

                        String[] updateOptions = {
                                "1. Update discount value",
                                "2. Update max uses",
                                "3. Update status",
                                "4. Update expiry date",
                                "5. Back"
                        };
                        String[] updateColors = { ConsoleUtils.GREEN, ConsoleUtils.GREEN, ConsoleUtils.GREEN,
                                ConsoleUtils.GREEN,
                                ConsoleUtils.RED };
                        ConsoleUtils.printMenuOptions(updateOptions, ConsoleUtils.DEFAULT_WIDTH, updateColors);
                        System.out.println();

                        System.out.print(centerText(ConsoleUtils.YELLOW + "Choose (1-5): " + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH));
                        int updateChoice = Integer.parseInt(sc.nextLine().trim());

                        switch (updateChoice) {
                                case 1:
                                        System.out.print(centerText(
                                                ConsoleUtils.CYAN + "Enter new discount value: "
                                                        + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH));
                                        java.math.BigDecimal newValue = new java.math.BigDecimal(sc.nextLine().trim());
                                        voucher.setDiscountValue(newValue);
                                        break;
                                case 2:
                                        System.out.print(
                                                centerText(ConsoleUtils.CYAN
                                                                + "Enter new max uses (0 = unlimited): "
                                                                + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH));
                                        int newMaxUses = Integer.parseInt(sc.nextLine().trim());
                                        voucher.setMaxUses(newMaxUses);
                                        break;
                                case 3:
                                        System.out.println();
                                        ConsoleUtils.printCenter(
                                                ConsoleUtils.CYAN + "Select new status:" + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH);
                                        System.out.println();
                                        String[] statusOptions = { "1. Active", "2. Inactive", "3. Expired" };
                                        String[] statusColors = { ConsoleUtils.GREEN, ConsoleUtils.ORANGE,
                                                ConsoleUtils.RED };
                                        ConsoleUtils.printMenuOptions(statusOptions, ConsoleUtils.DEFAULT_WIDTH,
                                                statusColors);
                                        System.out.println();
                                        System.out.print(centerText(
                                                ConsoleUtils.YELLOW + "Choose (1-3): " + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH));
                                        int statusChoice = Integer.parseInt(sc.nextLine().trim());
                                        enums.VoucherStatus newStatus;
                                        if (statusChoice == 1) {
                                                newStatus = enums.VoucherStatus.ACTIVE;
                                        } else if (statusChoice == 2) {
                                                newStatus = enums.VoucherStatus.INACTIVE;
                                        } else if (statusChoice == 3) {
                                                newStatus = enums.VoucherStatus.EXPIRED;
                                        } else {
                                                ConsoleUtils.printCenter(
                                                        ConsoleUtils.RED + "Invalid choice!"
                                                                + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                                System.out.print(
                                                        centerText(ConsoleUtils.YELLOW
                                                                        + "Press Enter to continue..."
                                                                        + ConsoleUtils.RESET,
                                                                ConsoleUtils.DEFAULT_WIDTH));
                                                sc.nextLine();
                                                return;
                                        }
                                        voucher.setStatus(newStatus);
                                        break;
                                case 4:
                                        System.out.print(centerText(ConsoleUtils.CYAN
                                                        + "Enter new expiry date (yyyy-MM-dd, or press Enter for no expiry): "
                                                        + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH));
                                        String newExpiryInput = sc.nextLine().trim();
                                        if (newExpiryInput.isEmpty()) {
                                                voucher.setExpiresAt(null);
                                        } else {
                                                try {
                                                        java.time.LocalDate newExpiryDate = java.time.LocalDate
                                                                .parse(newExpiryInput);
                                                        voucher.setExpiresAt(newExpiryDate.atTime(23, 59, 59));
                                                } catch (Exception e) {
                                                        ConsoleUtils.printCenter(
                                                                ConsoleUtils.RED + "Invalid date format!"
                                                                        + ConsoleUtils.RESET,
                                                                ConsoleUtils.DEFAULT_WIDTH);
                                                        System.out.print(
                                                                centerText(ConsoleUtils.YELLOW
                                                                                + "Press Enter to continue..."
                                                                                + ConsoleUtils.RESET,
                                                                        ConsoleUtils.DEFAULT_WIDTH));
                                                        sc.nextLine();
                                                        return;
                                                }
                                        }
                                        break;
                                case 5:
                                        return;
                                default:
                                        ConsoleUtils.printCenter(
                                                ConsoleUtils.RED + "Invalid choice!" + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH);
                                        System.out.print(centerText(
                                                ConsoleUtils.YELLOW + "Press Enter to continue..."
                                                        + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH));
                                        sc.nextLine();
                                        return;
                        }

                        voucherDAO.update(voucher);

                        System.out.println();
                        ConsoleUtils.printCenter(
                                ConsoleUtils.GREEN + " Voucher updated successfully!" + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH);
                        System.out.println();
                        System.out.print(centerText(
                                ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH));
                        sc.nextLine();

                } catch (NumberFormatException e) {
                        ConsoleUtils.printCenter(ConsoleUtils.RED + "Invalid number format!" + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH);
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

        private void deactivateVoucher() {
                try {
                        ConsoleUtils.clearScreen();
                        String[] logo = {
                                "▗▖  ▗▖ ▗▄▖ ▗▖ ▗▖ ▗▄▄▖▗▖ ▗▖▗▄▄▄▖▗▄▄▖     ▗▖  ▗▖ ▗▄▖ ▗▖  ▗▖ ▗▄▖  ▗▄▄▖▗▄▄▄▖▗▖  ▗▖▗▄▄▄▖▗▖  ▗▖▗▄▄▄▖",
                                "▐▌  ▐▌▐▌ ▐▌▐▌ ▐▌▐▌   ▐▌ ▐▌▐▌   ▐▌ ▐▌    ▐▛▚▞▜▌▐▌ ▐▌▐▛▚▖▐▌▐▌ ▐▌▐▌   ▐▌   ▐▛▚▞▜▌▐▌   ▐▛▚▖▐▌  █",
                                "▐▌  ▐▌▐▌ ▐▌▐▌ ▐▌▐▌   ▐▛▀▜▌▐▛▀▀▘▐▛▀▚▖    ▐▌  ▐▌▐▛▀▜▌▐▌ ▝▜▌▐▛▀▜▌▐▌▝▜▌▐▛▀▀▘▐▌  ▐▌▐▛▀▀▘▐▌ ▝▜▌  █",
                                " ▝▚▞▘ ▝▚▄▞▘▝▚▄▞▘▝▚▄▄▖▐▌ ▐▌▐▙▄▄▖▐▌ ▐▌    ▐▌  ▐▌▐▌ ▐▌▐▌  ▐▌▐▌ ▐▌▝▚▄▞▘▐▙▄▄▖▐▌  ▐▌▐▙▄▄▖▐▌  ▐▌  █",
                        };

                        ConsoleUtils.printLogoCentered(logo, ConsoleUtils.DEFAULT_WIDTH, 88);
                        ConsoleUtils.printCenter(
                                ConsoleUtils.ORANGE + "━━━━━━━━ ＤＥＡＣＴＩＶＡＴＥ ＶＯＵＣＨＥＲ ━━━━━━━━"
                                        + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH);
                        System.out.println();

                        System.out.print(centerText(
                                ConsoleUtils.CYAN + "Enter voucher ID to deactivate: " + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH));
                        int voucherId = Integer.parseInt(sc.nextLine().trim());

                        VoucherCode voucher = voucherDAO.findById(voucherId);
                        if (voucher == null) {
                                ConsoleUtils.printCenter(ConsoleUtils.RED + "Voucher not found!" + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                                System.out.print(centerText(
                                        ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH));
                                sc.nextLine();
                                return;
                        }


                        System.out.println();
                        ConsoleUtils.printCenter(ConsoleUtils.CYAN + "Voucher Details:" + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH);
                        System.out.println();
                        String[] headers = { "Code", "Discount", "Status" };
                        int[] columnWidths = { 15, 20, 15 };
                        ConsoleUtils.printTableHeader(headers, columnWidths, 140);

                        String discount = voucher.getDiscountType() != null
                                ? (voucher.getDiscountType().name().equals("PERCENT")
                                ? voucher.getDiscountValue() + "%"
                                : "₫" + voucher.getDiscountValue())
                                : "N/A";
                        String status = voucher.getStatus() != null ? voucher.getStatus().getDescription() : "INACTIVE";

                        ConsoleUtils.printTableRow(new String[] { voucher.getCode(), discount, status }, columnWidths,
                                140);
                        ConsoleUtils.printTableFooter(columnWidths, 140);

                        System.out.println();
                        System.out.print(centerText(
                                ConsoleUtils.RED + "Are you sure you want to deactivate this voucher? (yes/no): "
                                        + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH));
                        String confirmation = sc.nextLine().trim().toLowerCase();

                        if (confirmation.equals("yes") || confirmation.equals("y")) {
                                voucher.setStatus(enums.VoucherStatus.INACTIVE);
                                voucherDAO.update(voucher);

                                System.out.println();
                                ConsoleUtils.printCenter(
                                        ConsoleUtils.GREEN + " Voucher deactivated successfully!"
                                                + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                        } else {
                                System.out.println();
                                ConsoleUtils.printCenter(
                                        ConsoleUtils.YELLOW + "Operation cancelled!" + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                        }

                        System.out.println();
                        System.out.print(centerText(
                                ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH));
                        sc.nextLine();

                } catch (NumberFormatException e) {
                        ConsoleUtils.printCenter(ConsoleUtils.RED + "Invalid number format!" + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH);
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
