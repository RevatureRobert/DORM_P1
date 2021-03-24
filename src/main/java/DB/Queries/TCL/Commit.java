package DB.Queries.TCL;

import DB.ConnectionPool.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Commit {
    static StringBuilder sql = new StringBuilder();
    static PreparedStatement preparedStatement;

    private static void buildCommit(){
        sql.append("Commit");
    };

    public static int executeCommit() {
        buildCommit();
        System.out.println(sql);
        Connection connection = null;
        try {
            connection = DBConnection.getConnection();
            preparedStatement = connection.prepareStatement(sql.toString());
             return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }


    }


}
