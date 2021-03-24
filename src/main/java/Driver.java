import DB.ConnectionPool.BasicConnPool;
import DB.ConnectionPool.ConnectionPool2;
import Models.Database;
import Models.TableModel;
import Threads.MakeThreadPool;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static junit.framework.TestCase.assertTrue;

public class Driver {

    private static Logger log = Logger.getLogger(String.valueOf(Driver.class));
    public static void main(String[] args) {

        Logger.getAnonymousLogger().setLevel(Level.OFF);





        // Need to implement a create a new Field option for alter ?

        // How to do the pooling
        // Need a pool of DB connections
        // Need a pool of threads
        // Maybe each Db connection gets its own thread pool


        Database db = new Database();
        db.showAll(db.getTable("car"));
        db.showAll(db.getTable("car"));
        db.showAll(db.getTable("car"));
        db.showAll(db.getTable("car"));
        db.showAll(db.getTable("car"));
        db.showAll(db.getTable("car"));
        db.showAll(db.getTable("car"));
        db.showAll(db.getTable("car"));
        db.showAll(db.getTable("car"));
        db.showAll(db.getTable("car"));
        db.showAll(db.getTable("car"));
        db.showAll(db.getTable("car"));
        db.showAll(db.getTable("car"));
        db.showAll(db.getTable("car"));
        db.showAll(db.getTable("car"));
        db.showAll(db.getTable("car"));
        db.showAll(db.getTable("car"));
        db.showAll(db.getTable("car"));
        db.showAll(db.getTable("car"));
        db.showAll(db.getTable("car"));
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



