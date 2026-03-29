package exception;

import javax.xml.crypto.Data;

public class DatabaseException extends BusinessException{
    public DatabaseException(String message){
        super(message);
    }

    public DatabaseException(String message, Throwable cause){
        super(message, cause);
    }
}
