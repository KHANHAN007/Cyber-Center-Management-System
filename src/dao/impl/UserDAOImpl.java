package dao.impl;

import dao.BaseDAO;
import dao.interfaces.IUserDAO;
import enums.UserStatus;
import exception.DatabaseException;
import model.User;
import model.UserProfile;
import utils.CodeGenerator;

import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl extends BaseDAO implements IUserDAO {

    @Override
    public User findById(int userId) {
        String sql = "SELECT * FROM user WHERE user_id = ? AND is_deleted = FALSE";
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding user by ID: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return null;
    }

    @Override
    public User findByUsername(String username) {
        String sql = "SELECT * FROM user WHERE username = ? AND is_deleted = FALSE";
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding user by username: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return null;
    }

    @Override
    public User findByEmail(String email) {
        String sql = "SELECT * FROM user WHERE email = ? AND is_deleted = FALSE";
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding user by email: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "{CALL GetAllUsersWithRole()}";
        Connection conn = null;
        try {
            conn = getConnection();
            CallableStatement cs = conn.prepareCall(sql);
            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                User user = mapResultSetToUser(rs);
                // Set role information from stored procedure result
                model.Role role = new model.Role();
                role.setRoleName(rs.getString("role_name"));
                user.setRole(role);
                users.add(user);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding all users: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return users;
    }

    @Override
    public List<User> findAllActive() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM user WHERE status = ? AND is_deleted = FALSE";
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, UserStatus.ACTIVE.getValue());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding all active users: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return users;
    }

    @Override
    public void create(User user) {
        Connection conn = null;
        try {
            conn = getConnection();
            String userCode = CodeGenerator.generateUniqueCode(CodeGenerator.PREFIX_USER, "user", "user_code", conn);
            String sql = "INSERT INTO user (user_code, username, password, email, role_id, status, is_deleted) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, userCode);
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getEmail());
            ps.setInt(5, user.getRoleId());
            ps.setString(6, user.getStatus().getValue());
            ps.setBoolean(7, false);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error creating user: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }

    @Override
    public void update(User user) {
        String sql = "UPDATE user SET username = ?, password = ?, email = ?, role_id = ?, status = ? WHERE user_id = ?";
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getEmail());
            ps.setInt(4, user.getRoleId());
            ps.setString(5, user.getStatus().getValue());
            ps.setInt(6, user.getUserId());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error updating user: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }

    @Override
    public void delete(int userId) {
        String sql = "DELETE FROM user WHERE user_id = ?";
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting user: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }

    @Override
    public void softDelete(int userId) {
        String sql = "UPDATE user SET is_deleted = TRUE WHERE user_id = ?";
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error soft deleting user: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }

    @Override
    public boolean usernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM user WHERE username = ? AND is_deleted = FALSE";
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error checking username existence: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return false;
    }

    @Override
    public boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM user WHERE email = ? AND is_deleted = FALSE";
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error checking email existence: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return false;
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setUserCode(rs.getString("user_code"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        user.setRoleId(rs.getInt("role_id"));
        user.setStatus(UserStatus.fromValue(rs.getString("status")));
        user.setDeleted(rs.getBoolean("is_deleted"));

        if (rs.getTimestamp("created_at") != null) {
            user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        }
        if (rs.getTimestamp("updated_at") != null) {
            user.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        }

        return user;
    }

    @Override
    public UserProfile getUserWithProfile(int userId) {
        Connection conn = null;
        try {
            conn = getConnection();
            CallableStatement cs = conn.prepareCall("{CALL GetUserWithProfile(?)}");
            cs.setInt(1, userId);
            ResultSet rs = cs.executeQuery();

            UserProfile userProfile = new UserProfile();
            if (rs.next()) {
                userProfile.setProfileId(rs.getInt("profile_id"));
                userProfile.setUserId(userId);
                userProfile.setFullName(rs.getString("full_name"));
                userProfile.setPhone(rs.getString("phone"));
                String addressValue = rs.getString("address");
                if (addressValue != null) {
                    userProfile.setAddress(addressValue);
                }
                userProfile.setLoyaltyPoints(rs.getInt("points"));
            }
            return userProfile;
        } catch (SQLException e) {
            throw new DatabaseException("Error getting user with profile: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }

    @Override
    public User findUserByPhone(String phone) {
        String sql = "SELECT u.* FROM user u " +
                "INNER JOIN user_profile up ON u.user_id = up.user_id " +
                "WHERE up.phone = ? AND u.is_deleted = FALSE";
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, phone);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding user by phone: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return null;
    }
}