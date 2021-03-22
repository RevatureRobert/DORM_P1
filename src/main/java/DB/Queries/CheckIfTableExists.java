package DB.Queries;

import DB.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CheckIfTableExists {

    static StringBuilder sql = new StringBuilder();
    static PreparedStatement preparedStatement;
    static Connection connection = null;

    // Not sure i need to specify the table name
    private void buildQuery(String tableName) {
        sql.append("Select * from " + tableName);
    }

    public boolean executeCheckExists(String tableName) {

        //sql.append("Select table_name from Information_Schema.Tables where Table_Name = " + tableName);
//        sql.append("Select * from "+ tableName);
        sql.append("Create table " + tableName + "(id int)");

        try {
            connection = DBConnection.getConnection();
//            ResultSet rset = connection.getMetaData().getTables(null, null, tableName, null);
//            System.out.println(rset);

            preparedStatement = connection.prepareStatement(sql.toString());
            preparedStatement.executeUpdate();


        } catch (SQLException e) {
            if (e.getErrorCode() == 42101) {
                System.out.println("Table already exists ");
            } else {
                e.printStackTrace();
            }
            return false;
        }
        return false;
    }
}
