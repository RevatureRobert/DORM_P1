import Models.Database;
import TestTables.Car;
import TestTables.Person;
import TestTables.Song;

import java.time.LocalDate;

public class Driver {


    public static void main(String[] args) {

        Car c1 = new Car(1000, "Car1", "2002", 10);
        Car c2 = new Car(50, "Car2", "2005", 10);
        Car c3 = new Car(25, "Car3 ", "2006", 10);
        Car c4 = new Car(123, "Car4 ", "2500", 10);

        Song s1 = new Song(1, "Song1", 123123, "Akon");

        Person p1 = new Person(10, LocalDate.now(), "Mario", "Plumber");
        Person p2 = new Person(20, LocalDate.now(), "Charles", "Barkley");

        Person p3 = new Person(30, LocalDate.now(), "Messi", "Leo");
        Person p4 = new Person(33, LocalDate.now(), "Ronaldo", "Cris");


        Database db = new Database();

//        db.dropAllTables();
//        db.drop(new Song());
//
        db.create(p1 , c1 );
        db.create(s1);
//        //db.createAllTables();
//
//
        db.add(p1, c1 ,c2,c3,c4,p2,p3);
        db.add(p4);
        db.add(s1);

        db.printResultSet(db.readAll(p1));

        db.printResultSet(db.read(c1));

        db.printResultSet(db.readRow(s1));


        c1.setYear("2004");
        db.update(c1);

        db.printResultSet(db.readRow(c1));



        Person p5 = new Person(35, LocalDate.now(), "Mr", "Clean");
        db.update(p2 ,p5);

        db.printResultSet(db.readAll(p1));

        db.update(s1, new String[]{"name","id"},new String[] {"NotaSong","15"});

        db.printResultSet(db.readAll(s1));

        db.printResultSet(db.readAll(p1));
        db.delete(p3);

        db.printResultSet(db.readAll(p1));


        db.drop(s1);



        db.close();


    }
}




