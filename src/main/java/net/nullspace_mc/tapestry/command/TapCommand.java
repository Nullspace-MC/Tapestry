package net.nullspace_mc.tapestry.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import net.minecraft.command.AbstractCommand;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.IncorrectUsageException;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.ClickEventAction;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Action;
import net.minecraft.util.Formatting;
import net.nullspace_mc.tapestry.settings.ParsedRule;
import net.nullspace_mc.tapestry.settings.RuleCategory;
import net.nullspace_mc.tapestry.settings.SettingsManager;

public class TapCommand extends TapestryAbstractCommand {
    public String getCommandName() {
        return "tap";
    }

    public String getUsageTranslationKey(CommandSource source) {
        return "/tap <rule> <value>";
    }

    public int getPermissionLevel() {
        return 2;
    }

    private Text displayInteractiveSetting(String ruleName) {
        ParsedRule rule = SettingsManager.getParsedRule(ruleName);
        String def = rule.def;
        String val = rule.getValueString();
        Text out = new LiteralText(" - " + ruleName + " ");
        out.getStyle().setColor(Formatting.WHITE);
        out.getStyle().setClickEvent(new ClickEvent(ClickEventAction.RUN_COMMAND, "/tap " + ruleName));
        Text hoverText = new LiteralText(rule.desc);
        hoverText.getStyle().setColor(Formatting.YELLOW);
        out.getStyle().setHoverEvent(new HoverEvent(Action.SHOW_TEXT, hoverText));

        for(String option : rule.options) {
            Text opt = new LiteralText("[" + option + "]");
            opt.getStyle().setColor(val.equalsIgnoreCase(def) ? Formatting.GRAY : (option.equalsIgnoreCase(def) ? Formatting.YELLOW : Formatting.DARK_GREEN));

            if(option.equalsIgnoreCase(def)) {
                opt.getStyle().setBold(Boolean.TRUE);
            } else if(option.equalsIgnoreCase(val)) {
                opt.getStyle().setUnderline(Boolean.TRUE);
            }

            if(!option.equalsIgnoreCase(val)) {
                opt.getStyle().setClickEvent(new ClickEvent(ClickEventAction.RUN_COMMAND, "/tap " + ruleName + " " + option));
                Text optHoverText = new LiteralText("Switch to " + option);
                optHoverText.getStyle().setColor(Formatting.WHITE);
                opt.getStyle().setHoverEvent(new HoverEvent(Action.SHOW_TEXT, optHoverText));
            }

            out.append(opt);
        }

        return out;
    }

