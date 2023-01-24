package net.nullspace_mc.tapestry.mixin.feature.bettercompletions;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.server.command.Command;
import net.minecraft.server.command.SpawnPointCommand;
import net.minecraft.server.command.source.CommandSource;

import net.nullspace_mc.tapestry.command.TapestryCommand;
import net.nullspace_mc.tapestry.settings.Settings;

@Mixin(SpawnPointCommand.class)
public abstract class SpawnPointCommandMixin extends Command {

    @Inject(
        method = "getSuggestions",
        at = @At("HEAD"),
        cancellable = true
    )
    private void getBetterCompletions(CommandSource source, String[] args, CallbackInfoReturnable<List<String>> cir) {
        if (Settings.betterCompletions) {
            if (args.length > 1 && args.length <= 4) {
                cir.setReturnValue(TapestryCommand.suggestCoordinates(source, args, 1));
            }
        }
    }
}
