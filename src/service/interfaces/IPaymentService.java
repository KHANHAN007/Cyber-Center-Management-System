package service.interfaces;

import model.Payment;
import model.Booking;

public interface IPaymentService {
    Payment payByCash(Booking booking, double amount);

    Payment payByWallet(Booking booking, int walletId, double amount);

    double getWalletBalance(int walletId);

    void topupWallet(int walletId, double amount);

    void addLoyaltyPoints(int userId, double paymentAmount);
}
