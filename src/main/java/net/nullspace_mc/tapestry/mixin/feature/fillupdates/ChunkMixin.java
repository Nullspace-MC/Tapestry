package net.nullspace_mc.tapestry.mixin.feature.fillupdates;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityProvider;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;

import net.nullspace_mc.tapestry.helpers.SetBlockHelper;
import net.nullspace_mc.tapestry.settings.Settings;

@Mixin(WorldChunk.class)
public abstract class ChunkMixin {

    @Redirect(
            method = "setBlockWithMetadataAt",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/Block;onRemoved(Lnet/minecraft/world/World;IIILnet/minecraft/block/Block;I)V"
            )
    )
    private void onRemovedConditional(Block instance, World world, int x, int y, int z, Block block, int meta) {
        if (!SetBlockHelper.applyFillUpdatesRule || Settings.fillUpdates) {
            instance.onRemoved(world, x, y, z, block, meta);
        } else if (instance instanceof BlockEntityProvider) {
            world.removeBlockEntity(x, y, z);
        }
    }

    @Redirect(
            method = "setBlockWithMetadataAt",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/Block;onAdded(Lnet/minecraft/world/World;III)V"
            )
    )
    private void onAddedConditional(Block instance, World world, int x, int y, int z) {
        if (!SetBlockHelper.applyFillUpdatesRule || Settings.fillUpdates) {
            instance.onAdded(world, x, y, z);
        }
    }

    @Inject(
            method = "setBlockWithMetadataAt",
            at = @At("RETURN")
    )
    private void startFillUpdates(int x, int y, int z, Block block, int metadata, CallbackInfoReturnable<Boolean> cir) {
        SetBlockHelper.applyFillUpdatesRule = false;
    }
}
