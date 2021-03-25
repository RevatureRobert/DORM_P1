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


}
