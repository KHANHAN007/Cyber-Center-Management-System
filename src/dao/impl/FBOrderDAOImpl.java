package dao.impl;

import dao.BaseDAO;
import dao.interfaces.IFBOrderDAO;
import enums.OrderStatus;
import exception.DatabaseException;
import model.FBOrder;
import utils.CodeGenerator;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FBOrderDAOImpl extends BaseDAO implements IFBOrderDAO {

    @Override
    public FBOrder findById(int orderId) {
        String sql = "SELECT * FROM fb_order WHERE order_id = ?";
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToOrder(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding order by ID: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return null;
    }

    @Override
    public FBOrder findByBookingId(int bookingId) {
        String sql = "SELECT * FROM fb_order WHERE booking_id = ?";
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToOrder(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding order by booking ID: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return null;
    }

    @Override
    public List<FBOrder> findAll() {
        List<FBOrder> orders = new ArrayList<>();
        String sql = "SELECT * FROM fb_order";
        Connection conn = null;

        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                orders.add(mapResultSetToOrder(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding all orders: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return orders;
    }

    @Override
    public void create(FBOrder order) {
        Connection dbConn = null;
        try {
            dbConn = DBConnection.getConnection();
            String orderCode = CodeGenerator.generateUniqueCode(CodeGenerator.PREFIX_ORDER, "fb_order", "order_code",
                    dbConn);
            String sql = "INSERT INTO fb_order (order_code, booking_id, total_price, status) VALUES (?, ?, ?, ?)";
            Connection conn = null;

            try {
                conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setString(1, orderCode);


                if (order.getBookingId() != null && order.getBookingId() > 0) {
                    ps.setInt(2, order.getBookingId());
                } else {
                    ps.setNull(2, java.sql.Types.INTEGER);
                }

                ps.setDouble(3, order.getTotalPrice());
                ps.setString(4, order.getStatus().getValue());

                ps.executeUpdate();


                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    order.setOrderId(generatedKeys.getInt(1));
                }
            } catch (SQLException e) {
                throw new DatabaseException("Error creating order: " + e.getMessage(), e);
            } finally {
                closeConnection(conn);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error generating order code: " + e.getMessage(), e);
        } finally {
            if (dbConn != null) {
                DBConnection.closeConnection(dbConn);
            }
        }
    }

    @Override
    public void update(FBOrder order) {
        String sql = "UPDATE fb_order SET booking_id = ?, total_price = ?, status = ? WHERE order_id = ?";
        Connection conn = null;

        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, order.getBookingId());
            ps.setDouble(2, order.getTotalPrice());
            ps.setString(3, order.getStatus().getValue());
            ps.setInt(4, order.getOrderId());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error updating order: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }

    @Override
    public void updateStatus(int orderId, String status) {
        String sql = "UPDATE fb_order SET status = ? WHERE order_id = ?";
        Connection conn = null;

        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, OrderStatus.fromValue(status).getValue());
            ps.setInt(2, orderId);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error updating order status: " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }

    private FBOrder mapResultSetToOrder(ResultSet rs) throws SQLException {
        FBOrder order = new FBOrder();
        order.setOrderId(rs.getInt("order_id"));
        order.setOrderCode(rs.getString("order_code"));
        order.setBookingId(rs.getInt("booking_id"));
        order.setTotalPrice(rs.getDouble("total_price"));
        order.setStatus(OrderStatus.fromValue(rs.getString("status")));

        if (rs.getTimestamp("created_at") != null) {
            order.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        }

        return order;
    }
}
