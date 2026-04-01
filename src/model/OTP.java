package model;

import java.time.LocalDateTime;
import enums.OTPStatus;

public class OTP {
    private int otpId;
    private int bookingId;
    private String code;
    private LocalDateTime expiryTime;
    private OTPStatus status;

    public OTP() {
    }

    public OTP(int bookingId, String code, LocalDateTime expiryTime) {
        this.bookingId = bookingId;
        this.code = code;
        this.expiryTime = expiryTime;
        this.status = OTPStatus.PENDING;
    }

    public int getOtpId() {
        return otpId;
    }

    public void setOtpId(int otpId) {
        this.otpId = otpId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(LocalDateTime expiryTime) {
        this.expiryTime = expiryTime;
    }

    public OTPStatus getStatus() {
        return status;
    }

    public void setStatus(OTPStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "OTP{" +
                "otpId=" + otpId +
                ", bookingId=" + bookingId +
                ", code='" + code + '\'' +
                ", expiryTime=" + expiryTime +
                ", status='" + status + '\'' +
                '}';
    }
}
