package presentation;

public class ConsoleUtils {
    public static final int DEFAULT_WIDTH = 140;
    public static final int BOX_WIDTH = 26;
    public static final int MENU_WIDTH = (BOX_WIDTH + 4) * 2 + 2;

    public static final String GREEN = "\033[92m";
    public static final String CYAN = "\033[96m";
    public static final String RED = "\033[91m";
    public static final String YELLOW = "\033[93m";
    public static final String ORANGE = "\033[38;5;214m";
    public static final String GRAY = "\033[90m";
    public static final String RESET = "\033[0m";

    public static void printCenter(String text, int width) {
        System.out.println(centerText(text, width));
    }

    public static String centerText(String text, int width) {
        int visibleLen = getVisibleLength(text);
        int padding = Math.max(0, (width - visibleLen) / 2);
        return " ".repeat(padding) + text;
    }

    public static int getVisibleLength(String text) {
        return text.replaceAll("\u001b\\[[;\\d]*m|\033\\[[;\\d]*m", "").length();
    }

    public static void clearScreen() {
        for (int i = 0; i < 30; i++) {
            System.out.println();
        }
    }

    public static void printLogoCentered(String[] logo, int consoleWidth, int logoWidth) {
        clearScreen();
        int padding = Math.max(0, (consoleWidth - logoWidth) / 2);
        String pad = " ".repeat(padding);

        for (String line : logo) {
            System.out.println(pad + CYAN + line + RESET);
        }
    }

    public static void printMenuRow(String option1, String option2, int consoleWidth) {
        String color1 = determineMenuColor(option1);
        String color2 = determineMenuColor(option2);
        printMenuRow(option1, option2, consoleWidth, color1, color2);
    }

    public static void printMenuRow(String option1, String option2, int consoleWidth, String color1, String color2) {
        int WIDTH = 26;

        int totalWidth = (WIDTH + 4) * 2 + 2;
        int padding = Math.max(0, (consoleWidth - totalWidth) / 2);
        String pad = " ".repeat(padding);

        String border = "═".repeat(WIDTH + 4);
        String top = "╔" + border + "╗";
        String bottom = "╚" + border + "╝";

        // Remove color codes from text for length calculation
        String cleanOption1 = option1.replaceAll("\u001b\\[[;\\d]*m|\033\\[[;\\d]*m", "");
        String cleanOption2 = option2.replaceAll("\u001b\\[[;\\d]*m|\033\\[[;\\d]*m", "");

        int len1 = cleanOption1.length();
        int leftPad1 = Math.max(0, (WIDTH - len1) / 2);
        int rightPad1 = Math.max(0, WIDTH - len1 - leftPad1);
        String opt1 = " ".repeat(leftPad1) + cleanOption1 + " ".repeat(rightPad1);

        int len2 = cleanOption2.length();
        int leftPad2 = Math.max(0, (WIDTH - len2) / 2);
        int rightPad2 = Math.max(0, WIDTH - len2 - leftPad2);
        String opt2 = " ".repeat(leftPad2) + cleanOption2 + " ".repeat(rightPad2);

        if (color1 == null) {
            color1 = determineMenuColor(option1);
        }
        if (color2 == null) {
            color2 = determineMenuColor(option2);
        }

        System.out.println(pad + color1 + top + RESET + "  " + color2 + top + RESET);
        System.out.println(pad + color1 + "║  " + opt1 + "  ║" + RESET + "  " + color2 + "║  " + opt2 + "  ║" + RESET);
        System.out.println(pad + color1 + bottom + RESET + "  " + color2 + bottom + RESET);
    }

