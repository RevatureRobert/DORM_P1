package DB.Queries;

import DB.DBConnection;
import Models.TableModel;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertQuery {

    static StringBuilder sql = new StringBuilder();
    static PreparedStatement preparedStatement;


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
        buildInsert(table);
        System.out.println(sql);
        Connection connection = null;
        try {
            connection = DBConnection.getConnection();
            preparedStatement = connection.prepareStatement(sql.toString());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }

    public static <T> boolean executeInsert(T obj, TableModel table) {
        buildInsert(obj, table);
        System.out.println(sql);
        Connection connection = null;
        try {
            connection = DBConnection.getConnection();
            preparedStatement = connection.prepareStatement(sql.toString());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }

    public static <T> boolean executeInsert(String tableName, Field... fields) {
        buildInsert(tableName, fields);
        System.out.println(sql);
        Connection connection = null;
//        try {
//            connection = DBConnection.getConnection();
//            preparedStatement = connection.prepareStatement(sql.toString());
//            preparedStatement.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
        return true;

    }

}
