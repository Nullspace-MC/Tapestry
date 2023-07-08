package net.nullspace_mc.tapestry.mixin.feature.fillorientationfix;

import net.minecraft.block.BlockWithBlockEntity;
import net.minecraft.block.FurnaceBlock;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.nullspace_mc.tapestry.helpers.SetBlockHelper;
import net.nullspace_mc.tapestry.settings.Settings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FurnaceBlock.class)
public abstract class FurnaceBlockMixin extends BlockWithBlockEntity {

    protected FurnaceBlockMixin(Material material) {
        super(material);
    }

    @Shadow
    protected abstract void updateFacing(World world, int x, int y, int z);

    @Redirect(
            method = "onAdded",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/FurnaceBlock;updateFacing(Lnet/minecraft/world/World;III)V"
            )
    )
    private void fixOrientation(FurnaceBlock furnaceBlock, World world, int x, int y, int z) {
        if (!SetBlockHelper.applyFillOrientationFixRule || !Settings.fillOrientationFix) {
            this.updateFacing(world, x, y, z);
        }
    }
}
