package enums;

public enum ItemStatus {
    ACTIVE("ACTIVE", "Active"),
    SOLD_OUT("SOLD_OUT", "Sold out"),
    DELETED("DELETED", "Deleted");

    private final String value;
    private final String description;

    ItemStatus(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static ItemStatus fromValue(String value) {
        for (ItemStatus status : ItemStatus.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid ItemStatus: " + value);
    }
}
