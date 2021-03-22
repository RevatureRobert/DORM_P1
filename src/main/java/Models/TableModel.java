package Models;

import Annotations.FieldName;
import Annotations.PrimaryKey;

import java.lang.reflect.Field;
import java.util.*;

public class TableModel {

    Class clazz;
    Field[] fields;

    Map<String, String> types = new HashMap<>();

    public TableModel(Class clazz) {
        initlizeMap();
        this.clazz = clazz;
        this.fields = clazz.getDeclaredFields();
    }

    public TableModel() {

    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public Field[] getFields() {
        return fields;
    }

    public void setFields(Field[] fields) {
        this.fields = fields;
    }

    public List<Field> getPrimaryKeys() {
        List<Field> list = new LinkedList();
        for (Field field : this.fields) {
            if (field.isAnnotationPresent(PrimaryKey.class)) {
                list.add(field);
            }
        }
        return list;
    }

    public List getFieldNames() {
        List<String> list = new LinkedList();
        for (Field field : this.fields) {
            if (field.isAnnotationPresent(FieldName.class)) {
                list.add(field.getName());
            }
        }
        return list;
    }

    public void printPrimaryKeys() {
        System.out.println(getPrimaryKeys());
    }

    public void printFieldNames() {
        System.out.println(getFieldNames());
    }

    public void printColumns() {
        System.out.println("Primary Keys ");
        printPrimaryKeys();
        System.out.println();
        System.out.println("Fields");
        printFieldNames();
    }

    public String getTableName() {
        return this.clazz.getSimpleName();
    }



    public Class getFieldtype(Field field) {
        return field.getType();
    }

    // if the field has a primary key it needs to be added
    // If it has other constraints add them so i gotta check what the field has and then return accordingly
    public String getSQlCreateQuery(Field field) {
        if(field.isAnnotationPresent(FieldName.class)){
            return field.getName() + " " + getSQLType(field.getType().getSimpleName());
        }
        else if (field.isAnnotationPresent(PrimaryKey.class)){
            return field.getName() + " " + getSQLType(field.getType().getSimpleName()) + " Primary key";
        }

        return field.getName() + " " + getSQLType(field.getType().getSimpleName());
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

    // Not sure what to do about the String
    // Can always add and adjust
    public void initlizeMap() {
        types.put("String", "varchar(512)");
        types.put("int", "int");
        types.put("long", "BigInt");
        types.put("float", "Real");
        types.put("double", "double");
        types.put("byte[]", "LongVarBinary");
        types.put("Date", "Date");
        types.put("Time", "Time");
        types.put("TimeStamp", "TimeStamp");
    }


    // Having a map to store all the types and how they correspond to the SQl data type
    // The key is the Java Object and the value is the SQL data Type
    public String getSQLType(String type) {
        return this.types.get(type);
    }
}
