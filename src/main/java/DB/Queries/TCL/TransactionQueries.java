package DB.Queries.TCL;

import Annotations.FieldName;
import Annotations.ForeignKey;
import Annotations.IgnoreORM;
import Annotations.PrimaryKey;
import DB.Queries.ForeignKey.ForeignKeyCheck;
import Models.Database;
import Models.TableModel;
import Threads.MakeThreadPool;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class TransactionQueries {

    static StringBuilder sql = new StringBuilder();
    static PreparedStatement preparedStatement;
    static Map<String, String> types = new HashMap<>();
    static int queryResult;

    public <T> boolean executeDrop(T obj, Connection connection) {


        sql = new StringBuilder();
        sql.append("Drop Table " + obj.getClass().getSimpleName());
        try {
            preparedStatement = connection.prepareStatement(sql.toString());
            int rs = preparedStatement.executeUpdate();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }


    }

    public <T> int delete(T obj, Connection conn) {


        sql = new StringBuilder();
        Field[] fields = buildDelete(obj);
        try {
            preparedStatement = conn.prepareStatement(sql.toString());
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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
        if (pks.size() > 0) {
            sqlFields.delete(sqlFields.length() - 4, sqlFields.length());
        }

        sql.append(sqlFields);
        System.out.println(sql);
        return pks.toArray(new Field[0]);
    }

    public <T> int delete(T obj, String[] colNames, String[] colVals, Connection conn) {

        sql = new StringBuilder();

        try {
            preparedStatement = conn.prepareStatement(buildDelete(obj, colNames));
            for (int i = 0; i < colVals.length; i++) {
                try {
                    preparedStatement.setObject(i + 1, colVals[i]);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }


    }

    private <T> String buildDelete(T obj, String[] colNames) {
        StringBuilder sqlStr = new StringBuilder("Delete from");
        sqlStr.append(obj.getClass().getSimpleName() + " ");
        sqlStr.append("Where ");
        for (String colname : colNames) {
            sqlStr.append(colname + " = " + "?" + "AND");
        }

        sql = sqlStr.delete(sqlStr.length() - 3, sqlStr.length());
        return sql.toString();


    }

    public static int executeUpdate(TableModel table, Connection connection) {


        sql = new StringBuilder();

        try {
            preparedStatement = connection.prepareStatement(buildUpdate(table));
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }


    }

    // One issue i currently have is that i can only handle one Pk at a time and how do i decide if the user wants to an or an and
    @Deprecated
    private static String buildUpdate(TableModel table) {
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
                sqlFields.append(field.getName() + " = " + "\'" + table.getValue(field) + "\' ");
            } else {
                sqlFields.append(field.getName() + " = " + table.getValue(field));
            }
        }
        sql.append(sqlFields);
        return sql.toString();
    }

    public <T> int executeUpdate(T obj, Connection conn) {


        sql = new StringBuilder();

        try {
            preparedStatement = conn.prepareStatement(buildUpdate(obj));
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }


    }

    private <T> String buildUpdate(T obj) {
        sql.append("Update " + obj.getClass().getSimpleName() + " Set ");
        StringBuilder sqlFields = new StringBuilder();
        for (Field field : obj.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(IgnoreORM.class)) {
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
        sqlFields.delete(sqlFields.length() - 4, sqlFields.length());
        sql.append(sqlFields);

        return sql.toString();

    }

    public <T> int executeUpdate(T obj, String[] colNames, String[] colVals, Connection conn) {

        if (colNames.length != colVals.length)
            return -2;
        sql = new StringBuilder();

        try {
            preparedStatement = conn.prepareStatement(buildUpdate(obj, colNames, colVals));
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }


    }

    private <T> String buildUpdate(T obj, String[] colName, String[] colVals) {
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
        sql.append(sqlFields.deleteCharAt(sqlFields.length() - 1) + " Where ");
        sqlFields = new StringBuilder();
        for (int i = 0; i < colName.length; i++) {
            sqlFields.append(colName[i] + " = " + "\'" + colVals[i] + "\'" + "And");
        }
        sql.append(sqlFields.delete(sqlFields.length() - 3, sqlFields.length()));

        return sql.toString();
    }

    public static boolean executeCreate(TableModel table, Connection connection) {

        //System.out.println(Thread.currentThread().getId());
        sql = new StringBuilder();
        try {
            initlizeMap();
            preparedStatement = connection.prepareStatement(buildCreate(table));
            int rs = preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }


    }

    private static String buildCreate(TableModel table) {
        sql = new StringBuilder();
        sql.append("Create table IF NOT Exists " + table.getTableName());
        StringBuilder sqlFields = new StringBuilder();
        for (Field field : table.getColumns()) {
            sqlFields.append(getSQlCreateQuery(field) + ",");
        }
//        System.out.println(sqlFields);
        sql.append("(" + sqlFields.deleteCharAt(sqlFields.length() - 1) + ");");
//        System.out.println("Before in the sql "+sql);
        for (Field field : table.getAllForeignKeysArray()) {
//           System.out.println(field);
            sql.append(new ForeignKeyCheck().buildFK(field));
        }
        return sql.toString();

//        System.out.println( "In build "+ sql);
    }

    // Not sure what to do about the String
    // Can always add and adjust
    private static void initlizeMap() {
        types.put("String", "varchar(512)");
        types.put("int", "int");
        types.put("long", "BigInt");
        types.put("float", "Real");
        types.put("double", "double");
        types.put("byte[]", "LongVarBinary");
        types.put("Date", "Date");
        types.put("Time", "Time");
        types.put("TimeStamp", "TimeStamp");
    }


    // Having a map to store all the types and how they correspond to the SQl data type
    // The key is the Java Object and the value is the SQL data Type
    private static String getSQLType(String type) {
        return types.get(type);
    }

    // if the field has a primary key it needs to be added
    // If it has other constraints add them so i gotta check what the field has and then return accordingly
    private static String getSQlCreateQuery(Field field) {
        boolean notStr = true;
        if (field.getDeclaredAnnotations().length > 0) {
            StringBuilder returnStr = new StringBuilder(field.getName() + " " + getSQLType(field.getType().getSimpleName()));
            if (field.isAnnotationPresent(FieldName.class)) {
                if (field.getAnnotation(FieldName.class).notNull()) {
                    returnStr.append(" Not Null");
                }
                if (field.getAnnotation(FieldName.class).unique()) {
                    returnStr.append(" Unique");
                }
                if (!field.getAnnotation(FieldName.class).Check().equals("")) {
                    returnStr.append(" Check (" + field.getAnnotation(FieldName.class).Check() + ")");
                }
                if (!field.getAnnotation(FieldName.class).defaultVal().equals("")) {
                    returnStr.append(" Default " + field.getAnnotation(FieldName.class).defaultVal());
                }
                //There is a check constraint on the field
                // Only allow one constraint on one column or else it will break

                return returnStr.toString();
            }
            // I'm not sure what to do with the quotes im just gonna pray it works im not sure
            else if (field.isAnnotationPresent(PrimaryKey.class)) {
                returnStr.append(" Primary Key");
                if (!(field.getAnnotation(PrimaryKey.class).Check().equals(""))) {
                    returnStr.append(" Check (" + field.getAnnotation(PrimaryKey.class).Check() + ")");
                }
                if (!(field.getAnnotation(PrimaryKey.class).defaultVal().equals(""))) {
                    returnStr.append(" Default " + field.getAnnotation(PrimaryKey.class).defaultVal());
                }
                return returnStr.toString();
            } else if (field.isAnnotationPresent(ForeignKey.class)) {
                return returnStr.toString();
            }


        }

        return "";
    }


    public <T> boolean createTable(T obj, Connection connection) {


        try {
            initlizeMap();
            sql = buildCreate(obj);
            preparedStatement = connection.prepareStatement(sql.toString());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }


    }

    private static <T> StringBuilder buildCreate(T obj) {
        Class objClazz = obj.getClass();
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("Create table IF NOT exists " + objClazz.getSimpleName());
        StringBuilder sqlFields = new StringBuilder();
        for (Field fields : objClazz.getDeclaredFields()) {
            if (!getSQlCreateQuery(fields).equals(""))
                sqlFields.append(getSQlCreateQuery(fields) + ",");
        }
        System.out.println(sqlFields);
        sqlStr.append("(" + sqlFields.deleteCharAt(sqlFields.length() - 1) + ");");
        return sqlStr;
    }

    public static <T> boolean insert(T obj, Connection connection) {

        int rs = 0;
        try {
            sql = new StringBuilder(buildInsert(obj));
            preparedStatement = connection.prepareStatement(sql.toString());
            rs = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (rs != -1)
            return true;


        return false;
    }

    private static <T> String buildInsert(T obj) {
        Class objClazz = obj.getClass();
        sql = new StringBuilder();
        sql.append("Insert into " + objClazz.getSimpleName());
        StringBuilder sqlFields = new StringBuilder();
        for (Field field : objClazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(IgnoreORM.class)) {
                continue;
            } else {
                sqlFields.append(field.getName() + ",");
            }

        }
        sql.append("( " + sqlFields.deleteCharAt(sqlFields.length() - 1) + " ) Values (");
        sqlFields = new StringBuilder();
        for (Field field : objClazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(IgnoreORM.class)) {
                continue;
            }
            if (field.getType().getSimpleName().equals("String")) {
                try {
                    field.setAccessible(true);
                    sqlFields.append("\'" + field.get(obj) + "\'" + ",");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    field.setAccessible(true);
                    sqlFields.append(field.get(obj) + ",");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return sql.append(sqlFields.deleteCharAt(sqlFields.length() - 1) + ");").toString();


    }

}
