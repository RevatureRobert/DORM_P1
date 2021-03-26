package DB.Queries.TCL;

import Models.Database;
import Threads.MakeThreadPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class SavePoint {


    private static StringBuilder sql;
    private static PreparedStatement preparedStatement;
    private static int queryResult;

    public static int executeRollBack(String savePointName) {

        Future future = MakeThreadPool.executorService.submit((Callable) () -> {
            System.out.println(Thread.currentThread().getId());
            sql = new StringBuilder();
            Connection conn = Database.accessPool();
            preparedStatement = conn.prepareStatement(sql.toString());
            int rs = preparedStatement.executeUpdate();

            Database.releaseConn(conn);

            return rs;
        });

        try {
            queryResult = (int) future.get();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return -1;
        }

        if (queryResult > 0) {
            System.out.println(queryResult);
            return queryResult;
        }


        return -1;

    }

}
