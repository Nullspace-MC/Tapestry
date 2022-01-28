package net.nullspace_mc.tapestry.mixin.feature.fillorientationfix;

import net.minecraft.command.AbstractCommand;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.SetBlockCommand;
import net.nullspace_mc.tapestry.helpers.SetBlockHelper;
import net.nullspace_mc.tapestry.settings.Settings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SetBlockCommand.class)
public abstract class SetBlockCommandMixin extends AbstractCommand {
    @Inject(
            method = "execute",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;setBlockWithMetadata(IIILnet/minecraft/block/Block;II)Z"
            )
    )
    private void fixOrientation(CommandSource source, String[] args, CallbackInfo ci) {
        SetBlockHelper.applyFillOrientationFixRule = true;
    }
}
