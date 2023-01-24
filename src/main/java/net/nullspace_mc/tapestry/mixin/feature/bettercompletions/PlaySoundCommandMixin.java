package net.nullspace_mc.tapestry.mixin.feature.bettercompletions;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.Command;
import net.minecraft.server.command.PlaySoundCommand;
import net.minecraft.server.command.source.CommandSource;

import net.nullspace_mc.tapestry.command.TapestryCommand;
import net.nullspace_mc.tapestry.settings.Settings;

@Mixin(PlaySoundCommand.class)
public abstract class PlaySoundCommandMixin extends Command {

    public List<String> getSuggestions(CommandSource source, String[] args) {
        if (Settings.betterCompletions) {
            if (args.length == 2) {
                return TapestryCommand.suggestMatching(args, MinecraftServer.getInstance().getPlayerNames());
            } else if (args.length > 2 && args.length <= 5) {
                return TapestryCommand.suggestCoordinates(source, args, 2);
            }
        }
        return null;
    }
}
