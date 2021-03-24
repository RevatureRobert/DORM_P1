package Models;

import Annotations.FieldName;
import Annotations.PrimaryKey;

import java.lang.reflect.Field;
import java.nio.file.PathMatcher;
import java.util.*;

public class TableModel {

    Class clazz;
    Field[] allFields;
    List<Field> columns = new ArrayList<>();


    public TableModel(Class clazz) {
        this.clazz = clazz;
        this.allFields = clazz.getDeclaredFields();
        this.setColumns();
    }

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
            if (field.isAnnotationPresent(PrimaryKey.class) || field.isAnnotationPresent(FieldName.class))
                columns.add(field);
        }

    }

    public List<Field> getColumns() {
        return this.columns;
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


    //Todo :Make it so that whatever table it is comming from it uses that an an object
    /*
        Right now the problem is i don't know which class to create a new instance for dont know how many levels to go
        Look for the $but then that limits the user from a table name with a dollar sign
        I could look at the table name it self with getsimpleName and then check for a $ a
        If there is one i can act accordingly
     */
    public <T extends Object> T getValue(Field field) {
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

    private Field getField(String fieldName) {
        for (Field field : getAllFields()) {
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
