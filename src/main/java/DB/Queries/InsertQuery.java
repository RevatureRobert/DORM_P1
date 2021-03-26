package DB.Queries;

import Annotations.IgnoreORM;
import Models.Database;
import Models.TableModel;
import Threads.MakeThreadPool;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
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

//    private static void buildInsert(TableModel table) {
//        sql.append("Insert into " + table.getTableName());
//        StringBuilder sqlFields = new StringBuilder();
//        for (Field field : table.getAllFields()) {
//            sqlFields.append(field.getName() + ",");
//        }
//        sql.append("( " + sqlFields.deleteCharAt(sqlFields.length() - 1) + " ) Values (");
//        sqlFields = new StringBuilder();
//        for (Field field : table.getAllFields()) {
//            if (field.getType().getSimpleName().equals("String")) {
//                try {
//                    field.setAccessible(true);
//                    sqlFields.append("\'" + field.get(table.getClazz().newInstance()) + "\'" + ",");
//                } catch (IllegalAccessException | InstantiationException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                try {
//                    field.setAccessible(true);
//                    sqlFields.append(field.get(table.getClazz().newInstance()) + ",");
//                } catch (IllegalAccessException | InstantiationException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        sql.append(sqlFields.deleteCharAt(sqlFields.length() - 1) + ");");
//    }

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

    private static void buildInsert(String tableName, Field[] colName, String[] colVal) {
        sql.append("Insert into " + tableName + " (");
        TableModel temp = new TableModel();
        for (Field field : colName) {
            sql.append(field.getName() + ",");
        }
        //Get rid of the extra comma
        sql.deleteCharAt(sql.length() - 1);
        sql.append(") Values (");
        for (String x : colVal) {
            sql.append("? ,");
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(" );");
        System.out.println(sql);
    }


    public static boolean executeInsert(TableModel table) {
        Future future = MakeThreadPool.executorService.submit((Callable) () -> {
            //System.out.println(Thread.currentThread().getId());
            sql = new StringBuilder();
            buildInsert(table);
            System.out.println(sql);
            Connection conn = Database.accessPool();
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
            Connection conn = Database.accessPool();
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

    private static void  buildInsert(String tableName, Field[] colName, int colVals){
        sql.append("Insert into " + tableName + " (");
        TableModel temp = new TableModel();
        for (Field field : colName) {
            sql.append(field.getName() + ",");
        }
        //Get rid of the extra comma
        sql.deleteCharAt(sql.length() - 1);
        sql.append(") Values (");
        while ( colVals > 0) {
            sql.append("? ,");
            colVals--;
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(" );");







    }

    public static <T> boolean executeInsert(String tableName, Field[] colName , String[] colVals) {

        Future future = MakeThreadPool.executorService.submit((Callable) () -> {
            System.out.println(Thread.currentThread().getId());
            sql = new StringBuilder();
            buildInsert(tableName, colName ,colVals.length );
            Connection conn = Database.accessPool();
            preparedStatement = conn.prepareStatement(sql.toString());
            prepareTheStatement(preparedStatement ,colVals);
            int rs = preparedStatement.executeUpdate();
            Database.releaseConn(conn);

            return rs;
        });


        try {
            queryResult = (int) future.get();

        } catch (InterruptedException | ExecutionException e) {
            System.out.println(e.getCause());
            System.out.println("This caught it ");
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

    private static void prepareTheStatement(PreparedStatement preparedStatement, String[] colVals) throws SQLException {
        for (int i = 0 ; i < colVals.length;i++){
            preparedStatement.setObject(i+1 ,colVals[i]);

        }



    }

    private static boolean needQuotesForInsert(String fieldName){
//        if(type.equalsIgnoreCase("int") || type.equalsIgnoreCase("double") ||
//                type.equalsIgnoreCase("float") || type.equalsIgnoreCase("long")){
//            return false;
//        }
        return true;
    }

    public static <T> boolean insert(T obj){
        Future future = MakeThreadPool.executorService.submit((Callable) () -> {
            //System.out.println(Thread.currentThread().getId());
            sql = new StringBuilder();
            buildInsert(obj);
            Connection conn = Database.accessPool();
            preparedStatement = conn.prepareStatement(sql.toString());
            //prepareTheStatement(preparedStatement ,colVals);
            int counter = 1;
            for(Field field : obj.getClass().getDeclaredFields()){
                if(field.isAnnotationPresent(IgnoreORM.class)){
                    continue;
                }
                field.setAccessible(true);
                preparedStatement.setObject(counter , field.get(obj));
                counter++;
            }
            int rs = preparedStatement.executeUpdate();
            Database.releaseConn(conn);

            return rs;
        });
        try {
            queryResult = (int) future.get();
            return true;
        } catch (InterruptedException | ExecutionException e) {
            System.out.println(e.getCause());
            System.out.println("This caught it ");
            e.printStackTrace();
            return false;
        }
    }

    // So for this insert i will take in the first object as the where and then use the second object as the set
    public static <T> boolean insert(T... obj ){
        for(T objectThing : obj){
            Future future = MakeThreadPool.executorService.submit((Callable) () -> {
                System.out.println(Thread.currentThread().getId());
                sql = new StringBuilder();
                buildInsert(objectThing);
                System.out.println(sql);
                Connection conn = Database.accessPool();
                preparedStatement = conn.prepareStatement(sql.toString());
                //prepareTheStatement(preparedStatement ,colVals);
                int counter = 1;
                for(Field field : objectThing.getClass().getDeclaredFields()){
                    if(field.isAnnotationPresent(IgnoreORM.class)){
                        continue;
                    }
                    field.setAccessible(true);
                    preparedStatement.setObject(counter , field.get(objectThing));
                    counter++;
                }
                int rs = preparedStatement.executeUpdate();
                Database.releaseConn(conn);

                return rs;
            });
            try {
                queryResult = (int) future.get();
                return true;
            } catch (InterruptedException | ExecutionException e) {
                System.out.println(e.getCause());
                System.out.println("This caught it ");
                e.printStackTrace();
                return false;
            }
        }
       return true;
    }

    public static boolean insert(TableModel obj){
        Future future = MakeThreadPool.executorService.submit((Callable) () -> {
            System.out.println(Thread.currentThread().getId());
            sql = new StringBuilder();
            buildInsert(obj);
            Connection conn = Database.accessPool();
            preparedStatement = conn.prepareStatement(sql.toString());
            //prepareTheStatement(preparedStatement ,colVals);
            int rs = preparedStatement.executeUpdate();
            Database.releaseConn(conn);

            return rs;
        });


        try {
            queryResult = (int) future.get();
            return true;
        } catch (InterruptedException | ExecutionException e) {
            System.out.println(e.getCause());
            System.out.println("This caught it ");
            e.printStackTrace();
            return false;
        }


    }



    private static <T> void buildInsert(T obj){
        Class objClazz = obj.getClass();
        sql.append("Insert into " + objClazz.getSimpleName());
        StringBuilder sqlFields = new StringBuilder();
        for (Field field : objClazz.getDeclaredFields()) {
            if(field.isAnnotationPresent(IgnoreORM.class)){
                continue;
            }else{
                sqlFields.append(field.getName() + ",");
            }

        }
        sql.append("( " + sqlFields.deleteCharAt(sqlFields.length() - 1) + " ) Values (");
        sqlFields = new StringBuilder();
        for (Field field : objClazz.getDeclaredFields()) {
            if(field.isAnnotationPresent(IgnoreORM.class)){
                continue;
            }
            sqlFields.append(" ?  ,");
//            if (field.getType().getSimpleName().equals("String")) {
//                try {
//                    field.setAccessible(true);
//                    sqlFields.append("\'" + field.get(obj) + "\'" + ",");
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                try {
//                    field.setAccessible(true);
//                    sqlFields.append(field.get(obj) + ",");
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//            }
        }
        sql.append(sqlFields.deleteCharAt(sqlFields.length() - 1) + ");");



    }

    public static <T> T getValue(Field field) {
        try {
            field.setAccessible(true);
            return (T) field.get(field.getDeclaringClass().newInstance());
        } catch (IllegalAccessException e) {
            System.out.println("Trying to access a field you have access for");;
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }


}
