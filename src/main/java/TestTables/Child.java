package TestTables;

import Annotations.Entity;
import Annotations.FieldName;
import Annotations.IgnoreORM;
import Annotations.PrimaryKey;

@Entity
public class Child {


    @PrimaryKey(Check = "id>2")
    private int id = 35;

    @FieldName
    private String fname ="Daniel";

    @FieldName
    private String lname = "Kop";

    @IgnoreORM
    private String year ="1997";

    public Child(int id, String fname, String lname, String year) {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.year = year;
    }

    public Child() {

    }
}
