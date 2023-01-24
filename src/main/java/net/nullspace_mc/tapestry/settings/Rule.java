package net.nullspace_mc.tapestry.settings;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Any field in annotated with this class will be considered a Tapestry rule
 * The field must be public static and have a type of one of:
 * - boolean
 * - int
 * - long
 * - float
 * - double
 * - String
 * The initial value of the field will be the default value of the rule
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface Rule {

    /**
     * The rule name, defaults to the field name
     */
    String name() default "";

    /**
     * A brief description of the rule
     */
    String desc();

    /**
     * Extra information about the rule
     */
    String[] extra() default {};

    /**
     * A list of categories the rule is in
     */
    RuleCategory[] category();

    /**
     * A list of suggested options for the setting
     * Inferred for booleans
     */
    String[] options() default {};

    /**
     * The class containing the validator method called when the rule is changed
     */
    Class<? extends Validator<?>>[] validator() default {};
}
