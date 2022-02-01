package net.nullspace_mc.tapestry.command;

import net.minecraft.command.CommandSource;
import net.minecraft.command.IncorrectUsageException;
import net.minecraft.server.MinecraftServer;
import net.nullspace_mc.tapestry.helpers.TickSpeedHelper;
import net.nullspace_mc.tapestry.settings.Settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TickCommand extends TapestryAbstractCommand {

    @Override
    public String getName() {
        return "tick";
    }

    @Override
    public String getUsageTranslationKey(CommandSource source) {
        return "/tick rate [tickRate] | /tick warp [advance]";
    }

    @Override
    public int getPermissionLevel() {
        return Settings.commandTick ? 2 : 5;
    }

    @Override
    public void execute(CommandSource source, String[] args) {
        if (args.length == 0 | args.length > 2) throw new IncorrectUsageException(getUsageTranslationKey(source));
        if (args[0].equals("rate")) {
            if (args.length == 2) {
                TickSpeedHelper.setTickRate(parseDouble(source, args[1], 0.05D));
            }
            sendFeedback(source, String.format("Current tick rate is %.1f", TickSpeedHelper.tickRate), new Object[0]);
        } else if (args[0].equals("warp")) {
            long ticksToWarp = args.length == 2 ? (args[1].equalsIgnoreCase("stop") ? 0 : (long)parseInt(source, args[1], 0)) : (TickSpeedHelper.warping ? 0L : -1L);
            if (ticksToWarp != 0) {
                sendFeedback(source, "Started tick warp", new Object[0]);
            }
            TickSpeedHelper.startTickWarp(source, ticksToWarp);
        } else throw new IncorrectUsageException(getUsageTranslationKey(source));
    }

    @Override
    public List<String> getSuggestions(CommandSource source, String[] args) {
        if (!Settings.commandTick) return Collections.emptyList();
        if (args.length > 2) return new ArrayList<>();
        List<String> suggestions = new ArrayList<>();
        String prefix = args[args.length - 1];
        if (args.length == 1) suggestions.addAll(Arrays.asList("rate", "warp"));
        else if (args[0].equals("rate")) suggestions.addAll(Arrays.asList("20", "10", "40"));
        else if (args[0].equals("warp")) suggestions.addAll(Arrays.asList("3600", "72000", "stop"));
        suggestions.removeIf(suggestion -> !suggestion.startsWith(prefix));
        return suggestions;
    }
}
