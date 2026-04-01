package presentation.handlers;

import dao.interfaces.IComboDAO;
import dao.interfaces.IFoodItemDAO;
import model.Combo;
import model.FoodItem;
import presentation.ConsoleUtils;
import utils.InputValidator;
import utils.CodeGenerator;
import enums.ItemStatus;
import enums.ComboStatus;

import java.util.*;
import java.math.BigDecimal;

import static presentation.ConsoleUtils.centerText;

public class FoodManagementHandler {
        private Scanner sc;
        private IFoodItemDAO foodItemDAO;
        private IComboDAO comboDAO;

        public FoodManagementHandler(Scanner sc, IFoodItemDAO foodItemDAO, IComboDAO comboDAO) {
                this.sc = sc;
                this.foodItemDAO = foodItemDAO;
                this.comboDAO = comboDAO;
        }

        public void handleFoodManagement() {
                while (true) {
                        ConsoleUtils.clearScreen();
                        ConsoleUtils.printCenter(
                                        ConsoleUtils.CYAN + "FOOD MANAGEMENT" + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                        System.out.println();

                        String[] options = {
                                        "1. View menu",
                                        "2. Add food",
                                        "3. Update food item",
                                        "4. Delete food item",
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

                        System.out.print(
                                        centerText(
                                                        ConsoleUtils.YELLOW + "Choose an option (1-5): "
                                                                        + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH));

                        try {
                                int choice = Integer.parseInt(sc.nextLine());
                                switch (choice) {
                                        case 1:
                                                viewMenu();
                                                break;
                                        case 2:
                                                addFoodItem();
                                                break;
                                        case 3:
                                                updateFood();
                                                break;
                                        case 4:
                                                deleteFoodItem();
                                                break;
                                        case 5:
                                                return;
                                        default:
                                                ConsoleUtils.printCenter(
                                                                ConsoleUtils.RED + "Invalid choice!"
                                                                                + ConsoleUtils.RESET,
                                                                ConsoleUtils.DEFAULT_WIDTH);
                                                System.out.println(centerText(
                                                                ConsoleUtils.YELLOW + "Press Enter to continue..."
                                                                                + ConsoleUtils.RESET,
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

        private void viewMenu() {
                try {
                        boolean showDeleted = false;
                        while (true) {
                                ConsoleUtils.clearScreen();
                                System.out.println();
                                ConsoleUtils.printCenter(
                                                ConsoleUtils.CYAN + "MENU" + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH);
                                String[] filterOptions = {
                                                "1. Show available items",
                                                "2. Show all items",
                                                "3. Back"
                                };
                                String[] filterColors = { ConsoleUtils.GREEN, ConsoleUtils.YELLOW, ConsoleUtils.RED };

                                ConsoleUtils.printCenter(ConsoleUtils.CYAN + "Choose view option:" + ConsoleUtils.RESET,
                                                136);
                                System.out.println();
                                ConsoleUtils.printMenuOptions(filterOptions, 135, filterColors);
                                System.out.println();
                                System.out.print(centerText(
                                                ConsoleUtils.YELLOW + "Select (1, 2, or 3): " + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH));

                                try {
                                        int filterChoice = Integer.parseInt(sc.nextLine());
                                        if (filterChoice == 1) {
                                                showDeleted = false;
                                                break;
                                        } else if (filterChoice == 2) {
                                                showDeleted = true;
                                                break;
                                        } else if (filterChoice == 3) {
                                                return;
                                        } else {
                                                ConsoleUtils.printCenter(
                                                                ConsoleUtils.RED + "Invalid choice! Please enter 1, 2, or 3"
                                                                                + ConsoleUtils.RESET,
                                                                ConsoleUtils.DEFAULT_WIDTH);
                                                System.out.print(centerText(
                                                                ConsoleUtils.YELLOW + "Press Enter to continue..."
                                                                                + ConsoleUtils.RESET,
                                                                ConsoleUtils.DEFAULT_WIDTH));
                                                sc.nextLine();
                                        }
                                } catch (NumberFormatException e) {
                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.RED + "Invalid input! Please enter a number"
                                                                        + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                        System.out.print(centerText(
                                                        ConsoleUtils.YELLOW + "Press Enter to continue..."
                                                                        + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH));
                                        sc.nextLine();
                                }
                        }

                        displayMenuView(showDeleted);

                } catch (Exception e) {
                        ConsoleUtils.printCenter(ConsoleUtils.RED + "Error: " + e.getMessage() + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                }
        }

        private void displayMenuView(boolean showDeleted) {
                try {
                        ConsoleUtils.clearScreen();
                        System.out.println();
                        ConsoleUtils.printCenter(ConsoleUtils.CYAN + "FOOD MENU" + ConsoleUtils.RESET,
                                        180);

                        List<FoodItem> items = foodItemDAO.findAllWithCategoryAndPrices(showDeleted);
                        displayFoodItemsSection(items);

                        System.out.println();
                        System.out.println();

                        List<Combo> combos = comboDAO.findAll(showDeleted);
                        displayCombosSection(combos);

                        System.out.println();
                        System.out.print(centerText(
                                        ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                                        180));
                        sc.nextLine();

                } catch (Exception e) {
                        ConsoleUtils.printCenter(ConsoleUtils.RED + "Error: " + e.getMessage() + ConsoleUtils.RESET,
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
                                ConsoleUtils.GREEN + "FOOD ITEMS"
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
                                ConsoleUtils.ORANGE + "SPECIAL COMBOS"
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

        private void addFoodItem() {
                try {
                        while (true) {
                                ConsoleUtils.clearScreen();
                                System.out.println();
                                ConsoleUtils.printCenter(ConsoleUtils.GREEN + "ADD NEW FOOD" + ConsoleUtils.RESET,
                                                136);
                                System.out.println();

                                String[] addOptions = {
                                                "1. Add food item",
                                                "2. Add combo",
                                                "3. Back"
                                };
                                String[] addColors = { ConsoleUtils.GREEN, ConsoleUtils.GREEN, ConsoleUtils.RED };

                                ConsoleUtils.printCenter(ConsoleUtils.CYAN + "Choose option:" + ConsoleUtils.RESET,
                                                136);
                                System.out.println();
                                ConsoleUtils.printMenuOptions(addOptions, 135, addColors);
                                System.out.println();
                                System.out.print(centerText(
                                                ConsoleUtils.YELLOW + "Select (1, 2, or 3): " + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH));

                                try {
                                        int choice = Integer.parseInt(sc.nextLine());
                                        switch (choice) {
                                                case 1:
                                                        addNewFoodItem();
                                                        break;
                                                case 2:
                                                        addNewCombo();
                                                        break;
                                                case 3:
                                                        return;
                                                default:
                                                        ConsoleUtils.printCenter(
                                                                        ConsoleUtils.RED + "Invalid choice! Please enter 1, 2, or 3"
                                                                                        + ConsoleUtils.RESET,
                                                                        ConsoleUtils.DEFAULT_WIDTH);
                                                        System.out.print(centerText(
                                                                        ConsoleUtils.YELLOW
                                                                                        + "Press Enter to continue..."
                                                                                        + ConsoleUtils.RESET,
                                                                        ConsoleUtils.DEFAULT_WIDTH));
                                                        sc.nextLine();
                                        }
                                } catch (NumberFormatException e) {
                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.RED + "Invalid input! Please enter a number"
                                                                        + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                        System.out.print(centerText(
                                                        ConsoleUtils.YELLOW + "Press Enter to continue..."
                                                                        + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH));
                                        sc.nextLine();
                                }
                        }
                } catch (Exception e) {
                        ConsoleUtils.printCenter(ConsoleUtils.RED + "Error: " + e.getMessage() + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                }
        }

        private void addNewFoodItem() {
                try {
                        ConsoleUtils.clearScreen();
                        ConsoleUtils.printCenter(
                                        ConsoleUtils.GREEN + "ADD FOOD ITEM"
                                                        + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                        System.out.println();

                        String foodName = "";
                        while (foodName.isEmpty()) {
                                System.out.print(centerText(ConsoleUtils.CYAN + "Food Item Name: " + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH));
                                foodName = sc.nextLine().trim();
                                if (foodName.isEmpty()) {
                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.RED + "Food name cannot be empty!"
                                                                        + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                } else if (foodName.length() < 2) {
                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.RED + "Food name must be at least 2 characters!"
                                                                        + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                        foodName = "";
                                } else if (foodName.length() > 100) {
                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.RED + "Food name cannot exceed 100 characters!"
                                                                        + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                        foodName = "";
                                }
                        }

                        System.out.print(centerText(ConsoleUtils.CYAN + "Description: " + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH));
                        String description = sc.nextLine().trim();

                        double minPrice = -1;
                        while (minPrice <= 1000) {
                                System.out.print(centerText(ConsoleUtils.CYAN + "Min Price (₫): " + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH));
                                String minPriceInput = sc.nextLine().trim();
                                if (minPriceInput.isEmpty()) {
                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.RED + "Price cannot be empty!"
                                                                        + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                } else {
                                        try {
                                                minPrice = Double.parseDouble(minPriceInput);
                                                if (minPrice <= 1000) {
                                                        ConsoleUtils.printCenter(
                                                                        ConsoleUtils.RED + "Price must be greater than 1000!"
                                                                                        + ConsoleUtils.RESET,
                                                                        ConsoleUtils.DEFAULT_WIDTH);
                                                        minPrice = -1;
                                                }
                                        } catch (NumberFormatException e) {
                                                ConsoleUtils.printCenter(
                                                                ConsoleUtils.RED + "Invalid price format! Please enter a number."
                                                                                + ConsoleUtils.RESET,
                                                                ConsoleUtils.DEFAULT_WIDTH);
                                        }
                                }
                        }

                        double maxPrice = -1;
                        while (maxPrice <= 1000 || maxPrice < minPrice) {
                                System.out.print(centerText(ConsoleUtils.CYAN + "Max Price (₫): " + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH));
                                String maxPriceInput = sc.nextLine().trim();
                                if (maxPriceInput.isEmpty()) {
                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.RED + "Price cannot be empty!"
                                                                        + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                } else {
                                        try {
                                                maxPrice = Double.parseDouble(maxPriceInput);
                                                if (maxPrice <= 1000) {
                                                        ConsoleUtils.printCenter(
                                                                        ConsoleUtils.RED + "Price must be greater than 1000!"
                                                                                        + ConsoleUtils.RESET,
                                                                        ConsoleUtils.DEFAULT_WIDTH);
                                                        maxPrice = -1;
                                                } else if (maxPrice < minPrice) {
                                                        ConsoleUtils.printCenter(
                                                                        ConsoleUtils.RED + "Max price cannot be less than min price (₫"
                                                                                        + String.format("%.0f",
                                                                                                        minPrice)
                                                                                        + ")!" + ConsoleUtils.RESET,
                                                                        ConsoleUtils.DEFAULT_WIDTH);
                                                        maxPrice = -1;
                                                }
                                        } catch (NumberFormatException e) {
                                                ConsoleUtils.printCenter(
                                                                ConsoleUtils.RED + "Invalid price format! Please enter a number."
                                                                                + ConsoleUtils.RESET,
                                                                ConsoleUtils.DEFAULT_WIDTH);
                                        }
                                }
                        }

                        System.out.println();
                        ConsoleUtils.printCenter(ConsoleUtils.CYAN + "Select Category:" + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                        System.out.println(
                                        centerText("1 = Pizza | 2 = Burger | 3 = Drink | 4 = Dessert",
                                                        ConsoleUtils.DEFAULT_WIDTH));
                        int categoryId = 0;
                        while (categoryId < 1 || categoryId > 4) {
                                System.out.print(centerText(
                                                ConsoleUtils.YELLOW + "Category ID (1-4): " + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH));
                                String categoryInput = sc.nextLine().trim();
                                if (categoryInput.isEmpty()) {
                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.RED + "Category ID cannot be empty!"
                                                                        + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                } else {
                                        try {
                                                categoryId = Integer.parseInt(categoryInput);
                                                if (categoryId < 1 || categoryId > 4) {
                                                        ConsoleUtils.printCenter(
                                                                        ConsoleUtils.RED + "Category ID must be between 1 and 4!"
                                                                                        + ConsoleUtils.RESET,
                                                                        ConsoleUtils.DEFAULT_WIDTH);
                                                }
                                        } catch (NumberFormatException e) {
                                                ConsoleUtils.printCenter(
                                                                ConsoleUtils.RED + "Invalid input! Please enter a number (1-4)."
                                                                                + ConsoleUtils.RESET,
                                                                ConsoleUtils.DEFAULT_WIDTH);
                                        }
                                }
                        }

                        System.out.println();
                        ConsoleUtils.printCenter(ConsoleUtils.CYAN + "ADD AVAILABLE SIZES" + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                        ConsoleUtils.printCenter(
                                        ConsoleUtils.YELLOW
                                                        + "(Hợp lệ: s, m, l, xl, 2xl, 3xl | hoặc Small, Medium, Large...)"
                                                        + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                        ConsoleUtils.printCenter(
                                        ConsoleUtils.YELLOW + "(Nhập từng size một dòng, nhấn 0 để thoát)"
                                                        + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                        System.out.println();

                        java.util.List<String> validSizesList = new java.util.ArrayList<>();
                        while (true) {
                                System.out.print(centerText(
                                                ConsoleUtils.CYAN + "Size (or 0 to finish): " + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH));
                                String sizeInput = sc.nextLine().trim();

                                if (sizeInput.equals("0")) {
                                        break;
                                }

                                if (sizeInput.isEmpty()) {
                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.RED + "Size không được để trống!"
                                                                        + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                        continue;
                                }

                                String convertedSize = convertAndValidateSize(sizeInput);
                                if (convertedSize == null) {
                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.RED + " Size không hợp lệ! Chỉ nhận: s, m, l, xl, 2xl, 3xl..."
                                                                        + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                        continue;
                                }

                                if (!validSizesList.contains(convertedSize)) {
                                        validSizesList.add(convertedSize);
                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.GREEN + "✓ Đã thêm: " + convertedSize
                                                                        + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                } else {
                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.YELLOW + "⚠ Size này đã được thêm rồi!"
                                                                        + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                }
                        }

                        String sizes = validSizesList.isEmpty() ? "Standard" : String.join(", ", validSizesList);

                        System.out.println();
                        ConsoleUtils.printCenter(
                                        ConsoleUtils.CYAN + "ADD AVAILABLE TOPPINGS" + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                        ConsoleUtils.printCenter(
                                        ConsoleUtils.YELLOW + "(Optional - Nhập từng topping một dòng)"
                                                        + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                        ConsoleUtils.printCenter(ConsoleUtils.YELLOW + "(Nhấn 0 để thoát)" + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                        System.out.println();

                        java.util.List<String> validToppingsList = new java.util.ArrayList<>();
                        while (true) {
                                System.out.print(centerText(
                                                ConsoleUtils.CYAN + "Topping (or 0 to finish): " + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH));
                                String toppingInput = sc.nextLine().trim();

                                if (toppingInput.equals("0")) {
                                        break;
                                }

                                if (toppingInput.isEmpty()) {
                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.RED + "Topping không được để trống!"
                                                                        + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                        continue;
                                }

                                String capitalizedTopping = capitalizeWords(toppingInput);
                                if (!validToppingsList.contains(capitalizedTopping)) {
                                        validToppingsList.add(capitalizedTopping);
                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.GREEN + "✓ Đã thêm: " + capitalizedTopping
                                                                        + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                } else {
                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.YELLOW + "⚠ Topping này đã được thêm rồi!"
                                                                        + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                }
                        }

                        String toppings = String.join(", ", validToppingsList);

                        FoodItem foodItem = new FoodItem();
                        foodItem.setItemCode(CodeGenerator.generateFoodCode());
                        foodItem.setName(foodName);
                        foodItem.setDescription(description);
                        foodItem.setMinPrice(minPrice);
                        foodItem.setMaxPrice(maxPrice);
                        foodItem.setCategoryId(categoryId);
                        foodItem.setAvailableSizes(sizes);
                        foodItem.setAvailableToppings(toppings);
                        foodItem.setStatus(ItemStatus.ACTIVE);
                        foodItem.setDeleted(false);

                        try {
                                foodItemDAO.create(foodItem);
                                System.out.println();
                                ConsoleUtils.printCenter(
                                                ConsoleUtils.GREEN + "Food item added successfully!"
                                                                + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH);
                                System.out.println(centerText("  Code: " + foodItem.getItemCode(),
                                                ConsoleUtils.DEFAULT_WIDTH));
                                System.out.println(centerText("  Name: " + foodName, ConsoleUtils.DEFAULT_WIDTH));
                                System.out.println(centerText("  Description: " + description,
                                                ConsoleUtils.DEFAULT_WIDTH));
                                System.out.println(centerText(
                                                "  Price Range: " + ConsoleUtils.formatPriceRange(minPrice, maxPrice),
                                                ConsoleUtils.DEFAULT_WIDTH));
                                System.out
                                                .println(centerText("  Category: " + getCategoryName(categoryId),
                                                                ConsoleUtils.DEFAULT_WIDTH));
                                System.out.println(centerText("  Sizes: " + sizes, ConsoleUtils.DEFAULT_WIDTH));
                                if (!toppings.isEmpty()) {
                                        System.out.println(centerText("  Toppings: " + toppings,
                                                        ConsoleUtils.DEFAULT_WIDTH));
                                }
                        } catch (Exception saveError) {
                                ConsoleUtils.printCenter(
                                                ConsoleUtils.RED + "Failed to save food item: " + saveError.getMessage()
                                                                + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH);
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

        private String getCategoryName(int categoryId) {
                switch (categoryId) {
                        case 1:
                                return "Pizza";
                        case 2:
                                return "Burger";
                        case 3:
                                return "Drink";
                        case 4:
                                return "Dessert";
                        default:
                                return "Unknown";
                }
        }

        private String convertAndValidateSize(String input) {
                if (input == null || input.isEmpty()) {
                        return null;
                }

                String trimmed = input.trim().toLowerCase();
                switch (trimmed) {
                        case "s":
                        case "small":
                                return "Small";
                        case "m":
                        case "medium":
                                return "Medium";
                        case "l":
                        case "large":
                                return "Large";
                        case "xl":
                        case "extra large":
                                return "Extra Large";
                        case "xxl":
                        case "2xl":
                                return "2XL";
                        case "3xl":
                                return "3XL";
                        default:
                                return null;
                }
        }

        private String capitalizeWords(String input) {
                if (input == null || input.isEmpty()) {
                        return "";
                }

                String[] words = input.trim().split("\\s+");
                StringBuilder capitalized = new StringBuilder();
                for (String word : words) {
                        if (!word.isEmpty()) {
                                capitalized.append(word.substring(0, 1).toUpperCase())
                                                .append(word.substring(1).toLowerCase()).append(" ");
                        }
                }
                return capitalized.toString().trim();
        }

        private void addNewCombo() {
                try {
                        ConsoleUtils.clearScreen();
                        System.out.println();
                        ConsoleUtils.printCenter(
                                        ConsoleUtils.GREEN + "━━━━━━━━━━━ ＡＤＤ  ＣＯＭＢＯ ━━━━━━━━━━━" + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                        System.out.println();

                        String comboName = "";
                        while (comboName.isEmpty()) {
                                System.out.print(centerText(ConsoleUtils.CYAN + "Combo Name: " + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH));
                                comboName = sc.nextLine().trim();
                                if (comboName.isEmpty()) {
                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.RED + "Combo name cannot be empty!"
                                                                        + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                } else if (comboName.length() < 2) {
                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.RED + "Combo name must be at least 2 characters!"
                                                                        + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                        comboName = "";
                                } else if (comboName.length() > 100) {
                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.RED + "Combo name cannot exceed 100 characters!"
                                                                        + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                        comboName = "";
                                }
                        }

                        System.out.print(centerText(ConsoleUtils.CYAN + "Combo Price (₫): " + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH));
                        double price = 0;
                        boolean validPrice = false;
                        while (!validPrice) {
                                String priceInput = sc.nextLine().trim();
                                if (priceInput.isEmpty()) {
                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.RED + "Price cannot be empty!"
                                                                        + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                        System.out.print(centerText(
                                                        ConsoleUtils.CYAN + "Combo Price (₫): " + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH));
                                } else {
                                        try {
                                                price = Double.parseDouble(priceInput);
                                                if (price < 0) {
                                                        ConsoleUtils.printCenter(
                                                                        ConsoleUtils.RED + "Price cannot be negative! Please try again."
                                                                                        + ConsoleUtils.RESET,
                                                                        ConsoleUtils.DEFAULT_WIDTH);
                                                        System.out.print(centerText(
                                                                        ConsoleUtils.CYAN + "Combo Price (₫): "
                                                                                        + ConsoleUtils.RESET,
                                                                        ConsoleUtils.DEFAULT_WIDTH));
                                                } else if (price < 5000) {
                                                        ConsoleUtils.printCenter(
                                                                        ConsoleUtils.RED + "Price should be at least 5000! Please try again."
                                                                                        + ConsoleUtils.RESET,
                                                                        ConsoleUtils.DEFAULT_WIDTH);
                                                        System.out.print(centerText(
                                                                        ConsoleUtils.CYAN + "Combo Price (₫): "
                                                                                        + ConsoleUtils.RESET,
                                                                        ConsoleUtils.DEFAULT_WIDTH));
                                                } else {
                                                        validPrice = true;
                                                }
                                        } catch (NumberFormatException e) {
                                                ConsoleUtils.printCenter(
                                                                ConsoleUtils.RED + "Invalid price format! Please enter a valid number."
                                                                                + ConsoleUtils.RESET,
                                                                ConsoleUtils.DEFAULT_WIDTH);
                                                System.out.print(centerText(
                                                                ConsoleUtils.CYAN + "Combo Price (₫): "
                                                                                + ConsoleUtils.RESET,
                                                                ConsoleUtils.DEFAULT_WIDTH));
                                        }
                                }
                        }

                        System.out.println();
                        ConsoleUtils.printCenter(ConsoleUtils.CYAN + "ADD COMBO ITEMS" + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                        ConsoleUtils.printCenter(ConsoleUtils.YELLOW
                                        + "(Nhập từng item một dòng, format: ItemName x quantity)" + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                        ConsoleUtils.printCenter(ConsoleUtils.YELLOW + "(Nhấn 0 để thoát nhập)" + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                        System.out.println();

                        java.util.Map<String, Integer> parsedItems = new java.util.HashMap<>();
                        StringBuilder itemsBuilder = new StringBuilder();

                        while (true) {
                                System.out.print(centerText(
                                                ConsoleUtils.CYAN + "Item (or 0 to finish): " + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH));
                                String itemInput = sc.nextLine().trim();

                                if (itemInput.equals("0")) {
                                        break;
                                }

                                if (itemInput.isEmpty()) {
                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.RED + "Item không được để trống!"
                                                                        + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                        continue;
                                }

                                String[] parts = itemInput.split("x");
                                if (parts.length != 2) {
                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.RED + "✗ Format sai! Dùng: 'ItemName x quantity' (ví dụ: Pizza x1)"
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
                                                                ConsoleUtils.RED + "Số lượng phải > 0!"
                                                                                + ConsoleUtils.RESET,
                                                                ConsoleUtils.DEFAULT_WIDTH);
                                                continue;
                                        }
                                } catch (NumberFormatException e) {
                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.RED + "Số lượng phải là số!" + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                        continue;
                                }

                                List<FoodItem> allItems = foodItemDAO.findAll();
                                FoodItem foundItem = null;
                                for (FoodItem fi : allItems) {
                                        if (fi.getName().equalsIgnoreCase(itemName)) {
                                                foundItem = fi;
                                                break;
                                        }
                                }

                                if (foundItem == null) {
                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.RED + "Không tìm thấy item: '" + itemName + "'"
                                                                        + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.YELLOW + "Danh sách item có sẵn:"
                                                                        + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                        for (FoodItem fi : allItems) {
                                                ConsoleUtils.printCenter("  • " + fi.getName(),
                                                                ConsoleUtils.DEFAULT_WIDTH);
                                        }
                                        continue;
                                }

                                parsedItems.put(itemName, quantity);
                                if (itemsBuilder.length() > 0) {
                                        itemsBuilder.append(", ");
                                }
                                itemsBuilder.append(itemName).append(" x").append(quantity);

                                ConsoleUtils.printCenter(
                                                ConsoleUtils.GREEN + "✓ Đã thêm: " + itemName + " x" + quantity
                                                                + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH);
                        }

                        if (parsedItems.isEmpty()) {
                                ConsoleUtils.printCenter(
                                                ConsoleUtils.RED + "Combo must have at least 1 item!"
                                                                + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH);
                                System.out.print(centerText(
                                                ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH));
                                sc.nextLine();
                                return;
                        }

                        String itemsInput = itemsBuilder.toString();

                        try {
                                Combo combo = new Combo();
                                combo.setComboCode(CodeGenerator.generateComboCode());
                                combo.setName(comboName);
                                combo.setPrice(new BigDecimal(price));
                                combo.setStatus(ComboStatus.ACTIVE);
                                combo.setDeleted(false);
                                combo.setComboItems(itemsInput);

                                comboDAO.create(combo);

                                ConsoleUtils.printCenter(
                                                ConsoleUtils.GREEN + "Combo added successfully!" + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH);
                                System.out.println(centerText("  Code: " + combo.getComboCode(),
                                                ConsoleUtils.DEFAULT_WIDTH));
                                System.out.println(centerText("  Name: " + comboName, ConsoleUtils.DEFAULT_WIDTH));
                                System.out.println(centerText("  Price: " + ConsoleUtils.formatVND(price),
                                                ConsoleUtils.DEFAULT_WIDTH));
                                System.out.println(centerText("  Items:", ConsoleUtils.DEFAULT_WIDTH));
                                for (Map.Entry<String, Integer> entry : parsedItems.entrySet()) {
                                        System.out.println(
                                                        centerText("    • " + entry.getKey() + " x" + entry.getValue(),
                                                                        ConsoleUtils.DEFAULT_WIDTH));
                                }
                        } catch (Exception saveError) {
                                ConsoleUtils.printCenter(
                                                ConsoleUtils.RED + "Failed to save combo: " + saveError.getMessage()
                                                                + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH);
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

        private void updateFood() {
                try {
                        while (true) {
                                ConsoleUtils.clearScreen();
                                System.out.println();

                                String[] logo = {
                                                "▗▄▄▄▖ ▗▄▖  ▗▄▖ ▗▄▄▄      ▗▖  ▗▖ ▗▄▖ ▗▖  ▗▖ ▗▄▖  ▗▄▄▖▗▄▄▄▖▗▖  ▗▖▗▄▄▄▖▗▖  ▗▖▗▄▄▄▖",
                                                "▐▌   ▐▌ ▐▌▐▌ ▐▌▐▌  █     ▐▛▚▞▜▌▐▌ ▐▌▐▛▚▖▐▌▐▌ ▐▌▐▌   ▐▌   ▐▛▚▞▜▌▐▌   ▐▛▚▖▐▌  █",
                                                "▐▛▀▀▘▐▌ ▐▌▐▌ ▐▌▐▌  █     ▐▌  ▐▌▐▛▀▜▌▐▌ ▝▜▌▐▛▀▜▌▐▌▝▜▌▐▛▀▀▘▐▌  ▐▌▐▛▀▀▘▐▌ ▝▜▌  █",
                                                "▐▌   ▝▚▄▞▘▝▚▄▞▘▐▙▄▄▀     ▐▌  ▐▌▐▌ ▐▌▐▌  ▐▌▐▌ ▐▌▝▚▄▞▘▐▙▄▄▖▐▌  ▐▌▐▙▄▄▖▐▌  ▐▌  █",
                                                ""
                                };

                                ConsoleUtils.printLogoCentered(logo, ConsoleUtils.DEFAULT_WIDTH, 80);
                                ConsoleUtils.printCenter(
                                                ConsoleUtils.CYAN + "━━━━━━━━━━━ ＵＰＤＡＴＥ  ＦＯＯＤ ━━━━━━━━━━━"
                                                                + ConsoleUtils.RESET,
                                                136);
                                System.out.println();

                                String[] updateOptions = {
                                                "1. Update food item",
                                                "2. Update combo",
                                                "3. Back"
                                };
                                String[] updateColors = { ConsoleUtils.GREEN, ConsoleUtils.GREEN, ConsoleUtils.RED };

                                ConsoleUtils.printCenter(ConsoleUtils.CYAN + "Choose option:" + ConsoleUtils.RESET,
                                                136);
                                System.out.println();
                                ConsoleUtils.printMenuOptions(updateOptions, 135, updateColors);
                                System.out.println();
                                System.out.print(centerText(
                                                ConsoleUtils.YELLOW + "Select (1, 2, or 3): " + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH));

                                try {
                                        int choice = Integer.parseInt(sc.nextLine());
                                        switch (choice) {
                                                case 1:
                                                        updateExistingFoodItem();
                                                        break;
                                                case 2:
                                                        updateExistingCombo();
                                                        break;
                                                case 3:
                                                        return;
                                                default:
                                                        ConsoleUtils.printCenter(
                                                                        ConsoleUtils.RED + "Invalid choice! Please enter 1, 2, or 3"
                                                                                        + ConsoleUtils.RESET,
                                                                        ConsoleUtils.DEFAULT_WIDTH);
                                                        System.out.print(centerText(
                                                                        ConsoleUtils.YELLOW
                                                                                        + "Press Enter to continue..."
                                                                                        + ConsoleUtils.RESET,
                                                                        ConsoleUtils.DEFAULT_WIDTH));
                                                        sc.nextLine();
                                        }
                                } catch (NumberFormatException e) {
                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.RED + "Invalid input! Please enter a number"
                                                                        + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                        System.out.print(centerText(
                                                        ConsoleUtils.YELLOW + "Press Enter to continue..."
                                                                        + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH));
                                        sc.nextLine();
                                }
                        }
                } catch (Exception e) {
                        ConsoleUtils.printCenter(ConsoleUtils.RED + "Error: " + e.getMessage() + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                }
        }

        private void updateExistingFoodItem() {
                try {
                        ConsoleUtils.clearScreen();
                        String[] logo = {
                                        "▗▄▄▄▖ ▗▄▖  ▗▄▖ ▗▄▄▄      ▗▖  ▗▖ ▗▄▖ ▗▖  ▗▖ ▗▄▖  ▗▄▄▖▗▄▄▄▖▗▖  ▗▖▗▄▄▄▖▗▖  ▗▖▗▄▄▄▖",
                                        "▐▌   ▐▌ ▐▌▐▌ ▐▌▐▌  █     ▐▛▚▞▜▌▐▌ ▐▌▐▛▚▖▐▌▐▌ ▐▌▐▌   ▐▌   ▐▛▚▞▜▌▐▌   ▐▛▚▖▐▌  █",
                                        "▐▛▀▀▘▐▌ ▐▌▐▌ ▐▌▐▌  █     ▐▌  ▐▌▐▛▀▜▌▐▌ ▝▜▌▐▛▀▜▌▐▌▝▜▌▐▛▀▀▘▐▌  ▐▌▐▛▀▀▘▐▌ ▝▜▌  █",
                                        "▐▌   ▝▚▄▞▘▝▚▄▞▘▐▙▄▄▀     ▐▌  ▐▌▐▌ ▐▌▐▌  ▐▌▐▌ ▐▌▝▚▄▞▘▐▙▄▄▖▐▌  ▐▌▐▙▄▄▖▐▌  ▐▌  █",
                                        ""
                        };
                        ConsoleUtils.printCenter(
                                        ConsoleUtils.CYAN + "━━━━━━━━━━━ ＵＰＤＡＴＥ  ＦＯＯＤ  ＩＴＥＭ ━━━━━━━━━━━"
                                                        + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                        ConsoleUtils.printLogoCentered(logo, ConsoleUtils.DEFAULT_WIDTH, 80);
                        System.out.println();
                        System.out.print(centerText(ConsoleUtils.CYAN + "Enter food item ID to update (or 0 to back): "
                                        + ConsoleUtils.RESET, ConsoleUtils.DEFAULT_WIDTH));

                        FoodItem selectedItem = null;
                        while (selectedItem == null) {
                                try {
                                        int itemId = Integer.parseInt(sc.nextLine());
                                        if (itemId == 0) {
                                                return;
                                        }
                                        if (itemId <= 0) {
                                                ConsoleUtils.printCenter(
                                                                ConsoleUtils.RED + "✗ ID must be greater than 0!"
                                                                                + ConsoleUtils.RESET,
                                                                ConsoleUtils.DEFAULT_WIDTH);
                                                System.out.print(centerText(ConsoleUtils.CYAN
                                                                + "Enter food item ID to update (or 0 to back): "
                                                                + ConsoleUtils.RESET, ConsoleUtils.DEFAULT_WIDTH));
                                                continue;
                                        }

                                        List<FoodItem> allFoodItems = foodItemDAO.findAll();
                                        if (allFoodItems == null || allFoodItems.isEmpty()) {
                                                ConsoleUtils.printCenter(
                                                                ConsoleUtils.YELLOW + "No food items available!"
                                                                                + ConsoleUtils.RESET,
                                                                ConsoleUtils.DEFAULT_WIDTH);
                                                System.out.print(centerText(
                                                                ConsoleUtils.YELLOW + "Press Enter to continue..."
                                                                                + ConsoleUtils.RESET,
                                                                ConsoleUtils.DEFAULT_WIDTH));
                                                sc.nextLine();
                                                return;
                                        }

                                        for (FoodItem item : allFoodItems) {
                                                if (item.getItemId() == itemId) {
                                                        selectedItem = item;
                                                        break;
                                                }
                                        }

                                        if (selectedItem == null) {
                                                ConsoleUtils.printCenter(
                                                                ConsoleUtils.RED + "Food item with ID " + itemId
                                                                                + " not found!"
                                                                                + ConsoleUtils.RESET,
                                                                ConsoleUtils.DEFAULT_WIDTH);
                                                System.out.print(centerText(ConsoleUtils.CYAN
                                                                + "Enter food item ID to update (or 0 to back): "
                                                                + ConsoleUtils.RESET, ConsoleUtils.DEFAULT_WIDTH));
                                        }
                                } catch (NumberFormatException e) {
                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.RED + "Invalid input! Please enter a number.",
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                        System.out.print(centerText(ConsoleUtils.CYAN
                                                        + "Enter food item ID to update (or 0 to back): "
                                                        + ConsoleUtils.RESET, ConsoleUtils.DEFAULT_WIDTH));
                                }
                        }

                        while (true) {
                                ConsoleUtils.clearScreen();
                                ConsoleUtils.printCenter(
                                                ConsoleUtils.CYAN + "━━━━━━━━━━━ ＵＰＤＡＴＥ: " + selectedItem.getName()
                                                                + " ━━━━━━━━━━━"
                                                                + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH);
                                System.out.println();

                                ConsoleUtils.printCenter(
                                                ConsoleUtils.CYAN + "━━━━━━━━━━━ CURRENT INFORMATION ━━━━━━━━━━━"
                                                                + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH);
                                System.out.println();

                                String[] infoHeaders = { "Field", "Value" };
                                int[] infoColumnWidths = { 20, 80 };

                                ConsoleUtils.printTableHeader(infoHeaders, infoColumnWidths, 180);

                                String[][] infoData = {
                                                { "Name", selectedItem.getName() },
                                                { "Description",
                                                                selectedItem.getDescription() != null
                                                                                ? selectedItem.getDescription()
                                                                                : "N/A" },
                                                { "Price Range",
                                                                ConsoleUtils.formatPriceRange(
                                                                                selectedItem.getMinPrice(),
                                                                                selectedItem.getMaxPrice()) },
                                                { "Category", getCategoryName(selectedItem.getCategoryId()) },
                                                { "Status", selectedItem.getStatus().getDescription() },
                                                { "Sizes", selectedItem.getAvailableSizes() },
                                                { "Toppings",
                                                                selectedItem.getAvailableToppings() != null
                                                                                ? selectedItem.getAvailableToppings()
                                                                                : "None" }
                                };

                                for (int i = 0; i < infoData.length; i++) {
                                        ConsoleUtils.printTableRow(infoData[i], infoColumnWidths, 180);
                                        if (i < infoData.length - 1) {
                                                ConsoleUtils.printTableRowSeparator(infoColumnWidths, 180);
                                        }
                                }

                                ConsoleUtils.printTableFooter(infoColumnWidths, 180);
                                System.out.println();

                                String statusDescription = selectedItem.getStatus() == ItemStatus.ACTIVE
                                                ? "ACTIVE - Item is available for ordering"
                                                : selectedItem.getStatus() == enums.ItemStatus.SOLD_OUT
                                                                ? "SOLD OUT - Item is temporarily unavailable"
                                                                : "DELETED - Item has been removed";

                                String statusColor = selectedItem.getStatus() == ItemStatus.ACTIVE ? ConsoleUtils.GREEN
                                                : selectedItem.getStatus() == ItemStatus.SOLD_OUT ? ConsoleUtils.YELLOW
                                                                : ConsoleUtils.RED;

                                ConsoleUtils.printCenter(statusColor + statusDescription + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH);
                                System.out.println();

                                String[] updateOptions = {
                                                "1. Update Status",
                                                "2. Update Other Fields",
                                                "3. Back"
                                };

                                String[] updateColors = {
                                                ConsoleUtils.GREEN, ConsoleUtils.GREEN, ConsoleUtils.RED
                                };

                                ConsoleUtils.printCenter(
                                                ConsoleUtils.CYAN + "What do you want to update?" + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH);
                                System.out.println();
                                ConsoleUtils.printMenuOptions(updateOptions, 140, updateColors);
                                System.out.println();
                                System.out.print(centerText(ConsoleUtils.YELLOW + "Select (1-3): " + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH));

                                try {
                                        int updateChoice = Integer.parseInt(sc.nextLine());
                                        switch (updateChoice) {
                                                case 1:
                                                        updateFoodStatus(selectedItem);
                                                        break;
                                                case 2:
                                                        updateFoodOtherFields(selectedItem);
                                                        break;
                                                case 3:
                                                        try {
                                                                foodItemDAO.update(selectedItem);
                                                                ConsoleUtils.printCenter(
                                                                                ConsoleUtils.GREEN
                                                                                                + "✓ Food item updated successfully!"
                                                                                                + ConsoleUtils.RESET,
                                                                                ConsoleUtils.DEFAULT_WIDTH);
                                                                System.out.print(centerText(
                                                                                ConsoleUtils.YELLOW
                                                                                                + "Press Enter to continue..."
                                                                                                + ConsoleUtils.RESET,
                                                                                ConsoleUtils.DEFAULT_WIDTH));
                                                                sc.nextLine();
                                                        } catch (Exception saveError) {
                                                                ConsoleUtils.printCenter(
                                                                                ConsoleUtils.RED + "Failed to save changes: "
                                                                                                + saveError.getMessage()
                                                                                                + ConsoleUtils.RESET,
                                                                                ConsoleUtils.DEFAULT_WIDTH);
                                                                System.out.print(centerText(
                                                                                ConsoleUtils.YELLOW
                                                                                                + "Press Enter to continue..."
                                                                                                + ConsoleUtils.RESET,
                                                                                ConsoleUtils.DEFAULT_WIDTH));
                                                                sc.nextLine();
                                                        }
                                                        return;
                                                default:
                                                        ConsoleUtils.printCenter(
                                                                        ConsoleUtils.RED + "✗ Invalid choice!"
                                                                                        + ConsoleUtils.RESET,
                                                                        ConsoleUtils.DEFAULT_WIDTH);
                                                        System.out.print(
                                                                        centerText(ConsoleUtils.YELLOW
                                                                                        + "Press Enter to continue..."
                                                                                        + ConsoleUtils.RESET,
                                                                                        ConsoleUtils.DEFAULT_WIDTH));
                                                        sc.nextLine();
                                        }
                                } catch (NumberFormatException e) {
                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.RED + "Invalid input! Please enter a number.",
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                        System.out.print(centerText(
                                                        ConsoleUtils.YELLOW + "Press Enter to continue..."
                                                                        + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH));
                                        sc.nextLine();
                                }
                        }
                } catch (Exception e) {
                        ConsoleUtils.printCenter(ConsoleUtils.RED + "Error: " + e.getMessage() + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                }
        }

        private void updateFoodOtherFields(FoodItem item) {
                try {
                        System.out.println();
                        String newName = InputValidator.getValidatedInput(sc,
                                        centerText(ConsoleUtils.CYAN + "Food Item Name [" + item.getName()
                                                        + "] (Enter to skip): "
                                                        + ConsoleUtils.RESET, ConsoleUtils.DEFAULT_WIDTH),
                                        "",
                                        input -> input.length() >= 2 && input.length() <= 100,
                                        ConsoleUtils.RED + "✗ Name must be 2-100 characters!" + ConsoleUtils.RESET);
                        if (newName != null) {
                                item.setName(newName);
                                ConsoleUtils.printCenter(ConsoleUtils.GREEN + "✓ Name updated!" + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH);
                        }

                        System.out.println();
                        Double minPrice = InputValidator.getValidatedDouble(sc,
                                        centerText(ConsoleUtils.CYAN + "Min Price [₫"
                                                        + String.format("%.0f", item.getMinPrice())
                                                        + "] (Enter or 0 to skip): " + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH),
                                        price -> {
                                                try {
                                                        return Double.parseDouble(price) > 1000;
                                                } catch (Exception e) {
                                                        return false;
                                                }
                                        },
                                        ConsoleUtils.RED + "Price must be greater than 1000!" + ConsoleUtils.RESET,
                                        true);
                        if (minPrice != null) {
                                item.setMinPrice(minPrice);
                                ConsoleUtils.printCenter(
                                                ConsoleUtils.GREEN + "Min price updated!" + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH);
                        }

                        System.out.println();
                        Double maxPrice = InputValidator.getValidatedDouble(sc,
                                        centerText(ConsoleUtils.CYAN + "Max Price [₫"
                                                        + String.format("%.0f", item.getMaxPrice())
                                                        + "] (Enter or 0 to skip): " + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH),
                                        price -> {
                                                try {
                                                        double val = Double.parseDouble(price);
                                                        return val > 1000 && val >= item.getMinPrice();
                                                } catch (Exception e) {
                                                        return false;
                                                }
                                        },
                                        ConsoleUtils.RED + "✗ Price must be >1000 and ≥ min price!"
                                                        + ConsoleUtils.RESET,
                                        true);
                        if (maxPrice != null) {
                                item.setMaxPrice(maxPrice);
                                ConsoleUtils.printCenter(
                                                ConsoleUtils.GREEN + "✓ Max price updated!" + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH);
                        }

                        System.out.println();
                        ConsoleUtils.printCenter(
                                        ConsoleUtils.CYAN + "Category: 1=Pizza, 2=Burger, 3=Drink, 4=Dessert"
                                                        + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                        Integer categoryId = InputValidator.getValidatedInt(sc,
                                        centerText(ConsoleUtils.CYAN + "Category ID (Enter to skip): "
                                                        + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH),
                                        id -> {
                                                try {
                                                        int val = Integer.parseInt(id);
                                                        return val >= 1 && val <= 4;
                                                } catch (Exception e) {
                                                        return false;
                                                }
                                        },
                                        ConsoleUtils.RED + "✗ Category ID must be 1-4!" + ConsoleUtils.RESET,
                                        true);
                        if (categoryId != null) {
                                item.setCategoryId(categoryId);
                                ConsoleUtils.printCenter(
                                                ConsoleUtils.GREEN + "✓ Category updated!" + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH);
                        }

                        System.out.println();
                        String updateSizesResponse = InputValidator.getYesNoInput(sc,
                                        centerText(ConsoleUtils.CYAN + "Update Sizes? (y/n - Enter to skip): "
                                                        + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH));
                        if (updateSizesResponse != null && updateSizesResponse.equals("y")) {
                                ConsoleUtils.printCenter(
                                                ConsoleUtils.YELLOW + "Current sizes: " + item.getAvailableSizes()
                                                                + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH);
                                ConsoleUtils.printCenter(
                                                ConsoleUtils.YELLOW
                                                                + "(Format: s, m, l, xl, 2xl, 3xl - Enter 0 to finish)"
                                                                + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH);
                                System.out.println();

                                List<String> validSizesList = new ArrayList<>();
                                boolean sizeInputComplete = false;
                                while (!sizeInputComplete) {
                                        System.out.print(centerText(
                                                        ConsoleUtils.CYAN + "Size (or 0 to finish): "
                                                                        + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH));
                                        String sizeInput = sc.nextLine().trim();

                                        if (sizeInput.equals("0")) {
                                                if (validSizesList.isEmpty()) {
                                                        ConsoleUtils.printCenter(
                                                                        ConsoleUtils.RED + "✗ Must add at least 1 size!"
                                                                                        + ConsoleUtils.RESET,
                                                                        ConsoleUtils.DEFAULT_WIDTH);
                                                } else {
                                                        sizeInputComplete = true;
                                                }
                                                continue;
                                        }

                                        if (sizeInput.isEmpty()) {
                                                ConsoleUtils.printCenter(
                                                                ConsoleUtils.RED + "✗ Size cannot be empty!"
                                                                                + ConsoleUtils.RESET,
                                                                ConsoleUtils.DEFAULT_WIDTH);
                                                continue;
                                        }

                                        String convertedSize = convertAndValidateSize(sizeInput);
                                        if (convertedSize == null) {
                                                ConsoleUtils.printCenter(
                                                                ConsoleUtils.RED + "Invalid size! Only: s, m, l, xl, 2xl, 3xl"
                                                                                + ConsoleUtils.RESET,
                                                                ConsoleUtils.DEFAULT_WIDTH);
                                                continue;
                                        }

                                        if (!validSizesList.contains(convertedSize)) {
                                                validSizesList.add(convertedSize);
                                                ConsoleUtils.printCenter(
                                                                ConsoleUtils.GREEN + "Added: " + convertedSize
                                                                                + ConsoleUtils.RESET,
                                                                ConsoleUtils.DEFAULT_WIDTH);
                                        } else {
                                                ConsoleUtils.printCenter(
                                                                ConsoleUtils.YELLOW + "Already added!"
                                                                                + ConsoleUtils.RESET,
                                                                ConsoleUtils.DEFAULT_WIDTH);
                                        }
                                }

                                item.setAvailableSizes(String.join(", ", validSizesList));
                                ConsoleUtils.printCenter(ConsoleUtils.GREEN + "Sizes updated!" + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH);
                        }

                        System.out.println();
                        String updateToppingsResponse = InputValidator.getYesNoInput(sc,
                                        centerText(ConsoleUtils.CYAN + "Update Toppings? (y/n - Enter to skip): "
                                                        + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH));
                        if (updateToppingsResponse != null && updateToppingsResponse.equals("y")) {
                                ConsoleUtils.printCenter(
                                                ConsoleUtils.YELLOW + "Current toppings: "
                                                                + (item.getAvailableToppings() != null
                                                                                ? item.getAvailableToppings()
                                                                                : "None")
                                                                + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH);
                                ConsoleUtils.printCenter(
                                                ConsoleUtils.YELLOW
                                                                + "(Enter each topping per line - Enter 0 to finish)"
                                                                + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH);
                                System.out.println();

                                List<String> validToppingsList = new ArrayList<>();
                                boolean toppingInputComplete = false;
                                while (!toppingInputComplete) {
                                        System.out.print(centerText(
                                                        ConsoleUtils.CYAN + "Topping (or 0 to finish): "
                                                                        + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH));
                                        String toppingInput = sc.nextLine().trim();

                                        if (toppingInput.equals("0")) {
                                                toppingInputComplete = true;
                                                continue;
                                        }

                                        if (toppingInput.isEmpty()) {
                                                ConsoleUtils.printCenter(
                                                                ConsoleUtils.RED + "Topping cannot be empty!"
                                                                                + ConsoleUtils.RESET,
                                                                ConsoleUtils.DEFAULT_WIDTH);
                                                continue;
                                        }

                                        String capitalizedTopping = capitalizeWords(toppingInput);
                                        if (!validToppingsList.contains(capitalizedTopping)) {
                                                validToppingsList.add(capitalizedTopping);
                                                ConsoleUtils.printCenter(
                                                                ConsoleUtils.GREEN + "Added: " + capitalizedTopping
                                                                                + ConsoleUtils.RESET,
                                                                ConsoleUtils.DEFAULT_WIDTH);
                                        } else {
                                                ConsoleUtils.printCenter(
                                                                ConsoleUtils.YELLOW + "⚠ Already added!"
                                                                                + ConsoleUtils.RESET,
                                                                ConsoleUtils.DEFAULT_WIDTH);
                                        }
                                }

                                if (!validToppingsList.isEmpty()) {
                                        item.setAvailableToppings(String.join(", ", validToppingsList));
                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.GREEN + "Toppings updated!" + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                }
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

        private void updateFoodStatus(FoodItem item) {
                try {
                        boolean statusUpdateValid = false;

                        while (!statusUpdateValid) {
                                System.out.println();

                                String statusDescription = item.getStatus() == ItemStatus.ACTIVE
                                                ? "ACTIVE - Item is available for ordering"
                                                : item.getStatus() == ItemStatus.SOLD_OUT
                                                                ? "SOLD OUT - Item is temporarily unavailable"
                                                                : "DELETED - Item has been removed";

                                String statusColor = item.getStatus() == ItemStatus.ACTIVE ? ConsoleUtils.GREEN
                                                : item.getStatus() == ItemStatus.SOLD_OUT ? ConsoleUtils.YELLOW
                                                                : ConsoleUtils.RED;

                                ConsoleUtils.printCenter(
                                                statusColor + "Current Status: " + statusDescription
                                                                + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH);
                                System.out.println();

                                String[] statusOptions = {
                                                "1. Change to ACTIVE",
                                                "2. Change to SOLD OUT",
                                                "3. Cancel"
                                };

                                String[] optionColors = item.getStatus() == ItemStatus.ACTIVE
                                                ? new String[] { ConsoleUtils.GRAY, ConsoleUtils.GREEN,
                                                                ConsoleUtils.RED }
                                                : item.getStatus() == ItemStatus.SOLD_OUT
                                                                ? new String[] { ConsoleUtils.GREEN, ConsoleUtils.GRAY,
                                                                                ConsoleUtils.RED }
                                                                : new String[] { ConsoleUtils.GREEN, ConsoleUtils.GREEN,
                                                                                ConsoleUtils.RED };

                                ConsoleUtils.printCenter(
                                                ConsoleUtils.CYAN + "What status do you want to change to?"
                                                                + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH);
                                System.out.println();
                                ConsoleUtils.printMenuOptions(statusOptions, ConsoleUtils.DEFAULT_WIDTH, optionColors);
                                System.out.println();
                                System.out.print(centerText(ConsoleUtils.YELLOW + "Select (1-3): " + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH));

                                try {
                                        int choice = Integer.parseInt(sc.nextLine());
                                        switch (choice) {
                                                case 1:
                                                        if (item.getStatus() == ItemStatus.ACTIVE) {
                                                                ConsoleUtils.printCenter(
                                                                                ConsoleUtils.YELLOW
                                                                                                + "Item is already ACTIVE!"
                                                                                                + ConsoleUtils.RESET,
                                                                                ConsoleUtils.DEFAULT_WIDTH);
                                                        } else {
                                                                item.setStatus(ItemStatus.ACTIVE);
                                                                ConsoleUtils.printCenter(
                                                                                ConsoleUtils.GREEN
                                                                                                + "✓ Status updated to ACTIVE!"
                                                                                                + ConsoleUtils.RESET,
                                                                                ConsoleUtils.DEFAULT_WIDTH);
                                                        }
                                                        statusUpdateValid = true;
                                                        break;
                                                case 2:
                                                        if (item.getStatus() == ItemStatus.SOLD_OUT) {
                                                                ConsoleUtils.printCenter(
                                                                                ConsoleUtils.YELLOW
                                                                                                + "Item is already SOLD OUT!"
                                                                                                + ConsoleUtils.RESET,
                                                                                ConsoleUtils.DEFAULT_WIDTH);
                                                        } else {
                                                                item.setStatus(ItemStatus.SOLD_OUT);
                                                                ConsoleUtils.printCenter(
                                                                                ConsoleUtils.GREEN
                                                                                                + "✓ Status updated to SOLD OUT!"
                                                                                                + ConsoleUtils.RESET,
                                                                                ConsoleUtils.DEFAULT_WIDTH);
                                                        }
                                                        statusUpdateValid = true;
                                                        break;
                                                case 3:
                                                        ConsoleUtils.printCenter(
                                                                        ConsoleUtils.CYAN + "Operation cancelled"
                                                                                        + ConsoleUtils.RESET,
                                                                        ConsoleUtils.DEFAULT_WIDTH);
                                                        statusUpdateValid = true;
                                                        break;
                                                default:
                                                        ConsoleUtils.printCenter(
                                                                        ConsoleUtils.RED + "✗ Invalid choice!"
                                                                                        + ConsoleUtils.RESET,
                                                                        ConsoleUtils.DEFAULT_WIDTH);
                                                        System.out.print(centerText(
                                                                        ConsoleUtils.YELLOW
                                                                                        + "Press Enter to continue..."
                                                                                        + ConsoleUtils.RESET,
                                                                        ConsoleUtils.DEFAULT_WIDTH));
                                                        sc.nextLine();
                                        }
                                } catch (NumberFormatException e) {
                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.RED + "✗ Invalid input! Please enter a number (1-3)."
                                                                        + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                        System.out.print(centerText(
                                                        ConsoleUtils.YELLOW + "Press Enter to continue..."
                                                                        + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH));
                                        sc.nextLine();
                                }
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

        private void updateExistingCombo() {
                try {
                        ConsoleUtils.clearScreen();
                        String[] logo = {
                                        "▗▄▄▄▖ ▗▄▖  ▗▄▖ ▗▄▄▄      ▗▖  ▗▖ ▗▄▖ ▗▖  ▗▖ ▗▄▖  ▗▄▄▖▗▄▄▄▖▗▖  ▗▖▗▄▄▄▖▗▖  ▗▖▗▄▄▄▖",
                                        "▐▌   ▐▌ ▐▌▐▌ ▐▌▐▌  █     ▐▛▚▞▜▌▐▌ ▐▌▐▛▚▖▐▌▐▌ ▐▌▐▌   ▐▌   ▐▛▚▞▜▌▐▌   ▐▛▚▖▐▌  █",
                                        "▐▛▀▀▘▐▌ ▐▌▐▌ ▐▌▐▌  █     ▐▌  ▐▌▐▛▀▜▌▐▌ ▝▜▌▐▛▀▜▌▐▌▝▜▌▐▛▀▀▘▐▌  ▐▌▐▛▀▀▘▐▌ ▝▜▌  █",
                                        "▐▌   ▝▚▄▞▘▝▚▄▞▘▐▙▄▄▀     ▐▌  ▐▌▐▌ ▐▌▐▌  ▐▌▐▌ ▐▌▝▚▄▞▘▐▙▄▄▖▐▌  ▐▌▐▙▄▄▖▐▌  ▐▌  █",
                                        ""
                        };

                        ConsoleUtils.printLogoCentered(logo, ConsoleUtils.DEFAULT_WIDTH, 80);
                        ConsoleUtils.printCenter(
                                        ConsoleUtils.GREEN + "━━━━━━━━━━━━ ＵＰＤＡＴＥ  ＣＯＭＢＯ ━━━━━━━━━━━━"
                                                        + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                        System.out.println();

                        List<Combo> allCombos = comboDAO.findAll();
                        if (allCombos == null || allCombos.isEmpty()) {
                                ConsoleUtils.printCenter(
                                                ConsoleUtils.YELLOW + "No combos available to update!"
                                                                + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH);
                                System.out.println();
                                System.out.print(centerText(
                                                ConsoleUtils.YELLOW + "Press Enter to continue..." + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH));
                                sc.nextLine();
                                return;
                        }

                        System.out.print(centerText(ConsoleUtils.CYAN + "Enter combo ID to update (or 0 to back): "
                                        + ConsoleUtils.RESET, ConsoleUtils.DEFAULT_WIDTH));

                        Combo selectedCombo = null;
                        while (selectedCombo == null) {
                                try {
                                        int comboId = Integer.parseInt(sc.nextLine());
                                        if (comboId == 0) {
                                                return;
                                        }
                                        if (comboId <= 0) {
                                                ConsoleUtils.printCenter(
                                                                ConsoleUtils.RED + "✗ ID must be greater than 0!"
                                                                                + ConsoleUtils.RESET,
                                                                ConsoleUtils.DEFAULT_WIDTH);
                                                System.out.print(centerText(ConsoleUtils.CYAN
                                                                + "Enter combo ID to update (or 0 to back): "
                                                                + ConsoleUtils.RESET, ConsoleUtils.DEFAULT_WIDTH));
                                                continue;
                                        }

                                        for (Combo combo : allCombos) {
                                                if (combo.getComboId() == comboId) {
                                                        selectedCombo = combo;
                                                        break;
                                                }
                                        }

                                        if (selectedCombo == null) {
                                                ConsoleUtils.printCenter(
                                                                ConsoleUtils.RED + "✗ Combo with ID " + comboId
                                                                                + " not found!"
                                                                                + ConsoleUtils.RESET,
                                                                ConsoleUtils.DEFAULT_WIDTH);
                                                System.out.print(centerText(ConsoleUtils.CYAN
                                                                + "Enter combo ID to update (or 0 to back): "
                                                                + ConsoleUtils.RESET, ConsoleUtils.DEFAULT_WIDTH));
                                        }
                                } catch (NumberFormatException e) {
                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.RED + "Invalid input! Please enter a number.",
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                        System.out.print(centerText(
                                                        ConsoleUtils.CYAN + "Enter combo ID to update (or 0 to back): "
                                                                        + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH));
                                }
                        }

                        while (true) {
                                ConsoleUtils.clearScreen();
                                ConsoleUtils.printCenter(
                                                ConsoleUtils.GREEN + "━━━━━━━━━━━ ＵＰＤＡＴＥ: " + selectedCombo.getName()
                                                                + " ━━━━━━━━━━━"
                                                                + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH);
                                System.out.println();
                                ConsoleUtils.printCenter(
                                                ConsoleUtils.CYAN + "━━━━━━━━━━━ CURRENT INFORMATION ━━━━━━━━━━━"
                                                                + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH);
                                System.out.println();

                                String[] infoHeaders = { "Field", "Value" };
                                int[] infoColumnWidths = { 20, 80 };

                                ConsoleUtils.printTableHeader(infoHeaders, infoColumnWidths, 180);

                                String[][] infoData = {
                                                { "Name", selectedCombo.getName() },
                                                { "Price", ConsoleUtils
                                                                .formatVND(selectedCombo.getPrice().doubleValue()) },
                                                { "Status", selectedCombo.getStatus().getDescription() },
                                                { "Items", selectedCombo.getComboItems() != null
                                                                ? selectedCombo.getComboItems()
                                                                : "N/A" }
                                };

                                for (int i = 0; i < infoData.length; i++) {
                                        ConsoleUtils.printTableRow(infoData[i], infoColumnWidths, 180);
                                        if (i < infoData.length - 1) {
                                                ConsoleUtils.printTableRowSeparator(infoColumnWidths, 180);
                                        }
                                }

                                ConsoleUtils.printTableFooter(infoColumnWidths, 180);
                                System.out.println();

                                String statusDescription = selectedCombo.getStatus() == ComboStatus.ACTIVE
                                                ? "ACTIVE - Combo is available for ordering"
                                                : selectedCombo.getStatus() == ComboStatus.SOLD_OUT
                                                                ? "SOLD OUT - Combo is temporarily unavailable"
                                                                : "DELETED - Combo has been removed";

                                String statusColor = selectedCombo.getStatus() == ComboStatus.ACTIVE
                                                ? ConsoleUtils.GREEN
                                                : selectedCombo.getStatus() == ComboStatus.SOLD_OUT
                                                                ? ConsoleUtils.YELLOW
                                                                : ConsoleUtils.RED;

                                ConsoleUtils.printCenter(statusColor + statusDescription + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH);
                                System.out.println();

                                String[] updateOptions = {
                                                "1. Update Status",
                                                "2. Update Name",
                                                "3. Update Price",
                                                "4. Update Combo Items",
                                                "5. Back"
                                };

                                String[] updateColors = {
                                                ConsoleUtils.GREEN, ConsoleUtils.GREEN, ConsoleUtils.GREEN,
                                                ConsoleUtils.GREEN, ConsoleUtils.RED
                                };

                                ConsoleUtils.printCenter(
                                                ConsoleUtils.CYAN + "What do you want to update?" + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH);
                                System.out.println();
                                ConsoleUtils.printMenuOptions(updateOptions, 140, updateColors);
                                System.out.println();
                                System.out.print(centerText(ConsoleUtils.YELLOW + "Select (1-5): " + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH));

                                try {
                                        int updateChoice = Integer.parseInt(sc.nextLine());
                                        switch (updateChoice) {
                                                case 1:
                                                        updateComboStatus(selectedCombo);
                                                        break;
                                                case 2:
                                                        updateComboName(selectedCombo);
                                                        break;
                                                case 3:
                                                        updateComboPrice(selectedCombo);
                                                        break;
                                                case 4:
                                                        updateComboItems(selectedCombo);
                                                        break;
                                                case 5:
                                                        try {
                                                                comboDAO.update(selectedCombo);
                                                                ConsoleUtils.printCenter(
                                                                                ConsoleUtils.GREEN
                                                                                                + "✓ Combo updated successfully!"
                                                                                                + ConsoleUtils.RESET,
                                                                                ConsoleUtils.DEFAULT_WIDTH);
                                                                System.out.print(centerText(
                                                                                ConsoleUtils.YELLOW
                                                                                                + "Press Enter to continue..."
                                                                                                + ConsoleUtils.RESET,
                                                                                ConsoleUtils.DEFAULT_WIDTH));
                                                                sc.nextLine();
                                                        } catch (Exception saveError) {
                                                                ConsoleUtils.printCenter(
                                                                                ConsoleUtils.RED + "Failed to save changes: "
                                                                                                + saveError.getMessage()
                                                                                                + ConsoleUtils.RESET,
                                                                                ConsoleUtils.DEFAULT_WIDTH);
                                                                System.out.print(centerText(
                                                                                ConsoleUtils.YELLOW
                                                                                                + "Press Enter to continue..."
                                                                                                + ConsoleUtils.RESET,
                                                                                ConsoleUtils.DEFAULT_WIDTH));
                                                                sc.nextLine();
                                                        }
                                                        return;
                                                default:
                                                        ConsoleUtils.printCenter(
                                                                        ConsoleUtils.RED + "✗ Invalid choice!"
                                                                                        + ConsoleUtils.RESET,
                                                                        ConsoleUtils.DEFAULT_WIDTH);
                                                        System.out.print(
                                                                        centerText(ConsoleUtils.YELLOW
                                                                                        + "Press Enter to continue..."
                                                                                        + ConsoleUtils.RESET,
                                                                                        ConsoleUtils.DEFAULT_WIDTH));
                                                        sc.nextLine();
                                        }
                                } catch (NumberFormatException e) {
                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.RED + "✗ Invalid input! Please enter a number.",
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                        System.out.print(centerText(
                                                        ConsoleUtils.YELLOW + "Press Enter to continue..."
                                                                        + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH));
                                        sc.nextLine();
                                }
                        }
                } catch (Exception e) {
                        ConsoleUtils.printCenter(ConsoleUtils.RED + "Error: " + e.getMessage() + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                }
        }

        private void updateComboStatus(Combo combo) {
                try {
                        boolean statusUpdateValid = false;

                        while (!statusUpdateValid) {
                                System.out.println();

                                String statusDescription = combo.getStatus() == ComboStatus.ACTIVE
                                                ? "ACTIVE - Combo is available for ordering"
                                                : combo.getStatus() == ComboStatus.SOLD_OUT
                                                                ? "SOLD OUT - Combo is temporarily unavailable"
                                                                : "DELETED - Combo has been removed";

                                String statusColor = combo.getStatus() == ComboStatus.ACTIVE ? ConsoleUtils.GREEN
                                                : combo.getStatus() == ComboStatus.SOLD_OUT ? ConsoleUtils.YELLOW
                                                                : ConsoleUtils.RED;

                                ConsoleUtils.printCenter(
                                                statusColor + "Current Status: " + statusDescription
                                                                + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH);
                                System.out.println();

                                String[] statusOptions = {
                                                "1. Change to ACTIVE",
                                                "2. Change to SOLD OUT",
                                                "3. Cancel"
                                };

                                String[] optionColors = combo.getStatus() == ComboStatus.ACTIVE
                                                ? new String[] { ConsoleUtils.GRAY, ConsoleUtils.GREEN,
                                                                ConsoleUtils.RED }
                                                : combo.getStatus() == ComboStatus.SOLD_OUT
                                                                ? new String[] { ConsoleUtils.GREEN, ConsoleUtils.GRAY,
                                                                                ConsoleUtils.RED }
                                                                : new String[] { ConsoleUtils.GREEN, ConsoleUtils.GREEN,
                                                                                ConsoleUtils.RED };

                                ConsoleUtils.printCenter(
                                                ConsoleUtils.CYAN + "What status do you want to change to?"
                                                                + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH);
                                System.out.println();
                                ConsoleUtils.printMenuOptions(statusOptions, ConsoleUtils.DEFAULT_WIDTH, optionColors);
                                System.out.println();
                                System.out.print(centerText(ConsoleUtils.YELLOW + "Select (1-3): " + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH));

                                try {
                                        int choice = Integer.parseInt(sc.nextLine());
                                        switch (choice) {
                                                case 1:
                                                        if (combo.getStatus() == ComboStatus.ACTIVE) {
                                                                ConsoleUtils.printCenter(
                                                                                ConsoleUtils.YELLOW
                                                                                                + "Combo is already ACTIVE!"
                                                                                                + ConsoleUtils.RESET,
                                                                                ConsoleUtils.DEFAULT_WIDTH);
                                                        } else {
                                                                combo.setStatus(ComboStatus.ACTIVE);
                                                                ConsoleUtils.printCenter(
                                                                                ConsoleUtils.GREEN
                                                                                                + "✓ Status updated to ACTIVE!"
                                                                                                + ConsoleUtils.RESET,
                                                                                ConsoleUtils.DEFAULT_WIDTH);
                                                        }
                                                        statusUpdateValid = true;
                                                        break;
                                                case 2:
                                                        if (combo.getStatus() == ComboStatus.SOLD_OUT) {
                                                                ConsoleUtils.printCenter(
                                                                                ConsoleUtils.YELLOW
                                                                                                + "Combo is already SOLD OUT!"
                                                                                                + ConsoleUtils.RESET,
                                                                                ConsoleUtils.DEFAULT_WIDTH);
                                                        } else {
                                                                combo.setStatus(ComboStatus.SOLD_OUT);
                                                                ConsoleUtils.printCenter(
                                                                                ConsoleUtils.GREEN
                                                                                                + "✓ Status updated to SOLD OUT!"
                                                                                                + ConsoleUtils.RESET,
                                                                                ConsoleUtils.DEFAULT_WIDTH);
                                                        }
                                                        statusUpdateValid = true;
                                                        break;
                                                case 3:
                                                        ConsoleUtils.printCenter(
                                                                        ConsoleUtils.CYAN + "Operation cancelled"
                                                                                        + ConsoleUtils.RESET,
                                                                        ConsoleUtils.DEFAULT_WIDTH);
                                                        statusUpdateValid = true;
                                                        break;
                                                default:
                                                        ConsoleUtils.printCenter(
                                                                        ConsoleUtils.RED + "✗ Invalid choice!"
                                                                                        + ConsoleUtils.RESET,
                                                                        ConsoleUtils.DEFAULT_WIDTH);
                                                        System.out.print(centerText(
                                                                        ConsoleUtils.YELLOW
                                                                                        + "Press Enter to continue..."
                                                                                        + ConsoleUtils.RESET,
                                                                        ConsoleUtils.DEFAULT_WIDTH));
                                                        sc.nextLine();
                                        }
                                } catch (NumberFormatException e) {
                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.RED + "✗ Invalid input! Please enter a number (1-3)."
                                                                        + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                        System.out.print(centerText(
                                                        ConsoleUtils.YELLOW + "Press Enter to continue..."
                                                                        + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH));
                                        sc.nextLine();
                                }
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

        private void updateComboName(Combo combo) {
                try {
                        String newName = InputValidator.getValidatedInput(sc,
                                        centerText(ConsoleUtils.CYAN + "Combo Name [" + combo.getName()
                                                        + "] (Enter to skip): "
                                                        + ConsoleUtils.RESET, ConsoleUtils.DEFAULT_WIDTH),
                                        "",
                                        input -> input.length() >= 2 && input.length() <= 100,
                                        ConsoleUtils.RED + "✗ Name must be 2-100 characters!" + ConsoleUtils.RESET);
                        if (newName != null) {
                                combo.setName(newName);
                                ConsoleUtils.printCenter(
                                                ConsoleUtils.GREEN + "✓ Combo name updated!" + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH);
                        }
                } catch (Exception e) {
                        ConsoleUtils.printCenter(ConsoleUtils.RED + "Error: " + e.getMessage() + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                }
        }

        private void updateComboPrice(Combo combo) {
                try {
                        BigDecimal currentPrice = combo.getPrice();
                        Double newPrice = InputValidator.getValidatedDouble(sc,
                                        centerText(ConsoleUtils.CYAN + "Combo Price [₫"
                                                        + ConsoleUtils.formatVND(currentPrice.doubleValue())
                                                        + "] (Enter or 0 to skip): " + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH),
                                        price -> {
                                                try {
                                                        double val = Double.parseDouble(price);
                                                        return val >= 5000;
                                                } catch (Exception e) {
                                                        return false;
                                                }
                                        },
                                        ConsoleUtils.RED + "✗ Price must be at least 5000!" + ConsoleUtils.RESET,
                                        true);
                        if (newPrice != null) {
                                combo.setPrice(new BigDecimal(newPrice));
                                ConsoleUtils.printCenter(
                                                ConsoleUtils.GREEN + "✓ Combo price updated!" + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH);
                        }
                } catch (Exception e) {
                        ConsoleUtils.printCenter(ConsoleUtils.RED + "Error: " + e.getMessage() + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                }
        }

        private void updateComboItems(Combo combo) {
                try {
                        ConsoleUtils.printCenter(ConsoleUtils.CYAN + "━━ UPDATE COMBO ITEMS ━━" + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                        ConsoleUtils.printCenter(
                                        ConsoleUtils.YELLOW + "Current items: " + combo.getComboItems()
                                                        + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                        ConsoleUtils.printCenter(
                                        ConsoleUtils.YELLOW + "(Format: ItemName x quantity - ví dụ: Pizza x1, Coke x2)"
                                                        + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                        System.out.println();

                        Map<String, Integer> parsedItems = new HashMap<>();
                        StringBuilder itemsBuilder = new StringBuilder();
                        boolean itemInputComplete = false;

                        while (!itemInputComplete) {
                                System.out.print(centerText(
                                                ConsoleUtils.CYAN + "Item (or 0 to finish): " + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH));
                                String itemInput = sc.nextLine().trim();

                                if (itemInput.equals("0")) {
                                        if (parsedItems.isEmpty()) {
                                                ConsoleUtils.printCenter(
                                                                ConsoleUtils.RED + "✗ Combo phải có ít nhất 1 item!"
                                                                                + ConsoleUtils.RESET,
                                                                ConsoleUtils.DEFAULT_WIDTH);
                                        } else {
                                                itemInputComplete = true;
                                        }
                                        continue;
                                }

                                if (itemInput.isEmpty()) {
                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.RED + "✗ Item không được để trống!"
                                                                        + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                        continue;
                                }

                                String[] parts = itemInput.split("x");
                                if (parts.length != 2) {
                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.RED + "✗ Format sai! Dùng: 'ItemName x quantity' (ví dụ: Pizza x1)"
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
                                                                ConsoleUtils.RED + "✗ Số lượng phải > 0!"
                                                                                + ConsoleUtils.RESET,
                                                                ConsoleUtils.DEFAULT_WIDTH);
                                                continue;
                                        }
                                } catch (NumberFormatException e) {
                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.RED + "✗ Số lượng phải là số!"
                                                                        + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                        continue;
                                }

                                List<FoodItem> allItems = foodItemDAO.findAll();
                                FoodItem foundItem = null;
                                for (FoodItem fi : allItems) {
                                        if (fi.getName().equalsIgnoreCase(itemName)) {
                                                foundItem = fi;
                                                break;
                                        }
                                }

                                if (foundItem == null) {
                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.RED + "✗ Không tìm thấy item: '" + itemName + "'"
                                                                        + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                        ConsoleUtils.printCenter(
                                                        ConsoleUtils.YELLOW + "Danh sách item có sẵn:"
                                                                        + ConsoleUtils.RESET,
                                                        ConsoleUtils.DEFAULT_WIDTH);
                                        for (FoodItem fi : allItems) {
                                                ConsoleUtils.printCenter("  • " + fi.getName(),
                                                                ConsoleUtils.DEFAULT_WIDTH);
                                        }
                                        continue;
                                }
                                parsedItems.put(itemName, quantity);
                                if (itemsBuilder.length() > 0) {
                                        itemsBuilder.append(", ");
                                }
                                itemsBuilder.append(itemName).append(" x").append(quantity);

                                ConsoleUtils.printCenter(
                                                ConsoleUtils.GREEN + "✓ Đã thêm: " + itemName + " x" + quantity
                                                                + ConsoleUtils.RESET,
                                                ConsoleUtils.DEFAULT_WIDTH);
                        }

                        combo.setComboItems(itemsBuilder.toString());
                } catch (Exception e) {
                        ConsoleUtils.printCenter(ConsoleUtils.RED + "Error: " + e.getMessage() + ConsoleUtils.RESET,
                                        ConsoleUtils.DEFAULT_WIDTH);
                }
        }

        private void deleteFoodItem() {
                try {
                        ConsoleUtils.clearScreen();
                        System.out.println();
                        System.out.println();
                        ConsoleUtils.printCenter(ConsoleUtils.ORANGE + "ＤＥＬＥＴＥ  ＦＯＯＤ  ＩＴＥＭ" + ConsoleUtils.RESET, 135);
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
