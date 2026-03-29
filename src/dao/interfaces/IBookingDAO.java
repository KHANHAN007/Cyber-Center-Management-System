package dao.interfaces;

import model.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface IBookingDAO {
    Booking findById(int bookingId);

    List<Booking> findByUserId(int userId);

    List<Booking> findAll();

    List<Booking> findPendingBookings();

    Booking findDetailWithJoin(int bookingId);

    void create(Booking booking);

    void update(Booking booking);

    void updateStatus(int bookingId, String status);

    boolean isPCAvailable(int pcId, LocalDateTime startTime, LocalDateTime endTime);

    List<Booking> findByPCIdAndDateRange(int pcId, LocalDateTime startDate, LocalDateTime endDate);
}
