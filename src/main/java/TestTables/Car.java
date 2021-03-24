package TestTables;

import Annotations.Entity;
import Annotations.FieldName;
import Annotations.IgnoreORM;
import Annotations.PrimaryKey;

@Entity
public class Car {

    @PrimaryKey(defaultVal = "3",Check = "id>2")
    private int id = 35;

    @FieldName
    private String name ="Daniel";

    @FieldName
    private String year = "1997";

    @IgnoreORM
    private String type ="Honda";
    @FieldName
    private Person person ;

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

}
