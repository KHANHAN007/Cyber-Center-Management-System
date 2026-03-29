package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/PRJ_CYBER_JAVA";
    private static final String USER = "root";
    private static final String PASSWORD = "anlinh.99";

    static {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException e){
            System.err.println("Failed to load MySQL JDBC driver: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException{
        try{
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connection established successfully.");
            return conn;
        }catch (SQLException e){
            System.err.println("Failed to establish database connection: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public static void closeConnection(Connection conn){
        if(conn != null){
            try{
                conn.close();
                System.out.println("Database connection closed successfully.");
            }catch (SQLException e){
                System.err.println("Failed to close database connection: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static void testConnection(){
        try{
            Connection conn = getConnection();
            if(conn != null){
                System.out.println("Connection test successful!");
                closeConnection(conn);
            }
        }catch (SQLException e){
            System.err.println("Connection test failed: " + e.getMessage());}
    }
}
