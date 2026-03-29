package dao.impl;

import dao.BaseDAO;
import dao.interfaces.IPCDAO;
import enums.PCStatus;
import exception.DatabaseException;
import model.PC;
import model.PCDetail;
import utils.CodeGenerator;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PCDAOImpl extends BaseDAO implements IPCDAO {
    @Override
    public PC findById(int pcId) {
        String sql = "SELECT * FROM pc WHERE pc_id = ? AND is_deleted = FALSE";
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, pcId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToPC(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding PC by ID: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return null;
    }

    @Override
    public PCDetail findDetailWithConfigById(int pcId) {
        String sql = "{CALL GetPCDetailWithConfig(?)}";
        Connection conn = null;
        try {
            conn = getConnection();
            CallableStatement cs = conn.prepareCall(sql);
            cs.setInt(1, pcId);
            ResultSet rs = cs.executeQuery();
            if (rs.next()) {
                return mapResultSetToPCDetail(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding PC detail with config: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return null;
    }

    @Override
    public List<PC> findAll() {
        List<PC> pcs = new ArrayList<>();
        String sql = "SELECT * FROM pc WHERE is_deleted = FALSE";
        Connection conn = null;

        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                pcs.add(mapResultSetToPC(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding all PCs: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return pcs;
    }

    @Override
    public List<PC> findByZone(int zoneId) {
        List<PC> pcs = new ArrayList<>();
        String sql = "SELECT * FROM pc WHERE zone_id = ? AND is_deleted = FALSE";
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, zoneId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                pcs.add(mapResultSetToPC(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding PCs by zone: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return pcs;
    }

    @Override
    public List<PC> findAvailable() {
        List<PC> pcs = new ArrayList<>();
        String sql = "SELECT * FROM pc WHERE status = ? AND is_deleted = FALSE";
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, PCStatus.AVAILABLE.getValue());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                pcs.add(mapResultSetToPC(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding available PCs: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return pcs;
    }

    @Override
    public void create(PC pc) {
        String pcCode = CodeGenerator.generatePCCode();

        String sql = "INSERT INTO pc (pc_code, pc_name, zone_id, config_id, status) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, pcCode);
            ps.setString(2, pc.getPcName());
            ps.setInt(3, pc.getZoneId());
            ps.setInt(4, pc.getConfigId());
            ps.setString(5, pc.getStatus().getValue());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error creating PC: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }

    @Override
    public void update(PC pc) {
        String sql = "UPDATE pc SET pc_name = ?, zone_id = ?, config_id = ?, status = ? WHERE pc_id = ?";
        Connection conn = null;

        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, pc.getPcName());
            ps.setInt(2, pc.getZoneId());
            ps.setInt(3, pc.getConfigId());
            ps.setString(4, pc.getStatus().getValue());
            ps.setInt(5, pc.getPcId());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error updating PC: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }

    @Override
    public void updateStatus(int pcId, String status) {
        String sql = "UPDATE pc SET status = ? WHERE pc_id = ?";
        Connection conn = null;

        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, PCStatus.fromValue(status).getValue());
            ps.setInt(2, pcId);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error updating PC status: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }

    @Override
    public void softDelete(int pcId) {
        String sql = "UPDATE pc SET is_deleted = TRUE WHERE pc_id = ?";
        Connection conn = null;

        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, pcId);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting PC: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }

    private PC mapResultSetToPC(ResultSet rs) throws SQLException {
        PC pc = new PC();
        pc.setPcId(rs.getInt("pc_id"));
        pc.setPcCode(rs.getString("pc_code"));
        pc.setPcName(rs.getString("pc_name"));
        pc.setZoneId(rs.getInt("zone_id"));
        pc.setConfigId(rs.getInt("config_id"));
        pc.setStatus(PCStatus.fromValue(rs.getString("status")));
        pc.setDeleted(rs.getBoolean("is_deleted"));
        return pc;
    }

    private PCDetail mapResultSetToPCDetail(ResultSet rs) throws SQLException {
        PCDetail pcDetail = new PCDetail();
        pcDetail.setPcId(rs.getInt("pc_id"));
        pcDetail.setPcCode(rs.getString("pc_code"));
        pcDetail.setPcName(rs.getString("pc_name"));
        pcDetail.setZoneId(rs.getInt("zone_id"));
        pcDetail.setStatus(PCStatus.fromValue(rs.getString("status")));
        pcDetail.setConfigId(rs.getInt("config_id"));
        pcDetail.setConfigCode(rs.getString("config_code"));
        pcDetail.setCpu(rs.getString("cpu"));
        pcDetail.setRam(rs.getInt("ram"));
        pcDetail.setGpu(rs.getString("gpu"));
        pcDetail.setPricePerHour(rs.getDouble("price_per_hour"));
        return pcDetail;
    }
}
