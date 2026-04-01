package dao.interfaces;

import model.Wallet;

public interface IWalletDAO {
    Wallet findById(int walletId);

    Wallet findByUserId(int userId);

    void create(Wallet wallet);

    void updateBalance(int walletId, double newBalance);

    void addBalance(int walletId, double amount);

    void deductBalance(int walletId, double amount);

    double getBalance(int walletId);

    void recordTransaction(int walletId, double amount, String type);
}
