package DAO;

import DB.Queries.*;
import Models.TableModel;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BasicDao implements DAOUtil {

    public boolean CreateTable(TableModel table) {
        return CreateTableQuery.executeCreate(table);
    }

    public boolean checkIfExists(String tableName) {
        return new CheckIfTableExists().executeCheckExists(tableName);
    }

    public <T> boolean insertIntoTable(T obj) {
        return InsertQuery.executeInsert((TableModel) obj);
    }

    public <T> boolean insertIntoTable(T obj, TableModel table) {

        return InsertQuery.executeInsert(obj, table);
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


    // Should return -2 if the object is not the right type
    // In this case in needs to be a table model
    // might be for everyone but not sure
    // I dont know what im doing

    public int delete(TableModel table) {
//        TableModel tableModel = new TableModel();
//        if( tableModel.getClass() != obj.getClass()){
//            return -2;
//        }
        return new DeleteQuery().delete(table);
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

    public <T> boolean insert(T obj){
        return InsertQuery.insert(obj);
    }

  public <T> boolean insert(T... obj){
       boolean success =  false;
        for(T instance : obj){
            success = InsertQuery.insert(instance);
            if(!success){
                break;
            }
        }
        return success;
  }

    public <T> boolean create(T obj){
        return new CreateTableQuery().createTable(obj);
    }

    // If the class has an annotation go through the table model creation process
    public boolean create(TableModel t){
        return CreateTableQuery.executeCreate(t);
    }
    public <T> int update(T obj , String[] colNames ,String...colVals){
        return new UpdateQuery().executeUpdate(obj,colNames,colVals);
    }

    public <T> int update(T obj){
        return new UpdateQuery().executeUpdate(obj);
    }

    public <T> int update(T obj , T obj2){
        return UpdateQuery.executeUpdate(obj ,obj2);
    }

    @Deprecated
    public int update(TableModel obj){
        return UpdateQuery.executeUpdate(obj);
    }

    public <T> int delete(T obj, String[] colnNames, String[] colVal) {
        return new DeleteQuery().delete(obj ,colnNames ,colVal);
    }

    public <T> int delete(T obj){
        return new DeleteQuery().delete(obj);
    }

    public <T> boolean drop(T obj){
        return new DropQuery().executeDrop(obj);
    }

    public <T> ResultSet read(T obj){
        return ReadQuery.read(obj);
    }

    public <T> ResultSet readAll(T obj){
        return ReadQuery.readAll(obj);
    }

    public <T> ResultSet readRow(T obj){
        return ReadQuery.readRow(obj);
    }

}
