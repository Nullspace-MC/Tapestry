package net.nullspace_mc.tapestry.settings;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SettingsManager {
    public static boolean locked = false;

    public static final Logger LOGGER = LogManager.getLogger();

    public static final HashMap<String, Field> rules = new HashMap<String, Field>();
    public static final HashMap<String, String> defaults = new HashMap<String, String>();

    public static void parseRules() {
        for (Field field : Settings.class.getFields()) {
            if (field.isAnnotationPresent(Rule.class)) {
                Rule rule = field.getAnnotation(Rule.class);
                String name = rule.name().isEmpty() ? field.getName() : rule.name();

                if(field.getModifiers() != (Modifier.PUBLIC | Modifier.STATIC)) {
                    throw new AssertionError("Access modifiers of rule field \"" + name + "\" should be \"public static\"");
                }

                if (field.getType() != boolean.class && field.getType() != int.class && field.getType() != double.class && field.getType() != String.class) {
                    throw new AssertionError("Rule \"" + name + "\" has invalid type");
                }

                Object def;

                try {
                    def = field.get(null);
                } catch(ReflectiveOperationException e) {
                    throw new AssertionError(e);
                }

                if(def == null) {
                    throw new AssertionError("Rule \"" + name + "\" has null default value");
                }

                if (field.getType() != boolean.class) {
                    boolean containsDefault = false;

                    for (String option : rule.options()) {
                        Object val;

                        if(field.getType() == int.class) {
                            try {
                                val = Integer.parseInt(option);
                            } catch(NumberFormatException e) {
                                throw new AssertionError("Rule \"" + name + "\" has invalid option \"" + option + "\"");
                            }
                        } else if(field.getType() == double.class) {
                            try {
                                val = Double.parseDouble(option);
                            } catch(NumberFormatException e) {
                                throw new AssertionError("Rule \"" + name + "\" has invalid option \"" + option + "\"");
                            }
                        } else {
                            val = option;
                        }

                        if (val.equals(def)) {
                            containsDefault = true;
                        }
                    }

                    if (!containsDefault) {
                        throw new AssertionError("Default value of \"" + def + "\" for rule \"" + name + "\" is not included in its options.");
                    }
                }

                String validator = rule.validator();

                if(!validator.isEmpty()) {
                    Method method;

                    try {
                        method = Settings.class.getDeclaredMethod(validator, field.getType());
                    } catch(NoSuchMethodException e) {
                        throw new AssertionError("Validator \"" + validator + "\" for rule \"" + name + "\" doesn't exist");
                    }

                    if(!Modifier.isStatic(method.getModifiers()) || method.getReturnType() != boolean.class) {
                        throw new AssertionError("Validator \"" + validator + "\" for rule \"" + name + "\" must be a static method returning a boolean");
                    }
                }

                rules.put(name.toLowerCase(Locale.ENGLISH), field);
                defaults.put(name.toLowerCase(Locale.ENGLISH), String.valueOf(def));
            }
        }
    }

    public static boolean hasRule(String ruleName) {
        return rules.containsKey(ruleName.toLowerCase(Locale.ENGLISH));
    }

    public static String get(String ruleName) {
        Field field = rules.get(ruleName.toLowerCase(Locale.ENGLISH));

        if(field == null) {
            return "false";
        }

        try {
            return String.valueOf(field.get(null));
        } catch(ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

    public static String getDescription(String ruleName) {
        Field field = rules.get(ruleName.toLowerCase(Locale.ENGLISH));

        if(field == null) {
            return "Error";
        }

        return field.getAnnotation(Rule.class).desc();
    }

    public static RuleCategory[] getCategories(String ruleName) {
        Field field = rules.get(ruleName.toLowerCase(Locale.ENGLISH));

        if(field == null) {
            return new RuleCategory[0];
        }

        return field.getAnnotation(Rule.class).category();
    }

    public static String getDefault(String ruleName) {
        String def = defaults.get(ruleName.toLowerCase(Locale.ENGLISH));
        return def == null ? "false" : locked && ruleName.startsWith("command") ? "false" : def;
    }

    @SuppressWarnings("unchecked")
    public static String[] getOptions(String ruleName) {
        Field field = rules.get(ruleName.toLowerCase(Locale.ENGLISH));

        if(field == null || field.getType() == boolean.class) {
            return new String[] {"false", "true"};
        } else {
            return field.getAnnotation(Rule.class).options();
        }
    }

    public static String[] getExtraInfo(String ruleName) {
        Field field = rules.get(ruleName.toLowerCase(Locale.ENGLISH));

        if(field == null) {
            return new String[0];
        }

        return field.getAnnotation(Rule.class).extra();
    }

    public static String getActualName(String ruleName) {
        Field field = rules.get(ruleName.toLowerCase(Locale.ENGLISH));

        if(field == null) {
            return "null";
        }

        String name = field.getAnnotation(Rule.class).name();
        return name.isEmpty() ? field.getName() : name;
    }

    public static boolean isDouble(String ruleName) {
        Field field = rules.get(ruleName.toLowerCase(Locale.ENGLISH));

        if(field == null) {
            return false;
        } else {
            return field.getType() == double.class;
        }
    }

    @SuppressWarnings("unchecked")
    public static boolean set(String ruleName, String value) {
        Field field = rules.get(ruleName.toLowerCase(Locale.ENGLISH));

        if(field == null) {
            return false;
        }

        Class<?> fieldType = field.getType();
        Object newValue;

        if(fieldType == boolean.class) {
            if("true".equalsIgnoreCase(value)) {
                newValue = true;
            } else if("false".equalsIgnoreCase(value)) {
                newValue = false;
            } else {
                return false;
            }
        } else if(fieldType == int.class) {
            try {
                newValue = new Integer(value);
            } catch(NumberFormatException e) {
                return false;
            }
        } else if(fieldType == double.class) {
            try {
                newValue = new Double(value);
            } catch(NumberFormatException e) {
                return false;
            }
        } else if(fieldType == String.class) {
            newValue = value;
        } else {
            throw new AssertionError("Rule \"" + ruleName + "\" has an invalid type");
        }

        String validatorMethod = field.getDeclaredAnnotation(Rule.class).validator();

        if(!validatorMethod.isEmpty()) {
            try {
                Method validator = Settings.class.getDeclaredMethod(validatorMethod, field.getType());

                if(!((Boolean)validator.invoke(null, newValue))) {
                    return false;
                }
            } catch(ReflectiveOperationException e) {
                throw new AssertionError(e);
            }
        }

        try {
            field.set(null, newValue);
        } catch(ReflectiveOperationException e) {
            throw new AssertionError(e);
        }

        return true;
    }

    public static String[] findNonDefault() {
        ArrayList<String> nonDefault = new ArrayList<String>();

        for(String rule : rules.keySet()) {
            if(!get(rule).equalsIgnoreCase(getDefault(rule))) {
                nonDefault.add(getActualName(rule));
            }
        }

        Collections.sort(nonDefault);
        return nonDefault.toArray(new String[0]);
    }

    public static String[] findAll(String filter) {
        String actualFilter = filter == null ? null : filter.toLowerCase(Locale.ENGLISH);
        ArrayList<String> filtered = new ArrayList<String>();

        for(String rule : rules.keySet()) {
            if(actualFilter == null || rule.contains(actualFilter)) {
                filtered.add(rule);
                continue;
            }

            for(RuleCategory ctgy : getCategories(rule)) {
                if(ctgy.name().equalsIgnoreCase(actualFilter)) {
                    filtered.add(rule);
                    break;
                }
            }
        }

        return filtered.toArray(new String[0]);
    }

    public static void resetToUserDefaults() {
        resetToVanilla();
        applyRulesFromConf();
    }

    public static void resetToVanilla() {
        for(String rule : rules.keySet()) {
            set(rule, getDefault(rule));
        }
    }

    public static void resetToBugFixes() {
        resetToVanilla();

        for(Map.Entry<String, Field> rule : rules.entrySet()) {
            if(rule.getValue().isAnnotationPresent(RuleDefaults.BugFix.class)) {
                set(rule.getKey(), rule.getValue().getAnnotation(RuleDefaults.BugFix.class).value());
            }
        }
    }

    public static void resetToSurvival() {
        resetToVanilla();

        for(Map.Entry<String, Field> rule : rules.entrySet()) {
            if(rule.getValue().isAnnotationPresent(RuleDefaults.Survival.class)) {
                set(rule.getKey(), rule.getValue().getAnnotation(RuleDefaults.Survival.class).value());
            }
        }
    }

    public static void resetToCreative() {
        resetToVanilla();

        for(Map.Entry<String, Field> rule : rules.entrySet()) {
            if(rule.getValue().isAnnotationPresent(RuleDefaults.Creative.class)) {
                set(rule.getKey(), rule.getValue().getAnnotation(RuleDefaults.Creative.class).value());
            }
        }
    }

    public static void applyRulesFromConf() {
        Map<String, String> conf = readConf();
        boolean is_locked = locked;
        locked = false;

        if(is_locked) {
            LOGGER.info("Tapestry is locked by the administrator");
        }

        for(String key : conf.keySet()) {
            if(!set(key, conf.get(key))) {
                LOGGER.error("The value of " + conf.get(key) + " for " + key + " is not valid - ignoring...");
            }
            else
            {
                LOGGER.info("Loaded rule " + key + " as " + conf.get(key) + " from tapestry.conf");
            }
        }

        locked = is_locked;
    }

    private static Map<String, String> readConf() {
        try {
            File rules_file = MinecraftServer.getServer().getFile("tapestry.conf");
            BufferedReader b = new BufferedReader(new FileReader(rules_file));
            String line = "";
            Map<String, String> result = new HashMap<String, String>();

            while((line = b.readLine()) != null) {
                line = line.replaceAll("\\r|\\n", "");

                if("locked".equalsIgnoreCase(line)) {
                    locked = true;
                }

                String[] fields = line.split("\\s+", 2);

                if(fields.length > 1) {
                    if(!hasRule(fields[0])) {
                        LOGGER.error("[Tapesty]: Rule " + fields[0] + " is not valid - ignoring...");
                        continue;
                    }

                    result.put(fields[0], fields[1]);
                }
            }

            b.close();
            return result;
        } catch(FileNotFoundException e) {
            return new HashMap<String, String>();
        } catch(IOException e) {
            e.printStackTrace();
            return new HashMap<String, String>();
        }
    }

    private static void writeConf(Map<String, String> values) {
        if (locked) {
            return;
        }

        try {
            File rules_file = MinecraftServer.getServer().getFile("tapestry.conf");
            FileWriter fw = new FileWriter(rules_file);

            for(String key : values.keySet()) {
                fw.write(key + " " + values.get(key) + "\n");
            }

            fw.close();
        } catch(IOException e) {
            e.printStackTrace();
            LOGGER.error("Failed to write tapestry.conf");
        }
    }

    public static boolean addOrSetOverride(String ruleName, String ruleValue) {
        if(locked) {
            return false;
        }

        if(hasRule(ruleName)) {
            Map<String, String> conf = readConf();
            conf.put(ruleName, ruleValue);
            writeConf(conf);
            return set(ruleName, ruleValue);
        }

        return false;
    }

    public static boolean removeOverride(String ruleName) {
        if(locked) {
            return false;
        }

        if(hasRule(ruleName)) {
            Map<String, String> conf = readConf();
            conf.remove(ruleName);
            writeConf(conf);
            return true;
        }

        return false;
    }

    public static String[] findStartupOverrides() {
        ArrayList<String> res = new ArrayList<String>();

        if(locked) {
            return res.toArray(new String[0]);
        }

        Map <String, String> def = readConf();

        for(String rule : def.keySet()) {
            if(hasRule(rule)) {
                res.add(rule);
            }
        }

        return res.toArray(new String[0]);
    }
}
