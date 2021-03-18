package Tables;

import Annotations.Entity;
import Annotations.FieldName;
import Annotations.PrimaryKey;

import java.time.LocalDate;
import java.util.Calendar;
@Entity
public class Person {

    @PrimaryKey
    private int id = 424;
    @FieldName
    private LocalDate Dob= LocalDate.now();
    @FieldName
    private String fname = "DanielKOP";
    @FieldName
    private String lname = "BiggieSmalls";

}
