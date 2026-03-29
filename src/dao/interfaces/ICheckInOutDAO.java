package dao.interfaces;

import model.CheckInOut;

public interface ICheckInOutDAO {
    CheckInOut findByBookingId(int bookingId);
    void createCheckIn(CheckInOut checkInOut);
    void updateCheckOut(int bookingId);
}
