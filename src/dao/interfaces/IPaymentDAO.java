package dao.interfaces;

import model.Payment;

import java.util.List;

public interface IPaymentDAO {
    Payment findById(int paymentId);

    Payment findByBookingId(int bookingId);

    List<Payment> findAll();

    void create(Payment payment);

    void update(Payment payment);

    void updateStatus(int paymentId, String status);
}
