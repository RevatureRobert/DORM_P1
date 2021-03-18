import Annotations.Entity;
import Annotations.FieldName;
import Annotations.PrimaryKey;
import Tables.Car;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Set;

public class Driver {

    public static void main(String[] args) {
        //scan urls that contain 'my.package', include inputs starting with 'my.package', use the default scanners
        Reflections reflections = new Reflections("", new TypeAnnotationsScanner(), new SubTypesScanner(), new FieldAnnotationsScanner());

        //TypeAnnotationsScanner
        Set<Class<?>> tables =
                reflections.getTypesAnnotatedWith(Entity.class);

        for (Class table : tables) {
            System.out.println(table.getSimpleName());
            Field[] temp = table.getDeclaredFields();
            for (Field field : temp) {
                if (field.isAnnotationPresent(FieldName.class)) {
                    field.setAccessible(true);
                    try {
                        System.out.println(getVariable(field).toString());
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    }
                }
                else if (field.isAnnotationPresent(PrimaryKey.class)){
                    System.out.println("This is a primary key");
                    System.out.println(field.getName());
                }
                else {
                    System.out.println("What is this ?");
                    System.out.println(field.getName());
                }
                System.out.println();
            }

//            Constructor[] temp = table.getConstructors();
//            for(int i =0;i < temp.length;i++){
//                System.out.println(temp[i]);
//            }
        }

        Set<Field> pks =
                reflections.getFieldsAnnotatedWith(PrimaryKey.class);

        Set<Field> fields =
                reflections.getFieldsAnnotatedWith(FieldName.class);

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


    }

    //Todo :Make it so that whatver table it is comming from it uses that an an object
    /*
        Right now the probelm is i dont know which class to create a new instance for dont know how many levels to go
        Look for the $but then that limits the user from a table name with a dollar sign
        I could look at the table name it self with getsimpleName and then check for a $ a
       If there is one i can act accordiingly
     */
    public static <T extends Object> T getVariable(Field field) throws IllegalAccessException, InstantiationException {
//         String className = field.getDeclaringClass().getSimpleName();
//         Class<?>[] temp  = field.getDeclaringClass().getDeclaredClasses();
//        for(int i = 0 ;i < temp.length;i++){
//            System.out.println("In the loop");
//            System.out.println(temp[i]);
//        }
        return (T) field.get(field.getDeclaringClass().newInstance());
    }


}
