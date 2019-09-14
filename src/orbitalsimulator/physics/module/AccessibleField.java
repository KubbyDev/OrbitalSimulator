package orbitalsimulator.physics.module;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** This class is a custom annotation that must be placed on all the
 *  module fields that can be modified by the board computer */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AccessibleField {
}
