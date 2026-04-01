package enums;

public enum OTPStatus {
    PENDING("PENDING", "Chưa xác nhận"),
    VERIFIED("VERIFIED", "Đã xác nhận"),
    EXPIRED("EXPIRED", "Đã hết hạn");

    private final String value;
    private final String description;

    OTPStatus(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static OTPStatus fromValue(String value) {
        for (OTPStatus status : OTPStatus.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid OTPStatus: " + value);
    }
}
