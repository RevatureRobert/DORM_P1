package DB.Queries;

import Annotations.IgnoreORM;
import Annotations.PrimaryKey;
import Models.Database;
import Models.TableModel;
import Threads.MakeThreadPool;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
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

    @Deprecated
    public static int executeUpdate(TableModel table) {

        Future future = MakeThreadPool.executorService.submit((Callable) () -> {
            System.out.println(Thread.currentThread().getId());
            sql = new StringBuilder();
            buildUpdate(table);
            Connection conn = Database.accessPool();
            preparedStatement = conn.prepareStatement(sql.toString());
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
            return -1;
        }


        return queryResult;
    }


    public static int executeUpdate(String tableName, Field whereCond, Field... fields) {

        Future future = MakeThreadPool.executorService.submit((Callable) () -> {
            System.out.println(Thread.currentThread().getId());
            sql = new StringBuilder();
            buildUpdate(tableName, fields, whereCond);
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
            return -1;
        }


        return queryResult;
    }


    public static int executeUpdate(TableModel table, Field whereCond, Field... fields) {


        Future future = MakeThreadPool.executorService.submit((Callable) () -> {
            System.out.println(Thread.currentThread().getId());
            sql = new StringBuilder();
            buildUpdate(table, fields, whereCond);
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
            return -1;
        }


        return queryResult;
    }


    public <T> int executeUpdate(T obj, String[] colNames, String[] colVals) {

        Future future = MakeThreadPool.executorService.submit((Callable) () -> {
            if (colNames.length != colVals.length)
                return null;
            System.out.println(Thread.currentThread().getId());
            sql = new StringBuilder();
            buildUpdate(obj, colNames, colVals);
            Connection conn = Database.accessPool();
            preparedStatement = conn.prepareStatement(sql.toString());
            int rs = preparedStatement.executeUpdate();

            Database.releaseConn(conn);

            return rs;
        });


        try {
            queryResult = (int) future.get();
            return queryResult;

        } catch (InterruptedException e) {
            e.printStackTrace();
            return -1;
        } catch (NullPointerException e) {
            System.out.println("Not the same number of column names and ");
            System.out.println("column values ");
            return -1;
        } catch (ExecutionException e) {
            System.out.println("There was a probelm in the query");
            e.printStackTrace();
            return -1;
        }


    }


    private  <T> void buildUpdate(T obj) {
        sql.append("Update " + obj.getClass().getSimpleName() + " Set ");
        StringBuilder sqlFields = new StringBuilder();
        for (Field field : obj.getClass().getDeclaredFields()) {
            if(field.isAnnotationPresent(IgnoreORM.class)){
                continue;
            }
            field.setAccessible(true);
            try {
                field.setAccessible(true);
                if (field.getType().getSimpleName().equals("String")) {
                    sqlFields.append(field.getName() + "=" + "\'" + field.get(obj) + "\'" + ",");
                } else {
                    sqlFields.append(field.getName() + "=" + field.get(obj) + ",");
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
//        sql.append("( " + sqlFields.deleteCharAt(sqlFields.length() - 1) + " ) Values (");
        sql.append(sqlFields.deleteCharAt(sqlFields.length() - 1) + " Where ");
        sqlFields = new StringBuilder();
        // doing this to isolate all the fields with the ids in the name
        // Im not sure what the naming convention of their table since i was not able to read with reflections
        // Im also not sure that they will have the annotations above their fields
        LinkedList<Field> pks = new LinkedList<>();
        for (Field pk : obj.getClass().getDeclaredFields()) {
            if (pk.getName().contains("id") || pk.getName().contains("ID") || pk.getName().contains("Id") || pk.isAnnotationPresent(PrimaryKey.class)) {
                pks.add(pk);
            }
        }

        for (Field field : pks) {
            try {
                field.setAccessible(true);
                sqlFields.append(field.getName() + " = " + field.get(obj) + " AND ");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        sqlFields.delete(sqlFields.length()-4 ,sqlFields.length());
        sql.append(sqlFields);

    }

    public <T> int executeUpdate(T obj) {

        Future future = MakeThreadPool.executorService.submit((Callable) () -> {

            System.out.println(Thread.currentThread().getId());
            sql = new StringBuilder();
            buildUpdate(obj);
            Connection conn = Database.accessPool();
            preparedStatement = conn.prepareStatement(sql.toString());
//            for(int i=0;i < buildUpdate(obj);i++){
//                try {
//                    preparedStatement.setObject(i+1 ,);
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

        } catch (InterruptedException e) {
            e.printStackTrace();
            return -1;
        } catch (NullPointerException e) {
            System.out.println("Not the same number of column names and ");
            System.out.println("column values ");
            return -1;
        } catch (ExecutionException e) {
            System.out.println("There was a probelm in the query");
            e.printStackTrace();
            return -1;
        }


    }

    private <T> void buildUpdate(T obj, String[] colName, String[] colVals) {
        Class objClazz = obj.getClass();
        sql.append("Update " + objClazz.getSimpleName() + " Set ");
        StringBuilder sqlFields = new StringBuilder();
        for (Field field : objClazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(IgnoreORM.class)) {
                continue;
            }
            if (field.getType().getSimpleName().equals("String")) {
                try {
                    field.setAccessible(true);
                    sqlFields.append(field.getName() + "=" + "\'" + field.get(obj) + "\'" + ",");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    field.setAccessible(true);
                    sqlFields.append(field.getName() + "=" + field.get(obj) + ",");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
//        sql.append("( " + sqlFields.deleteCharAt(sqlFields.length() - 1) + " ) Values (");
        sql.append(sqlFields.deleteCharAt(sqlFields.length() - 1) + " Where ");
        sqlFields = new StringBuilder();
        for (int i = 0; i < colName.length; i++) {
            sqlFields.append(colName[i] + " = " + "\'" + colVals[i] + "\'" + "And");
        }
        sql.append(sqlFields.delete(sqlFields.length() - 3, sqlFields.length()));


    }
}



