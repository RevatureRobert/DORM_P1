package DB.Queries;

import Annotations.IgnoreORM;
import Annotations.PrimaryKey;
import Models.Database;
import Models.TableModel;
import Threads.MakeThreadPool;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
            buildUpdate(obj, colNames);
            System.out.println(sql);
            Connection conn = Database.accessPool();
            preparedStatement = conn.prepareStatement(sql.toString());

            for (int i = 0; i < colVals.length; i++) {
                Field temp = obj.getClass().getDeclaredField(colNames[i]);
                temp.setAccessible(true);
                if(isANum(colVals[i])){
                    preparedStatement.setInt(i+1 ,Integer.parseInt(colVals[i]));
                }
                else{
                    preparedStatement.setObject(i + 1, colVals[i]);
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

    private <T> void buildUpdate(T obj, String[] colNames) {
        sql = new StringBuilder();
        sql.append("Update " + obj.getClass().getSimpleName() + " Set ");
        StringBuilder sqlFields = new StringBuilder();

        // This should be the setting section
        for (int i = 0; i < colNames.length; i++) {
            Field field;
            try {
                field = obj.getClass().getDeclaredField(colNames[i]);

                if (field.isAnnotationPresent(IgnoreORM.class)) {
                    continue;
                }
                field.setAccessible(true);
//                if(field.getType().getSimpleName().equals("String")){
//                    sqlFields.append(field.getName()).append(" = \'").append(" ? ").append("\',");
//                }
//                else {
                sqlFields.append(field.getName()).append(" = ").append(" ? ").append(",");
                //}


            } catch (SecurityException | NoSuchFieldException e) {
                System.out.println("Please take your hand out of the cookie jar");

            }
////        sql.append("( " + sqlFields.deleteCharAt(sqlFields.length() - 1) + " ) Values (");
        }
        // this is the where section
        sql.append(sqlFields.deleteCharAt(sqlFields.length() - 1) + " Where ");
        sqlFields = new StringBuilder();
        for (String field : colNames) {
            Field temp = null;
            try {
                temp = obj.getClass().getDeclaredField(field);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            if (temp.isAnnotationPresent(IgnoreORM.class) || temp == null) {
                continue;
            }
            temp.setAccessible(true);
            try {
                if (temp.getType().getSimpleName().equals("String")) {
                    sqlFields.append(temp.getName() + " = \'" + temp.get(obj) + "\'  AND ");
                } else {
                    sqlFields.append(temp.getName() + " = " + temp.get(obj) + " AND ");
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
//            if(field.getName().contains("ID") || field.getName().contains("id") || field.getName().contains("Id") || field.isAnnotationPresent(PrimaryKey.class) ){
//                try {
//                    sqlFields.append(" " + field.getName() + " = " + field.get(obj) + " AND");
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//            }
        }
        sql.append(sqlFields.delete(sqlFields.length() - 4, sqlFields.length()) + " ;");

    }

    private static <T> void buildUpdate(T obj) {

        sql = new StringBuilder();
        sql.append("Update " + obj.getClass().getSimpleName() + " Set ");
        StringBuilder sqlFields = new StringBuilder();
        for (Field field : obj.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(IgnoreORM.class)) {
                continue;
            }
            field.setAccessible(true);
            try {
                field.setAccessible(true);
                if(field.getType().getSimpleName().equalsIgnoreCase("String") || field.getType().getSimpleName().equalsIgnoreCase("LocalDate")){
                    sqlFields.append(field.getName() + "="+ "\'"+ field.get(obj)+"\'" + ",");
                }
                else{
                    sqlFields.append(field.getName() + "=" + field.get(obj) + ",");
                }



            } catch (SecurityException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
////        sql.append("( " + sqlFields.deleteCharAt(sqlFields.length() - 1) + " ) Values (");
        sql.append(sqlFields.deleteCharAt(sqlFields.length() - 1) + " Where ");
//        for(Field field : obj.getClass().getDeclaredFields()){
//            field.setAccessible(true);
//            if (field.isAnnotationPresent(IgnoreORM.class)) {
//                continue;
//            }
//            field.setAccessible(true);
//            try {
//                field.setAccessible(true);
//                if(field.getType().getSimpleName().equalsIgnoreCase("String") || field.getType().getSimpleName().equalsIgnoreCase("LocalDate")){
//                    sqlFields.append(field.getName() + "="+ "\'"+ "?"+"\'" + " AND ");
//                }
//                else {
//                    sqlFields.append(field.getName() + "=" + " ?" + " AND ");
//                }
//
//            } catch (SecurityException e) {
//                e.printStackTrace();
//            }
//
//
//        }
        //sql.append(sqlFields.delete(sqlFields.length()-4 ,sqlFields.length()) + " ;");

            sqlFields = new StringBuilder();
//            // doing this to isolate all the fields with the ids in the name
//            // Im not sure what the naming convention of their table since i was not able to read with reflections
//            // Im also not sure that they will have the annotations above their fields
            LinkedList<Field> pks = new LinkedList<>();
           for (Field pk : obj.getClass().getDeclaredFields()) {
                if (pk.getName().contains("id") || pk.getName().contains("ID") || pk.getName().contains("Id") ||
                        pk.getName().contains("iD") ||pk.isAnnotationPresent(PrimaryKey.class)) {
                    pks.add(pk);
                }
            }

            for (Field fields : pks) {
                try {
                    fields.setAccessible(true);
                    sqlFields.append(fields.getName() + " = " + fields.get(obj) + " AND ");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            sqlFields.delete(sqlFields.length() - 4, sqlFields.length());
            sql.append(sqlFields);


    }

    public <T> int executeUpdate(T obj) {

        Future future = MakeThreadPool.executorService.submit((Callable) () -> {

            System.out.println(Thread.currentThread().getId());
            sql = new StringBuilder();
            buildUpdate(obj);
            Connection conn = Database.accessPool();
            preparedStatement = conn.prepareStatement(sql.toString());
//            System.out.println(sql);
//            int counter = 1;
//            for (Field field : obj.getClass().getDeclaredFields()) {
//                field.setAccessible(true);
//                if (field.isAnnotationPresent(IgnoreORM.class)) {
//                    continue;
//                } else {
//                    field.setAccessible(true);
//                    preparedStatement.setObject(counter, field.get(obj));
//                    counter++;
//                }
//
//            }
            int rs = preparedStatement.executeUpdate();

            Database.releaseConn(conn);

            return rs;
        });


        try {
            queryResult = (int) future.get();
            System.out.println(queryResult);
            return queryResult;

        } catch (InterruptedException e) {
            e.printStackTrace();
            return -1;
        } catch (NullPointerException e) {
            System.out.println("Not the same number of column names and ");
            System.out.println("column values ");
            return -1;
        } catch (ExecutionException e) {
            System.out.println("There was a problem in the query");
            e.printStackTrace();
            return -1;
        }

    }


    public static <T> int executeUpdate(T obj, T obj2) {

        Future future = MakeThreadPool.executorService.submit((Callable) () -> {

            System.out.println(Thread.currentThread().getId());
            sql = new StringBuilder();
            buildUpdate2(obj2);
            Connection conn = Database.accessPool();
            preparedStatement = conn.prepareStatement(sql.toString());
            System.out.println(sql);
            int counter = 1;
            for (Field field : obj.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(IgnoreORM.class)) {
                    continue;
                } else {
                    field.setAccessible(true);
                    preparedStatement.setObject(counter, field.get(obj));
                    counter++;
                }

            }
            int rs = preparedStatement.executeUpdate();


            Database.releaseConn(conn);

            return rs;
        });


        try {
            queryResult = (int) future.get();
            System.out.println(queryResult);
            return queryResult;

        } catch (InterruptedException e) {
            e.printStackTrace();
            return -1;
        } catch (NullPointerException e) {
            System.out.println("Not the same number of column names and ");
            System.out.println("column values ");
            return -1;
        } catch (ExecutionException e) {
            System.out.println("There was a problem in the query");
            e.printStackTrace();
            return -1;
        }

    }

    private static <T> void buildUpdate2(T obj) {

        sql = new StringBuilder();
        sql.append("Update " + obj.getClass().getSimpleName() + " Set ");
        StringBuilder sqlFields = new StringBuilder();
        for (Field field : obj.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(IgnoreORM.class)) {
                continue;
            }
            field.setAccessible(true);
            try {
                field.setAccessible(true);
                if(field.getType().getSimpleName().equalsIgnoreCase("string") || field.getType().getSimpleName().equalsIgnoreCase("LocalDate") ){
                    sqlFields.append(field.getName() + "=" + "\'"+ field.get(obj)+"\'" + ",");
                }
                else{
                    sqlFields.append(field.getName() + "=" + field.get(obj) + ",");
                }

            } catch (SecurityException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
////        sql.append("( " + sqlFields.deleteCharAt(sqlFields.length() - 1) + " ) Values (");
        sql.append(sqlFields.deleteCharAt(sqlFields.length() - 1) + " Where ");
        sqlFields = new StringBuilder();
        for (Field field : obj.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(IgnoreORM.class)) {
                continue;
            }
            field.setAccessible(true);
            try {
                field.setAccessible(true);

                sqlFields.append(field.getName() + " = " + " ? " + " AND ");


            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
//            // doing this to isolate all the fields with the ids in the name
//            // Im not sure what the naming convention of their table since i was not able to read with reflections
//            // Im also not sure that they will have the annotations above their fields
//            LinkedList<Field> pks = new LinkedList<>();
//            for (Field pk : obj.getClass().getDeclaredFields()) {
//                if (pk.getName().contains("id") || pk.getName().contains("ID") || pk.getName().contains("Id") || pk.isAnnotationPresent(PrimaryKey.class)) {
//                    pks.add(pk);
//                }
//            }
//
//            for (Field fields : pks) {
//                try {
//                    fields.setAccessible(true);
//                    sqlFields.append(fields.getName() + " = " + fields.get(obj) + " AND ");
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//            }
        sqlFields.delete(sqlFields.length() - 4, sqlFields.length());
        sql.append(sqlFields);

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



    // This is getting really really ugly but what can ya do
    private  static Object  formatDefualt(String type, String value) {
        if (type.equalsIgnoreCase("int")) {
            System.out.println("Hitting");
            return Integer.parseInt(value);
        } else if (type.equalsIgnoreCase("double")) {
            return Double.parseDouble(value);
        } else if (type.equalsIgnoreCase("long")) {
            return Long.parseLong(value);
        } else if (type.equalsIgnoreCase("boolean")) {
            return Boolean.getBoolean(value);
        } else if (type.equalsIgnoreCase("char[]")) {
            return value.toCharArray();
        } else if (type.equalsIgnoreCase("byte[]")) {
            return value.getBytes();
        } else if (type.equalsIgnoreCase("Date")) {
            SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd");
            try {
                return formatter.parse(value);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        return null;
    }

    private boolean isANum(String value){
        for(int i = 0 ; i < value.length(); i++){
            if(!Character.isDigit(value.charAt(i))){
                return false;
            }
        }
        return true;
    }

}





