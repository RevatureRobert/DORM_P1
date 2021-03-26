package DB.Queries.TCL;

import Models.Database;
import Threads.MakeThreadPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Rollback {
    static StringBuilder sql = new StringBuilder();
    static PreparedStatement preparedStatement;
    private static int queryResult;

    private static void buildRollBack() {
        sql.append("Rollback");
    }

    ;

    private static void buildRollBack(String savepointName) {
        sql.append("Rollback to " + savepointName);
    }

    public static int executeRollBack(Connection conn) {
        sql = new StringBuilder("Rollback");
        try {
            preparedStatement = conn.prepareStatement(sql.toString());
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }


    }

    public static int executeRollBack(String savePointName, Connection conn) {
        sql = new StringBuilder("Rollback " + savePointName);
        try {
            preparedStatement = conn.prepareStatement(sql.toString());
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }


    }
}