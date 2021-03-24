package DAO;

import Models.TableModel;

public interface DAOUtil {

    boolean CreateTable(TableModel table);

    boolean checkIfExists(String tableName);

    <T> boolean insertIntoTable(T obj);

    // Not sure what to return for the select type
    // should it be just the
     <T> T findByID(T obj );

    <T> T showAll();

    int updateByID(int i);

    int updateRow(TableModel table);

     <T> int delete(T obj, TableModel table);

     int deleteById(int id);
}
