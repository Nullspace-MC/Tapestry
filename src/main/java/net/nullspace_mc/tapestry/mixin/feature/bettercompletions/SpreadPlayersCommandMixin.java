package net.nullspace_mc.tapestry.mixin.feature.bettercompletions;

import java.util.List;
import net.minecraft.server.command.AbstractCommand;
import net.minecraft.server.command.CommandSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.SpreadPlayersCommand;
import net.nullspace_mc.tapestry.command.TapestryAbstractCommand;
import net.nullspace_mc.tapestry.settings.Settings;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SpreadPlayersCommand.class)
public abstract class SpreadPlayersCommandMixin extends AbstractCommand {

    public List getSuggestions(CommandSource source, String[] args) {
        if (Settings.betterCompletions) {
            if (args.length == 1) {
                return TapestryAbstractCommand.getCoordinateSuggestions(source, args, 0);
            } else if (args.length == 2) {
                return TapestryAbstractCommand.getCoordinateSuggestions(source, args, -1);
            } else if (args.length == 5) {
                return getMatchingArgs(args, new String[]{"true", "false"});
            } else if (args.length >= 6) {
                return getMatchingArgs(args, MinecraftServer.getServer().getPlayerNames());
            }
        }
        return null;
    }
}
