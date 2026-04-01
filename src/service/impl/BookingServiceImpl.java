package service.impl;

import dao.interfaces.*;
import enums.BookingStatus;
import enums.PCStatus;
import exception.BusinessException;
import exception.InvalidDataException;
import exception.NotFoundException;
import model.*;
import service.interfaces.IBookingService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class BookingServiceImpl implements IBookingService {
    private IBookingDAO bookingDAO;
    private ICheckInOutDAO checkInOutDAO;
    private IPCDAO pcDAO;
    private IPCConfigDAO configDAO;
    private IZoneDAO zoneDAO;

    public BookingServiceImpl(IBookingDAO bookingDAO, IPCDAO pcDAO, IZoneDAO zoneDAO, ICheckInOutDAO checkInOutDAO,
            IPCConfigDAO configDAO) {
        this.bookingDAO = bookingDAO;
        this.checkInOutDAO = checkInOutDAO;
        this.pcDAO = pcDAO;
        this.zoneDAO = zoneDAO;
        this.configDAO = configDAO;
    }

    @Override
    public Booking createBooking(int userId, int pcId, LocalDateTime startTime, LocalDateTime endTime)
            throws BusinessException {
        try {
            if (startTime == null || endTime == null) {
                throw new InvalidDataException("Start and end time cannot be empty");
            }

            if (endTime.isBefore(startTime)) {
                throw new InvalidDataException("End time must be after start time");
            }

            PC pc = pcDAO.findById(pcId);
            if (pc == null) {
                throw new NotFoundException("PC does not exist");
            }


            if (pc.getStatus() != null && pc.getStatus() != PCStatus.AVAILABLE) {
                throw new BusinessException("PC is not available for booking (current status: " + pc.getStatus() + ")");
            }

            Booking booking = new Booking(userId, pcId, startTime, endTime);
            double totalPrice = calculateBookingPrice(pc, startTime, endTime);
            booking.setTotalPrice(totalPrice);
            booking.setStatus(BookingStatus.PENDING);

            bookingDAO.create(booking);

            return booking;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("Error creating booking: " + e.getMessage(), e);
        }
    }

    @Override
    public Booking scheduleBooking(int userId, int pcId, LocalDateTime scheduledStartTime,
            LocalDateTime scheduledEndTime)
            throws BusinessException {
        try {
            if (scheduledStartTime == null || scheduledEndTime == null) {
                throw new InvalidDataException("Scheduled start and end time cannot be empty");
            }

            if (scheduledEndTime.isBefore(scheduledStartTime)) {
                throw new InvalidDataException("Scheduled end time must be after start time");
            }

            if (scheduledStartTime.isBefore(LocalDateTime.now())) {
                throw new InvalidDataException("Scheduled start time must be in the future");
            }

            PC pc = pcDAO.findById(pcId);
            if (pc == null) {
                throw new NotFoundException("PC does not exist");
            }

            if (!bookingDAO.isPCAvailable(pcId, scheduledStartTime, scheduledEndTime)) {
                throw new BusinessException("PC is not available for the selected time range");
            }

            Booking booking = new Booking(userId, pcId, scheduledStartTime, scheduledEndTime);
            double totalPrice = calculateBookingPrice(pc, scheduledStartTime, scheduledEndTime);
            booking.setTotalPrice(totalPrice);
            booking.setStatus(BookingStatus.PENDING);

            bookingDAO.create(booking);

            return booking;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("Error scheduling booking: " + e.getMessage(), e);
        }
    }

    @Override
    public Booking getBookingById(int bookingId) throws BusinessException {
        try {
            if (bookingId <= 0) {
                throw new InvalidDataException("Invalid booking ID");
            }

            Booking booking = bookingDAO.findById(bookingId);
            if (booking == null) {
                throw new NotFoundException("Booking does not exist");
            }

            return booking;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("Error retrieving booking information: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Booking> getBookingsByUserId(int userId) throws BusinessException {
        try {
            if (userId <= 0) {
                throw new InvalidDataException("Invalid user ID");
            }

            return bookingDAO.findByUserId(userId);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("Error retrieving booking list: " + e.getMessage(), e);
        }
    }

    @Override
    public void confirmBooking(int bookingId) throws BusinessException {
        try {
            Booking booking = bookingDAO.findById(bookingId);
            if (booking == null) {
                throw new NotFoundException("Booking không tồn tại");
            }

            if (booking.getStatus() != BookingStatus.PENDING) {
                throw new BusinessException("Only pending bookings can be confirmed");
            }

            booking.setStatus(BookingStatus.CONFIRMED);
            bookingDAO.update(booking);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("Error confirming booking: " + e.getMessage(), e);
        }
    }

    @Override
    public void cancelBooking(int bookingId) throws BusinessException {
        try {
            Booking booking = bookingDAO.findById(bookingId);
            if (booking == null) {
                throw new NotFoundException("Booking không tồn tại");
            }

            if (booking.getStatus() == BookingStatus.COMPLETED ||
                    booking.getStatus() == BookingStatus.CANCELLED) {
                throw new BusinessException("Cannot cancel completed or already cancelled bookings");
            }

            booking.setStatus(BookingStatus.CANCELLED);
            bookingDAO.update(booking);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("Error cancelling booking: " + e.getMessage(), e);
        }
    }

    @Override
    public void checkIn(int bookingId) throws BusinessException {
        try {
            Booking booking = bookingDAO.findById(bookingId);
            if (booking == null) {
                throw new NotFoundException("Booking không tồn tại");
            }

            if (booking.getStatus() != BookingStatus.CONFIRMED) {
                throw new BusinessException("Booking must be confirmed before check-in");
            }

            CheckInOut checkInOut = new CheckInOut();
            checkInOut.setBookingId(bookingId);
            checkInOut.setCheckInTime(LocalDateTime.now());
            checkInOutDAO.createCheckIn(checkInOut);
            booking.setStatus(BookingStatus.IN_PROGRESS);
            bookingDAO.update(booking);

            PC pc = pcDAO.findById(booking.getPcId());
            if (pc != null) {
                pc.setStatus(PCStatus.IN_USE);
                pcDAO.update(pc);
            }
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("Error during check-in: " + e.getMessage(), e);
        }
    }

    @Override
    public void checkOut(int bookingId) throws BusinessException {
        try {
            Booking booking = bookingDAO.findById(bookingId);
            if (booking == null) {
                throw new NotFoundException("Booking không tồn tại");
            }

            if (booking.getStatus() != BookingStatus.IN_PROGRESS) {
                throw new BusinessException("Booking must be in progress to check-out");
            }


            CheckInOut checkInOut = checkInOutDAO.getCheckInOutByBookingId(bookingId);
            if (checkInOut != null && checkInOut.getCheckInTime() != null) {
                LocalDateTime actualCheckOutTime = LocalDateTime.now();


                PC pc = pcDAO.findById(booking.getPcId());
                if (pc != null) {
                    double actualPrice = calculateBookingPrice(pc, checkInOut.getCheckInTime(), actualCheckOutTime);
                    double originalPrice = booking.getTotalPrice();


                    booking.setTotalPrice(actualPrice);


                    if (actualPrice != originalPrice) {
                        System.out.println("Price adjusted - Original: " + originalPrice + " VND, Actual: "
                                + actualPrice + " VND");
                    }
                }
            }

            checkInOutDAO.updateCheckOut(bookingId);
            booking.setStatus(BookingStatus.COMPLETED);
            bookingDAO.update(booking);

            PC pc = pcDAO.findById(booking.getPcId());
            if (pc != null) {
                pc.setStatus(PCStatus.AVAILABLE);
                pcDAO.update(pc);
            }
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("Error during check-out: " + e.getMessage(), e);
        }
    }

    @Override
    public double calculateBookingPrice(PC pc, LocalDateTime startTime, LocalDateTime endTime)
            throws BusinessException {
        try {
            if (pc == null) {
                throw new InvalidDataException("Invalid PC");
            }

            if (startTime == null || endTime == null) {
                throw new InvalidDataException("Invalid time");
            }

            PCConfig config = configDAO.findById(pc.getConfigId());
            Zone zone = zoneDAO.findById(pc.getZoneId());

            if (config == null || zone == null) {
                throw new NotFoundException("Cannot calculate price: configuration or zone does not exist");
            }

            long hours = ChronoUnit.HOURS.between(startTime, endTime);
            if (ChronoUnit.MINUTES.between(startTime, endTime) % 60 > 0) {
                hours++;
            }

            double basePrice = config.getPricePerHour();
            double zoneMultiplier = zone.getPriceMultiplier();

            return basePrice * hours * zoneMultiplier;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("Error calculating booking price: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Zone> getAllZones() throws BusinessException {
        try {
            return zoneDAO.findAll();
        } catch (Exception e) {
            throw new BusinessException("Error retrieving zone list: " + e.getMessage(), e);
        }
    }

    @Override
    public List<PC> getPCsByZone(int zoneId) throws BusinessException {
        try {
            if (zoneId <= 0) {
                throw new InvalidDataException("Invalid zone ID");
            }
            return pcDAO.findByZone(zoneId);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("Error retrieving PC list by zone: " + e.getMessage(), e);
        }
    }

    @Override
    public List<PC> getAvailablePCs() throws BusinessException {
        try {
            return pcDAO.findAvailable();
        } catch (Exception e) {
            throw new BusinessException("Error retrieving available PC list: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean isPCAvailable(int pcId, LocalDateTime startTime, LocalDateTime endTime) throws BusinessException {
        try {
            if (pcId <= 0) {
                throw new InvalidDataException("Invalid PC ID");
            }

            if (startTime == null || endTime == null) {
                throw new InvalidDataException("Invalid time");
            }
            return bookingDAO.isPCAvailable(pcId, startTime, endTime);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("Error checking PC availability: " + e.getMessage(), e);
        }
    }
}
