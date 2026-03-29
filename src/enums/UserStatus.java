package enums;

public enum UserStatus {
    ACTIVE("ACTIVE", "ACTIVE"),
    INACTIVE("INACTIVE", "INACTIVE"),
    BLOCKED("BLOCKED", "BLOCKED");

    private final String value;
    private final String description;

    UserStatus(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue(){
        return value;
    }

    public String getDescription() {
        return description;
    }


    public static UserStatus fromValue(String value) {
        for (UserStatus status : UserStatus.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown user status: " + value);
    }
}
