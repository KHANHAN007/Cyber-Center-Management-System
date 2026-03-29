package dao.interfaces;

import model.FBOrder;

import java.util.List;

public interface IFBOrderDAO {
    FBOrder findById(int orderId);
    FBOrder findByBookingId(int bookingId);
    List<FBOrder> findAll();
    void create(FBOrder order);
    void update(FBOrder order);
    void updateStatus(int orderId, String status);
}
