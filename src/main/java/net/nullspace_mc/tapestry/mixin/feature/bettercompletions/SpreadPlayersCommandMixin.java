package net.nullspace_mc.tapestry.mixin.feature.bettercompletions;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.AbstractCommand;
import net.minecraft.server.command.SpreadPlayersCommand;
import net.minecraft.server.command.source.CommandSource;

import net.nullspace_mc.tapestry.command.TapestryCommand;
import net.nullspace_mc.tapestry.settings.Settings;

@Mixin(SpreadPlayersCommand.class)
public abstract class SpreadPlayersCommandMixin extends AbstractCommand {

    public List<String> getSuggestions(CommandSource source, String[] args) {
        if (Settings.betterCompletions) {
            if (args.length == 1) {
                return TapestryCommand.suggestCoordinates(source, args, 0);
            } else if (args.length == 2) {
                return TapestryCommand.suggestCoordinates(source, args, -1);
            } else if (args.length == 5) {
                return suggestMatching(args, "true", "false");
            } else if (args.length >= 6) {
                return suggestMatching(args, MinecraftServer.getInstance().getPlayerNames());
            }
        }
        return null;
    }
}
