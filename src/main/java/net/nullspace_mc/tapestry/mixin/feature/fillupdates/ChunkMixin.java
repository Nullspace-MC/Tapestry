package net.nullspace_mc.tapestry.mixin.feature.fillupdates;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityProvider;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.nullspace_mc.tapestry.helpers.SetBlockHelper;
import net.nullspace_mc.tapestry.settings.Settings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Chunk.class)
public abstract class ChunkMixin {
    @Redirect(
            method = "setBlockWithMetadata",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/Block;onBlockRemoved(Lnet/minecraft/world/World;IIILnet/minecraft/block/Block;I)V"
            )
    )
    private void onBlockRemovedConditional(Block instance, World world, int x, int y, int z, Block block, int meta) {
        if (!SetBlockHelper.applyFillUpdatesRule || Settings.fillUpdates) {
            instance.onBlockRemoved(world, x, y, z, block, meta);
        } else if (instance instanceof BlockEntityProvider) {
            world.removeBlockEntity(x, y, z);
        }
    }

    @Redirect(
            method = "setBlockWithMetadata",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/Block;onBlockAdded(Lnet/minecraft/world/World;III)V"
            )
    )
    private void onBlockAddedConditional(Block instance, World world, int x, int y, int z) {
        if (!SetBlockHelper.applyFillUpdatesRule || Settings.fillUpdates) {
            instance.onBlockAdded(world, x, y, z);
        }
        SetBlockHelper.applyFillUpdatesRule = false;
    }
}
