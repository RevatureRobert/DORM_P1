package DB.Queries;

import Models.Database;
import Models.TableModel;
import Threads.MakeThreadPool;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class UpdateQuery {

    static StringBuilder sql = new StringBuilder();
    static PreparedStatement preparedStatement;
    private static int queryResult;


    public UpdateQuery() {

    }

    // One issue i currently have is that i can only handle one Pk at a time and how do i decide if the user wants to an or an and
    private static void buildUpdate(TableModel table) {
        sql.append("Update " + table.getTableName() + " Set ");
        StringBuilder sqlFields = new StringBuilder();
        for (Field field : table.getAllFields()) {
            if (field.getType().getSimpleName().equals("String")) {
                sqlFields.append(field.getName() + "=" + "\'" + table.getValue(field).toString() + "\'" + ",");
            } else {
                sqlFields.append(field.getName() + "=" + table.getValue(field).toString() + ",");
            }
        }
//        sql.append("( " + sqlFields.deleteCharAt(sqlFields.length() - 1) + " ) Values (");
        sql.append(sqlFields.deleteCharAt(sqlFields.length() - 1) + " Where ");
        sqlFields = new StringBuilder();
        for (Field field : table.getPrimaryKeys()) {

            if (field.getType().getSimpleName().equals("String")) {
                sqlFields.append(field.getName() + " = " + "\'table.getValue(field)\' ");
            } else {
                sqlFields.append(field.getName() + " = " + table.getValue(field));
            }
        }
        sql.append(sqlFields);
    }


    public static int executeUpdate(TableModel table) {

        Future future = MakeThreadPool.executorService.submit((Callable) () -> {
            System.out.println(Thread.currentThread().getId());
            sql = new StringBuilder();
            buildUpdate(table);
            Connection conn = new Database().getaccessPool();
            preparedStatement = conn.prepareStatement(sql.toString());
            int rs = preparedStatement.executeUpdate();

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
            return -1;
        }


        return queryResult;

    }


    // right now can only build a update with one condition
    private static void buildUpdate(String tableName, Field[] fields, String colName, String colValue) {
        sql.append("Update " + tableName + " Set (");
        TableModel temp = new TableModel();
        for (Field field : fields) {
            sql.append(field.getName() + "=" + temp.getValue(field) + " ,");
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(" ) Where " + colName + "=" + colValue);
    }

    private static void buildUpdate(String tableName, Field[] fields, Field whereCond) {
        sql.append("Update " + tableName + " Set (");
        TableModel temp = new TableModel();
        for (Field field : fields) {
            sql.append(field.getName() + "=" + temp.getValue(field) + " ,");
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(" ) Where " + whereCond.getName() + "=" + temp.getValue(whereCond));
    }

    private static void buildUpdate(TableModel table, Field[] fields, Field whereCond) {
        sql.append("Update " + table.getTableName() + " Set (");
        TableModel temp = new TableModel();
        for (Field field : fields) {
            sql.append(field.getName() + "=" + temp.getValue(field) + " ,");
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(" ) Where " + whereCond.getName() + "=" + temp.getValue(whereCond));
    }

    // This method should take in a table name and Column and a value and fields
    // It will use the table name to specify which table it wants to update
    // Second it use the field array to choose which values it wants to change
    // Then it will use the column name and value to give a where
    // Definitely more can implemented but i want to go to bed
    public static int executeUpdate(String tableName, String colName, String colValue, Field... fields) {

        Future future = MakeThreadPool.executorService.submit((Callable) () -> {
            System.out.println(Thread.currentThread().getId());
            sql = new StringBuilder();
            buildUpdate(tableName, fields, colName, colValue);
            Connection conn = new Database().getaccessPool();
            preparedStatement = conn.prepareStatement(sql.toString());
            int rs = preparedStatement.executeUpdate();

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
            return -1;
        }


        return queryResult;
    }


    public static int executeUpdate(String tableName, Field whereCond, Field... fields) {

        Future future = MakeThreadPool.executorService.submit((Callable) () -> {
            System.out.println(Thread.currentThread().getId());
            sql = new StringBuilder();
            buildUpdate(tableName, fields, whereCond);
            Connection conn = new Database().getaccessPool();
            preparedStatement = conn.prepareStatement(sql.toString());
            int rs = preparedStatement.executeUpdate();

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
            return -1;
        }


        return queryResult;
    }


    public static int executeUpdate(TableModel table, Field whereCond, Field... fields) {


        Future future = MakeThreadPool.executorService.submit((Callable) () -> {
            System.out.println(Thread.currentThread().getId());
            sql = new StringBuilder();
            buildUpdate(table, fields, whereCond);
            Connection conn = new Database().getaccessPool();
            preparedStatement = conn.prepareStatement(sql.toString());
            int rs = preparedStatement.executeUpdate();

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
            return -1;
        }


        return queryResult;
    }


}
