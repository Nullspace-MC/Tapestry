package net.nullspace_mc.tapestry.mixin.feature.bettercompletions;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.server.command.AbstractCommand;
import net.minecraft.server.command.SetWorldSpawnCommand;
import net.minecraft.server.command.source.CommandSource;

import net.nullspace_mc.tapestry.command.TapestryCommand;
import net.nullspace_mc.tapestry.settings.Settings;

@Mixin(SetWorldSpawnCommand.class)
public abstract class SetWorldSpawnCommandMixin extends AbstractCommand {

    public List<String> getSuggestions(CommandSource source, String[] args) {
        if (Settings.betterCompletions) {
            return TapestryCommand.suggestCoordinates(source, args, 0);
        }
        return null;
    }
}
