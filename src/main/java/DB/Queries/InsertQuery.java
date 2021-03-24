package DB.Queries;

import DB.ConnectionPool.DBConnection;
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

public class InsertQuery {

    static StringBuilder sql = new StringBuilder();
    static PreparedStatement preparedStatement;
    static int queryResult;


    public InsertQuery() {

    }

    // Need to build an insert ot handle not just getting a whole table
    // Maybe the user wants to insert some null values like not every field

    private static void buildInsert(TableModel table) {
        sql.append("Insert into " + table.getTableName());
        StringBuilder sqlFields = new StringBuilder();
        for (Field field : table.getAllFields()) {
            sqlFields.append(field.getName() + ",");
        }
        sql.append("( " + sqlFields.deleteCharAt(sqlFields.length() - 1) + " ) Values (");
        sqlFields = new StringBuilder();
        for (Field field : table.getAllFields()) {
            if (field.getType().getSimpleName().equals("String")) {
                sqlFields.append("\'" + table.getValue(field).toString() + "\'" + ",");
            } else {
                sqlFields.append(table.getValue(field).toString() + ",");
            }
        }
        sql.append(sqlFields.deleteCharAt(sqlFields.length() - 1) + ");");
    }

    // Not sure how to implement an insert that can take objects and insert them into a table
    // prob not going to implement
    private static <T> void buildInsert(T obj, TableModel table) {
        sql.append("Insert into " + table.getTableName());
        StringBuilder sqlFields = new StringBuilder();
        for (Field field : obj.getClass().getDeclaredFields()) {
            sqlFields.append(field.getName() + ",");
        }
        sql.append("( " + sqlFields.deleteCharAt(sqlFields.length() - 1) + " ) Values (");
        sqlFields = new StringBuilder();
        for (Field field : table.getAllFields()) {
            if (field.getType().getSimpleName().equals("String")) {
                sqlFields.append("\'" + table.getValue(field).toString() + "\'" + ",");
            } else {
                sqlFields.append(table.getValue(field).toString() + ",");
            }
        }
        sql.append(sqlFields.deleteCharAt(sqlFields.length() - 1) + ");");
        System.out.println(sql);
    }

    private static <T> void buildInsert(String tableName, Field... fields) {
        sql.append("Insert into " + tableName + " (");
        TableModel temp = new TableModel();
        for (Field field : fields) {
            sql.append(field.getName() + ",");
        }
        //Get rid of the extra comma
        sql.deleteCharAt(sql.length() - 1);
        sql.append(") Values (");
        for (Field field : fields) {
            sql.append(temp.getValue(field).toString() + ",");
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(" );");
    }


    public static boolean executeInsert(TableModel table) {
        Future future = MakeThreadPool.executorService.submit((Callable) () -> {
            System.out.println(Thread.currentThread().getId());
            sql = new StringBuilder();
            buildInsert(table);
            Connection conn = new Database().getaccessPool();
            preparedStatement = conn.prepareStatement(sql.toString());
            int rs = preparedStatement.executeUpdate();

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

    public static <T> boolean executeInsert(T obj, TableModel table) {

        Future future = MakeThreadPool.executorService.submit((Callable) () -> {
            System.out.println(Thread.currentThread().getId());
            sql = new StringBuilder();
            buildInsert(obj, table);
            Connection conn = new Database().getaccessPool();
            preparedStatement = conn.prepareStatement(sql.toString());
            int rs = preparedStatement.executeUpdate();

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



    public static <T> boolean executeInsert(String tableName, Field... fields) {

        Future future = MakeThreadPool.executorService.submit((Callable) () -> {
            System.out.println(Thread.currentThread().getId());
            sql = new StringBuilder();
            buildInsert(tableName, fields);
            Connection conn = new Database().getaccessPool();
            preparedStatement = conn.prepareStatement(sql.toString());
            int rs = preparedStatement.executeUpdate();

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

}
