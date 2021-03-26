package DB.Queries.TCL;

import DB.Queries.*;
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

    {
        connection = Database.accessPool();
    }



    public static void start() {

        try {
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

    public void add(){}


    public <T> boolean transInsert(T obj){
        return TransactionQueries.insert(obj ,connection);
    }

    public <T> boolean transCreate(T obj){
        return new TransactionQueries().createTable(obj,connection);
    }

    // If the class has an annotation go through the table model creation process
    public boolean transCreate(TableModel t){
        return TransactionQueries.executeCreate(t,connection);
    }
    public <T> int transUpdate(T obj , String[] colNames ,String...colVals){
        return new TransactionQueries().executeUpdate(obj,colNames,colVals ,connection);
    }

    public <T> int transUpdate(T obj){
        return new TransactionQueries().executeUpdate(obj,connection);
    }
    // Not sure if this works
    //Lets say its still in beta
    @Deprecated
//    public int transUpdate(TableModel obj){
//        return TransactionQueries.executeUpdate(obj);
//    }

    public <T> int transDelete(T obj, String[] colnNames, String[] colVal) {
        return new TransactionQueries().delete(obj ,colnNames ,colVal,connection);
    }

    public <T> int transDelete(T obj){
        return new TransactionQueries().delete(obj,connection);
    }

    public <T> boolean transDrop(T obj){
        return new TransactionQueries().executeDrop(obj,connection);

    }



}
