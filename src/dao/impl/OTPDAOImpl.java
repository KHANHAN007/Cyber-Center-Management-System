package dao.impl;

import dao.BaseDAO;
import dao.interfaces.IOTPDAO;
import model.OTP;
import enums.OTPStatus;
import exception.DatabaseException;

import java.sql.*;

public class OTPDAOImpl extends BaseDAO implements IOTPDAO {
    @Override
    public OTP findById(int otpId) {
        String sql = "SELECT * FROM otp WHERE otp_id = ?";
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, otpId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToOTP(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding OTP by ID: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return null;
    }

    @Override
    public OTP findByBookingId(int bookingId) {
        String sql = "SELECT * FROM otp WHERE booking_id = ? ORDER BY otp_id DESC LIMIT 1";
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToOTP(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding OTP: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return null;
    }

    @Override
    public void create(OTP otp) {
        String sql = "INSERT INTO otp (booking_id, code, expiry_time, status) VALUES (?, ?, ?, ?)";
        Connection conn = null;

        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, otp.getBookingId());
            ps.setString(2, otp.getCode());
            ps.setTimestamp(3, Timestamp.valueOf(otp.getExpiryTime()));
            ps.setString(4, otp.getStatus().getValue());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error creating OTP: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }

    @Override
    public void update(OTP otp) {
        String sql = "UPDATE otp SET booking_id = ?, code = ?, expiry_time = ?, status = ? WHERE otp_id = ?";
        Connection conn = null;

        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, otp.getBookingId());
            ps.setString(2, otp.getCode());
            ps.setTimestamp(3, Timestamp.valueOf(otp.getExpiryTime()));
            ps.setString(4, otp.getStatus().getValue());
            ps.setInt(5, otp.getOtpId());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error updating OTP: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }

    @Override
    public void updateStatus(int otpId, String status) {
        String sql = "UPDATE otp SET status = ? WHERE otp_id = ?";
        Connection conn = null;

        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, OTPStatus.fromValue(status).getValue());
            ps.setInt(2, otpId);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error updating OTP status: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }

    private OTP mapResultSetToOTP(ResultSet rs) throws SQLException {
        OTP otp = new OTP();
        otp.setOtpId(rs.getInt("otp_id"));
        otp.setBookingId(rs.getInt("booking_id"));
        otp.setCode(rs.getString("code"));

        if (rs.getTimestamp("expiry_time") != null) {
            otp.setExpiryTime(rs.getTimestamp("expiry_time").toLocalDateTime());
        }

        otp.setStatus(OTPStatus.fromValue(rs.getString("status")));
        return otp;
    }
}
