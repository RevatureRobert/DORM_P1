package Models;

import DAO.BasicDao;
import DAO.TransactionDAO;
import DB.ConnectionPool.BasicConnPool;
import DB.ConnectionPool.ConnectionPool2;
import FileReader.ReadPropertyFile.ReadingPropertyFile;
import RefelctionsWork.GetClasses;
import Threads.MakeThreadPool;

import java.io.File;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Database {

    public Set<TableModel> tables = new HashSet<>();
    private BasicDao dao = new BasicDao();
    private TransactionDAO transactionDAO;
    protected static ConnectionPool2 connectionPool = null;


    public Database() {
        for (Class clazz : GetClasses.getEntities()) {
            TableModel tableModel = new TableModel(clazz);
            tables.add(tableModel);
        }
        try {
            ReadingPropertyFile reader = new ReadingPropertyFile();
            connectionPool = BasicConnPool
                    .create(reader.getProp("postgres.url"), reader.getProp("postgres.username"), reader.getProp("postgres.access"), reader.getProp("postgres.classForName"));
        } catch (
                SQLException e) {
            e.printStackTrace();
        }
    }

    public Database(File file, String db) {
        for (Class clazz : GetClasses.getEntities()) {
            TableModel tableModel = new TableModel(clazz);
            tables.add(tableModel);
        }
        try {
            ReadingPropertyFile reader = new ReadingPropertyFile(file);
            connectionPool = BasicConnPool
                    .create(reader.getProp(db + ".url"), reader.getProp(db + ".username"), reader.getProp(db + ".access"), reader.getProp(db + ".access"));
        } catch (
                SQLException e) {
            e.printStackTrace();
        }
    }

    public Database(String user, String access, String url, String driver) {
        for (Class clazz : GetClasses.getEntities()) {
            TableModel tableModel = new TableModel(clazz);
            tables.add(tableModel);
        }
        try {
            connectionPool = BasicConnPool
                    .create("postgres", "RevaturePro", "jdbc:postgresql://project1db.cbo6usfmqg0y.us-east-2.rds.amazonaws.com/postgres", " org.postgresql.Driver");
        } catch (
                SQLException e) {
            e.printStackTrace();
        }
    }

    @Deprecated
    public void printTablesNames() {
        for (TableModel table : tables) {
            System.out.println(table.getClazz().getSimpleName());
        }
    }

    @Deprecated
    public void printFields(TableModel tableModel) {
        tableModel.printColumns();
    }

    @Deprecated
    public void printFields(String tableName) {
        getTable(tableName).printColumns();
    }

    public static Connection accessPool() {
        return connectionPool.getConnection();
    }

    public static void releaseConn(Connection conn) {
        connectionPool.releaseConnection(conn);
    }

    // This doesn't make any sense why would i want this ?
    private TableModel getTable(TableModel tableModel) {
        for (TableModel table : tables) {
            if (table.equals(tableModel)) {
                return tableModel;
            }
        }
        return null;

    }

    public Set<TableModel> getTables() {
        return tables;
    }

    public TableModel getTable(String tableName) {
        for (TableModel table : getTables()) {
            if (table.getClazz().getSimpleName().equalsIgnoreCase(tableName)) {
                return table;
            }
        }
        return null;
    }

//    public boolean createTable(TableModel table) {
//        return dao.CreateTable(table);
//    }


    // So my thinking is that they do not want to initialize every table
    // The string name that are valid are only the ones that have the Entity anotation on them
    public boolean createTable(String... tableNames) {
        int size = tableNames.length;
        for (int i = 0; i < size; i++) {
            dao.CreateTable(getTable(tableNames[i]));
        }
        return true;
    }

    // This method will create all the tables that were marked with the Entity Annotation
    public boolean createAllTables() {

        int size = tables.size();
        for (int i = 0; i < size; i++) {
            dao.CreateTable(getTableModelFromTables(i));
        }
        return true;
    }

    private TableModel getTableModelFromTables(int x) {
        int counter = 0;
        for (TableModel table : tables) {
            if (counter == x) {
                return table;
            } else {
                counter++;
            }
        }
        return null;
    }

    @Deprecated
    public <T> boolean insertIntoTable(T obj, String tableName) {
        return dao.insertIntoTable(obj, getTable(tableName));
    }
    @Deprecated
    public <T> boolean insertIntoTable(T obj, TableModel table) {
        return dao.insertIntoTable(obj, table);
    }

    @Deprecated
    public int deleteFromTable(String tableName) {
        return dao.delete(getTable(tableName));
    }

    @Deprecated
    public int deleteFromTable(TableModel table) {
        return dao.delete(table);
    }

    @Deprecated
    public int insertIntoTable(String tableName, Field[] colNames, String[] colVals) {
        return dao.insertIntoTable(tableName, colNames, colVals);
    }

    @Deprecated
    public ResultSet showAll(TableModel tableModel) {
        return dao.showAll(tableModel);
    }

    @Deprecated
    public int updateTable(String tableName, String colName, String colVal, Field... fields) {
        return dao.updateTable(tableName, colName, colVal, fields);
    }

    @Deprecated
    public ResultSet readAllTable(TableModel table) {
        return dao.showAll(table);
    }

    @Deprecated
    public void readTable(TableModel table, Field... fields) {
        dao.readTable(table, fields);
    }

    @Deprecated
    public void printReadTable(TableModel table, Field... fields) {
        dao.printReadTable(table, fields);
    }

    @Deprecated
    public boolean dropTable(TableModel tableModel) {
        return dao.dropTable(tableModel);
    }

    /*
        This method only drop the tables that have annotations
     */

    public boolean dropAllTables() {
        int size = tables.size();
        for (int i = 0; i < size; i++) {
            dao.dropTable(getTableModelFromTables(i));
        }
        return true;
    }

    @Deprecated
    public boolean deleteBy(String tableName, Field[] fields, String[] values) {
        return dao.deleteById(tableName, fields, values);
    }

    public <T> boolean add(T obj) {
        return dao.insert(obj);
    }

    public <T> boolean add(T... obj) { return dao.insert(obj); }

    public <T> boolean create(T obj) {
        for (TableModel t : getTables()) {
            if (t.getClazz() == obj.getClass()) {
                return dao.create(t);
            }
        }
        //If the object is not in the list of the tables with annotations then do the regular create
        return dao.create(obj);
    }

    @SafeVarargs
    public final <T> boolean create(T... obj) {
        boolean success = false;
        for (T instance : obj) {
            for (TableModel t : getTables()) {
                if (instance.getClass() == t.getClazz()) {
                    success = dao.create(t);
                }
            }
            //If the object is not in the list of the tables with annotations then do the regular create
            if (!success) {
                success = dao.create(instance);
            }

        }
        return success;
    }

    public <T> int update(T obj, String[] colNames, String[] colVals) {
        return dao.update(obj, colNames, colVals);
    }

    public <T> int update(T obj) {
        return dao.update(obj);
    }

    public <T> int update(T obj, T obj2) {
        return dao.update(obj, obj2);
    }

    public <T> int delete(T obj, String[] colNames, String[] colvals) {
        return dao.delete(obj, colNames, colvals);
    }

    public <T> int delete(T obj) {
        return dao.delete(obj);
    }

    public <T> boolean drop(T obj) {
        return dao.drop(obj);
    }

    public <T> ResultSet read(T obj) {
        return dao.readRow(obj);
    }

    public <T> ResultSet readAll(T obj) {
        return dao.readAll(obj);
    }

    public <T> ResultSet readRow(T obj) {
        return dao.readRow(obj);
    }


    public void printResultSet(ResultSet rs) {
        System.out.println();
        try {
            System.out.println("Select from " + rs.getMetaData().getTableName(1));
            if (rs != null) {
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    System.out.print(rs.getMetaData().getColumnName(i) + "-------------");
                }
            }
            System.out.println();
            while (rs.next()) {
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
//                    System.out.printf("'%-10s' %n", rs.getString(i) );
                    System.out.print(rs.getString(i) + "           ");
                }
                System.out.println();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void startTransaction(){ TransactionDAO.start(); }

    public void makeASavepoint(String savePointName){ TransactionDAO.savePoint(savePointName); }

    public void rollback(){ TransactionDAO.rollBack();}

    public void rollToSavePoint(String savePointName ){ TransactionDAO.rollBack(savePointName);}

    public void commit(){TransactionDAO.end();}


    public void close() {
        MakeThreadPool.executorService.shutdown();
    }

    @Override
    public void finalize() {

        MakeThreadPool.executorService.shutdown();
    }

    /*
        For the transaction i think all of the queries and method should be on the same thread an connection
        So im thinking of setting up a different DAO for transaction this way i can keep all of them on the same thread
        Make it be like transActionAdd() . . . Not sure yet though
     */
//    public boolean startTransaction(){
//        return dao.
//    }


}


