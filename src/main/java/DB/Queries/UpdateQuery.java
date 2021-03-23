package DB.Queries;

import Models.TableModel;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;

public class UpdateQuery {

    static StringBuilder sql = new StringBuilder();
    static PreparedStatement preparedStatement;


    public UpdateQuery () {

    }

    // One issue i currently have is that i can only handle one Pk at a time and how do i decide if the user wants to an or an and
    private static void buildUpdate(TableModel table){
        sql.append("Update " + table.getTableName() + " Set ");
        StringBuilder sqlFields = new StringBuilder();
        for (Field field : table.getAllFields()) {
            if(field.getType().getSimpleName().equals("String")){
                sqlFields.append(field.getName() + "=" + "\'"+table.getValue(field).toString()+"\'" + ",");
            }
            else{
                sqlFields.append(field.getName() + "=" + table.getValue(field).toString() + ",");
            }
        }
//        sql.append("( " + sqlFields.deleteCharAt(sqlFields.length() - 1) + " ) Values (");
        sql.append(sqlFields.deleteCharAt(sqlFields.length()-1) + " Where ");
        sqlFields = new StringBuilder();
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


    public static int executeUpdate(TableModel table) {
        buildUpdate(table);
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


    // right now can only build a update with one condition
    private static void buildUpdate(String tableName, Field[] fields, String colName, String colValue) {
        sql.append("Update " + tableName + " Set (");
        TableModel temp = new TableModel();
        for(Field field : fields){
            sql.append(field.getName() + "=" + temp.getValue(field) + " ,");
        }
        sql.deleteCharAt(sql.length()-1);
        sql.append(" ) Where " + colName + "=" + colValue);
    }

    private static void buildUpdate(String tableName, Field[] fields, Field whereCond) {
        sql.append("Update " + tableName + " Set (");
        TableModel temp = new TableModel();
        for(Field field : fields){
            sql.append(field.getName() + "=" + temp.getValue(field) + " ,");
        }
        sql.deleteCharAt(sql.length()-1);
        sql.append(" ) Where " + whereCond.getName() + "=" + temp.getValue(whereCond));
    }

    private static void buildUpdate(TableModel table, Field[] fields, Field whereCond) {
        sql.append("Update " + table.getTableName() + " Set (");
        TableModel temp = new TableModel();
        for(Field field : fields){
            sql.append(field.getName() + "=" + temp.getValue(field) + " ,");
        }
        sql.deleteCharAt(sql.length()-1);
        sql.append(" ) Where " + whereCond.getName() + "=" + temp.getValue(whereCond));
    }

    // This method should take in a table name and Column and a value and fields
    // It will use the table name to specify which table it wants to update
    // Second it use the field array to choose which values it wants to change
    // Then it will use the column name and value to give a where
    // Definitely more can implemented but i want to go to bed
    public static int executeUpdate(String tableName , String colName ,String colValue ,Field... fields){
        buildUpdate(tableName, fields , colName ,colValue);
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

    public static int executeUpdate(String tableName , Field whereCond ,Field... fields){
        buildUpdate(tableName, fields , whereCond);
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

    public static int executeUpdate(TableModel table , Field whereCond ,Field... fields){
        buildUpdate(table, fields , whereCond);
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



}
