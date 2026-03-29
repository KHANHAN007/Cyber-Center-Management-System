package dao;

import exception.DatabaseException;
import utils.DBConnection;

import java.sql.Connection;

public abstract class BaseDAO {
    protected Connection getConnection() throws DatabaseException{
        try{
            return DBConnection.getConnection();
        }catch (Exception e){
            throw new DatabaseException("Failed to get database connection", e);
        }
    }

    protected void closeConnection(Connection connection){
        if(connection != null){
            try{
                DBConnection.closeConnection(connection);
            }catch (Exception e){
                System.err.println("Failed to close database connection: " + e.getMessage());
            }
        }
    }
}
