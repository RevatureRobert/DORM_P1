package Annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FieldName {
    boolean unique() default false;
    boolean notNull() default false;
    boolean ForeignKey() default false;
    String defaultVal() default "";
    String Check() default "";


}
