package model;

import enums.VoucherStatus;
import enums.DiscountType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class VoucherCode {
    private int voucherId;
    private String code;
    private String description;
    private int promotionId;
    private String promotionName;

    private DiscountType discountType; // PERCENT or FIXED
    private BigDecimal discountValue; // Percentage or fixed amount
    private BigDecimal minOrderValue; // Minimum order value to use voucher

    private int maxUses; // 0 = unlimited, >0 = limited
    private int usedCount; // Current used count

    private VoucherStatus status; // ACTIVE, INACTIVE, EXPIRED
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;

    public VoucherCode() {
    }

    public VoucherCode(String code, int promotionId) {
        this.code = code;
        this.promotionId = promotionId;
    }

    public int getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(int voucherId) {
        this.voucherId = voucherId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(int promotionId) {
        this.promotionId = promotionId;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    public BigDecimal getMinOrderValue() {
        return minOrderValue;
    }

    public void setMinOrderValue(BigDecimal minOrderValue) {
        this.minOrderValue = minOrderValue;
    }

    public int getMaxUses() {
        return maxUses;
    }

    public void setMaxUses(int maxUses) {
        this.maxUses = maxUses;
    }

    public int getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(int usedCount) {
        this.usedCount = usedCount;
    }

    public VoucherStatus getStatus() {
        return status;
    }

    public void setStatus(VoucherStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public boolean isUnlimited() {
        return maxUses == 0;
    }

    public boolean isExpired() {
        if (expiresAt == null)
            return false;
        return LocalDateTime.now().isAfter(expiresAt);
    }

    public boolean canBeUsed() {
        return status == VoucherStatus.ACTIVE && !isExpired() &&
                (isUnlimited() || usedCount < maxUses);
    }

    public int getRemainingUses() {
        if (isUnlimited())
            return -1; // -1 means unlimited
        return maxUses - usedCount;
    }

    public String getRemainingUsesDisplay() {
        if (isUnlimited())
            return "Unlimited";
        int remaining = getRemainingUses();
        return remaining + " left";
    }

    public void incrementUsedCount() {
        if (!isUnlimited()) {
            this.usedCount++;
        }
    }

    @Override
    public String toString() {
        return "VoucherCode{" +
                "voucherId=" + voucherId +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", discountType=" + discountType +
                ", discountValue=" + discountValue +
                ", status=" + status +
                ", maxUses=" + maxUses +
                ", usedCount=" + usedCount +
                ", expiresAt=" + expiresAt +
                '}';
    }
}
