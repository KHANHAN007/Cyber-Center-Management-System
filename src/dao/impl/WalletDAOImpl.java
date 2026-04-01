package dao.impl;

import dao.BaseDAO;
import dao.interfaces.IWalletDAO;
import model.Wallet;
import exception.DatabaseException;
import utils.CodeGenerator;

import java.sql.*;

public class WalletDAOImpl extends BaseDAO implements IWalletDAO {

    @Override
    public Wallet findByUserId(int userId) {
        String sql = "SELECT * FROM wallet WHERE user_id = ?";
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToWallet(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding wallet by user ID: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return null;
    }

    @Override
    public Wallet findById(int walletId) {
        String sql = "SELECT * FROM wallet WHERE wallet_id = ?";
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, walletId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToWallet(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding wallet by ID: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return null;
    }

    @Override
    public void create(Wallet wallet) {
        Connection conn = null;
        try {
            conn = getConnection();
            String walletCode = CodeGenerator.generateUniqueCode(CodeGenerator.PREFIX_WALLET, "wallet", "wallet_code",
                    conn);
            String sql = "INSERT INTO wallet (wallet_code, user_id, balance) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, walletCode);
            ps.setInt(2, wallet.getUserId());
            ps.setDouble(3, wallet.getBalance());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error creating wallet: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }

    @Override
    public void updateBalance(int walletId, double newBalance) {
        String sql = "UPDATE wallet SET balance = ? WHERE wallet_id = ?";
        Connection conn = null;

        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDouble(1, newBalance);
            ps.setInt(2, walletId);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error updating wallet balance: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }

    @Override
    public void addBalance(int walletId, double amount) {
        String sql = "UPDATE wallet SET balance = balance + ? WHERE wallet_id = ?";
        Connection conn = null;

        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDouble(1, amount);
            ps.setInt(2, walletId);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error adding balance to wallet: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }

    @Override
    public void deductBalance(int walletId, double amount) {
        String sql = "UPDATE wallet SET balance = balance - ? WHERE wallet_id = ? AND balance >= ?";
        Connection conn = null;

        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDouble(1, amount);
            ps.setInt(2, walletId);
            ps.setDouble(3, amount);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error deducting balance from wallet: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }

    @Override
    public double getBalance(int walletId) {
        String sql = "SELECT balance FROM wallet WHERE wallet_id = ?";
        Connection conn = null;

        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, walletId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getDouble("balance");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error getting wallet balance: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return 0.0;
    }

    @Override
    public void recordTransaction(int walletId, double amount, String type) {
        String sql = "INSERT INTO wallet_transaction (wallet_id, amount, type, created_at) VALUES (?, ?, ?, NOW())";
        Connection conn = null;

        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, walletId);
            ps.setDouble(2, amount);
            ps.setString(3, type);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error recording wallet transaction: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }

    private Wallet mapResultSetToWallet(ResultSet rs) throws SQLException {
        Wallet wallet = new Wallet();
        wallet.setWalletId(rs.getInt("wallet_id"));
        wallet.setWalletCode(rs.getString("wallet_code"));
        wallet.setUserId(rs.getInt("user_id"));
        wallet.setBalance(rs.getDouble("balance"));
        return wallet;
    }
}
