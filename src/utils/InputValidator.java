package utils;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class InputValidator {
    public static int getIntInput(Scanner sc, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = sc.nextLine().trim();
                if (input.isEmpty()) {
                    System.err.println("Input cannot be empty. Please enter a valid integer.");
                    continue;
                }
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.err.println("Invalid input. Please enter a valid integer.");
            }
        }
    }

    public static double getDoubleInput(Scanner sc, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = sc.nextLine().trim();
                if (input.isEmpty()) {
                    System.err.println("Input cannot be empty. Please enter a valid number.");
                    continue;
                }
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.err.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    public static String getStringInput(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            if (input.isEmpty()) {
                System.err.println("Input cannot be empty. Please enter a valid string.");
                continue;
            }
            return input;
        }
    }

    public static LocalDateTime getDateTimeInput(Scanner sc, String prompt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        while (true) {
            try {
                System.out.print(prompt + " (format: yyyy-MM-dd HH:mm): ");
                String input = sc.nextLine().trim();
                if (input.isEmpty()) {
                    System.err.println("Input cannot be empty. Please enter a valid date and time.");
                    continue;
                }
                return LocalDateTime.parse(input, formatter);
            } catch (DateTimeException e) {
                System.err.println("Invalid input. Please enter a valid date and time in the format yyyy-MM-dd HH:mm.");
            }
        }
    }

    public static boolean isValidUsername(String username) {
        return username != null && username.length() >= 3 && username.matches("^[a-zA-Z0-9_]+$");
    }

    public static String getUsernameErrorMessage(String username) {
        if (username == null || username.isEmpty()) {
            return "Username cannot be empty";
        }
        if (username.length() < 3) {
            return "Username must be at least 3 characters (current: " + username.length() + " characters)";
        }
        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            return "Username can only contain: letters (a-z, A-Z), numbers (0-9), underscore (_)";
        }
        return null;
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    public static boolean isValidFullName(String fullName) {
        return fullName != null && !fullName.trim().isEmpty() && fullName.length() >= 3 &&
                !fullName.contains("'") && !fullName.contains("\"") && !fullName.contains(";");
    }

    public static String getFullNameErrorMessage(String fullName) {
        if (fullName == null || fullName.isEmpty()) {
            return "Full name cannot be empty";
        }
        if (fullName.length() < 3) {
            return "Full name must be at least 3 characters (current: " + fullName.length() + " characters)";
        }
        if (fullName.contains("'") || fullName.contains("\"") || fullName.contains(";")) {
            return "Full name cannot contain special characters: ' \" ;";
        }
        return null;
    }

    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    public static boolean isValidPhoneNumber(String phone) {
        return phone != null && phone.matches("^[0-9]{10,11}$");
    }

    public static boolean confirmAction(Scanner scanner, String message) {
        System.out.print(message + " (y/n): ");
        String input = scanner.nextLine().trim().toLowerCase();
        return input.equals("y") || input.equals("yes");
    }

    public static int displayMenuAndGetChoice(Scanner scanner, String[] options) {
        System.out.println("\n" + "=".repeat(50));
        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ". " + options[i]);
        }
        System.out.println("=".repeat(50));
        return getIntInput(scanner, "Enter your choice: ");
    }

    // Validation loop method - continues looping until valid input or skip
    public static String getValidatedInput(Scanner sc, String prompt, String skipOption, ValidationRule rule,
            String errorMessage) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();

            // Allow skip if input is empty or matches skipOption
            if (input.isEmpty() || input.equals(skipOption)) {
                return null; // null indicates skip
            }

            // Check if input passes validation
            if (rule.validate(input)) {
                return input;
            } else {
                System.err.println(errorMessage);
            }
        }
    }

    // Validation loop for yes/no prompts
    public static String getYesNoInput(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim().toLowerCase();

            if (input.equals("y") || input.equals("yes")) {
                return "y";
            } else if (input.equals("n") || input.equals("no")) {
                return "n";
            } else if (input.isEmpty()) {
                return null; // Skip
            } else {
                System.err.println("Please enter 'y', 'n', or press Enter to skip.");
            }
        }
    }

    // Validation loop for numeric input with custom validation
    public static Double getValidatedDouble(Scanner sc, String prompt, ValidationRule rule, String errorMessage,
            boolean allowSkip) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();

            // Allow skip
            if (allowSkip && (input.isEmpty() || input.equals("0"))) {
                return null; // null indicates skip
            }

            try {
                double value = Double.parseDouble(input);
                if (rule.validate(String.valueOf(value))) {
                    return value;
                } else {
                    System.err.println(errorMessage);
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    // Validation loop for integer input with custom validation
    public static Integer getValidatedInt(Scanner sc, String prompt, ValidationRule rule, String errorMessage,
            boolean allowSkip) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();

            // Allow skip
            if (allowSkip && input.isEmpty()) {
                return null; // null indicates skip
            }

            try {
                int value = Integer.parseInt(input);
                if (rule.validate(String.valueOf(value))) {
                    return value;
                } else {
                    System.err.println(errorMessage);
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid input. Please enter a valid integer.");
            }
        }
    }
}
