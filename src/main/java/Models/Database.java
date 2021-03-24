package Models;

import DAO.BasicDao;
import DB.ConnectionPool.BasicConnPool;
import DB.ConnectionPool.ConnectionPool2;
import RefelctionsWork.GetClasses;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class Database {

    public LinkedList<TableModel> tables = new LinkedList<>();
    private BasicDao dao = new BasicDao();
    private ConnectionPool2 connectionPool = null;



    public Database() {
        for (Class clazz : GetClasses.getEntities()) {
            tables.add(new TableModel(clazz));
        }
        try {
            connectionPool = BasicConnPool
                    .create("jdbc:h2:tcp://localhost/~/test", "sa", "");
        } catch (
                SQLException e) {
            e.printStackTrace();
        }
    }

    public void printTablesNames() {
        for (TableModel table : tables) {
            System.out.println(table.getClazz().getSimpleName());
        }
    }

    public void printFields(TableModel tableModel) {
        tableModel.printColumns();
    }

    public void printFields(String tableName) {
        getTable(tableName).printColumns();
    }

    public Connection getaccessPool(){
        return connectionPool.getConnection();
    }

    public  void realseConn(Connection conn){
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

    public TableModel getTable(String tableName) {
        for (TableModel table : tables) {
            if (table.getClazz().getSimpleName().equalsIgnoreCase(tableName)) {
                return table;
            }
        }
        return null;
    }

    public boolean createTable(TableModel table) {
        return dao.CreateTable(table);
    }

    public <T> boolean insertIntoTable(T obj, String tableName) {
        return dao.insertIntoTable(obj, getTable(tableName));
    }

    public <T> boolean insertIntoTable(T obj, TableModel table) {
        return dao.insertIntoTable(obj, table);
    }


    public <T> int deleteFromTable(T obj, String tableName) {
        return dao.delete(obj, getTable(tableName));
    }


    public <T> int deleteFromTable(T obj, TableModel table) {
        return dao.delete(obj, table);
    }

    public <T> boolean insertIntoTable(String tableName, Field... fields) {
        return dao.insertIntoTable(tableName, fields);
    }

    public boolean showAll(TableModel tableModel){return dao.showAll(tableModel);}

    public <T> int updateTable(String tableName, String colName, String colVal, Field... fields) {
        return dao.updateTable(tableName, colName, colVal, fields);
    }

    public <T> boolean readAllTable(TableModel table) {
        return dao.showAll(table);
    }

    public <T> void readTable(TableModel table, Field... fields) {
        dao.readTable(table, fields);
    }


}
