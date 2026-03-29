package model;

import enums.BookingStatus;

import java.time.LocalDateTime;

public class Booking {
    private int bookingId;
    private String bookingCode;
    private int userId;
    private int pcId;
    private int slotId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double totalPrice;
    private Integer voucherId;
    private Double discountAmount;
    private BookingStatus status;
    private LocalDateTime createdAt;
    private User user;
    private PC pc;

    public Booking() {
    }

    public Booking(int userId, int pcId, LocalDateTime startTime, LocalDateTime endTime) {
        this.userId = userId;
        this.pcId = pcId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = BookingStatus.PENDING;
    }

    public int getBookingId() {
        return bookingId;
    }

    public String getBookingCode() {
        return bookingCode;
    }

    public int getUserId() {
        return userId;
    }

    public int getPcId() {
        return pcId;
    }

    public int getSlotId() {
        return slotId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public Integer getVoucherId() {
        return voucherId;
    }

    public Double getDiscountAmount() {
        return discountAmount;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }

    public PC getPc() {
        return pc;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public void setBookingCode(String bookingCode) {
        this.bookingCode = bookingCode;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setPcId(int pcId) {
        this.pcId = pcId;
    }

    public void setSlotId(int slotId) {
        this.slotId = slotId;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setVoucherId(Integer voucherId) {
        this.voucherId = voucherId;
    }

    public void setDiscountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setPc(PC pc) {
        this.pc = pc;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingId=" + bookingId +
                ", bookingCode='" + bookingCode + '\'' +
                ", status='" + status + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
