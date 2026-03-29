package enums;

public enum PaymentStatus {
    PENDING("PENDING", "Pending payment"),
    PAID("PAID", "Payment successful"),
    FAILED("FAILED", "Payment failed");

    private final String value;
    private final String description;

    PaymentStatus(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static PaymentStatus fromValue(String value) {
        for (PaymentStatus status : PaymentStatus.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid PaymentStatus: " + value);
    }
}
