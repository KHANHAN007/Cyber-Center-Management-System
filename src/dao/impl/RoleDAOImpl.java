package dao.impl;

import dao.BaseDAO;
import dao.interfaces.IRoleDAO;
import exception.DatabaseException;
import model.Role;
import utils.CodeGenerator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleDAOImpl extends BaseDAO implements IRoleDAO {
    @Override
    public Role findById(int roleId) {
        String sql = "SELECT * FROM role WHERE role_id = ? AND is_deleted = FALSE";
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, roleId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToRole(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding role by ID: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return null;
    }

    @Override
    public Role findByCode(String roleCode) {
        String sql = "SELECT * FROM role WHERE role_code = ?";
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, roleCode);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToRole(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding role by code: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return null;
    }

    @Override
    public List<Role> findAll() {
        List<Role> roles = new ArrayList<>();
        String sql = "SELECT * FROM role WHERE is_deleted = FALSE";
        Connection conn = null;

        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                roles.add(mapResultSetToRole(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding all roles: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return roles;
    }

    @Override
    public void create(Role role) {
        String sql = "INSERT INTO role (role_code, role_name) VALUES (?, ?)";
        Connection conn = null;

        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, CodeGenerator.generateRoleCode(role.getRoleName()));
            ps.setString(2, role.getRoleName());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error creating role: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }

    private Role mapResultSetToRole(ResultSet rs) throws SQLException {
        Role role = new Role();
        role.setRoleId(rs.getInt("role_id"));
        role.setRoleCode(rs.getString("role_code"));
        role.setRoleName(rs.getString("role_name"));
        return role;
    }
}
