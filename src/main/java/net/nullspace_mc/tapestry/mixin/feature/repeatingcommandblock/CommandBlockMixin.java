package net.nullspace_mc.tapestry.mixin.feature.repeatingcommandblock;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.CommandBlock;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.nullspace_mc.tapestry.settings.Settings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CommandBlock.class)
public abstract class CommandBlockMixin extends BlockWithEntity {

    protected CommandBlockMixin(Material material) {
        super(material);
    }

    @Shadow
    public abstract int getTickRate(World world);

    @Inject(
            method = "scheduledTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;updateComparators(IIILnet/minecraft/block/Block;)V"
            )
    )
    private void makeCommandBlockRepeating(World world, int x, int y, int z, Random random, CallbackInfo ci) {
        if (world.isReceivingPower(x, y, z) && Settings.repeatingCommandBlock && world.getBlock(x, y - 1, z) == Blocks.DIAMOND_ORE)
            world.scheduleTick(x, y, z, this, this.getTickRate(world));
    }
}
