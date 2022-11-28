package net.nullspace_mc.tapestry.mixin.feature.fillorientationfix;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.command.Command;
import net.minecraft.server.command.SetBlockCommand;
import net.minecraft.server.command.source.CommandSource;

import net.nullspace_mc.tapestry.helpers.SetBlockHelper;

@Mixin(SetBlockCommand.class)
public abstract class SetBlockCommandMixin extends Command {

    @Inject(
            method = "run",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;setBlockWithMetadata(IIILnet/minecraft/block/Block;II)Z"
            )
    )
    private void fixOrientation(CommandSource source, String[] args, CallbackInfo ci) {
        SetBlockHelper.applyFillOrientationFixRule = true;
    }
}
