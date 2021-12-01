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
     * The name of the validator method called when the rule is changed
     * The validator method must:
     * - be declared in Settings.java
     * - be static
     * - have a return type of boolean
     * - have a single parameter whose type is the same as that of the rule field
     * The validator returns true if the value of the rule is accepted, and false otherwise
     */
    String validator() default "";

}
