import Annotations.Entity;
import TestTables.Car;
import TestTables.Child;
import TestTables.Person;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.Assert.*;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

import java.util.Set;

public class GetClassesTest {
    private Reflections reflections;
    private Set<Class<?>> classes;

    @BeforeEach
    public void setUP() throws Exception{
        this.reflections = new Reflections("", new TypeAnnotationsScanner(),
                new SubTypesScanner(), new FieldAnnotationsScanner());

        this.classes = reflections.getTypesAnnotatedWith(Entity.class);
    }



    @Test
    @DisplayName("Get the the first one")
    public void getEntities() {
        System.out.println(classes.toArray()[0]);
        Assertions.assertEquals( new Car().getClass(),classes.toArray()[0]);

    }

    @Test
    @DisplayName("Get the the Second one")
    public void getEntities1() {
        System.out.println(classes.toArray()[1]);
        Assertions.assertEquals( new Child().getClass(),classes.toArray()[1]);

    }

    @Test
    @DisplayName("Get the the Second one")
    public void getEntities2() {
        System.out.println(classes.toArray()[2]);
        Assertions.assertEquals( new Person().getClass(),classes.toArray()[2]);

    }


}
