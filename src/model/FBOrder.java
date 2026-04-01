package model;

import enums.OrderStatus;

import java.time.LocalDateTime;

public class FBOrder {
    private int orderId;
    private String orderCode;
    private Integer bookingId;
    private double totalPrice;
    private OrderStatus status;
    private String paymentStatus;
    private String paymentMethod;
    private LocalDateTime createdAt;

    public FBOrder() {
    }

    public FBOrder(int bookingId, OrderStatus status) {
        this.bookingId = bookingId > 0 ? bookingId : null;
        this.status = status;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "FBOrder{" +
                "orderId=" + orderId +
                ", orderCode='" + orderCode + '\'' +
                ", bookingId=" + bookingId +
                ", totalPrice=" + totalPrice +
                ", status='" + status + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                '}';
    }
}
