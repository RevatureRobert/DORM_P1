import DB.ConnectionPool.BasicConnPool;
import DB.ConnectionPool.ConnectionPool2;
import DB.Queries.CreateTableQuery;
import DB.Queries.UpdateQuery;
import Models.Database;

import Models.TableModel;
import TestTables.Car;
import TestTables.Child;
import TestTables.Person;
import Threads.MakeThreadPool;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;

public class Driver {


    public static void main(String[] args) {


        // Need to implement a create a new Field option for alter ?

        // How to do the pooling
        // Need a pool of DB connections
        // Need a pool of threads
        // Maybe each Db connection gets its own thread pool


//        Car c1 = new Car(1000, "Testing", "Code", 10);
        Car c2 = new Car(50, "Does it matter", "Nope", 10);
//        Car c3 = new Car(25, "Suck ", "I guess", 10);
//        Car c4 = new Car(123, "IDK ", "BFF JILL", 10);


//        Database db = new Database("jdbc:h2:tcp://localhost/~/test" , "sa","");
        Database db = new Database();
//
//
        db.printResultSet(db.read(c2));

        db.close();



    }
}




