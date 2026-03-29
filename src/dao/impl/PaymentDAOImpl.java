package dao.impl;

import dao.BaseDAO;
import dao.interfaces.IPaymentDAO;
import enums.PaymentMethod;
import enums.PaymentStatus;
import exception.DatabaseException;
import model.Payment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAOImpl extends BaseDAO implements IPaymentDAO {

    @Override
    public Payment findById(int paymentId) {
        String sql = "SELECT * FROM payment WHERE payment_id = ?";
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, paymentId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToPayment(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding payment by ID: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return null;
    }

    @Override
    public Payment findByBookingId(int bookingId) {
        String sql = "SELECT * FROM payment WHERE booking_id = ?";
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToPayment(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding payment by booking ID: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return null;
    }

    @Override
    public List<Payment> findAll() {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM payment";
        Connection conn = null;

        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                payments.add(mapResultSetToPayment(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding all payments: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return payments;
    }

    @Override
    public void create(Payment payment) {
        String paymentCode = "PAY" + System.currentTimeMillis();
        String sql = "INSERT INTO payment (payment_code, booking_id, wallet_id, amount, method, status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;

        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, paymentCode);
            ps.setInt(2, payment.getBookingId());

            if (payment.getWalletId() != null) {
                ps.setInt(3, payment.getWalletId());
            } else {
                ps.setNull(3, Types.INTEGER);
            }

            ps.setDouble(4, payment.getAmount());
            ps.setString(5, payment.getMethod().getValue());
            ps.setString(6, payment.getStatus().getValue());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error creating payment: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }

    @Override
    public void update(Payment payment) {
        String sql = "UPDATE payment SET booking_id = ?, wallet_id = ?, amount = ?, method = ?, status = ? WHERE payment_id = ?";
        Connection conn = null;

        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, payment.getBookingId());

            if (payment.getWalletId() != null) {
                ps.setInt(2, payment.getWalletId());
            } else {
                ps.setNull(2, Types.INTEGER);
            }

            ps.setDouble(3, payment.getAmount());
            ps.setString(4, payment.getMethod().getValue());
            ps.setString(5, payment.getStatus().getValue());
            ps.setInt(6, payment.getPaymentId());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error updating payment: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }

    @Override
    public void updateStatus(int paymentId, String status) {
        String sql = "UPDATE payment SET status = ? WHERE payment_id = ?";
        Connection conn = null;

        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, PaymentStatus.fromValue(status).getValue());
            ps.setInt(2, paymentId);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error updating payment status: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }

    private Payment mapResultSetToPayment(ResultSet rs) throws SQLException {
        Payment payment = new Payment();
        payment.setPaymentId(rs.getInt("payment_id"));
        payment.setPaymentCode(rs.getString("payment_code"));
        payment.setBookingId(rs.getInt("booking_id"));
        payment.setWalletId(rs.getObject("wallet_id") != null ? rs.getInt("wallet_id") : null);
        payment.setAmount(rs.getDouble("amount"));
        payment.setMethod(PaymentMethod.fromValue(rs.getString("method")));
        payment.setStatus(PaymentStatus.fromValue(rs.getString("status")));
        return payment;
    }
}
