package dao.interfaces;

import model.CheckInOut;
import java.util.List;
import java.util.Map;

public interface ICheckInOutDAO {
    CheckInOut findByBookingId(int bookingId);

    CheckInOut getCheckInOutByBookingId(int bookingId);

    void createCheckIn(CheckInOut checkInOut);

    void updateCheckOut(int bookingId);

    List<Map<String, Object>> findAllActiveSessions();

    Map<String, Object> findSessionDetailByBookingId(int bookingId);

    List<Map<String, Object>> findAllCheckInOut();
}
