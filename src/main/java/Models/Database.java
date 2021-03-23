package Models;

import DAO.BasicDao;
import RefelctionsWork.GetClasses;

import java.lang.reflect.Field;
import java.util.*;

public class Database {

    public LinkedList<TableModel> tables = new LinkedList<>();
    private BasicDao dao = new BasicDao();


    public Database() {
        for (Class clazz : GetClasses.getEntities()) {
            tables.add(new TableModel(clazz));
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


    // This doesn't make any sense why would i want this ?
    private TableModel getTable(TableModel tableModel) {
        for (TableModel table : tables) {
            if (table.equals(tableModel)) {
                return tableModel;
            }
        }
        return null;

    }

    private TableModel getTable(String tableName) {
        for (TableModel table : tables) {
            if (table.getClazz().getSimpleName().equals(tableName)) {
                return table;
            }
        }
        return null;
    }

    public boolean createTable(TableModel table) {
        return dao.CreateTable(table);
    }

    public <T> boolean insertIntoTable(T obj , String tableName){
        return dao.insertIntoTable(obj , getTable(tableName));
    }

    public <T> boolean insertIntoTable(T obj , TableModel table){
        return dao.insertIntoTable(obj ,table);
    }


    public <T> int deleteFromTable(T obj , String tableName){
        return dao.delete(obj , getTable(tableName));
    }


    public <T> int deleteFromTable(T obj , TableModel table){
        return dao.delete(obj , table);
    }

    public <T> boolean insertIntoTable(String tableName , Field...fields){
        return dao.insertIntoTable(tableName ,fields);
    };

    public <T> int updateTable(String tableName , String colName ,String colVal , Field...fields){
        return dao.updateTable(tableName , colName ,colVal ,fields);
    }


}
