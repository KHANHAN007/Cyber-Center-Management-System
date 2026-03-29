package dao.impl;

import dao.BaseDAO;
import dao.interfaces.IVoucherCodeDAO;
import enums.VoucherStatus;
import enums.DiscountType;
import exception.DatabaseException;
import model.VoucherCode;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VoucherCodeDAOImpl extends BaseDAO implements IVoucherCodeDAO {
    @Override
    public VoucherCode findByCode(String code) {
        String sql = "SELECT * FROM voucher_code WHERE code = ?";
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToVoucher(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding voucher by code: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return null;
    }

    @Override
    public VoucherCode findById(int voucherId) {
        String sql = "SELECT * FROM voucher_code WHERE voucher_id = ?";
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, voucherId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToVoucher(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding voucher by ID: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return null;
    }

    @Override
    public List<VoucherCode> findAll() {
        String sql = "SELECT * FROM voucher_code ORDER BY created_at DESC";
        Connection conn = null;
        List<VoucherCode> vouchers = new ArrayList<>();
        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                vouchers.add(mapResultSetToVoucher(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding all vouchers: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return vouchers;
    }

    @Override
    public List<VoucherCode> findAllActive() {
        String sql = "SELECT * FROM voucher_code WHERE status = 'ACTIVE' AND (expires_at IS NULL OR expires_at > NOW())";
        Connection conn = null;
        List<VoucherCode> vouchers = new ArrayList<>();
        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                vouchers.add(mapResultSetToVoucher(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding active vouchers: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return vouchers;
    }

    @Override
    public void create(VoucherCode voucherCode) {
        String sql = "INSERT INTO voucher_code (code, description, discount_type, discount_value, " +
                "min_order_value, max_uses, used_count, status, created_at, expires_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;

        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, voucherCode.getCode());
            ps.setString(2, voucherCode.getDescription());
            ps.setString(3, voucherCode.getDiscountType() != null ? voucherCode.getDiscountType().name() : "FIXED");
            ps.setBigDecimal(4, voucherCode.getDiscountValue());
            ps.setBigDecimal(5, voucherCode.getMinOrderValue());
            ps.setInt(6, voucherCode.getMaxUses());
            ps.setInt(7, voucherCode.getUsedCount());
            ps.setString(8, voucherCode.getStatus() != null ? voucherCode.getStatus().name() : "ACTIVE");
            ps.setObject(9, voucherCode.getCreatedAt());
            ps.setObject(10, voucherCode.getExpiresAt());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error creating voucher code: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }

    @Override
    public void update(VoucherCode voucherCode) {
        String sql = "UPDATE voucher_code SET code = ?, description = ?, discount_type = ?, " +
                "discount_value = ?, min_order_value = ?, max_uses = ?, used_count = ?, " +
                "status = ?, expires_at = ? WHERE voucher_id = ?";
        Connection conn = null;

        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, voucherCode.getCode());
            ps.setString(2, voucherCode.getDescription());
            ps.setString(3, voucherCode.getDiscountType() != null ? voucherCode.getDiscountType().name() : "FIXED");
            ps.setBigDecimal(4, voucherCode.getDiscountValue());
            ps.setBigDecimal(5, voucherCode.getMinOrderValue());
            ps.setInt(6, voucherCode.getMaxUses());
            ps.setInt(7, voucherCode.getUsedCount());
            ps.setString(8, voucherCode.getStatus() != null ? voucherCode.getStatus().name() : "ACTIVE");
            ps.setObject(9, voucherCode.getExpiresAt());
            ps.setInt(10, voucherCode.getVoucherId());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error updating voucher code: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }

    @Override
    public void delete(int voucherId) {
        String sql = "UPDATE voucher_code SET status = 'INACTIVE' WHERE voucher_id = ?";
        Connection conn = null;

        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, voucherId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting voucher code: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }

    public void incrementUsageCount(int voucherId) {
        String sql = "UPDATE voucher_code SET used_count = used_count + 1 WHERE voucher_id = ?";
        Connection conn = null;

        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, voucherId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error incrementing voucher usage: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }

    private VoucherCode mapResultSetToVoucher(ResultSet rs) throws SQLException {
        VoucherCode voucher = new VoucherCode();
        voucher.setVoucherId(rs.getInt("voucher_id"));
        voucher.setCode(rs.getString("code"));
        voucher.setDescription(rs.getString("description"));
        voucher.setPromotionId(rs.getInt("promotion_id"));

        // Discount info
        String discountTypeStr = rs.getString("discount_type");
        voucher.setDiscountType(
                discountTypeStr != null ? DiscountType.fromString(discountTypeStr) : DiscountType.FIXED);
        voucher.setDiscountValue(rs.getBigDecimal("discount_value"));
        voucher.setMinOrderValue(rs.getBigDecimal("min_order_value"));

        // Usage tracking
        voucher.setMaxUses(rs.getInt("max_uses"));
        voucher.setUsedCount(rs.getInt("used_count"));

        // Status
        String statusStr = rs.getString("status");
        voucher.setStatus(statusStr != null ? VoucherStatus.fromString(statusStr) : VoucherStatus.ACTIVE);

        // Timestamps
        Timestamp createdTs = rs.getTimestamp("created_at");
        if (createdTs != null) {
            voucher.setCreatedAt(createdTs.toLocalDateTime());
        }

        Timestamp expiresTs = rs.getTimestamp("expires_at");
        if (expiresTs != null) {
            voucher.setExpiresAt(expiresTs.toLocalDateTime());
        }

        return voucher;
    }
}
