package model;

public class Wallet {
    private int walletId;
    private String walletCode;
    private int userId;
    private double balance;

    public Wallet() {
    }

    public Wallet(int userId) {
        this.userId = userId;
        this.balance = 0;
    }

    public int getWalletId() {
        return walletId;
    }

    public String getWalletCode() {
        return walletCode;
    }

    public int getUserId() {
        return userId;
    }

    public double getBalance() {
        return balance;
    }

    public void setWalletId(int walletId) {
        this.walletId = walletId;
    }

    public void setWalletCode(String walletCode) {
        this.walletCode = walletCode;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "walletId=" + walletId +
                ", walletCode='" + walletCode + '\'' +
                ", userId=" + userId +
                ", balance=" + balance +
                '}';
    }
}
