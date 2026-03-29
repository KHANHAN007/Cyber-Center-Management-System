package enums;

public enum DiscountType {
    PERCENT("Percentage Discount"),
    FIXED("Fixed Amount Discount");

    private final String description;

    DiscountType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static DiscountType fromString(String type) {
        try {
            return DiscountType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            return FIXED;
        }
    }
}
