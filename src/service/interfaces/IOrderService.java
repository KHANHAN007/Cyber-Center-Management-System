package service.interfaces;

import model.FBOrder;
import model.OrderDetail;

import java.util.List;

public interface IOrderService {
    FBOrder createOrder(int bookingId);
    FBOrder getOrderById(int orderId);
    void addItemToOrder(int orderId, int itemId, int sizeId, int quantity, double price);
    List<OrderDetail> getOrderDetails(int orderId);
    double calculateOrderTotal(int orderId);
    void updateOrderStatus(int orderId, String status);
}
