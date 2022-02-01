package net.nullspace_mc.tapestry.mixin.feature.fillorientationfix;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.world.World;
import net.nullspace_mc.tapestry.helpers.SetBlockHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FallingBlockEntity.class)
public abstract class FallingBlockEntityMixin extends Entity {

    protected FallingBlockEntityMixin(World world) {
        super(world);
    }

    @Redirect(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;setBlockWithMetadata(IIILnet/minecraft/block/Block;II)Z"
            )
    )
    private boolean fixOrientation(World world, int x, int y, int z, Block block, int metadata, int flags) {
        SetBlockHelper.applyFillOrientationFixRule = true;
        return world.setBlockWithMetadata(x, y, z, block, metadata, flags);
    }

}
