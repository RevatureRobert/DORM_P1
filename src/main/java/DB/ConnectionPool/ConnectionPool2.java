package DB.ConnectionPool;

import java.sql.Connection;
import java.util.Collection;

//This interface should have the values needed in a basic Db connection
public interface ConnectionPool2 {
    Connection getConnection();
    boolean releaseConnection(Connection connection);
    String getUrl();
    String getUser();
    String getPassword();

}
