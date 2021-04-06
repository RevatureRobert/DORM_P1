package DAO;

import Models.TableModel;

import java.sql.ResultSet;

public interface DAOUtil {

     <T> boolean insert(T obj);

     <T> boolean create(T obj);

     boolean create(TableModel t);

     <T> int update(T obj);

     <T> int delete(T obj);

     <T> boolean drop(T obj);

     <T> ResultSet read(T obj);

     <T> ResultSet readAll(T obj);

     <T> ResultSet readRow(T obj);


}
