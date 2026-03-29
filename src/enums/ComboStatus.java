package enums;

public enum ComboStatus {
    ACTIVE("ACTIVE", "Active"),
    SOLD_OUT("SOLD_OUT", "Sold out"),
    DELETED("DELETED", "Deleted");

    private final String value;
    private final String description;

    ComboStatus(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static ComboStatus fromValue(String value) {
        for (ComboStatus status : ComboStatus.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid ComboStatus: " + value);
    }
}
