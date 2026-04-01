package dao.impl;

import dao.BaseDAO;
import dao.interfaces.ICheckInOutDAO;
import exception.DatabaseException;
import model.CheckInOut;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class CheckInOutDAOImpl extends BaseDAO implements ICheckInOutDAO {

    @Override
    public CheckInOut findByBookingId(int bookingId) {
        String sql = "CALL sp_GetCheckInByBookingId(?)";
        Connection conn = null;
        try {
            conn = getConnection();
            CallableStatement cs = conn.prepareCall(sql);
            cs.setInt(1, bookingId);
            ResultSet rs = cs.executeQuery();
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
    public CheckInOut getCheckInOutByBookingId(int bookingId) {
        return findByBookingId(bookingId);
    }

    @Override
    public void createCheckIn(CheckInOut checkInOut) {
        String sql = "CALL sp_CreateCheckIn(?, ?, ?, ?)";
        Connection conn = null;

        try {
            conn = getConnection();
            CallableStatement cs = conn.prepareCall(sql);
            cs.setInt(1, checkInOut.getBookingId());
            cs.setTimestamp(2, Timestamp.valueOf(checkInOut.getCheckInTime()));
            cs.registerOutParameter(3, Types.INTEGER);
            cs.registerOutParameter(4, Types.VARCHAR);

            cs.execute();

            int result = cs.getInt(3);
            String message = cs.getString(4);

            if (result <= 0) {

                System.out.println("Check-in info: " + message);
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error creating check-in: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }

    @Override
    public void updateCheckOut(int bookingId) {
        String sql = "CALL sp_UpdateCheckOut(?, ?, ?, ?)";
        Connection conn = null;

        try {
            conn = getConnection();
            CallableStatement cs = conn.prepareCall(sql);
            cs.setInt(1, bookingId);
            cs.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            cs.registerOutParameter(3, Types.INTEGER);
            cs.registerOutParameter(4, Types.VARCHAR);

            cs.execute();

            int result = cs.getInt(3);
            if (result <= 0) {
                throw new DatabaseException(cs.getString(4));
            }

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

    @Override
    public List<Map<String, Object>> findAllActiveSessions() {
        String sql = "CALL sp_GetAllActiveSessions()";
        Connection conn = null;
        List<Map<String, Object>> sessions = new ArrayList<>();

        try {
            conn = getConnection();
            CallableStatement cs = conn.prepareCall(sql);
            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                Map<String, Object> session = new LinkedHashMap<>();
                session.put("id", rs.getInt("id"));
                session.put("bookingId", rs.getInt("booking_id"));
                session.put("bookingCode", rs.getString("booking_code"));
                session.put("userId", rs.getInt("user_id"));
                session.put("username", rs.getString("username"));
                session.put("email", rs.getString("email"));
                session.put("fullName", rs.getString("full_name"));
                session.put("phone", rs.getString("phone"));
                session.put("pcId", rs.getInt("pc_id"));
                session.put("pcName", rs.getString("pc_name"));
                session.put("zoneName", rs.getString("zone_name"));
                session.put("totalPrice", rs.getDouble("total_price"));
                session.put("bookingStatus", rs.getString("booking_status"));
                session.put("checkInTime",
                        rs.getTimestamp("check_in_time") != null ? rs.getTimestamp("check_in_time").toLocalDateTime()
                                : null);
                session.put("checkOutTime",
                        rs.getTimestamp("check_out_time") != null ? rs.getTimestamp("check_out_time").toLocalDateTime()
                                : null);

                sessions.add(session);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding active sessions: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }

        return sessions;
    }

    @Override
    public Map<String, Object> findSessionDetailByBookingId(int bookingId) {
        String sql = "CALL sp_GetSessionDetailByBookingId(?)";
        Connection conn = null;

        try {
            conn = getConnection();
            CallableStatement cs = conn.prepareCall(sql);
            cs.setInt(1, bookingId);
            ResultSet rs = cs.executeQuery();

            if (rs.next()) {
                Map<String, Object> session = new LinkedHashMap<>();
                session.put("id", rs.getInt("id"));
                session.put("bookingId", rs.getInt("booking_id"));
                session.put("bookingCode", rs.getString("booking_code"));
                session.put("userId", rs.getInt("user_id"));
                session.put("username", rs.getString("username"));
                session.put("email", rs.getString("email"));
                session.put("fullName", rs.getString("full_name"));
                session.put("phone", rs.getString("phone"));
                session.put("address", rs.getString("address"));
                session.put("pcId", rs.getInt("pc_id"));
                session.put("pcName", rs.getString("pc_name"));
                session.put("zoneName", rs.getString("zone_name"));
                session.put("cpu", rs.getString("cpu"));
                session.put("ram", rs.getInt("ram"));
                session.put("gpu", rs.getString("gpu"));
                session.put("pricePerHour", rs.getDouble("price_per_hour"));
                session.put("totalPrice", rs.getDouble("total_price"));
                session.put("bookingStatus", rs.getString("booking_status"));
                session.put("checkInTime",
                        rs.getTimestamp("check_in_time") != null ? rs.getTimestamp("check_in_time").toLocalDateTime()
                                : null);
                session.put("checkOutTime",
                        rs.getTimestamp("check_out_time") != null ? rs.getTimestamp("check_out_time").toLocalDateTime()
                                : null);

                return session;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding session detail: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }

        return null;
    }

    @Override
    public List<Map<String, Object>> findAllCheckInOut() {
        String sql = "CALL sp_GetAllCheckInOut()";
        Connection conn = null;
        List<Map<String, Object>> records = new ArrayList<>();

        try {
            conn = getConnection();
            CallableStatement cs = conn.prepareCall(sql);
            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                Map<String, Object> record = new LinkedHashMap<>();
                record.put("id", rs.getInt("id"));
                record.put("bookingId", rs.getInt("booking_id"));
                record.put("bookingCode", rs.getString("booking_code"));
                record.put("userId", rs.getInt("user_id"));
                record.put("username", rs.getString("username"));
                record.put("email", rs.getString("email"));
                record.put("fullName", rs.getString("full_name"));
                record.put("phone", rs.getString("phone"));
                record.put("pcId", rs.getInt("pc_id"));
                record.put("pcName", rs.getString("pc_name"));
                record.put("zoneName", rs.getString("zone_name"));
                record.put("totalPrice", rs.getDouble("total_price"));
                record.put("bookingStatus", rs.getString("booking_status"));
                record.put("userStatus", rs.getString("user_status"));
                record.put("pcStatus", rs.getString("pc_status"));
                record.put("sessionStatus", rs.getString("session_status"));
                record.put("checkInTime",
                        rs.getTimestamp("check_in_time") != null ? rs.getTimestamp("check_in_time").toLocalDateTime()
                                : null);
                record.put("checkOutTime",
                        rs.getTimestamp("check_out_time") != null ? rs.getTimestamp("check_out_time").toLocalDateTime()
                                : null);

                records.add(record);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding all check-in/out records: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }

        return records;
    }
}
