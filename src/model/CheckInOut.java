package model;

import java.time.LocalDateTime;

public class CheckInOut {
    private int id;
    private int bookingId;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;

    public CheckInOut() {
    }

    public CheckInOut(int bookingId, LocalDateTime checkInTime) {
        this.bookingId = bookingId;
        this.checkInTime = checkInTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public LocalDateTime getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(LocalDateTime checkInTime) {
        this.checkInTime = checkInTime;
    }

    public LocalDateTime getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(LocalDateTime checkOutTime) {
        this.checkOutTime = checkOutTime;
    }
    @Override
    public String toString() {
        return "CheckInOut{" +
                "id=" + id +
                ", bookingId=" + bookingId +
                ", checkInTime=" + checkInTime +
                ", checkOutTime=" + checkOutTime +
                '}';
    }
}
