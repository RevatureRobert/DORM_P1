package DB.Queries;

import Models.TableModel;

import java.lang.reflect.Field;

public class DropQuery {

    public boolean executeRead(TableModel table, Field... fields) {
        buildDrop(table, fields);


//        try {
//            connection = DBConnection.getConnection();
////            ResultSet rset = connection.getMetaData().getTables(null, null, tableName, null);
////            System.out.println(rset);
//
//            preparedStatement = connection.prepareStatement(sql.toString());
//            preparedStatement.executeUpdate();
//
//
//        } catch (SQLException e) {
//
//            e.printStackTrace();
//
//            return false;
//        }
        return false;
    }

    private void buildDrop(TableModel table, Field[] fields) {

    }
}
