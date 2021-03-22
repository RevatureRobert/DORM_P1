package DAO;

import DB.Queries.*;
import Models.TableModel;

import javax.swing.table.DefaultTableModel;

public class BasicDao implements DAOUtil{
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
        return InsertQuery.executeInsert((TableModel)obj);
    }

    @Override
    public <T> T findByID(int i) {
        return null;
    }

    @Override
    public <T> T showAll() {
        return null;
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
    public <T> int delete(T obj) {
        TableModel tableModel = new TableModel();
        if( tableModel.getClass() != obj.getClass()){
            return -2;
        }
        return DeleteQuery.executeDelete((TableModel) obj);
    }

    @Override
    public int deleteById(int id) {
        return 0;
    }


}