    private void listRules(CommandSource source, String title, String[] rulesList) {
        if(source instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity)source;
            Text titleText = new LiteralText(title + ":");
            titleText.getStyle().setColor(Formatting.WHITE);
            titleText.getStyle().setBold(Boolean.TRUE);
            player.sendMessage(titleText);

            for(String rule : rulesList) {
                player.sendMessage(displayInteractiveSetting(rule));
            }
        } else {
            source.sendMessage(new LiteralText(title + ":"));

            for(String rule : rulesList) {
                source.sendMessage(new LiteralText(" - " + rule));
            }
        }
    }

    public void execute(CommandSource source, String[] args) {
        if(SettingsManager.locked) {
            listRules(source, "Tapestry locked with the following rules", SettingsManager.findNonDefault());
            return;
        }

        String tag = null;

        if(args.length == 0) {
            listRules(source, "Current Tapestry Settings", SettingsManager.findNonDefault());

            if(source instanceof PlayerEntity) {
                Text categoriesHeader = new LiteralText("Browse Categories:");
                categoriesHeader.getStyle().setColor(Formatting.WHITE);
                source.sendMessage(categoriesHeader);
                Text categories = new LiteralText("");
                boolean delimit = false;

                for(RuleCategory ctgy : RuleCategory.values()) {
                    if(delimit) {
                        Text delim = new LiteralText(" ");
                        delim.getStyle().setColor(Formatting.WHITE);
                        categories.append(delim);
                    }

                    delimit = true;
                    String ctgyName = ctgy.name().toLowerCase(Locale.ENGLISH);
                    Text category = new LiteralText("[" + ctgyName + "]");
                    category.getStyle().setColor(Formatting.AQUA);
                    Text ctgyHoverText = new LiteralText("List all " + ctgyName + " settings");
                    ctgyHoverText.getStyle().setColor(Formatting.GRAY);
                    category.getStyle().setHoverEvent(new HoverEvent(Action.SHOW_TEXT, ctgyHoverText));
                    category.getStyle().setClickEvent(new ClickEvent(ClickEventAction.RUN_COMMAND, "/tap list " + ctgyName));
                    categories.append(category);
                }

                source.sendMessage(categories);
            }

            return;
        }

        if(args.length == 1 && "list".equalsIgnoreCase(args[0])) {
            listRules(source, "All Tapestry Settings", SettingsManager.findAll(null));
            return;
        }

        if(args.length == 1 && "use".equalsIgnoreCase(args[0])) {
            throw new IncorrectUsageException("/tap use <preset>", new Object[0]);
        }

        if("defaults".equalsIgnoreCase(args[0])) {
            listRules(source, "Current Tapestry Defaults from tapestry.conf", SettingsManager.findStartupOverrides());
            return;
        }

        if("use".equalsIgnoreCase(args[0])) {
            if("default".equalsIgnoreCase(args[1])) {
                SettingsManager.resetToConf();
                method_10772(source, "Set all rules to user defaults", new Object[0]);
                return;
            }

            if("vanilla".equalsIgnoreCase(args[1])) {
                SettingsManager.resetToVanilla();
                method_10772(source, "Set all rules to vanilla", new Object[0]);
                return;
            }

            if("survival".equalsIgnoreCase(args[1])) {
                SettingsManager.resetToSurvival();
                method_10772(source, "Set all rules to survival defaults", new Object[0]);
                return;
            }

            if("creative".equalsIgnoreCase(args[1])) {
                SettingsManager.resetToCreative();
                method_10772(source, "Set all rules to creative defaults", new Object[0]);
                return;
            }

            if("bugfixes".equalsIgnoreCase(args[1])) {
                SettingsManager.resetToBugFix();
                method_10772(source, "Set all rules to bugfix defaults", new Object[0]);
                return;
            }

            throw new IncorrectUsageException("/tap use <preset>", new Object[0]);
        }

        if(args.length >= 2 && "list".equalsIgnoreCase(args[0])) {
            tag = args[1].toLowerCase();
            args = Arrays.copyOfRange(args, 2, args.length);
        }

        if(args.length == 0) {
            listRules(source, "Tapestry rules matching \"" + tag + "\"", SettingsManager.findAll(tag));
            return;
        }

        if("setDefault".equalsIgnoreCase(args[0])) {
            if(args.length == 2 && "current".equalsIgnoreCase(args[1])) {
                for(String override : SettingsManager.findStartupOverrides()) {
                    SettingsManager.removeOverride(override);
                }
                for(String current : SettingsManager.findNonDefault()) {
                    SettingsManager.addOrSetOverride(current, SettingsManager.getRule(current));
                }

                method_10772(source, "All current rules will be set upon restart", new Object[0]);
                return;
            }

            if(args.length >= 2 && !SettingsManager.hasRule(args[1])) {
                throw new CommandException("Unknown rule: " + args[1], new Object[0]);
            }

            if(args.length != 3) {
                throw new IncorrectUsageException("/tap setDefault <rule|current> [value]", new Object[0]);
            }

            boolean success = SettingsManager.addOrSetOverride(args[1], args[2]);

            if(success) {
                method_10772(source, args[1] + " will default to: " + args[2], new Object[0]);
            } else {
                throw new CommandException(args[2] + " is not a legal value for " + args[1], new Object[0]);
            }

            return;
        }

        if("removeDefault".equalsIgnoreCase(args[0])) {
            if(args.length != 2) {
                throw new IncorrectUsageException("/tap removeDefault <rule|all>");
            }

            if("all".equalsIgnoreCase(args[1])) {
                for(String override : SettingsManager.findStartupOverrides()) {
                    SettingsManager.removeOverride(override);
                }

                method_10772(source, "All rules will not be set upon restart", new Object[0]);
                return;
            }

            boolean success = SettingsManager.removeOverride(args[1]);

            if(success) {
                method_10772(source, args[1] + " will not be set upon restart", new Object[0]);
            } else {
                throw new CommandException("Unknown rule: " + args[1], new Object[0]);
            }

            return;
        }

        if(!SettingsManager.hasRule(args[0])) {
            throw new CommandException("Unknown rule: " + args[0], new Object[0]);
        }

        if(args.length == 2) {
            boolean success = SettingsManager.set(args[0], args[1]);

            if(!success) {
                throw new IncorrectUsageException(getUsageTranslationKey(source));
            }

            Text successMsg = new LiteralText(args[0] + ": " + SettingsManager.getRule(args[0]) + ", ");
            successMsg.getStyle().setColor(Formatting.WHITE);
            Text setDefault = new LiteralText("[change permanently?]");
            setDefault.getStyle().setColor(Formatting.AQUA);
            Text setDefaultHoverText = new LiteralText("Click to keep the rule in tapestry.conf to save across restarts");
            setDefaultHoverText.getStyle().setColor(Formatting.WHITE);
            setDefault.getStyle().setHoverEvent(new HoverEvent(Action.SHOW_TEXT, setDefaultHoverText));
            setDefault.getStyle().setClickEvent(new ClickEvent(ClickEventAction.SUGGEST_COMMAND, "/tap setDefault " + args[0] + " " + args[1]));
            successMsg.append(setDefault);
            source.sendMessage(successMsg);
            return;
        }

        if(source instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity)source;
            player.sendMessage(new LiteralText(""));
            Text refreshButton = new LiteralText(args[0]);
            refreshButton.getStyle().setColor(Formatting.WHITE);
            refreshButton.getStyle().setBold(Boolean.TRUE);
            Text refreshButtonHoverText = new LiteralText("refresh");
            refreshButtonHoverText.getStyle().setColor(Formatting.WHITE);
            refreshButton.getStyle().setHoverEvent(new HoverEvent(Action.SHOW_TEXT, refreshButtonHoverText));
            refreshButton.getStyle().setClickEvent(new ClickEvent(ClickEventAction.RUN_COMMAND, "/tap " + args[0]));
            player.sendMessage(refreshButton);

            ParsedRule rule = SettingsManager.getParsedRule(args[0]);

            player.sendMessage(new LiteralText(rule.desc));
            for(String info : rule.extra) {
                Text infoText = new LiteralText(" " + info);
                infoText.getStyle().setColor(Formatting.GRAY);
                player.sendMessage(infoText);
            }

            Text tags = new LiteralText("Tags :");
            tags.getStyle().setColor(Formatting.WHITE);
            boolean delimit = false;

            for(RuleCategory ctgy : rule.category) {
                if(delimit) {
                    Text delim = new LiteralText(", ");
                    delim.getStyle().setColor(Formatting.WHITE);
                    tags.append(delim);
                }

                delimit = true;
                String tagName = ctgy.name().toLowerCase(Locale.ENGLISH);
                Text tagText = new LiteralText("[" + tagName + "]");
                tagText.getStyle().setColor(Formatting.AQUA);
                Text tagHoverText = new LiteralText("list all " + tagName + " rules");
                tagHoverText.getStyle().setColor(Formatting.GRAY);
                tagText.getStyle().setHoverEvent(new HoverEvent(Action.SHOW_TEXT, tagHoverText));
                tagText.getStyle().setClickEvent(new ClickEvent(ClickEventAction.RUN_COMMAND, "/tap list " + tagName));
                tags.append(tagText);
            }

            player.sendMessage(tags);
            Text current = new LiteralText("Current value: ");
            current.getStyle().setColor(Formatting.WHITE);
            Text value = new LiteralText(String.format("%s (%s value)", rule.getValueString(), rule.getValueString().equalsIgnoreCase(rule.def) ? "default" : "modified"));
            value.getStyle().setColor(rule.getValueString().equalsIgnoreCase("true") ? Formatting.GREEN : Formatting.DARK_RED);
            value.getStyle().setBold(Boolean.TRUE);
            current.append(value);
            player.sendMessage(current);
            Text options = new LiteralText("Options: ");
            options.getStyle().setColor(Formatting.WHITE);
            Text optionsLeftBracket = new LiteralText("[ ");
            optionsLeftBracket.getStyle().setColor(Formatting.YELLOW);
            options.append(optionsLeftBracket);
            delimit = false;

            for(String o : rule.options) {
                if(delimit) {
                    Text delim = new LiteralText(" ");
                    delim.getStyle().setColor(Formatting.WHITE);
                    options.append(delim);
                }

                delimit = true;
                Text opt = new LiteralText(o);

                if(o.equals(rule.def)) {
                    opt.getStyle().setUnderline(Boolean.TRUE);
                }

                if(o.equals(rule.getValueString())) {
                    opt.getStyle().setBold(Boolean.TRUE);
                    opt.getStyle().setColor(Formatting.GREEN);
                } else {
                    opt.getStyle().setColor(Formatting.YELLOW);
                }

                Text optHoverText = new LiteralText("Switch to " + o);
                optHoverText.getStyle().setColor(Formatting.GRAY);
                opt.getStyle().setHoverEvent(new HoverEvent(Action.SHOW_TEXT, optHoverText));
                opt.getStyle().setClickEvent(new ClickEvent(ClickEventAction.SUGGEST_COMMAND, "/tap " + args[0] + " " + o));
                options.append(opt);
            }

            Text optionsRightBracket = new LiteralText(" ]");
            optionsRightBracket.getStyle().setColor(Formatting.YELLOW);
            options.append(optionsRightBracket);
            player.sendMessage(options);
        } else {
            Text current = new LiteralText(args[0] + " is set to: " + SettingsManager.getRule(args[0]));
            source.sendMessage(current);
        }
    }

    public List method_8398(CommandSource source, String[] args) {
        if(SettingsManager.locked) {
            return Collections.<String>emptyList();
        }

        if(args.length == 2 && "list".equalsIgnoreCase(args[0])) {
            RuleCategory[] categoryEnums = RuleCategory.values();
            String[] categories = new String[categoryEnums.length];

            for(int i = 0; i < categoryEnums.length; i++) {
                categories[i] = categoryEnums[i].toString().toLowerCase(Locale.ENGLISH);
            }

            return method_10786(args, categories);
        }

        if(args.length == 2 && "use".equalsIgnoreCase(args[0])) {
            return method_10786(args, new String[] {"survival", "creative", "default", "vanilla", "bugfixes"});
        }

        String tag = null;

        if(args.length > 2 && "list".equalsIgnoreCase(args[0])) {
            tag = args[1].toLowerCase();
            args = Arrays.copyOfRange(args, 2, args.length);
        }

        if(args.length == 1) {
            List<String> lst = new ArrayList<String>();

            if((tag != null) || (args[0].length() > 0)) {
                for(String rule : SettingsManager.findAll(tag)) {
                    lst.add(rule);
                }
            }

            lst.add("setDefault");
            lst.add("removeDefault");

            if(tag == null) {
                lst.add("defaults");
                lst.add("use");
                lst.add("list");
            }

            return method_10786(args, lst.toArray(new String[0]));
        }

        if(args.length == 2 && !"defaults".equalsIgnoreCase(args[0])) {
            if("setDefault".equalsIgnoreCase(args[0])) {
                String[] rules = SettingsManager.findAll(tag);
                String[] setDefaultOpts = new String[rules.length + 1];

                for(int i = 0; i < rules.length; i++) {
                    setDefaultOpts[i] = rules[i];
                }

                setDefaultOpts[setDefaultOpts.length - 1] = "current";
                return method_10786(args, setDefaultOpts);
            }

            if("removeDefault".equalsIgnoreCase(args[0])) {
                String[] rules = SettingsManager.findStartupOverrides();
                String[] removeDefaultOpts = new String[rules.length + 1];

                for(int i = 0; i < rules.length; i++) {
                    removeDefaultOpts[i] = rules[i];
                }

                removeDefaultOpts[removeDefaultOpts.length - 1] = "all";
                return method_10786(args, removeDefaultOpts);
            }

            return method_10786(args, SettingsManager.getParsedRule(args[0]).options);
        }

        if(args.length == 3 && "setDefault".equalsIgnoreCase(args[0])) {
            return method_10786(args, SettingsManager.getParsedRule(args[1]).options);
        }

        return Collections.<String>emptyList();
    }
}
