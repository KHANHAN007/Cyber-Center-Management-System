package dao.impl;

import dao.BaseDAO;
import dao.interfaces.IPCConfigDAO;
import exception.DatabaseException;
import model.PCConfig;
import utils.CodeGenerator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PCConfigDAOImpl extends BaseDAO implements IPCConfigDAO {
    @Override
    public PCConfig findById(int configId) {
        String sql = "SELECT * FROM pc_config WHERE config_id = ?";
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, configId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToConfig(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding PC config by ID: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return null;
    }

    @Override
    public List<PCConfig> findAll() {
        List<PCConfig> configs = new ArrayList<>();
        String sql = "SELECT * FROM pc_config";
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                configs.add(mapResultSetToConfig(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding all PC configs: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return configs;
    }

    @Override
    public void create(PCConfig config) {
        String configCode = CodeGenerator.generateConfigCode();

        String sql = "INSERT INTO pc_config (config_code, cpu, ram, gpu, price_per_hour) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;

        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, configCode);
            ps.setString(2, config.getCpu());
            ps.setInt(3, config.getRam());
            ps.setString(4, config.getGpu());
            ps.setDouble(5, config.getPricePerHour());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error creating PC config: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }

    @Override
    public void update(PCConfig config) {
        String sql = "UPDATE pc_config SET cpu = ?, ram = ?, gpu = ?, price_per_hour = ? WHERE config_id = ?";
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, config.getCpu());
            ps.setInt(2, config.getRam());
            ps.setString(3, config.getGpu());
            ps.setDouble(4, config.getPricePerHour());
            ps.setInt(5, config.getConfigId());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error updating PC config: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }

    private PCConfig mapResultSetToConfig(ResultSet rs) throws SQLException {
        PCConfig config = new PCConfig();
        config.setConfigId(rs.getInt("config_id"));
        config.setConfigCode(rs.getString("config_code"));
        config.setCpu(rs.getString("cpu"));
        config.setRam(rs.getInt("ram"));
        config.setGpu(rs.getString("gpu"));
        config.setPricePerHour(rs.getDouble("price_per_hour"));
        return config;
    }
}
