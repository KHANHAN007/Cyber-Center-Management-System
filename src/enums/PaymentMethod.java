package enums;

public enum PaymentMethod {
    CASH("CASH", "Cash payment"),
    WALLET("WALLET", "Wallet payment");

    private final String value;
    private final String description;

    PaymentMethod(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static PaymentMethod fromValue(String value) {
        for (PaymentMethod method : PaymentMethod.values()) {
            if (method.value.equalsIgnoreCase(value)) {
                return method;
            }
        }
        throw new IllegalArgumentException("Invalid PaymentMethod: " + value);
    }
}
