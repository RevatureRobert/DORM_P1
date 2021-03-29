package DAO;

import DB.Queries.*;
import DB.Queries.TCL.Commit;
import DB.Queries.TCL.Rollback;
import DB.Queries.TCL.SavePoint;
import DB.Queries.TCL.TransactionQueries;
import Models.Database;
import Models.TableModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Savepoint;


//Have them start a transaction do whatever they want and then just do whatever they want
// when to set an isolation level and too what
// i guess i need it when there a write
// so either insert , update , and or create , and delete (still need to be implemented )
public class TransactionDAO {

    static StringBuilder sql = new StringBuilder();
    static PreparedStatement preparedStatement;
    static Connection connection;





    public static void start() {

        try {
            connection = Database.accessPool();
            connection.setAutoCommit(false);
            return;
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }


    }

    public static void end() {
        Commit.executeCommit(connection);
        try {
            connection.setAutoCommit(true);
            Database.releaseConn(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void rollBack(){
        Rollback.executeRollBack(connection);
    }

    public static void rollBack(String savepoint){
        Rollback.executeRollBack(savepoint ,connection);
    }

    public static void savePoint(String savepoint){
        SavePoint.executeSavePoint(savepoint ,connection);
    }

    private void add(){}


    private  <T> boolean transInsert(T obj){
        return TransactionQueries.insert(obj ,connection);
    }

    private  <T> boolean transCreate(T obj){
        return new TransactionQueries().createTable(obj,connection);
    }

    // If the class has an annotation go through the table model creation process
    private boolean transCreate(TableModel t){
        return TransactionQueries.executeCreate(t,connection);
    }
    private  <T> int transUpdate(T obj , String[] colNames ,String...colVals){
        return new TransactionQueries().executeUpdate(obj,colNames,colVals ,connection);
    }

    private  <T> int transUpdate(T obj){
        return new TransactionQueries().executeUpdate(obj,connection);
    }

    private  <T> int transDelete(T obj, String[] colnNames, String[] colVal) {
        return new TransactionQueries().delete(obj ,colnNames ,colVal,connection);
    }

    private  <T> int transDelete(T obj){
        return new TransactionQueries().delete(obj,connection);
    }

    private  <T> boolean transDrop(T obj){
        return new TransactionQueries().executeDrop(obj,connection);

    }



}
