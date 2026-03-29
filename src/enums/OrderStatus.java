package enums;

public enum OrderStatus {
    PENDING("PENDING", "PENDING"),
    CONFIRMED("CONFIRMED", "CONFIRMED"),
    PREPARING("PREPARING", "PREPARING"),
    READY("READY", "READY"),
    COMPLETED("COMPLETED", "COMPLETED"),
    CANCELLED("CANCELLED", "CANCELLED");

    private final String value;
    private final String description;

    OrderStatus(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static OrderStatus fromValue(String value) {
        for (OrderStatus status : OrderStatus.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid OrderStatus: " + value);
    }
}
