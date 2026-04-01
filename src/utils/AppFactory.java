package utils;

import dao.impl.*;
import dao.interfaces.*;
import service.impl.BookingServiceImpl;
import service.impl.UserServiceImpl;
import service.interfaces.IBookingService;
import service.interfaces.IOrderService;
import service.interfaces.IPaymentService;
import service.interfaces.IUserService;

public class AppFactory {
    private static IUserDAO userDAO;
    private static IBookingDAO bookingDAO;
    private static IUserProfileDAO userProfileDAO;
    private static IWalletDAO walletDAO;
    private static ILoyaltyPointsDAO loyaltyPointsDAO;
    private static IRoleDAO roleDAO;
    private static IPCDAO pcDAO;
    private static IPCConfigDAO pcConfigDAO;
    private static IZoneDAO zoneDAO;
    private static ICheckInOutDAO checkInOutDAO;
    private static IPaymentDAO paymentDAO;
    private static IFBOrderDAO fbOrderDAO;
    private static IComboDAO comboDAO;
    private static IVoucherCodeDAO voucherCodeDAO;
    private static IOTPDAO otpDAO;
    private static IOrderDetailDAO orderDetailDAO;
    private static IFoodItemDAO foodItemDAO;

    private static IUserService userService;
    private static IBookingService bookingService;
    private static IPaymentService paymentService;
    private static IOrderService orderService;

    private static void initializeDAOs() {
        userDAO = new UserDAOImpl();
        userProfileDAO = new UserProfileDAOImpl();
        walletDAO = new WalletDAOImpl();
        loyaltyPointsDAO = new LoyaltyPointsDAOImpl();
        roleDAO = new RoleDAOImpl();
        bookingDAO = new BookingDAOImpl();
        pcDAO = new PCDAOImpl();
        pcConfigDAO = new PCConfigDAOImpl();
        zoneDAO = new ZoneDAOImpl();
        checkInOutDAO = new CheckInOutDAOImpl();
        paymentDAO = new PaymentDAOImpl();
        fbOrderDAO = new FBOrderDAOImpl();
        comboDAO = new ComboDAOImpl();
        voucherCodeDAO = new VoucherCodeDAOImpl();
        otpDAO = new OTPDAOImpl();
        orderDetailDAO = new OrderDetailDAOImpl();
        foodItemDAO = new FoodItemDAOImpl();
    }

    private static void initializeServices() {
        userService = new UserServiceImpl(userDAO, userProfileDAO, walletDAO, loyaltyPointsDAO, roleDAO);
        bookingService = new BookingServiceImpl(bookingDAO, pcDAO, zoneDAO, checkInOutDAO, pcConfigDAO);
        paymentService = new service.impl.PaymentServiceImpl(paymentDAO, bookingDAO, walletDAO, loyaltyPointsDAO);
        orderService = new service.impl.OrderServiceImpl(fbOrderDAO, orderDetailDAO);
    }

    public static IUserService getUserService() {
        if (userService == null) {
            initializeDAOs();
            initializeServices();
        }
        return userService;
    }

    public static IBookingService getBookingService() {
        if (bookingService == null) {
            initializeDAOs();
            initializeServices();
        }
        return bookingService;
    }

    public static IPaymentService getPaymentService() {
        if (paymentService == null) {
            initializeDAOs();
            initializeServices();
        }
        return paymentService;
    }

    public static IOrderService getOrderService() {
        if (orderService == null) {
            initializeDAOs();
            initializeServices();
        }
        return orderService;
    }

    public static IUserDAO getUserDAO() {
        if (userDAO == null) {
            initializeDAOs();
        }
        return userDAO;
    }

    public static ILoyaltyPointsDAO getLoyaltyPointsDAO() {
        if (loyaltyPointsDAO == null) {
            initializeDAOs();
        }
        return loyaltyPointsDAO;
    }

    public static IBookingDAO getBookingDAO() {
        if (bookingDAO == null) {
            initializeDAOs();
        }
        return bookingDAO;
    }

    public static IPaymentDAO getPaymentDAO() {
        if (paymentDAO == null) {
            initializeDAOs();
        }
        return paymentDAO;
    }

    public static IFBOrderDAO getFBOrderDAO() {
        if (fbOrderDAO == null) {
            initializeDAOs();
        }
        return fbOrderDAO;
    }

    public static IPCDAO getPCDAO() {
        if (pcDAO == null) {
            initializeDAOs();
        }
        return pcDAO;
    }

    public static IPCConfigDAO getPCConfigDAO() {
        if (pcConfigDAO == null) {
            initializeDAOs();
        }
        return pcConfigDAO;
    }

    public static IFoodItemDAO getFoodItemDAO() {
        if (foodItemDAO == null) {
            initializeDAOs();
        }
        return foodItemDAO;
    }

    public static IComboDAO getComboDAO() {
        if (comboDAO == null) {
            initializeDAOs();
        }
        return comboDAO;
    }

    public static IVoucherCodeDAO getVoucherCodeDAO() {
        if (voucherCodeDAO == null) {
            initializeDAOs();
        }
        return voucherCodeDAO;
    }

    public static ICheckInOutDAO getCheckInOutDAO() {
        if (checkInOutDAO == null) {
            initializeDAOs();
        }
        return checkInOutDAO;
    }

    public static IWalletDAO getWalletDAO() {
        if (walletDAO == null) {
            initializeDAOs();
        }
        return walletDAO;
    }

    public static IOTPDAO getOTPDAO() {
        if (otpDAO == null) {
            initializeDAOs();
        }
        return otpDAO;
    }
}
