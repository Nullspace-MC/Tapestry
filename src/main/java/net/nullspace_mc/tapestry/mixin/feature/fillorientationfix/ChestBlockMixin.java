package net.nullspace_mc.tapestry.mixin.feature.fillorientationfix;

import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.nullspace_mc.tapestry.helpers.SetBlockHelper;
import net.nullspace_mc.tapestry.settings.Settings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChestBlock.class)
public abstract class ChestBlockMixin extends BlockWithEntity {
    protected ChestBlockMixin(Material material) {
        super(material);
    }

    @Redirect(
            method = "onBlockAdded",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/ChestBlock;updateState(Lnet/minecraft/world/World;III)V",
                    ordinal = 0
            )
    )
    private void fixOrientation(ChestBlock chestBlock, World world, int x, int y, int z) {
        if (!SetBlockHelper.applyFillOrientationFixRule || !Settings.fillOrientationFix) {
            chestBlock.updateState(world, x, y, z);
        }
    }
}
