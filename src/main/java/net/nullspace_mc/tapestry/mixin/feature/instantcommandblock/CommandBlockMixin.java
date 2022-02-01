package net.nullspace_mc.tapestry.mixin.feature.instantcommandblock;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.CommandBlock;
import net.minecraft.world.World;
import net.nullspace_mc.tapestry.settings.Settings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CommandBlock.class)
public abstract class CommandBlockMixin {

    @Shadow
    public abstract void scheduledTick(World world, int x, int y, int z, Random rand);

    @Redirect(
            method = "neighborUpdate",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;scheduleTick(IIILnet/minecraft/block/Block;I)V"
            )
    )
    private void makeCommandBlockInstant(World world, int x, int y, int z, Block block, int delay) {
        if (Settings.instantCommandBlock && world.getBlock(x, y - 1, z) == Blocks.REDSTONE_ORE) {
            scheduledTick(world, x, y, z, world.random);
        } else {
            world.scheduleTick(x, y, z, block, delay);
        }
    }
}
