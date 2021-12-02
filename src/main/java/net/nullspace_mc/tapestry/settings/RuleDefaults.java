package net.nullspace_mc.tapestry.settings;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotations to denote which default configurations a rule falls into
 */
interface RuleDefaults {
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    static @interface Survival {
        String value() default "true";
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    static @interface Creative {
        String value() default "true";
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    static @interface BugFix {
        String value() default "true";
    }
}
