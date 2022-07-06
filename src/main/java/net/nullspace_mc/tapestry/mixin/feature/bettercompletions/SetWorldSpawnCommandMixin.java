package net.nullspace_mc.tapestry.mixin.feature.bettercompletions;

import java.util.List;
import net.minecraft.server.command.AbstractCommand;
import net.minecraft.server.command.CommandSource;
import net.minecraft.server.command.SetWorldSpawnCommand;
import net.nullspace_mc.tapestry.command.TapestryAbstractCommand;
import net.nullspace_mc.tapestry.settings.Settings;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SetWorldSpawnCommand.class)
public abstract class SetWorldSpawnCommandMixin extends AbstractCommand {

    public List getSuggestions(CommandSource source, String[] args) {
        if (Settings.betterCompletions) {
            return TapestryAbstractCommand.getCoordinateSuggestions(source, args, 0);
        }
        return null;
    }
}
