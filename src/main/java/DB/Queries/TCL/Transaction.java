package DB.Queries.TCL;

import DB.ConnectionPool.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


//Have them start a transaction do whatever they want and then just do whatever they want
// when to set an isolation level and too what
// i guess i need it when there a write
// so either insert , update , and or create , and delete (still need to be implemented )
public class Transaction {

    static StringBuilder sql = new StringBuilder();
    static PreparedStatement preparedStatement;
    Connection connection;

    {
        try {
            connection = DBConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void buildTransaction() {
        sql.append("Commit");
    }

    public void start() {

        try {
            connection.setAutoCommit(false);
            return;
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }


    }

    public void end() {
        Commit.executeCommit();
        try {
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void add(){}

//    public static int executeCommit() {
//        buildTransaction();
//        System.out.println(sql);
//        Connection connection = null;
//        try {
//            connection = DBConnection.getConnection();
//            preparedStatement = connection.prepareStatement(sql.toString());
//            return preparedStatement.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return -1;
//        }
//    }

}
