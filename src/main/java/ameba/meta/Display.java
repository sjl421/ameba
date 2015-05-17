package ameba.meta;

import java.lang.annotation.*;

/**
 * @author icode
 */
@Inherited
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Display {
    String value();
}
