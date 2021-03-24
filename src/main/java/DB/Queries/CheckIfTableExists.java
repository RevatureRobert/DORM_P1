package DB.Queries;

import DB.ConnectionPool.DBConnection;
import Models.Database;
import Threads.MakeThreadPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class CheckIfTableExists {

    static StringBuilder sql = new StringBuilder();
    static PreparedStatement preparedStatement;
    static Connection connection = null;
    private ResultSet queryResult;

    // Not sure i need to specify the table name
    private void buildQuery(String tableName) {
        sql.append("Select * from " + tableName);
    }

    public boolean executeCheckExists(String tableName) {

        Future future = MakeThreadPool.executorService.submit((Callable) () -> {
            System.out.println(Thread.currentThread().getId());
            sql = new StringBuilder();
            buildQuery(tableName);
            Connection conn = new Database().getaccessPool();
            preparedStatement = conn.prepareStatement(sql.toString());
            ResultSet rs = preparedStatement.executeQuery();

            return rs;
        });


        try


        {
            queryResult = (ResultSet) future.get();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }

        try {
            if (queryResult.next()) {
                System.out.println(queryResult);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }


        return false;


//        //sql.append("Select table_name from Information_Schema.Tables where Table_Name = " + tableName);
////        sql.append("Select * from "+ tableName);
//        sql.append("Create table " + tableName + "(id int)");
//
//        try {
//            connection = DBConnection.getConnection();
////            ResultSet rset = connection.getMetaData().getTables(null, null, tableName, null);
////            System.out.println(rset);
//
//            preparedStatement = connection.prepareStatement(sql.toString());
//            preparedStatement.executeUpdate();
//
//
//        } catch (SQLException e) {
//            if (e.getErrorCode() == 42101) {
//                System.out.println("Table already exists ");
//            } else {
//                e.printStackTrace();
//            }
//            return false;
//        }
//        return false;
//    }



}
}
