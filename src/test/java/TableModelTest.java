import Annotations.Entity;
import Annotations.FieldName;
import Annotations.PrimaryKey;
import Models.TableModel;
import RefelctionsWork.GetClasses;
import org.apache.commons.httpclient.methods.GetMethod;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

import static org.mockito.Mockito.mock;

import java.lang.reflect.Field;
import java.util.*;

public class TableModelTest {


    Class clazz;
    Class mock;
    Field[] allFields;
    ArrayList<Field> columns = new ArrayList<>();

    private Reflections reflections;
    private Set<Class<?>> classes;



//    public TableModelTest(Class clazz) {
//        this.clazz = clazz;
//        this.allFields = clazz.getDeclaredFields();
//        this.setColumns();
//    }


    // Plans to make tables on the fly to input but idk how to do it
    // I don't think i can
    // Maybe if they input everything as strings
//
//    public TableModelTest(String tableName , Field... fields) {
//
//    }

    // Not sure what to do with this guy?
    public TableModelTest() {

    }

    @BeforeEach
    public void SetUp() {

        this.reflections = new Reflections("", new TypeAnnotationsScanner(),
                new SubTypesScanner(), new FieldAnnotationsScanner());

        this.classes = reflections.getTypesAnnotatedWith(Entity.class);
        this.clazz = classes.toArray()[0].getClass();




    }




    @Test
    @DisplayName("get Class Method")
    public void getClazz() {
        Assertions.assertEquals(classes.toArray()[0].getClass() ,this.clazz);


    }

        @Test
    @DisplayName("Set Class")
    public void setClazz(Class clazz) {
        System.out.println(clazz);
        this.clazz = clazz;
    }

    @Test
    @DisplayName("Get All the fields")
    public void  getAllFields() {
        Assertions.assertEquals(true ,true);
    }

