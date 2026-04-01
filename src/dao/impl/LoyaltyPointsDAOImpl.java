package dao.impl;

import dao.BaseDAO;
import dao.interfaces.ILoyaltyPointsDAO;
import exception.DatabaseException;
import model.LoyaltyPoints;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoyaltyPointsDAOImpl extends BaseDAO implements ILoyaltyPointsDAO {
    @Override
    public LoyaltyPoints findByUserId(int userId) {
        String sql = "SELECT * FROM loyalty_points WHERE user_id = ?";
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToLoyalty(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding loyalty points: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return null;
    }

    @Override
    public void create(LoyaltyPoints loyalty) {
        String sql = "INSERT INTO loyalty_points (user_id, points) VALUES (?, ?)";
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, loyalty.getUserId());
            ps.setInt(2, loyalty.getPoints());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error creating loyalty points: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }

    @Override
    public void addPoints(int userId, int points) {
        String sql = "UPDATE loyalty_points SET points = points + ? WHERE user_id = ?";
        Connection conn = null;

        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, points);
            ps.setInt(2, userId);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error adding loyalty points: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }

    @Override
    public void deductPoints(int userId, int points) {
        String sql = "UPDATE loyalty_points SET points = points - ? WHERE user_id = ? AND points >= ?";
        Connection conn = null;

        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, points);
            ps.setInt(2, userId);
            ps.setInt(3, points);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error deducting loyalty points: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }

    @Override
    public int getPoints(int userId) {
        String sql = "SELECT points FROM loyalty_points WHERE user_id = ?";
        Connection conn = null;

        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("points");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error getting loyalty points: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return 0;
    }

    private LoyaltyPoints mapResultSetToLoyalty(ResultSet rs) throws SQLException {
        LoyaltyPoints loyalty = new LoyaltyPoints();
        loyalty.setLoyaltyId(rs.getInt("loyalty_id"));
        loyalty.setUserId(rs.getInt("user_id"));
        loyalty.setPoints(rs.getInt("points"));
        return loyalty;
    }
}
