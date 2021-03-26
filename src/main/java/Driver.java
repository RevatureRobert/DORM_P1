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


        Car c1 = new Car(1000, "Testing", "Code", 10);
        Car c2 = new Car(50, "Does it matter", "Nope", 10);
        Car c3 = new Car(25, "WEll", "I guess", 10);


        Database db = new Database();
//        db.add(c1);
//        db.add(c2);
//        db.add(c3);
//        db.delete(c2);
       // db.delete(c2);
//        db.create(new Child(250 ,"ITs a " , "Me Mario" , "year"));
        db.drop(new Child());

//        if(db.add(new Car(1000 ,"Testing" , "Code" ,10)))
//            System.out.println("It was a success");
//        else{
//            System.out.println("It should not work");
//        }
        //System.out.println(db.update(new Car(5000 ,"Testing2.0" ,"FreeBird", 10),new String[]{"name"},new String[]{"Testing"}));
//        System.out.println(new UpdateQuery().executeUpdate(new Car(5000 ,"Testing2.0" ,"FreeBird", 10),new String[]{"name"},new String[]{"Testing"}));

//        Database db = new Database();
//        //db.dropAllTables();
//
//        db.createTable(db.getTable("car"));
//        db.insertIntoTable("car" , db.getTable("car").getColumnsArray(), new String[]{"75", "Toyota", "2005"});
//        db.readTable(db.getTable("car"),db.getTable("car").getColumnsArray());
//        db.printReadTable(db.getTable("car"),db.getTable("car").getColumnsArray());
//        db.dropTable(db.getTable("child"));

//        System.out.println(db.getTable("Car").getPrimaryKeysArray()[0].getDeclaringClass().getSimpleName() + "Excuse me");


//        System.out.println( db.deleteByID("Car",db.getTable("car").getPrimaryKeysArray() ,new String[]{"75"}));
//
//        db.createTable(db.getTable("Person"));
//        db.createTable(db.getTable("Child"));
        MakeThreadPool.executorService.shutdown();


//        TableModel table = db.getTable("Person");
//        System.out.println(table.getTableName());
//        System.out.println(table.getFields("fname","lname")[0].getName());

//        db.createTable(db.tables.get(0));
//        db.insertIntoTable(db.tables.getFirst().getTableName() , db.tables.get(1).getAllFields());
////        //db.updateTable("car", "year" , "1997" ,db.tables.get(0).getAllFields() );
//        db.readTable(db.getTable("car"), db.getTable("car").getAllFields());


//
//       BasicDao playToy = new BasicDao();
////       playToy.CreateTable(tables.getFirst());
//     //  playToy.insertIntoTable(tables.getFirst());
////       new BasicDao().insertIntoTable(tables.getFirst());
//        System.out.println( playToy.delete(tables.getFirst()));

//       for(int i = 0; i < tables.size();i++){
//           System.out.println(tables.get(i).getTableName());
//       }
    }


//        for (Class table : tables) {
//            System.out.println(table.getSimpleName());
//            Field[] temp = table.getDeclaredFields();
//            for (Field field : temp) {
//                if (field.isAnnotationPresent(FieldName.class)) {
//                    field.setAccessible(true);
//                    try {
//                        System.out.println(getVariable(field).toString());
//                    } catch (IllegalAccessException e) {
//                        e.printStackTrace();
//                    } catch (InstantiationException e) {
//                        e.printStackTrace();
//                    }
//                }
//                else if (field.isAnnotationPresent(PrimaryKey.class)){
//                    System.out.println("This is a primary key");
//                    System.out.println(field.getName());
//                }
//                else {
//                    System.out.println("What is this ?");
//                    System.out.println(field.getName());
//                }
//                System.out.println();
//            }

//            Constructor[] temp = table.getConstructors();
//            for(int i =0;i < temp.length;i++){
//                System.out.println(temp[i]);
//            }
}

//        Set<Field> pks =
//                reflections.getFieldsAnnotatedWith(PrimaryKey.class);
//
//        Set<Field> fields =
//                reflections.getFieldsAnnotatedWith(FieldName.class);

//        for (Field field : fields) {
//            field.setAccessible(true);
//            try {
//                System.out.println(field.getName());
//                System.out.println((String) getVariable(field));
//
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            } catch (InstantiationException e) {
//                e.printStackTrace();
//            }
//        }


//    public static <T extends Object> T getVariable(Field field) throws IllegalAccessException, InstantiationException {
////         String className = field.getDeclaringClass().getSimpleName();
////         Class<?>[] temp  = field.getDeclaringClass().getDeclaredClasses();
////        for(int i = 0 ;i < temp.length;i++){
////            System.out.println("In the loop");
////            System.out.println(temp[i]);
////        }
//        return (T) field.get(field.getDeclaringClass().newInstance());
//    }



