package net.nullspace_mc.tapestry.mixin.counter;

import net.minecraft.block.HopperBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import net.nullspace_mc.tapestry.counter.CounterRegistry;
import net.nullspace_mc.tapestry.settings.Settings;
import net.nullspace_mc.tapestry.util.PositionUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HopperBlockEntity.class)
public abstract class HopperBlockEntityMixin extends BlockEntity {


    @Shadow private ItemStack[] inventory;

    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/entity/HopperBlockEntity;setCooldown(I)V"
            )
    )
    private void onHopperBlockEntityTick(CallbackInfo ci) {
        if (Settings.hopperCounter && this.isHopperCounter()) {
            for (ItemStack stack : inventory) {
                if (stack == null) continue;
                CounterRegistry.getCounter(getCounterColor()).addToCounter(stack.getName(), stack.count);
            }
            inventory = new ItemStack[5];
        }
    }

    private String getCounterColor() {
        int[] pos = posFacing();
        int metadata = world.getBlockMetadata(pos[0], pos[1], pos[2]);
        return DyeItem.colors[DyeItem.colors.length - metadata];
    }

    private boolean isHopperCounter() {
        int[] position = posFacing();
        return world.getBlock(position[0], position[1], position[2]).getMaterial() == Material.WOOL;
    }

    private int[] posFacing() {
        int direction = HopperBlock.getFacing(world.getBlockMetadata(this.x, this.y, this.z));
        return PositionUtil.offset(this.x, this.y, this.z, Direction.byId(direction));
    }
}
