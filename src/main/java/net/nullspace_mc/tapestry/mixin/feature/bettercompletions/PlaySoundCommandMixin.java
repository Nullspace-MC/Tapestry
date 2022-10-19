package net.nullspace_mc.tapestry.mixin.feature.bettercompletions;

import java.util.List;
import net.minecraft.server.command.Command;
import net.minecraft.server.command.source.CommandSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.PlaySoundCommand;
import net.nullspace_mc.tapestry.command.TapestryCommand;
import net.nullspace_mc.tapestry.settings.Settings;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PlaySoundCommand.class)
public abstract class PlaySoundCommandMixin extends Command {

    public List getSuggestions(CommandSource source, String[] args) {
        if (Settings.betterCompletions) {
            if (args.length == 2) {
                return suggestMatching(args, MinecraftServer.getInstance().getPlayerNames());
            } else if (args.length > 2 && args.length <= 5) {
                return TapestryCommand.suggestCoordinates(source, args, 2);
            }
        }
        return null;
    }
}
