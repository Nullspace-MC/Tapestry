package net.nullspace_mc.tapestry.settings;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class ParsedRule<T> {
    public final Field field;
    public final String name;
    public final String desc;
    public final String[] extra;
    public final RuleCategory[] category;
    public final String[] options;
    public final String def;
    public final ArrayList<Validator<T>> validators = new ArrayList<Validator<T>>();

    @SuppressWarnings("unchecked")
    public ParsedRule(Rule r, Field f) {
        this.field = f;
        this.name = r.name().isEmpty() ? f.getName() : r.name();
        this.desc = r.desc();
        this.extra = r.extra();
        this.category = r.category();
        this.options = f.getType() == boolean.class ? new String[] {"true", "false"} : r.options();

        try {
            this.def = String.valueOf(f.get(null));
        } catch(ReflectiveOperationException e) {
            throw new AssertionError(e);
        }

        for(Class<? extends Validator> v : r.validator()) {
            try {
                Constructor<? extends Validator> constructor = v.getDeclaredConstructor();
                constructor.setAccessible(true);
                validators.add((Validator<T>)constructor.newInstance());
            } catch(ReflectiveOperationException e) {
                throw new AssertionError(e);
            }
        }
    }

    public String getValueString() {
        try {
            return String.valueOf(this.field.get(null));
        } catch(ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

    @SuppressWarnings("unchecked")
    public boolean setValue(String value) {
        try {
            Class<?> fieldType = this.field.getType();
            Object newValue;

            try {
                if(fieldType == boolean.class) {
                    newValue = new Boolean(value);
                } else if(fieldType == int.class) {
                    newValue = new Integer(value);
                } else if(fieldType == long.class) {
                    newValue = new Long(value);
                } else if(fieldType == float.class) {
                    newValue = new Float(value);
                } else if(fieldType == double.class) {
                    newValue = new Double(value);
                } else if(fieldType == String.class) {
                    newValue = value;
                } else {
                    throw new AssertionError("Rule \"" + this.name + "\" has an invalid type");
                }
            } catch(NumberFormatException e) {
                return false;
            }

            for(Validator<T> validator : validators) {
                if(!validator.validate((T)newValue)) {
                    return false;
                }
            }

            this.field.set(null, newValue);
        } catch(ReflectiveOperationException e) {
            return false;
        }
        return true;
    }
}
