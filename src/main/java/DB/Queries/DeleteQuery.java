package DB.Queries;

import Models.TableModel;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;

public class DeleteQuery {

    static StringBuilder sql = new StringBuilder();
    static PreparedStatement preparedStatement;


    public DeleteQuery () {

    }

    // One issue i currently have is that i can only handle one Pk at a time and how do i decide if the user wants to an or an and
    private static void buildInsert(TableModel table){
        sql.append("Delete From " + table.getTableName() + " Where ");
        StringBuilder sqlFields = new StringBuilder();
//        for (Field field : table.getFields()) {
//            if(field.getType().getSimpleName().equals("String")){
//                sqlFields.append(field.getName() + "=" + "\'"+table.getValue(field).toString()+"\'" + ",");
//            }
//            else{
//                sqlFields.append(field.getName() + "=" + table.getValue(field).toString() + ",");
//            }
//        }
//        sql.append("( " + sqlFields.deleteCharAt(sqlFields.length() - 1) + " ) Values (");
//        sql.append(sqlFields.deleteCharAt(sqlFields.length()-1) + " Where ");
        for(Field field: table.getPrimaryKeys()){

            if(field.getType().getSimpleName().equals("String")){
                sqlFields.append(field.getName() + " = " + "\'table.getValue(field)\' ") ;
            }
            else{
                sqlFields.append(field.getName() + " = " + table.getValue(field)) ;
            }
        }
        sql.append(sqlFields);
    }


    public static <T> int executeDelete(T obj ,TableModel table) {
        buildInsert(table);
        System.out.println(sql);
//        Connection connection = null;
//        try {
//            connection = DBConnection.getConnection();
//            preparedStatement = connection.prepareStatement(sql.toString());
//             return preparedStatement.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return -1;
//        }
        return 27;

    }
}