    private static String determineColor(String text) {
        String cleanText = text.replaceAll("\u001b\\[[;\\d]*m|\033\\[[;\\d]*m", "").toUpperCase();

        // Check if text already has color codes embedded
        if (text.contains("\033[") || text.contains("\u001b[")) {
            if (text.contains(GREEN)) {
                return GREEN;
            } else if (text.contains(RED)) {
                return RED;
            } else if (text.contains(YELLOW)) {
                return YELLOW;
            } else if (text.contains(CYAN)) {
                return CYAN;
            } else if (text.contains(GRAY)) {
                return GRAY;
            }
        }

        if (cleanText.contains("LOGOUT") || cleanText.contains("EXIT")
                || cleanText.contains("BACK") || cleanText.contains("CANCEL")) {
            return RED;
        }

        return GREEN;
    }

    private static String determineMenuColor(String option) {
        return determineColor(option);
    }

    private static String determineWideBoxColor(String title) {
        return determineColor(title);
    }

    public static void printBox(String title, int boxWidth, int consoleWidth) {
        String border = "═".repeat(boxWidth);
        String topLine = "╔" + border + "╗";
        String bottomLine = "╚" + border + "╝";

        int titleLen = getVisibleLength(title);
        int leftPad = Math.max(0, (boxWidth - titleLen) / 2);
        int rightPad = Math.max(0, boxWidth - titleLen - leftPad);
        String middleLine = "║" + " ".repeat(leftPad) + title + " ".repeat(rightPad) + "║";

        String color = (title.toUpperCase().contains("EXIT")
                || title.toUpperCase().contains("LOGOUT")
                || title.toUpperCase().contains("BACK"))
                        ? RED
                        : CYAN;
        System.out.println(centerText(color + topLine + RESET, consoleWidth));
        System.out.println(centerText(color + middleLine + RESET, consoleWidth));
        System.out.println(centerText(color + bottomLine + RESET, consoleWidth));
    }

    public static void printWideMenuBox(String title, int consoleWidth) {
        int WIDTH = 60;

        String border = "═".repeat(WIDTH + 4);
        String top = "╔" + border + "╗";
        String bottom = "╚" + border + "╝";

        String cleanTitle = title.replaceAll("\u001b\\[[;\\d]*m|\033\\[[;\\d]*m", "");
        int len = getVisibleLength(title);
        int leftPad = Math.max(0, (WIDTH - len) / 2);
        int rightPad = Math.max(0, WIDTH - len - leftPad);
        String middle = "║  " + " ".repeat(leftPad) + title + " ".repeat(rightPad) + "  ║";

        String color = determineWideBoxColor(cleanTitle);

        int padding = Math.max(0, (consoleWidth - MENU_WIDTH) / 2);
        String pad = " ".repeat(padding);

        System.out.println(pad + color + top + RESET);
        System.out.println(pad + color + middle + RESET);
        System.out.println(pad + color + bottom + RESET);
    }

    public static void printAlignedBox(String title, int boxWidth, int consoleWidth) {
        String border = "═".repeat(boxWidth);
        String topLine = "╔" + border + "╗";
        String bottomLine = "╚" + border + "╝";

        int titleLen = getVisibleLength(title);
        int leftPad = Math.max(0, (boxWidth - titleLen) / 2);
        int rightPad = Math.max(0, boxWidth - titleLen - leftPad);
        String middleLine = "║" + " ".repeat(leftPad) + title + " ".repeat(rightPad) + "║";

        String color = (title.toUpperCase().contains("EXIT")
                || title.toUpperCase().contains("LOGOUT")
                || title.toUpperCase().contains("BACK"))
                        ? RED
                        : CYAN;

        int totalWidth = (BOX_WIDTH + 4) * 2 + 2;
        int padding = Math.max(0, (consoleWidth - totalWidth) / 2);
        String pad = " ".repeat(padding);

        System.out.println(pad + color + topLine + RESET);
        System.out.println(pad + color + middleLine + RESET);
        System.out.println(pad + color + bottomLine + RESET);
    }

    public static void printMenuOptions(String[] options, int consoleWidth) {
        printMenuOptions(options, consoleWidth, null);
    }

