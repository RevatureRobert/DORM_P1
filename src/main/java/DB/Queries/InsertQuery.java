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

    private static void buildInsert(TableModel table){
        sql.append("Insert into " + table.getTableName());
        StringBuilder sqlFields = new StringBuilder();
        for (Field field : table.getFields()) {
            sqlFields.append(field.getName() + ",");
        }
        sql.append("( " + sqlFields.deleteCharAt(sqlFields.length() - 1) + " ) Values (");
        sqlFields = new StringBuilder();
        for(Field field: table.getFields()){
            if(field.getType().getSimpleName().equals("String")){
                sqlFields.append("\'"+table.getValue(field).toString()+"\'" + ",");
            }
            else{
                sqlFields.append(table.getValue(field).toString() + ",");
            }
        }
        sql.append(sqlFields.deleteCharAt(sqlFields.length() - 1) + ");");
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
}
