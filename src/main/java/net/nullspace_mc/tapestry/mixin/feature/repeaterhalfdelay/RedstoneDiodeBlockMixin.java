package net.nullspace_mc.tapestry.mixin.feature.repeaterhalfdelay;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.RedstoneDiodeBlock;
import net.minecraft.world.World;
import net.nullspace_mc.tapestry.settings.Settings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RedstoneDiodeBlock.class)
public class RedstoneDiodeBlockMixin {

    @Redirect(
            method = "update",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;scheduleTick(IIILnet/minecraft/block/Block;II)V"
            )
    )
    private void modifyRepeaterComparatorDelay(World world, int x, int y, int z, Block block, int delay, int priority) {
        world.scheduleTick(
                x, y, z,
                block,
                delay / (Settings.repeaterHalfDelay && world.getBlock(x, y - 1, z) == Blocks.REDSTONE_ORE ? 2 : 1),
                priority);
    }
}
