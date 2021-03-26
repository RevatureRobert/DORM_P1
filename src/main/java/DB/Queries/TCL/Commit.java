package DB.Queries.TCL;

import Models.Database;
import Threads.MakeThreadPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Commit {
    static StringBuilder sql = new StringBuilder();
    static PreparedStatement preparedStatement;
    private static int queryResult;



    public static int executeCommit(Connection conn) {
        sql = new StringBuilder("Commit");
        try {
            preparedStatement = conn.prepareStatement(sql.toString());
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }




    }


}
