package net.nullspace_mc.tapestry.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.server.command.exception.CommandException;
import net.minecraft.server.command.exception.IncorrectUsageException;
import net.minecraft.server.command.source.CommandSource;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Formatting;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

import net.nullspace_mc.tapestry.settings.ParsedRule;
import net.nullspace_mc.tapestry.settings.RuleCategory;
import net.nullspace_mc.tapestry.settings.SettingsManager;

public class TapCommand extends TapestryCommand {

    @Override
    public String getName() {
        return "tap";
    }

    @Override
    public String getUsage(CommandSource source) {
        return "/tap <rule> <value>";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    private Text displayInteractiveSetting(String ruleName) {
        ParsedRule<?> rule = SettingsManager.getParsedRule(ruleName);
        String name = rule.name;
        String def = rule.def;
        String val = rule.getValueString();
        Text out = new LiteralText(" - " + name + " ");
        out.getStyle().setColor(Formatting.WHITE);
        out.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tap " + name));
        Text hoverText = new LiteralText(rule.desc);
        hoverText.getStyle().setColor(Formatting.YELLOW);
        out.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText));

        for (String option : rule.options) {
            Text opt = new LiteralText("[" + option + "]");
            opt.getStyle().setColor(val.equalsIgnoreCase(def) ? Formatting.GRAY : (option.equalsIgnoreCase(def) ? Formatting.YELLOW : Formatting.DARK_GREEN));

            if (option.equalsIgnoreCase(def)) {
                opt.getStyle().setBold(Boolean.TRUE);
            } else if (option.equalsIgnoreCase(val)) {
                opt.getStyle().setUnderlined(Boolean.TRUE);
            }

            if (!option.equalsIgnoreCase(val)) {
                opt.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tap " + name + " " + option));
                Text optHoverText = new LiteralText("Switch to " + option);
                optHoverText.getStyle().setColor(Formatting.WHITE);
                opt.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, optHoverText));
            }

            out.append(opt);
        }

        return out;
    }

    private void listRules(CommandSource source, String title, String[] rulesList) {
        if (source instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity)source;
            Text titleText = new LiteralText(title + ":");
            titleText.getStyle().setColor(Formatting.WHITE);
            titleText.getStyle().setBold(Boolean.TRUE);
            player.sendMessage(titleText);

            for (String rule : rulesList) {
                player.sendMessage(displayInteractiveSetting(rule));
            }
        } else {
            source.sendMessage(new LiteralText(title + ":"));

            for (String rule : rulesList) {
                source.sendMessage(new LiteralText(" - " + rule));
            }
        }
    }

    @Override
    public void run(CommandSource source, String[] args) {
        if (SettingsManager.locked) {
            listRules(source, "Tapestry locked with the following rules", SettingsManager.findNonDefault());
            return;
        }

        String tag = null;

        if (args.length == 0) {
            listRules(source, "Current Tapestry Settings", SettingsManager.findNonDefault());

            if (source instanceof PlayerEntity) {
                Text categoriesHeader = new LiteralText("Browse Categories:");
                categoriesHeader.getStyle().setColor(Formatting.WHITE);
                source.sendMessage(categoriesHeader);
                Text categories = new LiteralText("");
                boolean delimit = false;

                for (RuleCategory ctgy : RuleCategory.values()) {
                    if (delimit) {
                        Text delim = new LiteralText(" ");
                        delim.getStyle().setColor(Formatting.WHITE);
                        categories.append(delim);
                    }

                    delimit = true;
                    String ctgyName = ctgy.name().toLowerCase();
                    Text category = new LiteralText("[" + ctgyName + "]");
                    category.getStyle().setColor(Formatting.AQUA);
                    Text ctgyHoverText = new LiteralText("List all " + ctgyName + " settings");
                    ctgyHoverText.getStyle().setColor(Formatting.GRAY);
                    category.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, ctgyHoverText));
                    category.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tap list " + ctgyName));
                    categories.append(category);
                }

                source.sendMessage(categories);
            }

            return;
        }

        if (args.length == 1 && "list".equalsIgnoreCase(args[0])) {
            listRules(source, "All Tapestry Settings", SettingsManager.findAll(null));
            return;
        }

        if (args.length == 1 && "use".equalsIgnoreCase(args[0])) {
            throw new IncorrectUsageException("/tap use <preset>");
        }

        if ("defaults".equalsIgnoreCase(args[0])) {
            listRules(source, "Current Tapestry Defaults from tapestry.conf", SettingsManager.findStartupOverrides());
            return;
        }

        if ("use".equalsIgnoreCase(args[0])) {
            if ("default".equalsIgnoreCase(args[1])) {
                SettingsManager.resetToConf();
                sendSuccess(source, "Set all rules to user defaults");
                return;
            }

            if ("vanilla".equalsIgnoreCase(args[1])) {
                SettingsManager.resetToVanilla();
                sendSuccess(source, "Set all rules to vanilla");
                return;
            }

            if ("survival".equalsIgnoreCase(args[1])) {
                SettingsManager.resetToSurvival();
                sendSuccess(source, "Set all rules to survival defaults");
                return;
            }

            if ("creative".equalsIgnoreCase(args[1])) {
                SettingsManager.resetToCreative();
                sendSuccess(source, "Set all rules to creative defaults");
                return;
            }

            if ("bugfixes".equalsIgnoreCase(args[1])) {
                SettingsManager.resetToBugFix();
                sendSuccess(source, "Set all rules to bugfix defaults");
                return;
            }

            throw new IncorrectUsageException("/tap use <preset>");
        }

        if (args.length >= 2 && "list".equalsIgnoreCase(args[0])) {
            tag = args[1].toLowerCase();
            args = Arrays.copyOfRange(args, 2, args.length);
        }

        if (args.length == 0) {
            listRules(source, "Tapestry rules matching \"" + tag + "\"", SettingsManager.findAll(tag));
            return;
        }

        if ("setDefault".equalsIgnoreCase(args[0])) {
            if (args.length == 2 && "current".equalsIgnoreCase(args[1])) {
                for (String override : SettingsManager.findStartupOverrides()) {
                    SettingsManager.removeOverride(override);
                }
                for (String current : SettingsManager.findNonDefault()) {
                    SettingsManager.addOrSetOverride(current, SettingsManager.getRule(current));
                }

                sendSuccess(source, "All current rules will be set upon restart");
                return;
            }

            if (args.length >= 2 && !SettingsManager.hasRule(args[1])) {
                throw new CommandException("Unknown rule: " + args[1]);
            }

            if (args.length != 3) {
                throw new IncorrectUsageException("/tap setDefault <rule|current> [value]");
            }

            boolean success = SettingsManager.addOrSetOverride(args[1], args[2]);

            if (success) {
                sendSuccess(source, SettingsManager.getParsedRule(args[1]).name + " will default to: " + args[2]);
            } else {
                throw new CommandException(args[2] + " is not a legal value for " + SettingsManager.getParsedRule(args[1]).name);
            }

            return;
        }

        if ("removeDefault".equalsIgnoreCase(args[0])) {
            if (args.length != 2) {
                throw new IncorrectUsageException("/tap removeDefault <rule|all>");
            }

            if ("all".equalsIgnoreCase(args[1])) {
                for (String override : SettingsManager.findStartupOverrides()) {
                    SettingsManager.removeOverride(override);
                }

                sendSuccess(source, "All rules will not be set upon restart");
                return;
            }

            boolean success = SettingsManager.removeOverride(args[1]);

            if (success) {
                sendSuccess(source, SettingsManager.getParsedRule(args[1]).name + " will not be set upon restart");
            } else {
                throw new CommandException("Unknown rule: " + args[1]);
            }

            return;
        }

        if (!SettingsManager.hasRule(args[0])) {
            throw new CommandException("Unknown rule: " + args[0]);
        }

        if (args.length == 2) {
            boolean success = SettingsManager.set(args[0], args[1]);

            if (!success) {
                throw new IncorrectUsageException(getUsage(source));
            }

            Text successMsg = new LiteralText(SettingsManager.getParsedRule(args[0]).name + ": " + SettingsManager.getRule(args[0]) + ", ");
            successMsg.getStyle().setColor(Formatting.WHITE);
            Text setDefault = new LiteralText("[change permanently?]");
            setDefault.getStyle().setColor(Formatting.AQUA);
            Text setDefaultHoverText = new LiteralText("Click to keep the rule in tapestry.conf to save across restarts");
            setDefaultHoverText.getStyle().setColor(Formatting.WHITE);
            setDefault.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, setDefaultHoverText));
            setDefault.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tap setDefault " + SettingsManager.getParsedRule(args[0]).name + " " + args[1]));
            successMsg.append(setDefault);
            source.sendMessage(successMsg);
            return;
        }

        if (source instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity)source;
            player.sendMessage(new LiteralText(""));
            Text refreshButton = new LiteralText(args[0]);
            refreshButton.getStyle().setColor(Formatting.WHITE);
            refreshButton.getStyle().setBold(Boolean.TRUE);
            Text refreshButtonHoverText = new LiteralText("refresh");
            refreshButtonHoverText.getStyle().setColor(Formatting.WHITE);
            refreshButton.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, refreshButtonHoverText));
            refreshButton.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tap " + args[0]));
            player.sendMessage(refreshButton);

            ParsedRule<?> rule = SettingsManager.getParsedRule(args[0]);

            player.sendMessage(new LiteralText(rule.desc));
            for (String info : rule.extra) {
                Text infoText = new LiteralText(" " + info);
                infoText.getStyle().setColor(Formatting.GRAY);
                player.sendMessage(infoText);
            }

            Text tags = new LiteralText("Tags :");
            tags.getStyle().setColor(Formatting.WHITE);
            boolean delimit = false;

            for (RuleCategory ctgy : rule.category) {
                if (delimit) {
                    Text delim = new LiteralText(", ");
                    delim.getStyle().setColor(Formatting.WHITE);
                    tags.append(delim);
                }

                delimit = true;
                String tagName = ctgy.name().toLowerCase();
                Text tagText = new LiteralText("[" + tagName + "]");
                tagText.getStyle().setColor(Formatting.AQUA);
                Text tagHoverText = new LiteralText("list all " + tagName + " rules");
                tagHoverText.getStyle().setColor(Formatting.GRAY);
                tagText.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, tagHoverText));
                tagText.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tap list " + tagName));
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

            for (String o : rule.options) {
                if (delimit) {
                    Text delim = new LiteralText(" ");
                    delim.getStyle().setColor(Formatting.WHITE);
                    options.append(delim);
                }

                delimit = true;
                Text opt = new LiteralText(o);

                if (o.equals(rule.def)) {
                    opt.getStyle().setUnderlined(Boolean.TRUE);
                }

                if (o.equals(rule.getValueString())) {
                    opt.getStyle().setBold(Boolean.TRUE);
                    opt.getStyle().setColor(Formatting.GREEN);
                } else {
                    opt.getStyle().setColor(Formatting.YELLOW);
                }

                Text optHoverText = new LiteralText("Switch to " + o);
                optHoverText.getStyle().setColor(Formatting.GRAY);
                opt.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, optHoverText));
                opt.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tap " + SettingsManager.getParsedRule(args[0]).name + " " + o));
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

    @Override
    public List<String> getSuggestions(CommandSource source, String[] args) {
        if (SettingsManager.locked) {
            return Collections.<String>emptyList();
        }

        if (args.length == 2 && "list".equalsIgnoreCase(args[0])) {
            RuleCategory[] categoryEnums = RuleCategory.values();
            String[] categories = new String[categoryEnums.length];

            for (int i = 0; i < categoryEnums.length; i++) {
                categories[i] = categoryEnums[i].toString().toLowerCase();
            }

            return suggestMatching(args, categories);
        }

        if (args.length == 2 && "use".equalsIgnoreCase(args[0])) {
            return suggestMatching(args, new String[] {"survival", "creative", "default", "vanilla", "bugfixes"});
        }

        String tag = null;

        if (args.length > 2 && "list".equalsIgnoreCase(args[0])) {
            tag = args[1].toLowerCase();
            args = Arrays.copyOfRange(args, 2, args.length);
        }

        if (args.length == 1) {
            List<String> lst = new ArrayList<String>();

            if ((tag != null) || (args[0].length() > 0)) {
                for (String rule : SettingsManager.findAll(tag)) {
                    lst.add(rule);
                }
            }

            lst.add("setDefault");
            lst.add("removeDefault");

            if (tag == null) {
                lst.add("defaults");
                lst.add("use");
                lst.add("list");
            }

            return suggestMatching(args, lst.toArray(new String[0]));
        }

        if (args.length == 2 && !"defaults".equalsIgnoreCase(args[0])) {
            if ("setDefault".equalsIgnoreCase(args[0])) {
                String[] rules = SettingsManager.findAll(tag);
                String[] setDefaultOpts = new String[rules.length + 1];

                for (int i = 0; i < rules.length; i++) {
                    setDefaultOpts[i] = rules[i];
                }

                setDefaultOpts[setDefaultOpts.length - 1] = "current";
                return suggestMatching(args, setDefaultOpts);
            }

            if ("removeDefault".equalsIgnoreCase(args[0])) {
                String[] rules = SettingsManager.findStartupOverrides();
                String[] removeDefaultOpts = new String[rules.length + 1];

                for (int i = 0; i < rules.length; i++) {
                    removeDefaultOpts[i] = rules[i];
                }

                removeDefaultOpts[removeDefaultOpts.length - 1] = "all";
                return suggestMatching(args, removeDefaultOpts);
            }

            return suggestMatching(args, SettingsManager.getParsedRule(args[0]).options);
        }

        if (args.length == 3 && "setDefault".equalsIgnoreCase(args[0])) {
            return suggestMatching(args, SettingsManager.getParsedRule(args[1]).options);
        }

        return Collections.<String>emptyList();
    }
}
