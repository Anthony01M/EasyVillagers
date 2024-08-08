package xyz.berrystudios.easyvillagers.database.table.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
    String name();
    String defaultValue() default "";
    boolean nullable() default false;
    boolean unique() default false;
    boolean primary() default false;
    boolean autoIncrement() default false;
    int length() default 0;
}
