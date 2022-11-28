package net.nullspace_mc.tapestry.mixin.feature.liquiddamagedisabled;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingLiquidBlock;
import net.minecraft.world.World;
import net.nullspace_mc.tapestry.settings.Settings;

@Mixin(FlowingLiquidBlock.class)
public class FlowingFluidBlockMixin {

    @Inject(
            method = "blocksSpreading",
            at = @At("HEAD"),
            cancellable = true
    )
    private void makeBlocksNonBreakableByWater(World world, int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
        if (Settings.liquidDamageDisabled) cir.setReturnValue(world.getBlock(x, y, z) != Blocks.AIR);
    }
}