    public static void printMenuOptions(String[] options, int consoleWidth, String[] colors) {
        int count = options.length;
        int rows = (count + 1) / 2;
        for (int i = 0; i < rows; i++) {
            int idx1 = i * 2;
            int idx2 = idx1 + 1;

            if (idx2 < count) {
                String color1 = (colors != null && idx1 < colors.length) ? colors[idx1] : null;
                String color2 = (colors != null && idx2 < colors.length) ? colors[idx2] : null;
                printMenuRow(options[idx1], options[idx2], consoleWidth, color1, color2);
            } else {
                String color1 = (colors != null && idx1 < colors.length) ? colors[idx1] : null;
                printSingleMenuOption(options[idx1], consoleWidth, color1);
            }
        }
    }

    private static void printSingleMenuOption(String option, int consoleWidth, String color) {
        int WIDTH = 60;

        String border = "═".repeat(WIDTH + 4);
        String top = "╔" + border + "╗";
        String bottom = "╚" + border + "╝";

        String cleanOption = option.replaceAll("\u001b\\[[;\\d]*m|\033\\[[;\\d]*m", "");
        int len = getVisibleLength(option);
        int leftPad = Math.max(0, (WIDTH - len) / 2);
        int rightPad = Math.max(0, WIDTH - len - leftPad);
        String middle = "║  " + " ".repeat(leftPad) + option + " ".repeat(rightPad) + "  ║";

        if (color == null) {
            color = determineWideBoxColor(cleanOption);
        }

        int padding = Math.max(0, (consoleWidth - MENU_WIDTH) / 2);
        String pad = " ".repeat(padding);

        System.out.println(pad + color + top + RESET);
        System.out.println(pad + color + middle + RESET);
        System.out.println(pad + color + bottom + RESET);
    }

    public static int calculateTableWidth(int[] columnWidths) {
        int width = 1; // Leading border ║
        for (int i = 0; i < columnWidths.length; i++) {
            width += columnWidths[i] + 2; // Column content + 1 space on each side
            width += 1; // Border separator ║
        }
        return width;
    }

    public static String getTablePadding(int[] columnWidths, int consoleWidth) {
        int tableWidth = calculateTableWidth(columnWidths);
        int padding = Math.max(0, (consoleWidth - tableWidth) / 2);
        return " ".repeat(Math.max(0, padding));
    }

    public static void printTableHeader(String[] headers, int[] columnWidths, int consoleWidth) {
        StringBuilder top = new StringBuilder("╔");
        StringBuilder headerRow = new StringBuilder("║");

        for (int i = 0; i < headers.length; i++) {
            top.append("═".repeat(columnWidths[i] + 2));
            if (i < headers.length - 1) {
                top.append("╦");
            }
        }
        top.append("╗");

        for (int i = 0; i < headers.length; i++) {
            String h = headers[i];
            int width = columnWidths[i];
            int textLen = getVisibleLength(h);
            int padding = Math.max(0, (width - textLen) / 2);
            int rightPad = Math.max(0, width - textLen - padding);
            headerRow.append(" ").append(" ".repeat(padding)).append(CYAN).append(h).append(RESET)
                    .append(" ".repeat(rightPad)).append(" ");
            if (i < headers.length - 1) {
                headerRow.append("║");
            }
        }
        headerRow.append("║");

        String tablePadding = getTablePadding(columnWidths, consoleWidth);

        System.out.println(tablePadding + top);
        System.out.println(tablePadding + headerRow);

        StringBuilder separator = new StringBuilder("╠");
        for (int i = 0; i < headers.length; i++) {
            separator.append("═".repeat(columnWidths[i] + 2));
            if (i < headers.length - 1) {
                separator.append("╬");
            }
        }
        separator.append("╣");
        System.out.println(tablePadding + separator);
    }

