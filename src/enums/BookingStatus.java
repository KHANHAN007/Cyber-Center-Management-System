package enums;

public enum BookingStatus {
    PENDING("PENDING", "Pending confirmation"),
    CONFIRMED("CONFIRMED", "Confirmed"),
    IN_PROGRESS("IN_PROGRESS", "In progress"),
    COMPLETED("COMPLETED", "Completed"),
    CANCELLED("CANCELLED", "Cancelled");

    private final String value;
    private final String description;

    BookingStatus(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue(){
        return value;
    }

    public String getDescription(){
        return description;
    }

    public static BookingStatus fromValue(String value) {
        for (BookingStatus status : BookingStatus.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid BookingStatus: " + value);
    }
}
