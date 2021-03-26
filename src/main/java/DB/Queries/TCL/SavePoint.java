package DB.Queries.TCL;

import Models.Database;
import Threads.MakeThreadPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class SavePoint {


    private static StringBuilder sql;
    private static PreparedStatement preparedStatement;
    private static int queryResult;

    public static int executeSavePoint(String savePointName , Connection conn) {


            sql = new StringBuilder("Savepoint "+ savePointName + " ;" );
        try {
            preparedStatement = conn.prepareStatement(sql.toString());
             return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }


//




    }

}
