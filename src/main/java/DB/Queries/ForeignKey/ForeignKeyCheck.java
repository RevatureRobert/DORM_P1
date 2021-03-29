package DB.Queries.ForeignKey;

import Annotations.ForeignKey;
import Models.Database;
import Models.TableModel;
import Threads.MakeThreadPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.lang.reflect.Field;

public class ForeignKeyCheck {
    static StringBuilder sql = new StringBuilder();
    static PreparedStatement preparedStatement;
    static Future query;
    static ResultSet queryResult;

    // So from my understand im doing a little sql injection here and its very very dangerous
    // but the show must go on
    public String buildFK(Field... fields) {
        StringBuilder sqlStr = new StringBuilder();
        for (Field field : fields) {
            if (isFKValid(field.getAnnotation(ForeignKey.class).tableReferencing(), field.getAnnotation(ForeignKey.class).ColumnReferencing())) {
                sqlStr.append(" ALTER TABLE " + field.getDeclaringClass().getSimpleName() + " ADD Foreign Key (" + field.getName() +
                        ") References " + field.getAnnotation(ForeignKey.class).tableReferencing() + "(" + field.getAnnotation(ForeignKey.class).ColumnReferencing() + ") ");
            } else {
            }

//             Car
//            ADD FOREIGN KEY (PersonID) REFERENCES Person(ID)  ;
//            Alter Table Car
//            ADD Foreign Key (name) references Person(fname);
        }
        return sqlStr.toString();
    }

    public boolean isFKValid(String tableName, String colName) {
        Future future = MakeThreadPool.executorService.submit((Callable) () -> {
            //System.out.println(Thread.currentThread().getId());
            sql = new StringBuilder("Select " + colName + " From " + tableName + ";");
            Connection conn = Database.accessPool();
            preparedStatement = conn.prepareStatement(sql.toString());
            ResultSet rs = preparedStatement.executeQuery();

            Database.releaseConn(conn);

            return rs;
        });

        try {
            queryResult = (ResultSet) future.get();
//            System.out.println("This is in is valid " + queryResult.getMetaData().getColumnCount());
            return true;

        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Foreign Key Not able to be created ");
            return false;
        }

    }

    public boolean isFKValid(TableModel tableModel, String colName) {
        return true;
    }

    public boolean isFKValid(TableModel tableModel) {
        return true;
    }
}
