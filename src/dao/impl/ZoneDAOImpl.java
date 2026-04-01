package dao.impl;

import dao.BaseDAO;
import dao.interfaces.IZoneDAO;
import exception.DatabaseException;
import model.Zone;
import utils.CodeGenerator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ZoneDAOImpl extends BaseDAO implements IZoneDAO {

    @Override
    public Zone findById(int zoneId) {
        String sql = "SELECT * FROM zone WHERE zone_id = ?";
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, zoneId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToZone(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding zone by ID: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return null;
    }

    @Override
    public List<Zone> findAll() {
        List<Zone> zones = new ArrayList<>();
        String sql = "SELECT * FROM zone";
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                zones.add(mapResultSetToZone(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding all zones: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return zones;
    }

    @Override
    public void create(Zone zone) {
        String sql = "INSERT INTO zone (zone_code, zone_name, price_multiplier) VALUES (?, ?, ?)";
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, CodeGenerator.generateZoneCode());
            ps.setString(2, zone.getZoneName());
            ps.setDouble(3, zone.getPriceMultiplier());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error creating zone: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }

    @Override
    public void update(Zone zone) {
        String sql = "UPDATE zone SET zone_name = ?, price_multiplier = ? WHERE zone_id = ?";
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, zone.getZoneName());
            ps.setDouble(2, zone.getPriceMultiplier());
            ps.setInt(3, zone.getZoneId());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error updating zone: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }

    private Zone mapResultSetToZone(ResultSet rs) throws SQLException {
        Zone zone = new Zone();
        zone.setZoneId(rs.getInt("zone_id"));
        zone.setZoneCode(rs.getString("zone_code"));
        zone.setZoneName(rs.getString("zone_name"));
        zone.setPriceMultiplier(rs.getDouble("price_multiplier"));
        return zone;
    }
}
