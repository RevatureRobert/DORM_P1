package DB.Queries;

import Annotations.FieldName;
import Annotations.PrimaryKey;

import Models.Database;
import Models.TableModel;
import Threads.MakeThreadPool;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class CreateTableQuery {

    static StringBuilder sql = new StringBuilder();
    static PreparedStatement preparedStatement;

    static Map<String, String> types = new HashMap<>();
    private static int queryResult;


    public CreateTableQuery() {

    }

    private static void buildCreate(TableModel table) {
        sql.append("Create table " + table.getTableName());
        StringBuilder sqlFields = new StringBuilder();
        for (Field field : table.getColumns()) {
            sqlFields.append(getSQlCreateQuery(field) + ",");
        }
        System.out.println(sqlFields);
        sql.append("( " + sqlFields.deleteCharAt(sqlFields.length()-1) + " );");
    }


    // Just a note the error is 42101 is the error code fro table already existing in H2
    // There are other methods to check if a table exits like metaData.getTables() or querying the information Schema
    public static boolean executeCreate(TableModel table) {

        Future future = MakeThreadPool.executorService.submit((Callable) () -> {
            System.out.println(Thread.currentThread().getId());
            sql = new StringBuilder();
            initlizeMap();
            buildCreate(table);
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

    // if the field has a primary key it needs to be added
    // If it has other constraints add them so i gotta check what the field has and then return accordingly
    private static String getSQlCreateQuery(Field field) {
        System.out.println(returnType(field));
        boolean notStr = true;
        if (field.getDeclaredAnnotations().length > 0) {
            StringBuilder returnStr = new StringBuilder(field.getName() + " " + getSQLType(field.getType().getSimpleName()));
            if (field.isAnnotationPresent(FieldName.class)) {
                if(field.getAnnotation(FieldName.class).notNull()){
                    returnStr.append(" Not Null");
                }
                if(field.getAnnotation(FieldName.class).unique()){
                    returnStr.append(" Unique");
                }
                if(!field.getAnnotation(FieldName.class).Check().equals("")){
                    returnStr.append(" Check (" + field.getAnnotation(FieldName.class).Check() + ")");
                }
                if (!field.getAnnotation(FieldName.class).defaultVal().equals("")) {
                    returnStr.append(" Default " + field.getAnnotation(FieldName.class).defaultVal());
                }
                //There is a check constraint on the field
                // Only allow one constraint on one column or else it will break
//            return field.getName() + " " + getSQLType(field.getType().getSimpleName());
//            boolean unique() default false;
//                boolean notNull() default false;
//                boolean ForeignKey() default false;
//                String defaultVal() default "";
//                String Check() default "";
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
            }
        }

        return "";
    }



    public static Class getFieldtype(Field field) {
        return field.getType();
    }

    private static String returnType(Field field) {
        return getFieldtype(field).getSimpleName();
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

    // This is getting really really ugly but what can ya do
    private static Object formatDefualt(String type, String value) {
        if (type.equalsIgnoreCase("int")) {
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

}
