package net.nullspace_mc.tapestry.mixin.feature.bettercompletions;

import java.util.List;
import net.minecraft.command.AbstractCommand;
import net.minecraft.command.CommandSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.TeleportCommand;
import net.nullspace_mc.tapestry.command.TapestryAbstractCommand;
import net.nullspace_mc.tapestry.settings.Settings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TeleportCommand.class)
public abstract class TeleportCommandMixin extends AbstractCommand {
    @Inject(
        method = "getSuggestions",
        at = @At("HEAD"),
        cancellable = true
    )
    @SuppressWarnings("unchecked")
    private void getBetterCompletions(CommandSource source, String[] args, CallbackInfoReturnable<List> cir) {
        if (Settings.betterCompletions) {
            if (args.length == 2) {
                List ret = TapestryAbstractCommand.getCoordinateSuggestions(source, args, 1);
                ret.addAll(getMatchingArgs(args, MinecraftServer.getServer().getPlayerNames()));
                cir.setReturnValue(ret);
            } else if (args.length > 2 && args.length <= 4) {
                cir.setReturnValue(TapestryAbstractCommand.getCoordinateSuggestions(source, args, 1));
            }
        }
    }
}
