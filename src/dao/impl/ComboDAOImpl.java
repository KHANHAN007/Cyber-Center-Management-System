package dao.impl;

import dao.BaseDAO;
import dao.interfaces.IComboDAO;
import enums.ComboStatus;
import exception.DatabaseException;
import model.Combo;
import utils.CodeGenerator;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ComboDAOImpl extends BaseDAO implements IComboDAO {
    @Override
    public Combo findById(int comboId) {
        String sql = "SELECT * FROM combo WHERE combo_id = ?";
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, comboId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToCombo(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding combo by ID: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return null;
    }

    @Override
    public List<Combo> findAll() {
        return findAll(false);
    }

    @Override
    public List<Combo> findAll(boolean showDeleted) {
        List<Combo> combos = new ArrayList<>();
        String sql = "CALL GetCombosWithItems(?)";
        Connection conn = null;

        try {
            conn = getConnection();
            CallableStatement cs = conn.prepareCall(sql);
            cs.setBoolean(1, showDeleted);
            ResultSet rs = cs.executeQuery();

            Combo currentCombo = null;
            StringBuilder itemsBuilder = new StringBuilder();

            while (rs.next()) {
                int comboId = rs.getInt("combo_id");

                // Nếu là combo khác, lưu combo cũ và tạo combo mới
                if (currentCombo == null || currentCombo.getComboId() != comboId) {
                    if (currentCombo != null && itemsBuilder.length() > 0) {
                        currentCombo.setContainedItems(itemsBuilder.toString());
                    }

                    currentCombo = new Combo();
                    currentCombo.setComboId(comboId);
                    currentCombo.setComboCode(rs.getString("combo_code"));
                    currentCombo.setName(rs.getString("combo_name"));
                    currentCombo.setPrice(rs.getBigDecimal("combo_price"));
                    currentCombo.setStatus(ComboStatus.fromValue(rs.getString("status")));
                    currentCombo.setComboItems(rs.getString("combo_items"));
                    currentCombo.setDeleted(rs.getBoolean("is_deleted"));
                    combos.add(currentCombo);
                    itemsBuilder = new StringBuilder();
                }

                // Thêm item vào contained_items (kiểm tra item_code để xác định có item hay
                // không)
                if (rs.getString("item_code") != null) {
                    if (itemsBuilder.length() > 0) {
                        itemsBuilder.append(", ");
                    }
                    String itemName = rs.getString("item_name");
                    int quantity = rs.getInt("quantity");
                    itemsBuilder.append(itemName)
                            .append(" x").append(quantity);
                }
            }

            // Lưu combo cuối cùng
            if (currentCombo != null && itemsBuilder.length() > 0) {
                currentCombo.setContainedItems(itemsBuilder.toString());
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error finding all combos: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return combos;
    }

    @Override
    public void create(Combo combo) {
        String comboCode = CodeGenerator.generateComboCode();
        String insertComboSql = "INSERT INTO combo (combo_code, name, price, combo_items, status) VALUES (?, ?, ?, ?, ?)";
        String insertComboItemSql = "INSERT INTO combo_item (combo_id, item_id, quantity) VALUES (?, ?, ?)";
        Connection conn = null;

        try {
            conn = getConnection();
            conn.setAutoCommit(false); // Bắt đầu transaction

            // 1. Lưu combo
            PreparedStatement ps = conn.prepareStatement(insertComboSql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, comboCode);
            ps.setString(2, combo.getName());
            ps.setBigDecimal(3, combo.getPrice());
            ps.setString(4, combo.getComboItems() != null ? combo.getComboItems() : "");
            ps.setString(5, combo.getStatus() != null ? combo.getStatus().getValue() : "ACTIVE");
            ps.executeUpdate();

            // Lấy combo_id vừa tạo
            ResultSet rs = ps.getGeneratedKeys();
            int comboId = 0;
            if (rs.next()) {
                comboId = rs.getInt(1);
            }

            // 2. Parse và lưu combo_items từ string
            if (combo.getComboItems() != null && !combo.getComboItems().isEmpty()) {
                String[] items = combo.getComboItems().split(",");
                for (String item : items) {
                    String[] parts = item.trim().split("x");
                    if (parts.length == 2) {
                        String itemName = parts[0].trim();
                        try {
                            int quantity = Integer.parseInt(parts[1].trim());

                            // Tìm item_id từ tên item
                            String findItemSql = "SELECT item_id FROM food_item WHERE name = ? AND is_deleted = FALSE";
                            PreparedStatement findPs = conn.prepareStatement(findItemSql);
                            findPs.setString(1, itemName);
                            ResultSet itemRs = findPs.executeQuery();

                            if (itemRs.next()) {
                                int itemId = itemRs.getInt("item_id");

                                // Lưu combo_item
                                PreparedStatement itemPs = conn.prepareStatement(insertComboItemSql);
                                itemPs.setInt(1, comboId);
                                itemPs.setInt(2, itemId);
                                itemPs.setInt(3, quantity);
                                itemPs.executeUpdate();
                            }
                            findPs.close();
                        } catch (NumberFormatException e) {
                            // Bỏ qua nếu quantity không phải số
                        }
                    }
                }
            }

            conn.commit(); // Commit transaction
        } catch (SQLException e) {
            try {
                if (conn != null)
                    conn.rollback(); // Rollback nếu có lỗi
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            throw new DatabaseException("Error creating combo: " + e.getMessage(), e);
        } finally {
            try {
                if (conn != null)
                    conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            closeConnection(conn);
        }
    }

    @Override
    public void update(Combo combo) {
        String sql = "UPDATE combo SET name = ?, price = ? WHERE combo_id = ?";
        Connection conn = null;

        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, combo.getName());
            ps.setBigDecimal(2, combo.getPrice());
            ps.setInt(3, combo.getComboId());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error updating combo: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }

    private Combo mapResultSetToCombo(ResultSet rs) throws SQLException {
        Combo combo = new Combo();
        combo.setComboId(rs.getInt("combo_id"));
        combo.setComboCode(rs.getString("combo_code"));
        combo.setName(rs.getString("name"));
        combo.setPrice(rs.getBigDecimal("price"));
        return combo;
    }
}
