package RefelctionsWork;

import Annotations.Entity;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

import java.util.Set;

public class GetClasses {

    public static Set<Class<?>> getEntities(){
        Reflections reflections = new Reflections("", new TypeAnnotationsScanner(),
                new SubTypesScanner(), new FieldAnnotationsScanner());

        return reflections.getTypesAnnotatedWith(Entity.class);
    }

}
