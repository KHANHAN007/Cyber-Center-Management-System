package dao.impl;

import dao.BaseDAO;
import dao.interfaces.IFoodItemDAO;
import enums.ItemStatus;
import exception.DatabaseException;
import model.FoodItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.CallableStatement;
import java.util.ArrayList;
import java.util.List;

public class FoodItemDAOImpl extends BaseDAO implements IFoodItemDAO {
    @Override
    public FoodItem findById(int itemId) {
        String sql = "SELECT * FROM food_item WHERE item_id = ? AND status = ?";
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, itemId);
            ps.setString(2, ItemStatus.ACTIVE.getValue());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToFoodItem(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding food item by ID: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return null;
    }

    @Override
    public List<FoodItem> findByCategory(int categoryId) {
        List<FoodItem> items = new ArrayList<>();
        String sql = "SELECT * FROM food_item WHERE category_id = ? AND status = ?";
        Connection conn = null;

        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, categoryId);
            ps.setString(2, ItemStatus.ACTIVE.getValue());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                items.add(mapResultSetToFoodItem(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding food items by category: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return items;
    }

    @Override
    public List<FoodItem> findAll() {
        List<FoodItem> items = new ArrayList<>();
        String sql = "SELECT * FROM food_item WHERE status = ?";
        Connection conn = null;

        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, ItemStatus.ACTIVE.getValue());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                items.add(mapResultSetToFoodItem(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding all food items: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return items;
    }

    @Override
    public List<FoodItem> findAllWithCategoryAndPrices() {
        return findAllWithCategoryAndPrices(false);
    }

    @Override
    public List<FoodItem> findAllWithCategoryAndPrices(boolean showDeleted) {
        List<FoodItem> items = new ArrayList<>();
        String sql = "CALL GetFoodItemsWithCategoryAndToppings(?)";
        Connection conn = null;

        try {
            conn = getConnection();
            CallableStatement cs = conn.prepareCall(sql);
            cs.setBoolean(1, showDeleted);
            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                FoodItem item = new FoodItem();
                item.setItemId(rs.getInt("item_id"));
                item.setItemCode(rs.getString("item_code"));
                item.setName(rs.getString("item_name"));
                item.setCategoryId(rs.getInt("category_id"));
                item.setCategoryName(rs.getString("category_name"));
                item.setDescription(rs.getString("description"));
                item.setStatus(ItemStatus.fromValue(rs.getString("status")));
                item.setMinPrice(rs.getDouble("min_price"));
                item.setMaxPrice(rs.getDouble("max_price"));
                item.setAvailableSizes(rs.getString("available_sizes"));
                item.setAvailableToppings(rs.getString("available_toppings"));
                item.setDeleted(rs.getBoolean("is_deleted"));
                items.add(item);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding food items with category and prices: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return items;
    }

    @Override
    public void create(FoodItem foodItem) {
        String sql = "INSERT INTO food_item (item_code, name, category_id, description, min_price, max_price, available_sizes, available_toppings, status, is_deleted) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;

        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, foodItem.getItemCode());
            ps.setString(2, foodItem.getName());
            ps.setInt(3, foodItem.getCategoryId());
            ps.setString(4, foodItem.getDescription());
            ps.setDouble(5, foodItem.getMinPrice() != null ? foodItem.getMinPrice() : 0);
            ps.setDouble(6, foodItem.getMaxPrice() != null ? foodItem.getMaxPrice() : 0);
            ps.setString(7, foodItem.getAvailableSizes());
            ps.setString(8, foodItem.getAvailableToppings() != null ? foodItem.getAvailableToppings() : "");
            ps.setString(9, foodItem.getStatus().getValue());
            ps.setBoolean(10, foodItem.isDeleted());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error creating food item: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }

    @Override
    public void update(FoodItem item) {
        String sql = "UPDATE food_item SET name = ?, category_id = ?, description = ?, min_price = ?, max_price = ?, available_sizes = ?, available_toppings = ?, status = ?, is_deleted = ? WHERE item_id = ?";
        Connection conn = null;

        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, item.getName());
            ps.setInt(2, item.getCategoryId());
            ps.setString(3, item.getDescription());
            ps.setDouble(4, item.getMinPrice() != null ? item.getMinPrice() : 0);
            ps.setDouble(5, item.getMaxPrice() != null ? item.getMaxPrice() : 0);
            ps.setString(6, item.getAvailableSizes());
            ps.setString(7, item.getAvailableToppings() != null ? item.getAvailableToppings() : "");
            ps.setString(8, item.getStatus().getValue());
            ps.setBoolean(9, item.isDeleted());
            ps.setInt(10, item.getItemId());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error updating food item: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }

    private FoodItem mapResultSetToFoodItem(ResultSet rs) throws SQLException {
        FoodItem item = new FoodItem();
        item.setItemId(rs.getInt("item_id"));
        item.setItemCode(rs.getString("item_code"));
        item.setName(rs.getString("name"));
        item.setCategoryId(rs.getInt("category_id"));
        item.setDescription(rs.getString("description"));
        item.setMinPrice(rs.getDouble("min_price"));
        item.setMaxPrice(rs.getDouble("max_price"));
        item.setAvailableSizes(rs.getString("available_sizes"));
        item.setAvailableToppings(rs.getString("available_toppings"));
        item.setStatus(ItemStatus.fromValue(rs.getString("status")));
        item.setDeleted(rs.getBoolean("is_deleted"));
        return item;
    }
}
