package net.nullspace_mc.tapestry.mixin.feature.fillorientationfix;

import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.nullspace_mc.tapestry.helpers.SetBlockHelper;
import net.nullspace_mc.tapestry.settings.Settings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DispenserBlock.class)
public abstract class DispenserBlockMixin extends BlockWithEntity {
    protected DispenserBlockMixin(Material material) {
        super(material);
    }

    @Shadow
    protected abstract void updateState(World world, int x, int y, int z);

    @Redirect(
            method = "onBlockAdded",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/DispenserBlock;updateState(Lnet/minecraft/world/World;III)V"
            )
    )
    private void fixOrientation(DispenserBlock dispenserBlock, World world, int x, int y, int z) {
        if (!SetBlockHelper.applyFillOrientationFixRule || !Settings.fillOrientationFix) {
            this.updateState(world, x, y, z);
        }
    }
}
