package net.nullspace_mc.tapestry.mixin.feature.bettercompletions;

import java.util.List;
import net.minecraft.command.AbstractCommand;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.SpawnPointCommand;
import net.nullspace_mc.tapestry.command.TapestryAbstractCommand;
import net.nullspace_mc.tapestry.settings.Settings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpawnPointCommand.class)
public abstract class SpawnPointCommandMixin extends AbstractCommand {
    @Inject(
        method = "getSuggestions",
        at = @At("HEAD"),
        cancellable = true
    )
    private void getBetterCompletions(CommandSource source, String[] args, CallbackInfoReturnable<List> cir) {
        if (Settings.betterCompletions) {
            if (args.length > 1 && args.length <= 4) {
                cir.setReturnValue(TapestryAbstractCommand.getCoordinateSuggestions(source, args, 1));
            }
        }
    }
}
