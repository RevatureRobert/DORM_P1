package DB.Queries;

import DB.DBConnection;
import Models.TableModel;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class CreateTableQuery {

    static StringBuilder sql = new StringBuilder();
    static PreparedStatement preparedStatement;


    public CreateTableQuery() {

    }

    private static void buildCreate(TableModel table){
        sql.append("Create table " + table.getTableName());
        StringBuilder sqlFields = new StringBuilder();
        for (Field field : table.getFields()) {
            sqlFields.append(table.getSQlCreateQuery(field) + ",");
        }
        sql.append("( " + sqlFields.deleteCharAt(sqlFields.length() - 1) + ");");
    }


    // Just a note the error is 42101 is the error code fro table already existing in H2
    // There are other methods to check if a table exits like metaData.getTables() or querying the information Schema
    public static boolean executeCreate(TableModel table) {
        buildCreate(table);
        Connection connection = null;
        try {
            connection = DBConnection.getConnection();
            preparedStatement = connection.prepareStatement(sql.toString());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            if(e.getErrorCode() == 42101){
                System.out.println("Table already exists ");
            }else {
                e.printStackTrace();
            }
            return false;
        }
        System.out.println(table.getTableName() + " Created Succesfully");
        return true;

    }




}
