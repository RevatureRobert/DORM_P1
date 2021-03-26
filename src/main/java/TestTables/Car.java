package TestTables;

import Annotations.*;

@Entity
public class Car {

    @PrimaryKey(Check = "id>2")
    private int id = 35;

    @FieldName
    private String name = "Daniel";

    @FieldName
    private String year = "1997";

    @IgnoreORM
    private String type = "Honda";

    @ForeignKey(tableReferencing = "Person", ColumnReferencing = "id")
    private int personID;

    public Car(int id, String name, String year, int personID) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.personID = personID;
    }

    public Car() {

    }
}

//    @Entity
//    public class Wheels{
//        @PrimaryKey
//        private int Wheelsid = 35;
//        @FieldName
//        private String weekend ="AfterHours";
//
//
//        public class Rims{
//            @PrimaryKey
//            private String yearid = "5";
//
//            @FieldName
//            private String keepAway ="Gotemm";
//
//        }


    //}

//}
