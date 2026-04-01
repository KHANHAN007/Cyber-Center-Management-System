package dao.impl;

import dao.interfaces.IOrderDetailDAO;
import model.OrderDetail;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailDAOImpl implements IOrderDetailDAO {

    @Override
    public List<OrderDetail> findByOrderId(int orderId) {
        List<OrderDetail> orderDetails = new ArrayList<>();
        String sql = "SELECT * FROM order_detail WHERE order_id = ?";

        try (Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, orderId);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                OrderDetail detail = new OrderDetail();
                detail.setDetailId(rs.getInt("detail_id"));
                detail.setOrderId(rs.getInt("order_id"));
                detail.setItemId(rs.getInt("item_id"));
                detail.setComboId(rs.getInt("combo_id"));
                detail.setSizeId(rs.getInt("size_id"));
                detail.setQuantity(rs.getInt("quantity"));
                detail.setPrice(rs.getDouble("price"));
                orderDetails.add(detail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderDetails;
    }

    @Override
    public void create(OrderDetail orderDetail) {
        String sql = "INSERT INTO order_detail (order_id, item_id, combo_id, size_id, quantity, price) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, orderDetail.getOrderId());


            if (orderDetail.getItemId() != null && orderDetail.getItemId() > 0) {
                statement.setInt(2, orderDetail.getItemId());
            } else {
                statement.setNull(2, java.sql.Types.INTEGER);
            }


            if (orderDetail.getComboId() != null && orderDetail.getComboId() > 0) {
                statement.setInt(3, orderDetail.getComboId());
            } else {
                statement.setNull(3, java.sql.Types.INTEGER);
            }


            if (orderDetail.getSizeId() != null && orderDetail.getSizeId() > 0) {
                statement.setInt(4, orderDetail.getSizeId());
            } else {
                statement.setNull(4, java.sql.Types.INTEGER);
            }

            statement.setInt(5, orderDetail.getQuantity());
            statement.setDouble(6, orderDetail.getPrice());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error creating order detail: " + e.getMessage(), e);
        }
    }
}
