package net.nullspace_mc.tapestry.mixin.feature.bettercompletions;

import java.util.List;
import net.minecraft.server.command.Command;
import net.minecraft.server.command.source.CommandSource;
import net.minecraft.server.command.SetWorldSpawnCommand;
import net.nullspace_mc.tapestry.command.TapestryCommand;
import net.nullspace_mc.tapestry.settings.Settings;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SetWorldSpawnCommand.class)
public abstract class SetWorldSpawnCommandMixin extends Command {

    public List getSuggestions(CommandSource source, String[] args) {
        if (Settings.betterCompletions) {
            return TapestryCommand.suggestCoordinates(source, args, 0);
        }
        return null;
    }
}
