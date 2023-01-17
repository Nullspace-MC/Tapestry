package net.nullspace_mc.tapestry.mixin.feature.fillupdates;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.Block;
import net.minecraft.server.command.Command;
import net.minecraft.server.command.SetBlockCommand;
import net.minecraft.world.World;
import net.nullspace_mc.tapestry.helpers.SetBlockFlags;
import net.nullspace_mc.tapestry.helpers.SetBlockHelper;
import net.nullspace_mc.tapestry.settings.Settings;

@Mixin(SetBlockCommand.class)
public abstract class SetBlockCommandMixin extends Command {

    @Redirect(
            method = "run",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;setBlockWithMetadata(IIILnet/minecraft/block/Block;II)Z"
            )
    )
    private boolean disableUpdate(World world, int x, int y, int z, Block block, int metadata, int flags) {
        SetBlockHelper.applyFillUpdatesRule = true;

        if (!Settings.fillUpdates) {
            flags &= ~SetBlockFlags.UPDATE_NEIGHBORS; // disable UPDATE_NEIGHBORS flag
        }

        return world.setBlockWithMetadata(x, y, z, block, metadata, flags);
    }
}
