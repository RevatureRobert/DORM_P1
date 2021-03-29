package DB.Queries;

import Annotations.FieldName;
import Annotations.ForeignKey;
import Annotations.PrimaryKey;

import DB.Queries.ForeignKey.ForeignKeyCheck;
import Models.Database;
import Models.TableModel;
import Threads.MakeThreadPool;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class CreateTableQuery {

    static ArrayList<Boolean> isFKValid = new ArrayList<>();
    static StringBuilder sql = new StringBuilder();
    static PreparedStatement preparedStatement;
    static boolean FkisValid = false;
    static Map<String, String> types = new HashMap<>();
    private static int queryResult;

    static {
        // Not sure what to do about the String
        // Can always add and adjust

        types.put("String", "varchar(512)");
        types.put("int", "int");
        types.put("long", "BigInt");
        types.put("float", "DECIMAL(50,10)");
        types.put("double", "BigINT");
        types.put("byte[]", "LongVarBinary");
        types.put("Date", "Date");
        types.put("LocalDate", "Date");
        types.put("Time", "Time");
        types.put("TimeStamp", "TimeStamp");

    }


    public CreateTableQuery() {

    }

    private static <T> StringBuilder buildCreate(T obj) {
        Class objClazz = obj.getClass();
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("Create table IF NOT exists " + objClazz.getSimpleName());
        StringBuilder sqlFields = new StringBuilder();
        System.out.println(objClazz.getSimpleName());
        for (Field fields : objClazz.getDeclaredFields()) {
            if (!getSQlCreateQuery(fields).equals(""))
                sqlFields.append(getSQlCreateQuery(fields) + ",");
        }
        sqlStr.append("(" + sqlFields.deleteCharAt(sqlFields.length() - 1) + ");");
        return sqlStr;
    }

    private static void buildCreate(TableModel table) {
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


//        System.out.println( "In build "+ sql);
    }


    // Just a note the error is 42101 is the error code fro table already existing in H2
    // There are other methods to check if a table exits like metaData.getTables() or querying the information Schema
    public static boolean executeCreate(TableModel table) {
        Future future = MakeThreadPool.executorService.submit((Callable) () -> {
            //System.out.println(Thread.currentThread().getId());
            sql = new StringBuilder();
            buildCreate(table);
            Connection conn = Database.accessPool();
            //System.out.println(sql);
            preparedStatement = conn.prepareStatement(sql.toString());
            int rs = preparedStatement.executeUpdate();
            Database.releaseConn(conn);

            return rs;
        });


        try {
            queryResult = (int) future.get();
            return true;

        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Something went wrong in create");
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }

    }

    public <T> boolean createTable(T obj) {

        Future future = MakeThreadPool.executorService.submit((Callable) () -> {
            //System.out.println(Thread.currentThread().getId());
            sql = buildCreate(obj);
            Connection conn = Database.accessPool();
            preparedStatement = conn.prepareStatement(sql.toString());
            int rs = preparedStatement.executeUpdate();
            Database.releaseConn(conn);

            return rs;
        });

        try {
            queryResult = (int) future.get();
            System.out.println("Success");
            return true;

        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Something went wrong in create");
            System.out.println(e.getMessage());
//            e.printStackTrace();
            return false;
        }

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

        return new StringBuilder(field.getName() + " " + getSQLType(field.getType().getSimpleName())).toString();
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
                System.out.println("Something went wrong in the date formatter");
            }
        }
        return null;
    }

}
