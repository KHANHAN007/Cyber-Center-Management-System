package dao.interfaces;

import model.OTP;

public interface IOTPDAO {
    OTP findById(int otpId);

    OTP findByBookingId(int bookingId);

    void create(OTP otp);

    void update(OTP otp);

    void updateStatus(int otpId, String status);
}
