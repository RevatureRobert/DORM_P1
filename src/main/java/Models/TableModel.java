package Models;

import Annotations.FieldName;
import Annotations.ForeignKey;
import Annotations.IgnoreORM;
import Annotations.PrimaryKey;

import java.lang.reflect.Field;
import java.util.*;

public class TableModel {

    Class clazz;
    Field[] allFields;
    ArrayList<Field> columns = new ArrayList<>();


    public TableModel(Class clazz) {
        this.clazz = clazz;
        this.allFields = clazz.getDeclaredFields();
        this.setColumns();
    }


    // Plans to make tables on the fly to input but idk how to do it
    // I don't think i can
    // Maybe if they input everything as strings

    public TableModel(String tableName, Field... fields) {

    }

    // Not sure what to do with this guy?
    public TableModel() {

    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public Field[] getAllFields() {
        return allFields;
    }

    private void setColumns() {
        for (Field field : allFields) {
            if (field.isAnnotationPresent(PrimaryKey.class) || field.isAnnotationPresent(FieldName.class) || field.isAnnotationPresent(ForeignKey.class))
                columns.add(field);
        }

    }

    public List<Field> getColumns() {
        return this.columns;
    }

    // Get an Array of all of the fields
    public Field[] getColumnsArray() {
        Field[] fields = new Field[columns.size()];
        for (int i = 0; i < fields.length; i++) {
            fields[i] = columns.get(i);
        }
        return fields;
    }

    // only get an array of the all columns that the user wants
    public Field[] getColumnsArray(String... fieldName) {
        Field[] fields = new Field[columns.size()];
        for (int i = 0; i < fields.length; i++) {
            if (columns.contains(fieldName[i])) {
                fields[i] = columns.get(columns.indexOf(fieldName[i]));
            }

        }
        return fields;
    }

    public Field[] getForeignKeysArray(String... fieldName) {
        Field[] fields = new Field[numberofFks()];
        int counter = 0;
        for (int i = 0; i < columns.size(); i++) {
            if (columns.contains(fieldName[i])) {
                Field temp = columns.get(columns.indexOf(fieldName[i]));
                if (temp.isAnnotationPresent(PrimaryKey.class)) {
                    fields[counter] = temp;
                    counter++;
                }

            }

        }
        return fields;
    }

    public Field[] getAllForeignKeysArray() {
        Field[] fields = new Field[numberofFks()];
        int counter = 0;
        for (int i = 0; i < columns.size(); i++) {
            Field temp = columns.get(i);
            if (temp.isAnnotationPresent(ForeignKey.class)) {
                fields[counter] = temp;
                counter++;
            }
        }


        return fields;
    }

    // only get an array of the all columns that the user wants
    public Field[] getPrimaryKeysArray(String... fieldName) {
        Field[] fields = new Field[numberofPks()];
        for (int i = 0; i < columns.size(); i++) {
            if (columns.contains(fieldName[i])) {
                Field temp = columns.get(columns.indexOf(fieldName[i]));
                if (temp.isAnnotationPresent(PrimaryKey.class))
                    fields[i] = temp;
            }

        }
        return fields;
    }

    public Field[] getPrimaryKeysArray() {
        int counter = 0;
        if (numberofPks() == 0) {
            System.out.println("There are no primary Keys");
            return null;
        }
        Field[] fields = new Field[numberofPks()];
        for (int i = 0; i < columns.size(); i++) {
            if (columns.get(i).isAnnotationPresent(PrimaryKey.class)) {
                fields[counter] = columns.get(i);
                counter++;
            }

        }

        return fields;
    }

    // Getting the number of primary keys
    private int numberofPks() {
        int counter = 0;
        for (Field field : columns) {
            if (field.isAnnotationPresent(PrimaryKey.class))
                counter++;
        }
        return counter;
    }

    private int numberofFields() {
        int counter = 0;
        for (Field field : columns) {
            if (field.isAnnotationPresent(FieldName.class))
                counter++;
        }
        return counter;
    }

    private int numberofFks() {
        int counter = 0;
        for (Field field : columns) {
            if (field.isAnnotationPresent(ForeignKey.class))
                counter++;
        }
        return counter;
    }

    private int numberofIgnored() {
        int counter = 0;
        for (Field field : columns) {
            if (field.isAnnotationPresent(IgnoreORM.class))
                counter++;
        }
        return counter;
    }

    public void setAllFields(Field[] allFields) {

        this.allFields = allFields;
    }

    public List<Field> getPrimaryKeys() {
        List<Field> list = new LinkedList();
        for (Field field : this.allFields) {
            if (field.isAnnotationPresent(PrimaryKey.class)) {
                list.add(field);
            }
        }
        return list;
    }

    public List<Field> getFieldNames() {
        List<Field> list = new LinkedList();
        for (Field field : this.allFields) {
            if (field.isAnnotationPresent(FieldName.class)) {
                list.add(field);
            }
        }
        return list;
    }

    public void printPrimaryKeys() {
        for (Field field : getPrimaryKeys()) {
            System.out.println(field.getName());
        }
    }

    public void printFieldNames() {

        for (Field field : getFieldNames()) {
            System.out.println(field.getName());
        }
    }

    public void printColumns() {
        System.out.println("Primary Keys");
        System.out.println("------------");
        printPrimaryKeys();
        System.out.println();
        System.out.println();
        System.out.println("Fields");
        System.out.println("------");
        printFieldNames();
    }

    public String getTableName() {
        return this.clazz.getSimpleName();
    }


    public Class getFieldtype(Field field) {
        return field.getType();
    }
//    public Field getField(String fieldName){
//        for(Field fields :allFields){
//            if(fields.getName().equalsIgnoreCase(fieldName)){
//                return fields;
//            }
//        }
//        return null;
//
//    }


    //Todo :Make it so that whatever table it is comming from it uses that an an object
    /*
        Right now the problem is i don't know which class to create a new instance for dont know how many levels to go
        Look for the $but then that limits the user from a table name with a dollar sign
        I could look at the table name it self with getsimpleName and then check for a $ a
        If there is one i can act accordingly
     */
    public <T> T getValue(Field field) {
        try {
            field.setAccessible(true);
            return (T) field.get(field.getDeclaringClass().newInstance());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Field[] getFields(String... fieldNames) {
        Field[] fields = new Field[fieldNames.length];
        for (int i = 0; i < fieldNames.length; i++) {
            if (isField(fieldNames[i])) {
                fields[i] = (getField(fieldNames[i]));
            }
        }
        return fields;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TableModel)) return false;
        TableModel that = (TableModel) o;
        return Objects.equals(getClazz(), that.getClazz()) && Arrays.equals(getAllFields(), that.getAllFields());
    }

    private boolean isField(String field) {
        if (getFieldNames().contains(getField(field))) {
            return true;
        } else {
            return false;
        }
    }

    public Field getField(String fieldName) {
        System.out.println("Hitting");
        for (Field field : getAllFields()) {
            System.out.println(field);
            if (field.getName().equals(fieldName)) {
                return field;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "TableModel{" +
                "clazz=" + clazz +
                ", allFields=" + Arrays.toString(allFields) +
                ", columns=" + columns +
                '}';
    }

    public void printField(Field field) {
        System.out.println("Field name" + field.getName());
    }
}
