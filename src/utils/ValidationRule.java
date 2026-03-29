package utils;

@FunctionalInterface
public interface ValidationRule {
    boolean validate(String input);
}