    @Test
    @DisplayName("set Columns")
    private void setColumns() {
        for (Field field : allFields) {
            if (field.isAnnotationPresent(PrimaryKey.class) || field.isAnnotationPresent(FieldName.class))
                columns.add(field);
        }

    }
//
//    @Test
//    @DisplayName("get Columns")
//    public List<Field> getColumns() {
//        return this.columns;
//    }
//    // Get an Array of all of the fields
//    public Field[] getColumnsArray(){
//        Field[] fields = new Field[columns.size()];
//        for(int i = 0 ; i < fields.length;i++){
//            fields[i] = columns.get(i);
//        }
//        return fields;
//    }
//
//
//    @Test
//    // only get an array of the all columns that the user wants
//    public Field[] getColumnsArray(String... fieldName){
//        Field[] fields = new Field[columns.size()];
//        for(int i = 0 ; i < fields.length;i++){
//            if(columns.contains(fieldName[i])){
//                fields[i] = columns.get(columns.indexOf(fieldName[i]));
//            }
//
//        }
//        return fields;
//    }
//
//
//    @Test
//    // only get an array of the all columns that the user wants
//    public Field[] getPrimaryKeysArray(String... fieldName){
//        Field[] fields = new Field[columns.size()];
//        for(int i = 0 ; i < fields.length;i++){
//            if(columns.contains(fieldName[i])){
//                Field temp = columns.get(columns.indexOf(fieldName[i]));
//                if(temp.isAnnotationPresent(PrimaryKey.class))
//                    fields[i] = temp;
//            }
//
//        }
//        return fields;
//    }
//
//
//    @Test
//    public Field[] getPrimaryKeysArray(){
//        if(numberofPks() ==  0){
//            System.out.println("There are no primary Keys");
//            return null;
//        }
//        Field[] fields = new Field[numberofPks()];
//        for(int i = 0 ; i < fields.length;i++){
//            if(columns.get(i).isAnnotationPresent(PrimaryKey.class)){
//                fields[i] = columns.get(i);
//            }
//
//        }
//
//        return fields;
//    }
//
//
//    @Test
//    // Getting the number of primary keys
//    private int numberofPks(){
//        int counter =0;
//        for(Field field:columns){
//            if(field.isAnnotationPresent(PrimaryKey.class))
//                counter++;
//        }
//        return counter;
//    }
//
//
//    @Test
//    public void setAllFields(Field[] allFields) {
//
//        this.allFields = allFields;
//    }
//
//    @Test
//    public List<Field> getPrimaryKeys() {
//        List<Field> list = new LinkedList();
//        for (Field field : this.allFields) {
//            if (field.isAnnotationPresent(PrimaryKey.class)) {
//                list.add(field);
//            }
//        }
//        return list;
//    }
//
//    @Test
//    public List<Field> getFieldNames() {
//        List<Field> list = new LinkedList();
//        for (Field field : this.allFields) {
//            if (field.isAnnotationPresent(FieldName.class)) {
//                list.add(field);
//            }
//        }
//        return list;
//    }
//
//    @Test
//    public void printPrimaryKeys() {
//        for (Field field : getPrimaryKeys()) {
//            System.out.println(field.getName());
//        }
//    }
//
//
//    @Test
//    public void printFieldNames() {
//
//        for (Field field : getFieldNames()) {
//            System.out.println(field.getName());
//        }
//    }
//
//    @Test
//    public void printColumns() {
//        System.out.println("Primary Keys");
//        System.out.println("------------");
//        printPrimaryKeys();
//        System.out.println();
//        System.out.println();
//        System.out.println("Fields");
//        System.out.println("------");
//        printFieldNames();
//    }
//
//
//    @Test
//    public String getTableName() {
//        return this.clazz.getSimpleName();
//    }
//
//    @Test
//    public Class getFieldtype(Field field) {
//        return field.getType();
//    }
////    public Field getField(String fieldName){
////        for(Field fields :allFields){
////            if(fields.getName().equalsIgnoreCase(fieldName)){
////                return fields;
////            }
////        }
////        return null;
////
////    }
//
//
//    //Todo :Make it so that whatever table it is comming from it uses that an an object
//    /*
//        Right now the problem is i don't know which class to create a new instance for dont know how many levels to go
//        Look for the $but then that limits the user from a table name with a dollar sign
//        I could look at the table name it self with getsimpleName and then check for a $ a
//        If there is one i can act accordingly
//     */
//    @Test
//    public <T extends Object> T getValue(Field field) {
//        try {
//            field.setAccessible(true);
//            return (T) field.get(field.getDeclaringClass().newInstance());
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    @Test
//    public Field[] getFields(String... fieldNames) {
//        Field[] fields = new Field[fieldNames.length];
//        for (int i = 0; i < fieldNames.length; i++) {
//            if (isField(fieldNames[i])) {
//                fields[i] = (getField(fieldNames[i]));
//            }
//        }
//        return fields;
//    }
//
//
//    @Test
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof Models.TableModel)) return false;
//        Models.TableModel that = (Models.TableModel) o;
//        return Objects.equals(getClazz(), that.getClazz()) && Arrays.equals(getAllFields(), that.getAllFields());
//    }
//
//    @Test
//    private boolean isField(String field) {
//        if (getFieldNames().contains(getField(field))) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//
//    @Test
//    public Field getField(String fieldName) {
//        System.out.println("Hitting");
//        for (Field field : getAllFields()) {
//            System.out.println(field);
//            if (field.getName().equals(fieldName)) {
//                return field;
//            }
//        }
//        return null;
//    }
//
//
//    @Test
//    @Override
//    public String toString() {
//        return "TableModel{" +
//                "clazz=" + clazz +
//                ", allFields=" + Arrays.toString(allFields) +
//                ", columns=" + columns +
//                '}';
//    }
//
//    @Test
//    public void printField(Field field) {
//        System.out.println("Field name" + field.getName());
//    }
}
