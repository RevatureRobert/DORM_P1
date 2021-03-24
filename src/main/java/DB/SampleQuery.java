package DB;

import DB.ConnectionPool.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SampleQuery {

    public SampleQuery(){
        Connection conn = null;
        PreparedStatement preparedStatement;

        try {
            conn = DBConnection.getConnection();
//             preparedStatement = conn.prepareStatement("create table DanielsStuff(" +
//                    "dankid int primary key ," +
//                    "fname varchar(25)" +
//                    ");");
//           preparedStatement = conn.prepareStatement("Select * from Information_schema.columns;");
//            preparedStatement= conn.prepareStatement("Insert into DanielsStuff (DANKId) values " +
//                    "(100 )");
//            int x = preparedStatement.executeUpdate();
//            System.out.println(x);
            preparedStatement = conn.prepareStatement("Select * from Dankstuff");

            ResultSet rs = preparedStatement.executeQuery();
//               if(rs.next() == false){
//                   System.out.println("Its null");
//               }
            while (rs.next()) {
                System.out.println(rs.getString(1));
                System.out.println(rs.getString(2));
//                System.out.println(rs.getString(3));
//                System.out.println(rs.getString(4));
                System.out.println();

                //@RunWith(JUnitPlatform.class)
                //@SelectPackages("com")
                //public class TestRunner {
                //}


            }
//              System.out.println("Number of rows effcted" + x);

        } catch (SQLException e) {
            System.out.println("It failed");
            e.printStackTrace();
        }
    }
}
