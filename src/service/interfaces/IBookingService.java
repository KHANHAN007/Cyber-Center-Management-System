package service.interfaces;

import model.Booking;
import model.PC;
import model.Zone;

import java.time.LocalDateTime;
import java.util.List;

public interface IBookingService {
    Booking createBooking(int userId, int pcId, LocalDateTime startTime, LocalDateTime endTime);

    Booking scheduleBooking(int userId, int pcId, LocalDateTime scheduledStartTime, LocalDateTime scheduledEndTime);

    Booking getBookingById(int bookingId);

    List<Booking> getBookingsByUserId(int userId);

    void confirmBooking(int bookingId);

    void cancelBooking(int bookingId);

    void checkIn(int bookingId);

    void checkOut(int bookingId);

    double calculateBookingPrice(PC pc, LocalDateTime startTime, LocalDateTime endTime);

    List<Zone> getAllZones();

    List<PC> getPCsByZone(int zoneId);

    List<PC> getAvailablePCs();

    boolean isPCAvailable(int pcId, LocalDateTime startTime, LocalDateTime endTime);
}
