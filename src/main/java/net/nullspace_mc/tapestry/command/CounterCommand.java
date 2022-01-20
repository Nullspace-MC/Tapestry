package net.nullspace_mc.tapestry.command;

import net.minecraft.command.CommandSource;
import net.minecraft.command.IncorrectUsageException;
import net.minecraft.text.LiteralText;
import net.nullspace_mc.tapestry.counter.Counter;
import net.nullspace_mc.tapestry.counter.CounterRegistry;
import net.nullspace_mc.tapestry.util.MathUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CounterCommand extends TapestryAbstractCommand {
    @Override
    public String getName() {
        return "counter";
    }

    @Override
    public String getUsageTranslationKey(CommandSource source) {
        return "/counter <channel|all> [reset]";
    }

    @Override
    public void execute(CommandSource source, String[] args) {
        if (args.length == 0) throw new IncorrectUsageException(getUsageTranslationKey(source));
        if (!args[0].equalsIgnoreCase("all") && !CounterRegistry.getCounterColors().contains(args[0].toLowerCase())) throw new IncorrectUsageException("Unrecognized counter \"" + args[0] + "\"");

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("all")) {
                boolean none = true;
                for (String color : CounterRegistry.getCounterColors()) {
                    if (CounterRegistry.getCounter(color).getTotalCount() != 0) {
                        sendCounterInfo(color, source);
                        none = false;
                    }
                }
                if (none) source.sendMessage(new LiteralText("All counters empty"));
            } else {
                sendCounterInfo(args[0].toLowerCase(), source);
            }
        } else if (args.length == 2 && args[1].equalsIgnoreCase("reset")) {
            if (args[0].equalsIgnoreCase("all")) {
                for (String color : CounterRegistry.getCounterColors()) {
                    CounterRegistry.getCounter(color).resetCounter();
                }
                source.sendMessage(new LiteralText("All counters reset"));
            } else {
                CounterRegistry.getCounter(args[0].toLowerCase()).resetCounter();
                source.sendMessage(new LiteralText(String.format("Counter %s reset", args[0].toLowerCase())));
            }
        } else {
            throw new IncorrectUsageException(getUsageTranslationKey(source));
        }
    }

    @Override
    public List<String> getSuggestions(CommandSource source, String[] args) {
        String prefix = args[args.length - 1].toLowerCase();
        List<String> suggestions = new ArrayList<>();
        if (args.length == 1) {
            suggestions.addAll(CounterRegistry.getCounterColors());
            suggestions.add("all");
        } else if (args.length == 2) suggestions.add("reset");
        suggestions.removeIf(suggestion -> !suggestion.startsWith(prefix));
        return suggestions;
    }

    public void sendCounterInfo(String counterColor, CommandSource source) {
        Counter counter = CounterRegistry.getCounter(counterColor);
        Set<String> nameSet = counter.getCounterMap().keySet();
        if (nameSet.isEmpty()) source.sendMessage(new LiteralText(String.format("The counter %s has no item", counterColor)));
        double hoursRunning = counter.getRunningTime() / 72000D;
        source.sendMessage(new LiteralText(String.format("Counter %s (running for %s) : %d items | %f items/h", counterColor, MathUtil.getFancyTime(hoursRunning), counter.getTotalCount(), MathUtil.round(2, counter.getTotalCount() / hoursRunning))));
        for (String itemName : nameSet) source.sendMessage(new LiteralText(String.format("%s : %d | %f items/h", itemName, counter.getCounterMap().get(itemName), MathUtil.round(2, counter.getCounterMap().get(itemName) / hoursRunning))));
    }
}
