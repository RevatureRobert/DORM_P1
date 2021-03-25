package DB.Queries;

import Models.Database;
import Models.TableModel;
import Threads.MakeThreadPool;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class DropQuery {

    static StringBuilder sql = new StringBuilder();
    static PreparedStatement preparedStatement;
    static Future query;
    static int queryResult;

    public boolean executeDrop(TableModel table) {
        Future future = MakeThreadPool.executorService.submit((Callable) () -> {
            System.out.println(Thread.currentThread().getId());
            sql = new StringBuilder();
            buildDrop(table);
            Connection conn = Database.getaccessPool();
            preparedStatement = conn.prepareStatement(sql.toString());
            int rs = preparedStatement.executeUpdate();
            Database.realseConn(conn);

            return rs;
        });

        try {
            queryResult = (int) future.get();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }

        if (queryResult > 0) {
            System.out.println(queryResult);
            return true;
        }


        return false;

    }

    public boolean executeDrop(String tableName) {
        Future future = MakeThreadPool.executorService.submit((Callable) () -> {
            System.out.println(Thread.currentThread().getId());
            sql = new StringBuilder();
            buildDrop(tableName);
            Connection conn = Database.getaccessPool();
            preparedStatement = conn.prepareStatement(sql.toString());
            int rs = preparedStatement.executeUpdate();
            Database.realseConn(conn);

            return rs;
        });

        try {
            queryResult = (int) future.get();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }

        if (queryResult > 0) {
            System.out.println(queryResult);
            return true;
        }

        System.out.println("Reached the bottom not sure why");
        return false;

    }

    private void buildDrop(TableModel table) {
        sql.append("Drop table " + table.getTableName());


    }

    private void buildDrop(String tableName) {
        sql.append("Drop table " + new Database().getTable(tableName));
    }
}
