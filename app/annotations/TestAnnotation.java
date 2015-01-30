package annotations;

/**
 * Created by akropp on 1/29/2015.
 */

import com.google.inject.BindingAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@BindingAnnotation
public @interface TestAnnotation {
}
