package dao.impl;

import dao.BaseDAO;
import dao.interfaces.IBookingDAO;
import enums.BookingStatus;
import exception.DatabaseException;
import model.Booking;
import utils.CodeGenerator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookingDAOImpl extends BaseDAO implements IBookingDAO {
    @Override
    public Booking findById(int bookingId) {
        String sql = "SELECT * FROM booking WHERE booking_id = ?";
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToBooking(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding booking by ID: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return null;
    }

    @Override
    public List<Booking> findByUserId(int userId) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM booking WHERE user_id = ?";
        Connection conn = null;

        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                bookings.add(mapResultSetToBooking(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding bookings by user ID: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return bookings;
    }

    @Override
    public List<Booking> findAll() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM booking";
        Connection conn = null;

        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                bookings.add(mapResultSetToBooking(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding all bookings: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return bookings;
    }

    @Override
    public List<Booking> findPendingBookings() {
        List<Booking> bookings = new ArrayList<>();
        Connection conn = null;

        try {
            conn = getConnection();
            CallableStatement cs = conn.prepareCall("{CALL GetPendingBookings()}");
            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                bookings.add(mapResultSetToBooking(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding pending bookings: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return bookings;
    }

    @Override
    public Booking findDetailWithJoin(int bookingId) {
        Connection conn = null;
        try {
            conn = getConnection();
            CallableStatement cs = conn.prepareCall("{CALL GetBookingDetailWithJoin(?)}");
            cs.setInt(1, bookingId);
            ResultSet rs = cs.executeQuery();

            if (rs.next()) {
                return mapResultSetToBooking(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding booking detail with join: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return null;
    }

    @Override
    public void create(Booking booking) {
        String sql = "INSERT INTO booking (booking_code, user_id, pc_id, start_time, end_time, status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;

        try {
            conn = getConnection();
            String bookingCode = CodeGenerator.generateUniqueCode(CodeGenerator.PREFIX_BOOKING, "booking",
                    "booking_code", conn);

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, bookingCode);
            ps.setInt(2, booking.getUserId());
            ps.setInt(3, booking.getPcId());
            ps.setTimestamp(4, Timestamp.valueOf(booking.getStartTime()));
            ps.setTimestamp(5, Timestamp.valueOf(booking.getEndTime()));
            ps.setString(6, booking.getStatus().getValue());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error creating booking: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }

    @Override
    public void update(Booking booking) {
        String sql = "UPDATE booking SET user_id = ?, pc_id = ?, start_time = ?, end_time = ?, status = ? WHERE booking_id = ?";
        Connection conn = null;

        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, booking.getUserId());
            ps.setInt(2, booking.getPcId());
            ps.setTimestamp(3, Timestamp.valueOf(booking.getStartTime()));
            ps.setTimestamp(4, Timestamp.valueOf(booking.getEndTime()));
            ps.setString(5, booking.getStatus().getValue());
            ps.setInt(6, booking.getBookingId());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error updating booking: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }

    @Override
    public void updateStatus(int bookingId, String status) {
        String sql = "UPDATE booking SET status = ? WHERE booking_id = ?";
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, BookingStatus.fromValue(status).getValue());
            ps.setInt(2, bookingId);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error updating booking status: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }

    @Override
    public boolean isPCAvailable(int pcId, LocalDateTime startTime, LocalDateTime endTime) {
        String sql = "SELECT COUNT(*) FROM booking WHERE pc_id = ? AND status IN ('CONFIRMED', 'IN_PROGRESS') " +
                "AND ((start_time < ? AND end_time > ?) OR (start_time < ? AND end_time > ?))";
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, pcId);
            ps.setTimestamp(2, Timestamp.valueOf(endTime));
            ps.setTimestamp(3, Timestamp.valueOf(startTime));
            ps.setTimestamp(4, Timestamp.valueOf(endTime));
            ps.setTimestamp(5, Timestamp.valueOf(startTime));

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error checking PC availability: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return false;
    }

    @Override
    public List<Booking> findByPCIdAndDateRange(int pcId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM booking WHERE pc_id = ? AND start_time >= ? AND end_time <= ?";
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, pcId);
            ps.setTimestamp(2, Timestamp.valueOf(startDate));
            ps.setTimestamp(3, Timestamp.valueOf(endDate));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                bookings.add(mapResultSetToBooking(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding bookings by PC ID and date range: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return bookings;
    }

    private Booking mapResultSetToBooking(ResultSet rs) throws SQLException {
        Booking booking = new Booking();
        booking.setBookingId(rs.getInt("booking_id"));
        booking.setBookingCode(rs.getString("booking_code"));
        booking.setUserId(rs.getInt("user_id"));
        booking.setPcId(rs.getInt("pc_id"));
        booking.setSlotId(rs.getInt("slot_id"));
        if (rs.getTimestamp("start_time") != null) {
            booking.setStartTime(rs.getTimestamp("start_time").toLocalDateTime());
        }
        if (rs.getTimestamp("end_time") != null) {
            booking.setEndTime(rs.getTimestamp("end_time").toLocalDateTime());
        }
        if (rs.getObject("total_price") != null) {
            booking.setTotalPrice(rs.getDouble("total_price"));
        }
        booking.setVoucherId(rs.getObject("voucher_id") != null ? rs.getInt("voucher_id") : null);
        booking.setDiscountAmount(rs.getObject("discount_amount") != null ? rs.getDouble("discount_amount") : null);
        booking.setStatus(BookingStatus.fromValue(rs.getString("status")));
        if (rs.getTimestamp("created_at") != null) {
            booking.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        }
        return booking;
    }
}
