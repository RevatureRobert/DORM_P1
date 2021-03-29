package Models;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TableModelTest {

    Class clazz;
    Field[] allFields;
    ArrayList<Field> columns = new ArrayList<>();
    

    @Test
    void TableModel(Class clazz) {
        this.clazz = clazz;
        this.allFields = clazz.getDeclaredFields();
        this.setColumns();
    }

    private void setColumns() {
    }

}