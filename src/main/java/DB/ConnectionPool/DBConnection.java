package DB.ConnectionPool;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    //    static String jdbcUrl = "jdbc:h2:tcp://localhost/~/test-user=sa;DATABASE_TO_UPPER=false";
    static String jdbcUrl = "jdbc:h2:tcp://localhost/~/test";
//    static String jdbcUrl = "jdbc:h2:~/test-user=sa" jdbc:h2:mem:tcp://localhost/`/test;
    static Connection conn = null;

    public static synchronized Connection getConnection() throws SQLException {

        if (conn == null) {
            try {
                Class.forName("org.h2.Driver");
            } catch (ClassNotFoundException e) {
                System.out.println("Could not register driver!");
                e.printStackTrace();
            }
//            conn = DriverManager.getConnection("jdbc:h2:"+"./Database/my","sa", "");
            conn = DriverManager.getConnection(jdbcUrl,"sa" ,"");
        }


        //If connection was closed then retrieve a new connection
        if (conn.isClosed()) {
            System.out.println("Opening new connection...");
            conn = DriverManager.getConnection(jdbcUrl,"sa" ,"");
        }
        return conn;
    }

}
