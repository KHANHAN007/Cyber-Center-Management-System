package utils;

import exception.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class CodeGenerator {
    private static final Map<String, AtomicInteger> counters = new HashMap<>();

    public static final String PREFIX_CUSTOMER = "CU";
    public static final String PREFIX_STAFF = "ST";
    public static final String PREFIX_ADMIN = "AD";
    public static final String PREFIX_USER = "U";
    public static final String PREFIX_PC = "PC";
    public static final String PREFIX_ZONE = "ZN";
    public static final String PREFIX_BOOKING = "BK";
    public static final String PREFIX_ORDER = "ORD";
    public static final String PREFIX_PAYMENT = "PAY";
    public static final String PREFIX_WALLET = "WL";
    public static final String PREFIX_CONFIG = "CFG";
    public static final String PREFIX_ROLE = "RL";
    public static final String PREFIX_FOOD = "FD";
    public static final String PREFIX_CATEGORY = "CAT";
    public static final String PREFIX_VOUCHER = "VC";
    public static final String PREFIX_OTP = "OTP";
    public static final String PREFIX_COMBO = "CMB";

    public static String generateCode(String prefix, int digits) {
        AtomicInteger counter = counters.computeIfAbsent(prefix, k -> new AtomicInteger(1));
        int sequenceNumber = counter.getAndIncrement();
        String sequence = String.format("%0" + digits + "d", sequenceNumber);
        return prefix + sequence;
    }

    public static String generateUniqueCode(String prefix, String tableName, String columnName, int digits,
            Connection connection) {
        String sql = "SELECT MAX(CAST(SUBSTRING(" + columnName + ", " + (prefix.length() + 1)
                + ") AS UNSIGNED)) as max_num FROM " + tableName + " WHERE " + columnName + " LIKE ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, prefix + "%");
            ResultSet rs = ps.executeQuery();
            int nextNum = 1;
            if (rs.next()) {
                int maxNum = rs.getInt("max_num");
                if (maxNum > 0) {
                    nextNum = maxNum + 1;
                }
            }
            rs.close();
            ps.close();
            return String.format(prefix + "%0" + digits + "d", nextNum);
        } catch (SQLException e) {
            throw new DatabaseException("Error generating unique code: " + e.getMessage(), e);
        }
    }

    public static String generateUniqueCode(String prefix, String tableName, String columnName, Connection connection) {
        return generateUniqueCode(prefix, tableName, columnName, 3, connection);
    }

    public static String generateRoleCode(String roleName) {
        String prefix;
        switch (roleName.toUpperCase()) {
            case "ADMIN":
                prefix = PREFIX_ADMIN;
                break;
            case "STAFF":
                prefix = PREFIX_STAFF;
                break;
            case "CUSTOMER":
                prefix = PREFIX_CUSTOMER;
                break;
            default:
                throw new IllegalArgumentException("Unknown role: " + roleName);
        }
        try {
            Connection conn = DBConnection.getConnection();
            return generateUniqueCode(prefix, "role", "role_code", conn);
        } catch (Exception e) {
            throw new DatabaseException("Error generating role code: " + e.getMessage(), e);
        }
    }

    public static String generatePCCode() {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            return generateUniqueCode(PREFIX_PC, "pc", "pc_code", conn);
        } catch (Exception e) {
            throw new DatabaseException("Error generating PC code: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                DBConnection.closeConnection(conn);
            }
        }
    }

    public static String generateBookingCode() {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            return generateUniqueCode(PREFIX_BOOKING, "booking", "booking_code", conn);
        } catch (Exception e) {
            throw new DatabaseException("Error generating booking code: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                DBConnection.closeConnection(conn);
            }
        }
    }

    public static String generateOrderCode() {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            return generateUniqueCode(PREFIX_ORDER, "fb_order", "order_code", conn);
        } catch (Exception e) {
            throw new DatabaseException("Error generating order code: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                DBConnection.closeConnection(conn);
            }
        }
    }

    public static String generateZoneCode() {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            return generateUniqueCode(PREFIX_ZONE, "zone", "zone_code", conn);
        } catch (Exception e) {
            throw new DatabaseException("Error generating zone code: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                DBConnection.closeConnection(conn);
            }
        }
    }

    public static String generateWalletCode() {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            return generateUniqueCode(PREFIX_WALLET, "wallet", "wallet_code", conn);
        } catch (Exception e) {
            throw new DatabaseException("Error generating wallet code: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                DBConnection.closeConnection(conn);
            }
        }
    }

    public static String generateConfigCode() {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            return generateUniqueCode(PREFIX_CONFIG, "pc_config", "config_code", conn);
        } catch (Exception e) {
            throw new DatabaseException("Error generating config code: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                DBConnection.closeConnection(conn);
            }
        }
    }

    public static String generateVoucherCode() {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            return generateUniqueCode(PREFIX_VOUCHER, "voucher", "voucher_code", conn);
        } catch (Exception e) {
            throw new DatabaseException("Error generating voucher code: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                DBConnection.closeConnection(conn);
            }
        }
    }

    public static String generateOTPCode() {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            return generateUniqueCode(PREFIX_OTP, "otp", "otp_code", conn);
        } catch (Exception e) {
            throw new DatabaseException("Error generating OTP code: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                DBConnection.closeConnection(conn);
            }
        }
    }

    public static String generateComboCode() {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            return generateUniqueCode(PREFIX_COMBO, "combo", "combo_code", conn);
        } catch (Exception e) {
            throw new DatabaseException("Error generating combo code: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                DBConnection.closeConnection(conn);
            }
        }
    }

    public static String generateFoodCode() {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            return generateUniqueCode(PREFIX_FOOD, "food_item", "item_code", conn);
        } catch (Exception e) {
            throw new DatabaseException("Error generating food code: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                DBConnection.closeConnection(conn);
            }
        }
    }

    public static void resetCounter(String prefix) {
        counters.put(prefix, new AtomicInteger(1));
    }

    public static void resetAllCounters() {
        counters.clear();
    }

    public static int getCurrentCounter(String prefix) {
        return counters.getOrDefault(prefix, new AtomicInteger(0)).get();
    }
}
