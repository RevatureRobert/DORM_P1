package DB.Queries;

import Annotations.PrimaryKey;
import Models.Database;
import Models.TableModel;
import Threads.MakeThreadPool;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class DeleteQuery {

    static StringBuilder sql = new StringBuilder();
    static PreparedStatement preparedStatement;
    private static int queryResult;


    public DeleteQuery() {

    }


    public static int executeDelete(TableModel table) {
        Future future = MakeThreadPool.executorService.submit((Callable) () -> {
            System.out.println(Thread.currentThread().getId());
            sql = new StringBuilder();
            buildDelete(table);
            Connection conn = Database.accessPool();
            preparedStatement = conn.prepareStatement(sql.toString());
//            for(int i=0;i < values.length;i++){
//                try {
//                    preparedStatement.setObject(i+1 ,values[i]);
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
            int rs = preparedStatement.executeUpdate();
            Database.releaseConn(conn);

            return rs;
        });


        try {
            queryResult = (int) future.get();
            return queryResult;

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return -1;
        }

    }


    private static void buildDelete(String tableName, Field[] fields) {
        sql.append("Delete from " + tableName + " Where ");
        //System.out.println(fields.length);
        for (Field field : fields) {
            //System.out.println("Hitting");
            sql.append(field.getName()).append(" = ").append("?").append(" AND");
        }
        sql.delete(sql.length() - 3, sql.length());
        sql.append(" ;");
    }


    // Not that is fundamentally what happens in SQL
    public static boolean executeDelete(String tableName, Field[] fields, String[] values) {

        Future future = MakeThreadPool.executorService.submit((Callable) () -> {
            //System.out.println(Thread.currentThread().getId());
            sql = new StringBuilder();
            buildDelete(tableName, fields);
            Connection conn = Database.accessPool();
            preparedStatement = conn.prepareStatement(sql.toString());
            for (int i = 0; i < values.length; i++) {
                try {
                    preparedStatement.setObject(i + 1, values[i]);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            int rs = preparedStatement.executeUpdate();
            Database.releaseConn(conn);

            return rs;
        });


        try {
            queryResult = (int) future.get();
            return true;


        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    public <T> int delete(T obj, String[] colNames, String[] colVals) {
        Future future = MakeThreadPool.executorService.submit((Callable) () -> {
            System.out.println(Thread.currentThread().getId());
            sql = new StringBuilder();
            buildDelete(obj, colNames);
            Connection conn = Database.accessPool();
            preparedStatement = conn.prepareStatement(sql.toString());
            for (int i = 0; i < colVals.length; i++) {
                try {
                    preparedStatement.setObject(i + 1, colVals[i]);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            int rs = preparedStatement.executeUpdate();
            Database.releaseConn(conn);

            return rs;
        });


        try {
            queryResult = (int) future.get();
            return queryResult;

        } catch (InterruptedException e) {
            System.out.println("Something really went wrong in the query");
            System.out.println("you might want to consider using an actual ORM");
            return -1;
        } catch (ExecutionException e) {
            System.out.println("Something went wrong in the query");
            return -1;
        }
    }

    private <T> void buildDelete(T obj, String[] colNames) {
        StringBuilder sqlStr = new StringBuilder("Delete from");
        sqlStr.append(obj.getClass().getSimpleName() + " ");
        sqlStr.append("Where ");
        for (String colname : colNames) {
            sqlStr.append(colname + " = " + "?" + "AND");
        }

        sql = sqlStr.delete(sqlStr.length() - 3, sqlStr.length());
//        System.out.println(sql);
    }

    public <T> int delete(T obj) {
        Future future = MakeThreadPool.executorService.submit((Callable) () -> {
            System.out.println(Thread.currentThread().getId());
            sql = new StringBuilder();
            Field[] fields = buildDelete(obj);
            Connection conn = Database.accessPool();
            preparedStatement = conn.prepareStatement(sql.toString());
//            for (int i = 0; i < fields.length; i++) {
//                try {
//                    fields[i].setAccessible(true);
//                    preparedStatement.setObject(i + 1, fields[i].get(obj));
//                } catch (SQLException | IllegalAccessException e) {
//                    System.out.println("Something went wrong in the delete Query");
//                    e.printStackTrace();
//
//                }
//            }
            int rs = preparedStatement.executeUpdate();
            Database.releaseConn(conn);

            return rs;
        });


        try {
            queryResult = (int) future.get();
            return queryResult;

        } catch (InterruptedException e) {
            System.out.println("Something really went wrong in the query");
            System.out.println("you might want to consider using an actual ORM");
            return -1;
        } catch (ExecutionException e) {
            System.out.println("Something went wrong in the query");
            return -1;
        }


    }

    private static <T> Field[] buildDelete(T obj) {
        sql.append("Delete From " + obj.getClass().getSimpleName() + " Where ");
        StringBuilder sqlFields = new StringBuilder();

        // doing this to isolate all the fields with the ids in the name
        // Im not sure what the naming convention of their table since i was not able to read with reflections
        // Im also not sure that they will have the annotations above their fields
        ArrayList<Field> pks = new ArrayList<>();
        for (Field pk : obj.getClass().getDeclaredFields()) {
            if (pk.getName().contains("id") || pk.getName().contains("ID") || pk.getName().contains("Id") || pk.isAnnotationPresent(PrimaryKey.class)) {
                pks.add(pk);
            }
        }
        System.out.println(pks.size());
        for (Field field : pks) {
            try {
                field.setAccessible(true);
                sqlFields.append(field.getName() + " = " + field.get(obj) + " AND ");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        // If there are no primary keys
        // but i think this will break everything
        if(pks.size() > 0){
            sqlFields.delete(sqlFields.length() - 4, sqlFields.length());
        }

        sql.append(sqlFields);
        System.out.println(sql);
        return pks.toArray(new Field[0]);
    }
}
