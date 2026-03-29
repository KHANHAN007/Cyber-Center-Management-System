package dao.interfaces;

import model.OrderDetail;

import java.util.List;

public interface IOrderDetailDAO {
    List<OrderDetail> findByOrderId(int orderId);
    void create(OrderDetail orderDetail);
}
