package net.nullspace_mc.tapestry.command;

import net.minecraft.command.CommandSource;
import net.minecraft.command.IncorrectUsageException;
import net.minecraft.entity.player.PlayerEntity;
import net.nullspace_mc.tapestry.loggers.Logger;
import net.nullspace_mc.tapestry.loggers.LoggerRegistry;

import java.util.ArrayList;
import java.util.List;

public class LogCommand extends TapestryAbstractCommand {
    @Override
    public String getName() {
        return "log";
    }

    @Override
    public String getUsageTranslationKey(CommandSource source) {
        return "/log <logger> <channel>";
    }

    @Override
    public int getPermissionLevel() {
        return 0;
    }

    @Override
    public void execute(CommandSource source, String[] args) {
        if (!(source instanceof PlayerEntity)) return;
        if (args.length == 0) throw new IncorrectUsageException(getUsageTranslationKey(source));
        if (!LoggerRegistry.getAllLoggersName().contains(args[0]))
            throw new IncorrectUsageException(String.format("%s. Logger %s doesn't exist.", getUsageTranslationKey(source), args[0]));

        Logger logger = LoggerRegistry.getLoggerFromName(args[0]);

        switch (args.length) {
            case 1:
                if (logger.getIsChannelRequired()) throw new IncorrectUsageException(String.format("The logger %s requires a channel.", args[0]));
                logger.onLogCommand(source.getName().asString());
                break;
            case 2:
                if (!logger.getAvailableChannels().contains(args[1])) throw new IncorrectUsageException(String.format("The channel %s doesn't exist for the logger %s.", args[0], args[1]));
                logger.onLogCommand(source.getName().asString(), args[1]);
                break;
            default:
                throw new IncorrectUsageException(getUsageTranslationKey(source));
        }

    }

    @Override
    public List getSuggestions(CommandSource source, String[] args) {
        ArrayList<String> suggestions = new ArrayList<>();
        String prefix = args[args.length-1].toLowerCase();
        if (args.length == 1) suggestions.addAll(LoggerRegistry.getAllLoggersName());
        else if (args.length == 2) {
            Logger logger = LoggerRegistry.getLoggerFromName(args[1]);
            if (logger != null) {
                suggestions.addAll(logger.getAvailableChannels());
            }
        }
        suggestions.removeIf(suggestion -> !suggestion.startsWith(prefix));
        return suggestions;
    }
}
