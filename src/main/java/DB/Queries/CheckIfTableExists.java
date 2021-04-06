package DB.Queries;

import Models.Database;
import Threads.MakeThreadPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class CheckIfTableExists {

    static StringBuilder sql = new StringBuilder();
    static PreparedStatement preparedStatement;
    static Connection connection = null;
    private ResultSet queryResult;

    // Not sure i need to specify the table name
    private void buildQuery(String tableName) {
        sql.append("Select * from " + tableName);
    }

    public boolean executeCheckExists(String tableName) {

        Future future = MakeThreadPool.executorService.submit((Callable) () -> {
            System.out.println(Thread.currentThread().getId());
            sql = new StringBuilder();
            buildQuery(tableName);
            Connection conn = Database.accessPool();
            preparedStatement = conn.prepareStatement(sql.toString());
            ResultSet rs = preparedStatement.executeQuery();

            Database.releaseConn(conn);


            return rs;
        });
        try {
            queryResult = (ResultSet) future.get();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }

        try {
            if (queryResult.next()) {
                System.out.println(queryResult);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }


        return false;
    }
}
