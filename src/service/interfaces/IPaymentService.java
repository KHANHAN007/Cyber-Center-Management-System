package service.interfaces;

import model.Payment;

public interface IPaymentService {
    Payment payByCash(int bookingId, double amount);
    Payment payByWallet(int bookingId, int walletId, double amount);
    double getWalletBalance(int walletId);
    void topupWallet(int walletId, double amount);
    void addLoyaltyPoints(int userId, double paymentAmount);
}
