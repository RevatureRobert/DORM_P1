package DAO;

import DB.Queries.*;
import Models.TableModel;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BasicDao implements DAOUtil {
    @Override
    public boolean CreateTable(TableModel table) {
        return CreateTableQuery.executeCreate(table);
    }

    @Override
    public boolean checkIfExists(String tableName) {
        return new CheckIfTableExists().executeCheckExists(tableName);
    }

    @Override
    public <T> boolean insertIntoTable(T obj) {
        return InsertQuery.executeInsert((TableModel) obj);
    }

    public <T> boolean insertIntoTable(T obj, TableModel table) {

        return InsertQuery.executeInsert(obj, table);
    }

    // I need more
    // Maybe just search on PKS of the object
    @Override
    public <T> T findByID(T obj) {

         return null;
    }

    @Override
    public <T> T showAll() {
        return null;
    }

    public boolean readTable(TableModel table , Field...fields){
        try {
            return ReadQuery.executeRead(table , fields);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void printReadTable(TableModel table , Field...fields){
        ReadQuery.executeReadPrint(table , fields);
    }

    public <T> ResultSet showAll(TableModel obj) {
        return new ReadQuery().executeRead(obj);
    }

    @Override
    public int updateByID(int i) {
        return 0;
    }

    @Override
    public int updateRow(TableModel table) {
        return UpdateQuery.executeUpdate(table);
    }

    // Should return -2 if the object is not the right type
    // In this case in needs to be a table model
    // might be for everyone but not sure
    // I dont know what im doing
    @Override
    public int delete(TableModel table) {
//        TableModel tableModel = new TableModel();
//        if( tableModel.getClass() != obj.getClass()){
//            return -2;
//        }
        return DeleteQuery.executeDelete(table);
    }

    @Override
    public int deleteById(int id) {
        return 0;
    }


    public boolean deleteById(String tableName, Field[] fields, String[] values) {
        return DeleteQuery.executeDelete( tableName,  fields,  values);
    }

//    public int deleteById(TableModel tablename , int id) {
//        return DeleteQuery.executeDelete(tablename, id);
//    }

    public boolean insertIntoTable(String tableName, Field[] colName, String[] colVal) {
        return InsertQuery.executeInsert(tableName, colName ,colVal);
    }

    public int updateTable(String tableName, String colName, String colVal, Field... fields) {
        return UpdateQuery.executeUpdate(tableName, colName, colVal, fields);
    }

    public boolean dropTable(TableModel tableModel){
        return new DropQuery().executeDrop(tableModel);
    }



}