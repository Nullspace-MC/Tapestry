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
import java.util.LinkedList;
import java.util.List;
import java.util.Collections;
import java.util.TreeMap;
import java.util.Map;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SettingsManager {

    public static boolean locked = false;

    public static final Logger LOGGER = LogManager.getLogger();

    public static final TreeMap<String, ParsedRule> rules = new TreeMap<String, ParsedRule>();

    public static void parseRules() {
        for (Field f : Settings.class.getDeclaredFields()) {
            Rule r = f.getAnnotation(Rule.class);
            if (r == null) continue;
            ParsedRule pr = new ParsedRule(r, f);
            rules.put(pr.name.toLowerCase(), pr);
        }
    }

    public static boolean hasRule(String ruleName) {
        return rules.containsKey(ruleName.toLowerCase());
    }


    public static String getRule(String ruleName) {
        ParsedRule pr = rules.get(ruleName.toLowerCase());

        if (pr == null) {
            return "false";
        }

        try {
            return String.valueOf(pr.field.get(null));
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

    public static ParsedRule getParsedRule(String ruleName) {
        return rules.get(ruleName.toLowerCase());
    }

    public static boolean set(String ruleName, String value) {
        return rules.get(ruleName.toLowerCase()).setValue(value);
    }

    public static String[] findNonDefault() {
        List<String> nonDefault = new LinkedList<String>();

        for (ParsedRule rule : rules.values()) {
            if (!rule.getValueString().equalsIgnoreCase(rule.def)) {
                nonDefault.add(rule.name);
            }
        }

        Collections.sort(nonDefault);
        return nonDefault.toArray(new String[0]);
    }

    public static String[] findAll(String filter) {
        String actualFilter = filter == null ? null : filter.toLowerCase();
        List<String> filtered = new LinkedList<String>();

        for (Map.Entry<String, ParsedRule> rule : rules.entrySet()) {
            if (actualFilter == null || rule.getKey().contains(actualFilter)) {
                filtered.add(rule.getValue().name);
                continue;
            }

            for (RuleCategory ctgy : rule.getValue().category) {
                if (ctgy.name().equalsIgnoreCase(actualFilter)) {
                    filtered.add(rule.getValue().name);
                    break;
                }
            }
        }

        return filtered.toArray(new String[0]);
    }

    public static void resetToVanilla() {
        for (ParsedRule rule : rules.values()) {
            rule.setValue(rule.def);
        }
    }

    public static void resetToConf() {
        resetToVanilla();
        applyConf();
    }

    public static void resetToSurvival() {
        resetToVanilla();
        for (ParsedRule rule : rules.values()) {
            if (rule.field.isAnnotationPresent(RuleDefaults.Survival.class)) {
                rule.setValue(rule.field.getAnnotation(RuleDefaults.Survival.class).value());
            }
        }
    }

    public static void resetToCreative() {
        resetToVanilla();
        for (ParsedRule rule : rules.values()) {
            if (rule.field.isAnnotationPresent(RuleDefaults.Creative.class)) {
                rule.setValue(rule.field.getAnnotation(RuleDefaults.Creative.class).value());
            }
        }
    }

    public static void resetToBugFix() {
        resetToVanilla();
        for (ParsedRule rule : rules.values()) {
            if (rule.field.isAnnotationPresent(RuleDefaults.BugFix.class)) {
                rule.setValue(rule.field.getAnnotation(RuleDefaults.BugFix.class).value());
            }
        }
    }

    public static void applyConf() {
        Map<String, String> conf = readConf();
        boolean is_locked = locked;
        locked = false;

        if (is_locked) {
            LOGGER.info("Tapestry is locked by the administrator");
        }

        for (Map.Entry<String, String> entry : conf.entrySet()) {
            if (!rules.get(entry.getKey().toLowerCase()).setValue(entry.getValue())) {
                LOGGER.error("The value of " + entry.getValue() + " for " + entry.getKey() + " is not valid - ignoring...");
            } else {
                LOGGER.info("Loaded rule " + entry.getKey() + " as " + entry.getValue() + " from tapestry.conf");
            }
        }

        locked = is_locked;
    }

    private static Map<String, String> readConf() {
        try {
            File rules_file = MinecraftServer.getServer().getFile("config/tapestry.conf");
            BufferedReader b = new BufferedReader(new FileReader(rules_file));
            String line = "";
            Map<String, String> result = new TreeMap<String, String>();

            while ((line = b.readLine()) != null) {
                line = line.replaceAll("\\r|\\n", "");

                if ("locked".equalsIgnoreCase(line)) {
                    locked = true;
                }

                String[] fields = line.split("\\s+", 2);

                if (fields.length > 1) {
                    if (!hasRule(fields[0])) {
                        LOGGER.error("Rule " + fields[0] + " is not valid - ignoring...");
                        continue;
                    }

                    result.put(rules.get(fields[0].toLowerCase()).name, fields[1]);
                }
            }

            b.close();
            return result;
        } catch (FileNotFoundException e) {
            return new TreeMap<String, String>();
        } catch (IOException e) {
            e.printStackTrace();
            return new TreeMap<String, String>();
        }
    }

    private static void writeConf(Map<String, String> values) {
        if (locked) {
            return;
        }

        try {
            File rules_file = MinecraftServer.getServer().getFile("config/tapestry.conf");
            FileWriter fw = new FileWriter(rules_file);

            for (String key : values.keySet()) {
                fw.write(key + " " + values.get(key) + "\n");
            }

            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("Failed to write tapestry.conf");
        }
    }

    public static boolean addOrSetOverride(String ruleName, String ruleValue) {
        if (locked) {
            return false;
        }

        if (hasRule(ruleName)) {
            Map<String, String> conf = readConf();
            conf.put(ruleName, ruleValue);
            writeConf(conf);
            return set(ruleName, ruleValue);
        }

        return false;
    }

    public static boolean removeOverride(String ruleName) {
        if (locked) {
            return false;
        }

        if (hasRule(ruleName)) {
            Map<String, String> conf = readConf();
            conf.remove(ruleName);
            writeConf(conf);
            return true;
        }

        return false;
    }

    public static String[] findStartupOverrides() {
        List<String> res = new LinkedList<String>();

        if (locked) {
            return new String[0];
        }

        Map <String, String> def = readConf();

        for (String rule : def.keySet()) {
            if (hasRule(rule)) {
                res.add(rule);
            }
        }

        return res.toArray(new String[0]);
    }
}
