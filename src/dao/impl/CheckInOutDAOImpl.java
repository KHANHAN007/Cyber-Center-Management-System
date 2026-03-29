package dao.impl;

import dao.BaseDAO;
import dao.interfaces.ICheckInOutDAO;
import exception.DatabaseException;
import model.CheckInOut;

import java.sql.*;
import java.time.LocalDateTime;

public class CheckInOutDAOImpl extends BaseDAO implements ICheckInOutDAO {

    @Override
    public CheckInOut findByBookingId(int bookingId) {
        String sql = "SELECT * FROM check_in_out WHERE booking_id = ?";
        Connection conn = null;
        try{
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToCheckInOut(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding check-in/out: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return null;
    }

    @Override
    public void createCheckIn(CheckInOut checkInOut) {
        String sql = "INSERT INTO check_in_out (booking_id, check_in_time) VALUES (?, ?)";
        Connection conn = null;

        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, checkInOut.getBookingId());
            ps.setTimestamp(2, Timestamp.valueOf(checkInOut.getCheckInTime()));

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error creating check-in: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }

    @Override
    public void updateCheckOut(int bookingId) {
        String sql = "UPDATE check_in_out SET check_out_time = ? WHERE booking_id = ?";
        Connection conn = null;

        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(2, bookingId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error updating check-out: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }

    private CheckInOut mapResultSetToCheckInOut(ResultSet rs) throws SQLException {
        CheckInOut checkInOut = new CheckInOut();
        checkInOut.setId(rs.getInt("id"));
        checkInOut.setBookingId(rs.getInt("booking_id"));
        if (rs.getTimestamp("check_in_time") != null) {
            checkInOut.setCheckInTime(rs.getTimestamp("check_in_time").toLocalDateTime());
        }
        if (rs.getTimestamp("check_out_time") != null) {
            checkInOut.setCheckOutTime(rs.getTimestamp("check_out_time").toLocalDateTime());
        }
        return checkInOut;
    }
}
