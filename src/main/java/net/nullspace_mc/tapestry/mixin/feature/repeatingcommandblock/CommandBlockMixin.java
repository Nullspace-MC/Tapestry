package net.nullspace_mc.tapestry.mixin.feature.repeatingcommandblock;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.BlockWithBlockEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.CommandBlock;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

import net.nullspace_mc.tapestry.settings.Settings;

@Mixin(CommandBlock.class)
public abstract class CommandBlockMixin extends BlockWithBlockEntity {

    private CommandBlockMixin(Material material) {
        super(material);
    }

    @Shadow
    public abstract int getTickRate(World world);

    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;updateNeighborComparators(IIILnet/minecraft/block/Block;)V"
            )
    )
    private void makeCommandBlockRepeating(World world, int x, int y, int z, Random random, CallbackInfo ci) {
        if (world.hasNeighborSignal(x, y, z) && Settings.repeatingCommandBlock && world.getBlock(x, y - 1, z) == Blocks.DIAMOND_ORE)
            world.scheduleTick(x, y, z, this, this.getTickRate(world));
    }
}
