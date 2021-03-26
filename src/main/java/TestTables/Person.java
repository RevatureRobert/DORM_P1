package TestTables;

import Annotations.Entity;
import Annotations.FieldName;
import Annotations.PrimaryKey;

import java.time.LocalDate;

@Entity
public class Person {

    @PrimaryKey
    private int id = 424;
    @FieldName(unique = false,notNull = true)
    private LocalDate Dob= LocalDate.now();
    @FieldName(unique = false)
    private String fname = "DanielKOP";
    @FieldName(unique = false)
    private String lname = "BiggieSmalls";

    public Person(int id, LocalDate dob, String fname, String lname) {
        this.id = id;
        Dob = dob;
        this.fname = fname;
        this.lname = lname;
    }

    public Person() {

    }
}
