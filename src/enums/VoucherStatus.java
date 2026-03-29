package enums;

public enum VoucherStatus {
    ACTIVE("Active - Voucher is available for use"),
    INACTIVE("Inactive - Voucher is disabled"),
    EXPIRED("Expired - Voucher has passed its expiration date");

    private final String description;

    VoucherStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static VoucherStatus fromString(String status) {
        try {
            return VoucherStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return INACTIVE;
        }
    }
}
