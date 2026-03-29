package dao.impl;

import dao.BaseDAO;
import dao.interfaces.IUserProfileDAO;
import exception.DatabaseException;
import model.UserProfile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserProfileDAOImpl extends BaseDAO implements IUserProfileDAO {

    @Override
    public UserProfile findByUserId(int userId) {
        String sql = "SELECT * FROM user_profile WHERE user_id = ?";
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToUserProfile(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding user profile by user ID: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return null;
    }

    @Override
    public void create(UserProfile userProfile) {
        String sql = "INSERT INTO user_profile (user_id, full_name, phone, address) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userProfile.getUserId());
            ps.setString(2, userProfile.getFullName());
            ps.setString(3, userProfile.getPhone());
            ps.setString(4, userProfile.getAddress());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error creating user profile: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }

    @Override
    public void update(UserProfile userProfile) {
        String sql = "UPDATE user_profile SET full_name = ?, phone = ?, address = ? WHERE user_id = ?";
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, userProfile.getFullName());
            ps.setString(2, userProfile.getPhone());
            ps.setString(3, userProfile.getAddress());
            ps.setInt(4, userProfile.getUserId());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error updating user profile: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }

    private UserProfile mapResultSetToUserProfile(ResultSet rs) throws SQLException {
        UserProfile userProfile = new UserProfile();
        userProfile.setProfileId(rs.getInt("profile_id"));
        userProfile.setUserId(rs.getInt("user_id"));
        userProfile.setFullName(rs.getString("full_name"));
        userProfile.setPhone(rs.getString("phone"));
        userProfile.setAddress(rs.getString("address"));
        return userProfile;
    }
}
