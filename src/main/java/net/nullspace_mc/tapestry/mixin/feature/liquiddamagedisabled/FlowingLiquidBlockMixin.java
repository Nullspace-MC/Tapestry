package net.nullspace_mc.tapestry.mixin.feature.liquiddamagedisabled;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingLiquidBlock;
import net.minecraft.block.material.LiquidMaterial;
import net.minecraft.world.World;
import net.nullspace_mc.tapestry.settings.Settings;

@Mixin(FlowingLiquidBlock.class)
public class FlowingLiquidBlockMixin {

    @Inject(
            method = "blocksSpreading",
            at = @At("HEAD"),
            cancellable = true
    )
    private void makeBlocksNonBreakableByWater(World world, int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
        if (Settings.liquidDamageDisabled) {
            Block block = world.getBlock(x, y, z);
            cir.setReturnValue(block != Blocks.AIR && !(block.getMaterial() instanceof LiquidMaterial));
        }
    }
}