    public static void printTableRow(String[] data, int[] columnWidths, int consoleWidth) {
        int maxLines = 1;
        String[][] wrappedData = new String[data.length][];

        for (int i = 0; i < data.length; i++) {
            wrappedData[i] = wrapText(data[i], columnWidths[i]);
            maxLines = Math.max(maxLines, wrappedData[i].length);
        }

        String tablePadding = getTablePadding(columnWidths, consoleWidth);
        for (int line = 0; line < maxLines; line++) {
            StringBuilder row = new StringBuilder("|");
            for (int i = 0; i < columnWidths.length; i++) {
                String cellText = (line < wrappedData[i].length) ? wrappedData[i][line] : "";
                int width = columnWidths[i];
                int textLen = getVisibleLength(cellText);
                int padding = Math.max(0, (width - textLen) / 2);
                int rightPad = Math.max(0, width - textLen - padding);
                row.append(" ").append(" ".repeat(padding)).append(GREEN).append(cellText).append(RESET)
                        .append(" ".repeat(rightPad)).append(" ");
                if (i < columnWidths.length - 1) {
                    row.append("|");
                }
            }
            row.append("|");
            System.out.println(tablePadding + row);
        }
    }

    private static String[] wrapText(String text, int width) {
        if (text == null || text.isEmpty()) {
            return new String[] { "" };
        }
        String cleanText = text.replaceAll("\u001b\\[[;\\d]*m|\033\\[[;\\d]*m", "");

        if (cleanText.length() <= width) {
            return new String[] { text };
        }

        java.util.List<String> lines = new java.util.ArrayList<>();
        StringBuilder currentLine = new StringBuilder();
        int currentLen = 0;

        for (char c : cleanText.toCharArray()) {
            if (currentLen >= width) {
                lines.add(currentLine.toString());
                currentLine = new StringBuilder();
                currentLen = 0;
            }
            currentLine.append(c);
            currentLen++;
        }

        if (currentLine.length() > 0) {
            lines.add(currentLine.toString());
        }

        return lines.toArray(new String[0]);
    }

    public static void printTableRowSeparator(int[] columnWidths, int consoleWidth) {
        StringBuilder separator = new StringBuilder("├");
        for (int i = 0; i < columnWidths.length; i++) {
            separator.append("─".repeat(columnWidths[i] + 2));
            if (i < columnWidths.length - 1) {
                separator.append("┼");
            }
        }
        separator.append("┤");

        String tablePadding = getTablePadding(columnWidths, consoleWidth);
        System.out.println(tablePadding + separator);
    }

    public static void printTableFooter(int[] columnWidths, int consoleWidth) {
        StringBuilder bottom = new StringBuilder("╚");
        for (int i = 0; i < columnWidths.length; i++) {
            bottom.append("═".repeat(columnWidths[i] + 2));
            if (i < columnWidths.length - 1) {
                bottom.append("╩");
            }
        }
        bottom.append("╝");

        String tablePadding = getTablePadding(columnWidths, consoleWidth);
        System.out.println(tablePadding + bottom);
    }

    /**
     * Format price in Vietnamese Dong (VND)
     * 
     * @param amount price amount
     * @return formatted price string with ₫ symbol
     */
    public static String formatVND(double amount) {
        return String.format("₫%.0f", amount);
    }

    /**
     * Format price in Vietnamese Dong (VND) - overload for Number types
     * 
     * @param amount price amount (Double, BigDecimal, etc.)
     * @return formatted price string with ₫ symbol
     */
    public static String formatVND(Number amount) {
        if (amount == null) {
            return "N/A";
        }
        return String.format("₫%.0f", amount.doubleValue());
    }

    /**
     * Format price range in Vietnamese Dong (VND)
     * 
     * @param minPrice minimum price
     * @param maxPrice maximum price
     * @return formatted price range string
     */
    public static String formatPriceRange(double minPrice, double maxPrice) {
        return String.format("₫%.0f-₫%.0f", minPrice, maxPrice);
    }

}
