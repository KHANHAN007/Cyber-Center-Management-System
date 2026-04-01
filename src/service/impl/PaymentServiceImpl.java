package service.impl;

import dao.interfaces.IBookingDAO;
import dao.interfaces.ILoyaltyPointsDAO;
import dao.interfaces.IPaymentDAO;
import dao.interfaces.IWalletDAO;
import enums.BookingStatus;
import enums.PaymentMethod;
import enums.PaymentStatus;
import exception.InvalidDataException;
import exception.NotFoundException;
import model.Booking;
import model.Payment;
import model.Wallet;
import service.interfaces.IPaymentService;

public class PaymentServiceImpl implements IPaymentService {
    private final IPaymentDAO paymentDAO;
    private final IBookingDAO bookingDAO;
    private final IWalletDAO walletDAO;
    private final ILoyaltyPointsDAO loyaltyPointsDAO;
    private static final double POINTS_CONVERSION_RATE = 10000.0;

    public PaymentServiceImpl(IPaymentDAO paymentDAO, IBookingDAO bookingDAO, IWalletDAO walletDAO,
            ILoyaltyPointsDAO loyaltyPointsDAO) {
        this.paymentDAO = paymentDAO;
        this.bookingDAO = bookingDAO;
        this.walletDAO = walletDAO;
        this.loyaltyPointsDAO = loyaltyPointsDAO;
    }

    @Override
    public Payment payByCash(Booking booking, double amount) {
        if (booking == null) {
            throw new NotFoundException("Booking not found");
        }

        if (Math.abs(booking.getTotalPrice() - amount) > 0.01) {
            throw new InvalidDataException("Payment amount does not match booking price");
        }

        Payment payment = new Payment(booking.getBookingId(), amount, PaymentMethod.CASH);
        payment.setStatus(PaymentStatus.PAID);
        paymentDAO.create(payment);

        bookingDAO.updateStatus(booking.getBookingId(), BookingStatus.CONFIRMED.getValue());
        addLoyaltyPoints(booking.getUserId(), amount);

        return payment;
    }

    @Override
    public Payment payByWallet(Booking booking, int walletId, double amount) {
        if (booking == null) {
            throw new NotFoundException("Booking not found");
        }

        Wallet wallet = walletDAO.findById(walletId);
        if (wallet == null) {
            throw new NotFoundException("Wallet not found with ID: " + walletId);
        }

        if (wallet.getBalance() < amount) {
            throw new InvalidDataException("Insufficient wallet balance");
        }

        if (Math.abs(booking.getTotalPrice() - amount) > 0.01) {
            throw new InvalidDataException("Payment amount does not match booking price");
        }

        walletDAO.deductBalance(walletId, amount);
        walletDAO.recordTransaction(walletId, amount, "PAYMENT");

        Payment payment = new Payment(booking.getBookingId(), amount, PaymentMethod.WALLET);
        payment.setWalletId(walletId);
        payment.setStatus(PaymentStatus.PAID);
        paymentDAO.create(payment);
        bookingDAO.updateStatus(booking.getBookingId(), BookingStatus.CONFIRMED.getValue());
        addLoyaltyPoints(booking.getUserId(), amount);
        return payment;
    }

    @Override
    public double getWalletBalance(int walletId) {
        Wallet wallet = walletDAO.findById(walletId);
        if (wallet == null) {
            throw new NotFoundException("Wallet not found with ID: " + walletId);
        }
        return wallet.getBalance();
    }

    @Override
    public void topupWallet(int walletId, double amount) {
        if (amount <= 0) {
            throw new InvalidDataException("Top-up amount must be greater than 0");
        }

        Wallet wallet = walletDAO.findById(walletId);
        if (wallet == null) {
            throw new NotFoundException("Wallet not found with ID: " + walletId);
        }

        walletDAO.addBalance(walletId, amount);
        walletDAO.recordTransaction(walletId, amount, "TOPUP");
    }

    @Override
    public void addLoyaltyPoints(int userId, double paymentAmount) {
        int points = (int) (paymentAmount / POINTS_CONVERSION_RATE);
        if (points > 0) {
            loyaltyPointsDAO.addPoints(userId, points);
        }
    }
}
