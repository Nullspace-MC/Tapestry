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
        return "/counter <channel> reset";
    }

    @Override
    public void execute(CommandSource source, String[] args) {
        if (!CounterRegistry.getCounterColors().contains(args[0]) || args.length > 2) throw new IncorrectUsageException(getUsageTranslationKey(source));
        if (args.length == 1) sendCounterInfo(args[0], source);
        else if (args[1].equals("reset")) {
            CounterRegistry.getCounter(args[0]).resetCounter();
            source.sendMessage(new LiteralText(String.format("Counter %s reset", args[0])));
        }
    }

    @Override
    public List<String> getSuggestions(CommandSource source, String[] args) {
        String prefix = args[args.length - 1];
        List<String> suggestions = new ArrayList<>();
        if (args.length == 1) suggestions.addAll(CounterRegistry.getCounterColors());
        else if (args.length == 2) suggestions.add("reset");
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
