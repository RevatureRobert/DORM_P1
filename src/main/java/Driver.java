import DAO.BasicDao;
import Models.TableModel;
import RefelctionsWork.GetClasses;

import java.util.LinkedList;
import java.util.Set;

public class Driver {

    public static void main(String[] args) {
       Set<Class<?>> classes =GetClasses.getEntities();
       LinkedList<TableModel> tables = new LinkedList<>();
       for(Class clazz :classes){
           tables.add(new TableModel(clazz));
       }

       BasicDao playToy = new BasicDao();
//       playToy.CreateTable(tables.getFirst());
     //  playToy.insertIntoTable(tables.getFirst());
//       new BasicDao().insertIntoTable(tables.getFirst());
        System.out.println( playToy.delete(tables.getFirst()));

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



