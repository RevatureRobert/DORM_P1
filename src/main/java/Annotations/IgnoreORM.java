package Annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


// Not sure what to do with this guy might delete in refactor
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreORM {
}
