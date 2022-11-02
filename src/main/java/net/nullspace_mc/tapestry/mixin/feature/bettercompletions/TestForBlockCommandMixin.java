package net.nullspace_mc.tapestry.mixin.feature.bettercompletions;

import java.util.List;
import net.minecraft.server.command.Command;
import net.minecraft.server.command.source.CommandSource;
import net.minecraft.server.command.TestForBlockCommand;
import net.nullspace_mc.tapestry.command.TapestryCommand;
import net.nullspace_mc.tapestry.settings.Settings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TestForBlockCommand.class)
public abstract class TestForBlockCommandMixin extends Command {

    @Inject(
        method = "getSuggestions",
        at = @At("HEAD"),
        cancellable = true
    )
    private void getBetterCompletions(CommandSource source, String[] args, CallbackInfoReturnable<List> cir) {
        if (Settings.betterCompletions) {
            if (args.length > 0 && args.length <= 3) {
                cir.setReturnValue(TapestryCommand.suggestCoordinates(source, args, 0));
            }
        }
    }
}
