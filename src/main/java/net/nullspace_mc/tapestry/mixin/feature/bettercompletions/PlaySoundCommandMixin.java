package net.nullspace_mc.tapestry.mixin.feature.bettercompletions;

import java.util.List;
import net.minecraft.command.AbstractCommand;
import net.minecraft.command.CommandSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.PlaySoundCommand;
import net.nullspace_mc.tapestry.command.TapestryAbstractCommand;
import net.nullspace_mc.tapestry.settings.Settings;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PlaySoundCommand.class)
public abstract class PlaySoundCommandMixin extends AbstractCommand {
    public List getSuggestions(CommandSource source, String[] args) {
        if (Settings.betterCompletions) {
            if (args.length == 2) {
                return getMatchingArgs(args, MinecraftServer.getServer().getPlayerNames());
            } else if (args.length > 2 && args.length <= 5) {
                return TapestryAbstractCommand.getCoordinateSuggestions(source, args, 2);
            }
        }
        return null;
    }
}
