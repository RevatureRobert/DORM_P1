package DB.Queries;

import DB.ConnectionPool.BasicConnPool;
import DB.ConnectionPool.ConnectionPool2;
import Models.Database;
import Models.TableModel;
import Threads.MakeThreadPool;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


public class ReadQuery {

    static StringBuilder sql = new StringBuilder();
    static PreparedStatement preparedStatement;
    static Future query;
    static ResultSet queryResult;

    // Not sure i need to specify the table name
    private void buildSelect(String tableName) {
        sql.append("Select * from " + tableName);
    }

    private void buildSelect(String tableName, Field... fields) {

        sql.append("Select (");
        for (Field field : fields) {
            sql.append(field.getName() + ",");
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(" ) From " + tableName);
    }

    private static void buildSelect(TableModel table, Field... fields) {
        if (table.getAllFields().equals(fields)) {
            sql.append("Select * from " + table.getTableName());
            return;
        }

        sql.append("Select (");
        for (Field field : fields) {
            sql.append(field.getName() + ",");
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(" ) From " + table.getTableName());
    }


    private void buildSelect(TableModel table) {
        sql.append("Select * From " + table.getTableName());
    }

    public boolean executeRead(String tableName, Field... fields) {

        Future future = MakeThreadPool.executorService.submit((Callable) () -> {
            System.out.println(Thread.currentThread().getId());
            sql = new StringBuilder();
            buildSelect(tableName, fields);
            Connection conn = new Database().getaccessPool();
            preparedStatement = conn.prepareStatement(sql.toString());
            ResultSet rs = preparedStatement.executeQuery();

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
        }
        System.out.println("Reached the bottom not sure why");
        return false;


    }

    public boolean executeRead(TableModel table) {
        Future future = MakeThreadPool.executorService.submit((Callable) () -> {
            System.out.println(Thread.currentThread().getId());
            sql = new StringBuilder();
            buildSelect(table);
            Connection conn = new Database().getaccessPool();
            preparedStatement = conn.prepareStatement(sql.toString());
            ResultSet rs = preparedStatement.executeQuery();

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
        }
        System.out.println("Reached the bottom not sure why");
        return false;

    }

    public static boolean executeRead(TableModel table, Field... fields) throws SQLException {
        Future future = MakeThreadPool.executorService.submit((Callable) () -> {
            System.out.println(Thread.currentThread().getId());
            sql = new StringBuilder();
            buildSelect(table, fields);
            Connection conn = new Database().getaccessPool();
            preparedStatement = conn.prepareStatement(sql.toString());
            ResultSet rs = preparedStatement.executeQuery();

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
        }
        System.out.println("Reached the bottom not sure why");
        return false;

    }


}
