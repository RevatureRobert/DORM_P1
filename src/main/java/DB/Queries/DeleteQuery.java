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

public class DeleteQuery {

    static StringBuilder sql = new StringBuilder();
    static PreparedStatement preparedStatement;
    private static int queryResult;


    public DeleteQuery () {

    }

    // One issue i currently have is that i can only handle one Pk at a time and how do i decide if the user wants to an or an and
    private static void buildDelete(TableModel table){
        sql.append("Delete From " + table.getTableName() + " Where ");
        StringBuilder sqlFields = new StringBuilder();
//        for (Field field : table.getFields()) {
//            if(field.getType().getSimpleName().equals("String")){
//                sqlFields.append(field.getName() + "=" + "\'"+table.getValue(field).toString()+"\'" + ",");
//            }
//            else{
//                sqlFields.append(field.getName() + "=" + table.getValue(field).toString() + ",");
//            }
//        }
//        sql.append("( " + sqlFields.deleteCharAt(sqlFields.length() - 1) + " ) Values (");
//        sql.append(sqlFields.deleteCharAt(sqlFields.length()-1) + " Where ");
        for(Field field: table.getPrimaryKeys()){

            if(field.getType().getSimpleName().equals("String")){
                sqlFields.append(field.getName() + " = " + "\'table.getValue(field)\' ") ;
            }
            else{
                sqlFields.append(field.getName() + " = " + table.getValue(field)) ;
            }
        }
        sql.append(sqlFields);
    }


    public static <T> int executeDelete(T obj ,TableModel table) {
        buildDelete(table);
        System.out.println(sql);
//        Connection connection = null;
//        try {
//            connection = DBConnection.getConnection();
//            preparedStatement = connection.prepareStatement(sql.toString());
//             return preparedStatement.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return -1;
//        }
        return 27;

    }

    public static <T> int executeDelete(TableModel table, Field...fields) {
        buildDelete(table.getTableName(),fields);
        System.out.println(sql);
//        Connection connection = null;
//        try {
//            connection = DBConnection.getConnection();
//            preparedStatement = connection.prepareStatement(sql.toString());
//             return preparedStatement.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return -1;
//        }
        return 27;

    }

    private static void buildDelete(String tableName, Field[] fields) {
    }


    private static boolean executeDelete(String tableName, Field[] fields) {

        Future future = MakeThreadPool.executorService.submit((Callable) () -> {
            System.out.println(Thread.currentThread().getId());
            sql = new StringBuilder();
            buildDelete(tableName ,fields);
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
            return false;
        }


        return true;

    }

//    public static <T> int executeDelete(TableModel table, Field...fields) {
//        buildInsert(table.getTableName(), fields);
//        System.out.println(sql);
////        Connection connection = null;
////        try {
////            connection = DBConnection.getConnection();
////            preparedStatement = connection.prepareStatement(sql.toString());
////             return preparedStatement.executeUpdate();
////        } catch (SQLException e) {
////            e.printStackTrace();
////            return -1;
////        }
//        return 27;
//
//    }


}
