package presentation.handlers;

import dao.interfaces.IPCDAO;
import dao.interfaces.IPCConfigDAO;
import model.PC;
import model.PCConfig;
import model.PCDetail;
import presentation.ConsoleUtils;

import java.util.List;
import java.util.Scanner;

import static presentation.ConsoleUtils.centerText;

public class PCManagementHandler {
    private Scanner sc;
    private IPCDAO pcDAO;
    private IPCConfigDAO pcConfigDAO;

    public PCManagementHandler(Scanner sc, IPCDAO pcDAO, IPCConfigDAO pcConfigDAO) {
        this.sc = sc;
        this.pcDAO = pcDAO;
        this.pcConfigDAO = pcConfigDAO;
    }

    public void handlePCManagement() {
        while (true) {
            ConsoleUtils.clearScreen();
            String[] logo = {
                    "▗▄▄▖  ▗▄▄▖    ▗▖  ▗▖ ▗▄▖ ▗▖  ▗▖ ▗▄▖  ▗▄▄▖▗▄▄▄▖▗▖  ▗▖▗▄▄▄▖▗▖  ▗▖▗▄▄▄▖",
                    "▐▌ ▐▌▐▌       ▐▛▚▞▜▌▐▌ ▐▌▐▛▚▖▐▌▐▌ ▐▌▐▌   ▐▌   ▐▛▚▞▜▌▐▌   ▐▛▚▖▐▌  █",
                    "▐▛▀▘ ▐▌       ▐▌  ▐▌▐▛▀▜▌▐▌ ▝▜▌▐▛▀▜▌▐▌▝▜▌▐▛▀▀▘▐▌  ▐▌▐▛▀▀▘▐▌ ▝▜▌  █",
                    "▐▌   ▝▚▄▄▖    ▐▌  ▐▌▐▌ ▐▌▐▌  ▐▌▐▌ ▐▌▝▚▄▞▘▐▙▄▄▖▐▌  ▐▌▐▙▄▄▖▐▌  ▐▌  █",
            };

            ConsoleUtils.printLogoCentered(logo, 147, 70);

            String[] options = {
                    "1. View all PCs",
                    "2. Add new PC",
                    "3. Update PC status",
                    "4. PC detail",
                    "5. PC Config Management",
                    "6. Back"
            };

            String[] colors = {
                    ConsoleUtils.GREEN,
                    ConsoleUtils.GREEN,
                    ConsoleUtils.GREEN,
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
                        viewAllPCs();
                        break;
                    case 2:
                        addNewPC();
                        break;
                    case 3:
                        updatePCStatus();
                        break;
                    case 4:
                        viewPCDetail();
                        break;
                    case 5:
                        handlePCConfigManagement();
                        break;
                    case 6:
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

    private void handlePCConfigManagement() {
        while (true) {
            ConsoleUtils.clearScreen();
            String[] logo = {
                    "▗▄▄▖  ▗▄▄▖    ▗▖  ▗▖ ▗▄▖ ▗▖  ▗▖ ▗▄▖  ▗▄▄▖▗▄▄▄▖▗▖  ▗▖▗▄▄▄▖▗▖  ▗▖▗▄▄▄▖",
                    "▐▌ ▐▌▐▌       ▐▛▚▞▜▌▐▌ ▐▌▐▛▚▖▐▌▐▌ ▐▌▐▌   ▐▌   ▐▛▚▞▜▌▐▌   ▐▛▚▖▐▌  █",
                    "▐▛▀▘ ▐▌       ▐▌  ▐▌▐▛▀▜▌▐▌ ▝▜▌▐▛▀▜▌▐▌▝▜▌▐▛▀▀▘▐▌  ▐▌▐▛▀▀▘▐▌ ▝▜▌  █",
                    "▐▌   ▝▚▄▄▖    ▐▌  ▐▌▐▌ ▐▌▐▌  ▐▌▐▌ ▐▌▝▚▄▞▘▐▙▄▄▖▐▌  ▐▌▐▙▄▄▖▐▌  ▐▌  █",
            };

            ConsoleUtils.printLogoCentered(logo, 147, 70);

            String[] options = {
                    "1. Add PC Config",
                    "2. View PC Config detail",
                    "3. Back"
            };

            String[] colors = {
                    ConsoleUtils.GREEN,
                    ConsoleUtils.GREEN,
                    ConsoleUtils.RED
            };

            ConsoleUtils.printMenuOptions(options, ConsoleUtils.DEFAULT_WIDTH, colors);
            System.out.println();

            System.out.print(
                    centerText(
                            ConsoleUtils.YELLOW + "Choose an option (1-3): " + ConsoleUtils.RESET,
                            ConsoleUtils.DEFAULT_WIDTH));

            try {
                int choice = Integer.parseInt(sc.nextLine());
                switch (choice) {
                    case 1:
                        addPCConfig();
                        break;
                    case 2:
                        viewPCConfigDetail();
                        break;
                    case 3:
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

    private void viewAllPCs() {
        try {
            ConsoleUtils.clearScreen();
            String[] logo = {
                    "▗▄▄▖  ▗▄▄▖    ▗▖  ▗▖ ▗▄▖ ▗▖  ▗▖ ▗▄▖  ▗▄▄▖▗▄▄▄▖▗▖  ▗▖▗▄▄▄▖▗▖  ▗▖▗▄▄▄▖",
                    "▐▌ ▐▌▐▌       ▐▛▚▞▜▌▐▌ ▐▌▐▛▚▖▐▌▐▌ ▐▌▐▌   ▐▌   ▐▛▚▞▜▌▐▌   ▐▛▚▖▐▌  █",
                    "▐▛▀▘ ▐▌       ▐▌  ▐▌▐▛▀▜▌▐▌ ▝▜▌▐▛▀▜▌▐▌▝▜▌▐▛▀▀▘▐▌  ▐▌▐▛▀▀▘▐▌ ▝▜▌  █",
                    "▐▌   ▝▚▄▄▖    ▐▌  ▐▌▐▌ ▐▌▐▌  ▐▌▐▌ ▐▌▝▚▄▞▘▐▙▄▄▖▐▌  ▐▌▐▙▄▄▖▐▌  ▐▌  █",
            };
            ConsoleUtils.printLogoCentered(logo, 147, 70);

            List<PC> pcs = pcDAO.findAll();

            ConsoleUtils.printCenter(ConsoleUtils.GREEN + "All PCs" + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH);
            System.out.println();

            String[] headers = { "ID", "PC Code", "Zone", "Status", "Config" };
            int[] columnWidths = { 5, 20, 10, 15, 15 };
            ConsoleUtils.printTableHeader(headers, columnWidths, ConsoleUtils.DEFAULT_WIDTH);

            if (pcs != null && !pcs.isEmpty()) {
                for (PC pc : pcs) {
                    String[] row = {
                            String.valueOf(pc.getPcId()),
                            pc.getPcCode(),
                            String.valueOf(pc.getZoneId()),
                            pc.getStatus().getDescription(),
                            String.valueOf(pc.getConfigId())
                    };
                    ConsoleUtils.printTableRow(row, columnWidths, ConsoleUtils.DEFAULT_WIDTH);
                }
            } else {
                ConsoleUtils.printCenter(ConsoleUtils.YELLOW + "No PCs found" + ConsoleUtils.RESET,
                        ConsoleUtils.DEFAULT_WIDTH);
            }
            ConsoleUtils.printTableFooter(columnWidths, ConsoleUtils.DEFAULT_WIDTH);

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

    private void addNewPC() {
        try {
            ConsoleUtils.clearScreen();
            String[] logo = {
                    "▗▄▄▖  ▗▄▄▖    ▗▖  ▗▖ ▗▄▖ ▗▖  ▗▖ ▗▄▖  ▗▄▄▖▗▄▄▄▖▗▖  ▗▖▗▄▄▄▖▗▖  ▗▖▗▄▄▄▖",
                    "▐▌ ▐▌▐▌       ▐▛▚▞▜▌▐▌ ▐▌▐▛▚▖▐▌▐▌ ▐▌▐▌   ▐▌   ▐▛▚▞▜▌▐▌   ▐▛▚▖▐▌  █",
                    "▐▛▀▘ ▐▌       ▐▌  ▐▌▐▛▀▜▌▐▌ ▝▜▌▐▛▀▜▌▐▌▝▜▌▐▛▀▀▘▐▌  ▐▌▐▛▀▀▘▐▌ ▝▜▌  █",
                    "▐▌   ▝▚▄▄▖    ▐▌  ▐▌▐▌ ▐▌▐▌  ▐▌▐▌ ▐▌▝▚▄▞▘▐▙▄▄▖▐▌  ▐▌▐▙▄▄▖▐▌  ▐▌  █",
            };
            ConsoleUtils.printLogoCentered(logo, 147, 70);
            ConsoleUtils.printCenter(ConsoleUtils.CYAN + "Add New PC" + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH);
            System.out.println();

            System.out.println(centerText(ConsoleUtils.YELLOW + "PC Name: " + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH));
            String pcName = sc.nextLine().trim();

            if (pcName.isEmpty()) {
                ConsoleUtils.printCenter(ConsoleUtils.RED + "PC Name cannot be empty!" + ConsoleUtils.RESET,
                        ConsoleUtils.DEFAULT_WIDTH);
                System.out.println(centerText(
                        ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                        ConsoleUtils.DEFAULT_WIDTH));
                sc.nextLine();
                return;
            }

            System.out.println(centerText(ConsoleUtils.YELLOW + "Zone ID: " + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH));
            int zoneId = Integer.parseInt(sc.nextLine().trim());

            System.out.println(centerText(ConsoleUtils.YELLOW + "Config ID: " + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH));
            int configId = Integer.parseInt(sc.nextLine().trim());

            PC pc = new PC();
            pc.setPcName(pcName);
            pc.setZoneId(zoneId);
            pc.setConfigId(configId);
            pc.setStatus(enums.PCStatus.AVAILABLE);

            pcDAO.create(pc);

            System.out.println();
            ConsoleUtils.printCenter(
                    ConsoleUtils.GREEN + "PC added successfully!" + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH);
            System.out.println(centerText(
                    ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH));
            sc.nextLine();
        } catch (NumberFormatException e) {
            ConsoleUtils.printCenter(ConsoleUtils.RED + "Invalid input format!" + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH);
            System.out.println(centerText(
                    ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH));
            sc.nextLine();
        } catch (Exception e) {
            ConsoleUtils.printCenter(ConsoleUtils.RED + "Error: " + e.getMessage() + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH);
            System.out.println(centerText(
                    ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH));
            sc.nextLine();
        }
    }

    private void viewPCDetail() {
        try {
            ConsoleUtils.clearScreen();
            String[] logo = {
                    "▗▄▄▖  ▗▄▄▖    ▗▖  ▗▖ ▗▄▖ ▗▖  ▗▖ ▗▄▖  ▗▄▄▖▗▄▄▄▖▗▖  ▗▖▗▄▄▄▖▗▖  ▗▖▗▄▄▄▖",
                    "▐▌ ▐▌▐▌       ▐▛▚▞▜▌▐▌ ▐▌▐▛▚▖▐▌▐▌ ▐▌▐▌   ▐▌   ▐▛▚▞▜▌▐▌   ▐▛▚▖▐▌  █",
                    "▐▛▀▘ ▐▌       ▐▌  ▐▌▐▛▀▜▌▐▌ ▝▜▌▐▛▀▜▌▐▌▝▜▌▐▛▀▀▘▐▌  ▐▌▐▛▀▀▘▐▌ ▝▜▌  █",
                    "▐▌   ▝▚▄▄▖    ▐▌  ▐▌▐▌ ▐▌▐▌  ▐▌▐▌ ▐▌▝▚▄▞▘▐▙▄▄▖▐▌  ▐▌▐▙▄▄▖▐▌  ▐▌  █",
            };
            ConsoleUtils.printLogoCentered(logo, ConsoleUtils.DEFAULT_WIDTH, 68);
            ConsoleUtils.printCenter(ConsoleUtils.GREEN + "PC Details" + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH);
            System.out.println();

            System.out.println(centerText(ConsoleUtils.YELLOW + "Enter PC ID: " + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH));
            int pcId = Integer.parseInt(sc.nextLine().trim());

            PCDetail pcDetail = pcDAO.findDetailWithConfigById(pcId);

            if (pcDetail != null) {
                System.out.println();
                String[] headers = { "Attribute", "Value" };
                int[] columnWidths = { 25, 35 };
                ConsoleUtils.printTableHeader(headers, columnWidths, ConsoleUtils.DEFAULT_WIDTH);

                String[][] rows = {
                        { "PC ID", String.valueOf(pcDetail.getPcId()) },
                        { "PC Code", pcDetail.getPcCode() },
                        { "PC Name", pcDetail.getPcName() },
                        { "Zone ID", String.valueOf(pcDetail.getZoneId()) },
                        { "Status", pcDetail.getStatus().getDescription() },
                        { "Config ID", String.valueOf(pcDetail.getConfigId()) },
                        { "Config Code", pcDetail.getConfigCode() != null ? pcDetail.getConfigCode() : "N/A" },
                        { "CPU", pcDetail.getCpu() != null ? pcDetail.getCpu() : "N/A" },
                        { "RAM (GB)", String.valueOf(pcDetail.getRam()) },
                        { "GPU", pcDetail.getGpu() != null ? pcDetail.getGpu() : "N/A" },
                        { "Price per Hour (VND)", String.format("%.0f", pcDetail.getPricePerHour()) }
                };

                for (int i = 0; i < rows.length; i++) {
                    ConsoleUtils.printTableRow(rows[i], columnWidths, 140);
                    if (i < rows.length - 1) {
                        ConsoleUtils.printTableRowSeparator(columnWidths, 140);
                    }
                }
                ConsoleUtils.printTableFooter(columnWidths, ConsoleUtils.DEFAULT_WIDTH);
                System.out.println();
            } else {
                System.out.println();
                ConsoleUtils.printCenter(ConsoleUtils.RED + "PC not found!" + ConsoleUtils.RESET,
                        ConsoleUtils.DEFAULT_WIDTH);
            }

            System.out.println(centerText(
                    ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH));
            sc.nextLine();
        } catch (NumberFormatException e) {
            ConsoleUtils.printCenter(ConsoleUtils.RED + "Invalid PC ID format!" + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH);
            System.out.println(centerText(
                    ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH));
            sc.nextLine();
        } catch (Exception e) {
            ConsoleUtils.printCenter(ConsoleUtils.RED + "Error: " + e.getMessage() + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH);
            System.out.println(centerText(
                    ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH));
            sc.nextLine();
        }
    }

    private void addPCConfig() {
        try {
            ConsoleUtils.clearScreen();
            String[] logo = {
                    "▗▄▄▖  ▗▄▄▖    ▗▖  ▗▖ ▗▄▖ ▗▖  ▗▖ ▗▄▖  ▗▄▄▖▗▄▄▄▖▗▖  ▗▖▗▄▄▄▖▗▖  ▗▖▗▄▄▄▖",
                    "▐▌ ▐▌▐▌       ▐▛▚▞▜▌▐▌ ▐▌▐▛▚▖▐▌▐▌ ▐▌▐▌   ▐▌   ▐▛▚▞▜▌▐▌   ▐▛▚▖▐▌  █",
                    "▐▛▀▘ ▐▌       ▐▌  ▐▌▐▛▀▜▌▐▌ ▝▜▌▐▛▀▜▌▐▌▝▜▌▐▛▀▀▘▐▌  ▐▌▐▛▀▀▘▐▌ ▝▜▌  █",
                    "▐▌   ▝▚▄▄▖    ▐▌  ▐▌▐▌ ▐▌▐▌  ▐▌▐▌ ▐▌▝▚▄▞▘▐▙▄▄▖▐▌  ▐▌▐▙▄▄▖▐▌  ▐▌  █",
            };
            ConsoleUtils.printLogoCentered(logo, 147, 76);
            ConsoleUtils.printCenter(ConsoleUtils.CYAN + "Add PC Config" + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH);
            System.out.println();

            System.out
                    .print(centerText(ConsoleUtils.YELLOW + "CPU: " + ConsoleUtils.RESET, ConsoleUtils.DEFAULT_WIDTH));
            String cpu = sc.nextLine().trim();

            System.out
                    .print(centerText(ConsoleUtils.YELLOW + "RAM (GB): " + ConsoleUtils.RESET,
                            ConsoleUtils.DEFAULT_WIDTH));
            int ram = Integer.parseInt(sc.nextLine().trim());

            System.out
                    .print(centerText(ConsoleUtils.YELLOW + "GPU: " + ConsoleUtils.RESET, ConsoleUtils.DEFAULT_WIDTH));
            String gpu = sc.nextLine().trim();

            System.out.print(centerText(ConsoleUtils.YELLOW + "Price per Hour (VND): " + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH));
            double price = 0;
            boolean validPrice = false;
            while (!validPrice) {
                try {
                    String priceInput = sc.nextLine().trim();
                    price = Double.parseDouble(priceInput);
                    if (price < 0) {
                        ConsoleUtils.printCenter(
                                ConsoleUtils.RED + "✗ Price cannot be negative! Please try again." + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH);
                        System.out.print(centerText(ConsoleUtils.YELLOW + "Price per Hour (VND): " + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH));
                    } else {
                        validPrice = true;
                    }
                } catch (NumberFormatException e) {
                    ConsoleUtils.printCenter(
                            ConsoleUtils.RED + "✗ Invalid price format! Please enter a valid number."
                                    + ConsoleUtils.RESET,
                            ConsoleUtils.DEFAULT_WIDTH);
                    System.out.print(centerText(ConsoleUtils.YELLOW + "Price per Hour (VND): " + ConsoleUtils.RESET,
                            ConsoleUtils.DEFAULT_WIDTH));
                }
            }

            PCConfig config = new PCConfig();
            config.setCpu(cpu);
            config.setRam(ram);
            config.setGpu(gpu);
            config.setPricePerHour(price);

            pcConfigDAO.create(config);

            System.out.println();
            ConsoleUtils.printCenter(
                    ConsoleUtils.GREEN + "PC Config added successfully!" + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH);
            System.out.println(centerText(
                    ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH));
            sc.nextLine();
        } catch (NumberFormatException e) {
            ConsoleUtils.printCenter(ConsoleUtils.RED + "Invalid input format!" + ConsoleUtils.RESET,
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

    private void updatePCStatus() {
        try {
            ConsoleUtils.clearScreen();
            String[] logo = {
                    "▗▄▄▖  ▗▄▄▖    ▗▖  ▗▖ ▗▄▖ ▗▖  ▗▖ ▗▄▖  ▗▄▄▖▗▄▄▄▖▗▖  ▗▖▗▄▄▄▖▗▖  ▗▖▗▄▄▄▖",
                    "▐▌ ▐▌▐▌       ▐▛▚▞▜▌▐▌ ▐▌▐▛▚▖▐▌▐▌ ▐▌▐▌   ▐▌   ▐▛▚▞▜▌▐▌   ▐▛▚▖▐▌  █",
                    "▐▛▀▘ ▐▌       ▐▌  ▐▌▐▛▀▜▌▐▌ ▝▜▌▐▛▀▜▌▐▌▝▜▌▐▛▀▀▘▐▌  ▐▌▐▛▀▀▘▐▌ ▝▜▌  █",
                    "▐▌   ▝▚▄▄▖    ▐▌  ▐▌▐▌ ▐▌▐▌  ▐▌▐▌ ▐▌▝▚▄▞▘▐▙▄▄▖▐▌  ▐▌▐▙▄▄▖▐▌  ▐▌  █",
            };
            ConsoleUtils.printLogoCentered(logo, 147, 70);
            ConsoleUtils.printCenter(ConsoleUtils.CYAN + "Update PC Status" + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH);
            System.out.println();

            System.out.print(centerText(ConsoleUtils.YELLOW + "Enter PC ID: " + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH));
            int pcId = Integer.parseInt(sc.nextLine().trim());

            PC pc = pcDAO.findById(pcId);

            if (pc == null) {
                ConsoleUtils.printCenter(ConsoleUtils.RED + "PC not found!" + ConsoleUtils.RESET,
                        ConsoleUtils.DEFAULT_WIDTH);
                System.out.println(centerText(
                        ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                        ConsoleUtils.DEFAULT_WIDTH));
                sc.nextLine();
                return;
            }

            String pcCode = pc.getPcCode();
            String currentStatus = pc.getStatus().getDescription();
            String fullInfo = "PC Code: " + pcCode + " | Current Status: " + currentStatus;
            ConsoleUtils.printCenter(ConsoleUtils.CYAN + fullInfo + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH);
            System.out.println();

            // Loop to keep asking for status until valid choice
            boolean statusSelected = false;
            enums.PCStatus newStatus = null;

            while (!statusSelected) {
                String[] statusOptions = {
                        "1. Available",
                        "2. Booked",
                        "3. In Use",
                        "4. Maintenance",
                        "5. Cancel"
                };

                String[] statusColors = new String[5];
                for (int i = 0; i < 4; i++) {
                    statusColors[i] = ConsoleUtils.GREEN;
                }
                statusColors[4] = ConsoleUtils.RED;

                switch (pc.getStatus()) {
                    case AVAILABLE:
                        statusColors[0] = ConsoleUtils.GRAY;
                        break;
                    case BOOKED:
                        statusColors[1] = ConsoleUtils.GRAY;
                        break;
                    case IN_USE:
                        statusColors[2] = ConsoleUtils.GRAY;
                        break;
                    case MAINTENANCE:
                        statusColors[3] = ConsoleUtils.GRAY;
                        break;
                }

                ConsoleUtils.printMenuOptions(statusOptions, ConsoleUtils.DEFAULT_WIDTH, statusColors);
                System.out.println(centerText(
                        ConsoleUtils.YELLOW + "Enter new status (1-5): " + ConsoleUtils.RESET,
                        ConsoleUtils.DEFAULT_WIDTH));

                int choice = Integer.parseInt(sc.nextLine().trim());

                switch (choice) {
                    case 1:
                        newStatus = enums.PCStatus.AVAILABLE;
                        break;
                    case 2:
                        newStatus = enums.PCStatus.BOOKED;
                        break;
                    case 3:
                        newStatus = enums.PCStatus.IN_USE;
                        break;
                    case 4:
                        newStatus = enums.PCStatus.MAINTENANCE;
                        break;
                    case 5:
                        System.out.println();
                        ConsoleUtils.printCenter(ConsoleUtils.CYAN + "Operation cancelled" + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH);
                        System.out.println(centerText(
                                ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH));
                        sc.nextLine();
                        return;
                    default:
                        System.out.println();
                        ConsoleUtils.printCenter(ConsoleUtils.RED + "Invalid choice!" + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH);
                        System.out.println(centerText(
                                ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH));
                        sc.nextLine();
                        System.out.println();
                        ConsoleUtils.clearScreen();
                        ConsoleUtils.printLogoCentered(logo, ConsoleUtils.DEFAULT_WIDTH, 68);
                        ConsoleUtils.printCenter(ConsoleUtils.CYAN + "Update PC Status" + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH);
                        System.out.println();
                        ConsoleUtils.printCenter(ConsoleUtils.CYAN + fullInfo + ConsoleUtils.RESET,
                                ConsoleUtils.DEFAULT_WIDTH);
                        System.out.println();
                        continue;
                }

                if (newStatus == pc.getStatus()) {
                    System.out.println();
                    ConsoleUtils.printCenter(
                            ConsoleUtils.YELLOW + "PC is already in " + pc.getStatus().getDescription()
                                    + " status! Please choose a different status."
                                    + ConsoleUtils.RESET,
                            ConsoleUtils.DEFAULT_WIDTH);
                    System.out.println(centerText(
                            ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                            ConsoleUtils.DEFAULT_WIDTH));
                    sc.nextLine();
                    System.out.println();
                    ConsoleUtils.clearScreen();
                    ConsoleUtils.printLogoCentered(logo, ConsoleUtils.DEFAULT_WIDTH, 68);
                    ConsoleUtils.printCenter(ConsoleUtils.CYAN + "Update PC Status" + ConsoleUtils.RESET,
                            ConsoleUtils.DEFAULT_WIDTH);
                    System.out.println();
                    ConsoleUtils.printCenter(ConsoleUtils.CYAN + fullInfo + ConsoleUtils.RESET,
                            ConsoleUtils.DEFAULT_WIDTH);
                    System.out.println();
                    continue;
                }

                statusSelected = true;
            }

            pc.setStatus(newStatus);
            pcDAO.update(pc);

            System.out.println();
            ConsoleUtils.printCenter(
                    ConsoleUtils.GREEN + "PC status updated successfully!" + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH);
            ConsoleUtils.printCenter(
                    ConsoleUtils.GREEN + "New Status: " + pc.getStatus().getDescription() + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH);
            System.out.println(centerText(
                    ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH));
            sc.nextLine();
        } catch (NumberFormatException e) {
            ConsoleUtils.printCenter(ConsoleUtils.RED + "Invalid input format!" + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH);
            System.out.println(centerText(
                    ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH));
            sc.nextLine();
        } catch (Exception e) {
            ConsoleUtils.printCenter(ConsoleUtils.RED + "Error: " + e.getMessage() + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH);
            System.out.println(centerText(
                    ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH));
            sc.nextLine();
        }
    }

    private void viewPCConfigDetail() {
        try {
            ConsoleUtils.clearScreen();
            String[] logo = {
                    "▗▄▄▖  ▗▄▄▖    ▗▖  ▗▖ ▗▄▖ ▗▖  ▗▖ ▗▄▖  ▗▄▄▖▗▄▄▄▖▗▖  ▗▖▗▄▄▄▖▗▖  ▗▖▗▄▄▄▖",
                    "▐▌ ▐▌▐▌       ▐▛▚▞▜▌▐▌ ▐▌▐▛▚▖▐▌▐▌ ▐▌▐▌   ▐▌   ▐▛▚▞▜▌▐▌   ▐▛▚▖▐▌  █",
                    "▐▛▀▘ ▐▌       ▐▌  ▐▌▐▛▀▜▌▐▌ ▝▜▌▐▛▀▜▌▐▌▝▜▌▐▛▀▀▘▐▌  ▐▌▐▛▀▀▘▐▌ ▝▜▌  █",
                    "▐▌   ▝▚▄▄▖    ▐▌  ▐▌▐▌ ▐▌▐▌  ▐▌▐▌ ▐▌▝▚▄▞▘▐▙▄▄▖▐▌  ▐▌▐▙▄▄▖▐▌  ▐▌  █",
            };
            ConsoleUtils.printLogoCentered(logo, 147, 73);
            ConsoleUtils.printCenter(ConsoleUtils.GREEN + "PC Config Details" + ConsoleUtils.RESET,
                    ConsoleUtils.DEFAULT_WIDTH);
            System.out.println();

            List<PCConfig> configs = pcConfigDAO.findAll();
            String[] headers = { "Config ID", "Config Code", "CPU", "RAM", "GPU", "Price/Hour" };
            int[] columnWidths = { 10, 15, 20, 8, 15, 15 };
            ConsoleUtils.printTableHeader(headers, columnWidths, ConsoleUtils.DEFAULT_WIDTH);

            if (configs != null && !configs.isEmpty()) {
                for (PCConfig config : configs) {
                    String[] row = {
                            String.valueOf(config.getConfigId()),
                            config.getConfigCode(),
                            config.getCpu(),
                            String.valueOf(config.getRam()),
                            config.getGpu(),
                            String.format("%.0f", config.getPricePerHour())
                    };
                    ConsoleUtils.printTableRow(row, columnWidths, ConsoleUtils.DEFAULT_WIDTH);
                }
            } else {
                ConsoleUtils.printCenter(ConsoleUtils.YELLOW + "No PC configs found" + ConsoleUtils.RESET,
                        ConsoleUtils.DEFAULT_WIDTH);
            }

            ConsoleUtils.printTableFooter(columnWidths, ConsoleUtils.DEFAULT_WIDTH);
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