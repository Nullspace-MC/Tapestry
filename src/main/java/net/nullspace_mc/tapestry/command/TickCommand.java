package net.nullspace_mc.tapestry.command;

import net.minecraft.command.CommandSource;
import net.minecraft.command.IncorrectUsageException;
import net.minecraft.server.MinecraftServer;
import net.nullspace_mc.tapestry.helpers.TickSpeedHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TickCommand extends TapestryAbstractCommand {
    @Override
    public String getName() {
        return "tick";
    }

    @Override
    public String getUsageTranslationKey(CommandSource source) {
        return "/tick rate|warp <tick_rate>|<amount_of_ticks>";
    }

    @Override
    public void execute(CommandSource source, String[] args) {
        if (args.length == 0 | args.length > 2) throw new IncorrectUsageException(getUsageTranslationKey(source));
        if (args[0].equals("rate")) {
            if (args.length == 1) {
                TickSpeedHelper.setTickRate(20);
            } else {
                try {
                    TickSpeedHelper.setTickRate(Integer.parseInt(args[1]));
                } catch (NumberFormatException ignored) {
                    throw new IncorrectUsageException(getUsageTranslationKey(source));
                }
            }
        } else if (args[0].equals("warp")) {
            if (args.length == 1) {
                TickSpeedHelper.startTickWarp(Integer.MAX_VALUE - MinecraftServer.getServer().getTicks());
            } else {
                if (args[1].equals("stop")) {
                    TickSpeedHelper.startTickWarp(0);
                } else {
                    try {
                        TickSpeedHelper.startTickWarp(Integer.parseInt(args[1]));
                    } catch (NumberFormatException ignored) {
                        throw new IncorrectUsageException(getUsageTranslationKey(source));
                    }
                }
            }
        } else throw new IncorrectUsageException(getUsageTranslationKey(source));
    }

    @Override
    public List<String> getSuggestions(CommandSource source, String[] args) {
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
