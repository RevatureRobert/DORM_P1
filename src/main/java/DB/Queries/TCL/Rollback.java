package DB.Queries.TCL;

import DB.ConnectionPool.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Rollback {
    static StringBuilder sql = new StringBuilder();
    static PreparedStatement preparedStatement;

    private static void buildRollBack(){
        sql.append("Rollback");
    };
    private static  void buildRollBack(String savepointName){
        sql.append("Rollback to " + savepointName);
    }

    public static int executeCommit() {
        buildRollBack();
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

    public static int executeCommit(String savePointName) {
        buildRollBack(savePointName);
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