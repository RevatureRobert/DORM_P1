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
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;

public class Driver {


    public static void main(String[] args) {

        Car c1 = new Car(1000, "Car1", "2002", 10);
        Car c2 = new Car(50, "Car2", "2005", 10);
        Car c3 = new Car(25, "Suck ", "2006", 10);
        Car c4 = new Car(123, "IDK ", "BFF JILL", 10);


        Database db = new Database();
        // db.add(new Person());
        db.drop(new Car());
        db.create(new Car());

//
        Person p1 = new Person(10, LocalDate.now(), "Mario", "PLumber");
//        Person p2 = new Person(20, LocalDate.now(), "Charles", "Barkley");
//
//        Person p3 = new Person(30, LocalDate.now(), "Messi", "Leo");
//        Person p4 = new Person(33, LocalDate.now(), "Ronaldo", "Cris");
//        // Not sure what to do with the update i can update based on the PK
//        //db.update(p4,new String[]{"id","fname"}, new String[] {"50","Lebron"});
//
//        db.printResultSet(db.readAll(new Person()));
//        db.add(c1);
        db.add(c2);
//        db.add(c3);
////        db.add(c4);
//
//        db.printResultSet(db.read(new Car()));
//
//
////        db.add(new Person());
//            db.add(p1);
////        db.add(p2);
////        db.add(p3);
////        db.add(p4);
//
        db.printResultSet(db.readAll(new Person()));
//
//
//        db.delete(p1);
////        db.delete(p2);
////        db.delete(p3);
////        db.delete(p4);
//
        db.printResultSet(db.readAll(new Person()));
//
//        db.update(c2,c4);
//
//        c4.setYear("1950");
//
//        db.update(c4);
//
//        db.printResultSet(db.readAll(new Car()));
//
//        db.update(c4, new String[]{"id","year"}, new String[] {"5000", "2000"});


        db.printResultSet(db.readAll(new Car()));
//
//
        //db.drop(new Car());

        db.printResultSet(db.readAll(new Car()));


        db.close();


    }
}




