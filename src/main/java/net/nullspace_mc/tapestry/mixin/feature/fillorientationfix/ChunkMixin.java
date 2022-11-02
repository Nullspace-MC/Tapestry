package net.nullspace_mc.tapestry.mixin.feature.fillorientationfix;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;
import net.minecraft.world.chunk.WorldChunk;

import net.nullspace_mc.tapestry.helpers.SetBlockHelper;

@Mixin(WorldChunk.class)
public abstract class ChunkMixin {

    @Inject(
            method = "setBlockWithMetadataAt",
            at = @At("RETURN")
    )
    private void stopFixOrientation(int x, int y, int z, Block block, int metadata, CallbackInfoReturnable<Boolean> cir) {
        SetBlockHelper.applyFillOrientationFixRule = false;
    }
}
