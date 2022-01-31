package net.nullspace_mc.tapestry.mixin.feature.fillorientationfix;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.nullspace_mc.tapestry.helpers.SetBlockHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Chunk.class)
public abstract class ChunkMixin {
    @Inject(
            method = "setBlockWithMetadata",
            at = @At("RETURN")
    )
    private void stopFixOrientation(int x, int y, int z, Block block, int metadata, CallbackInfoReturnable cir) {
        SetBlockHelper.applyFillOrientationFixRule = false;
    }
}
