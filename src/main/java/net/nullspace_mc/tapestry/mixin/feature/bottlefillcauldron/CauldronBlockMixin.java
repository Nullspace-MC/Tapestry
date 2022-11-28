package net.nullspace_mc.tapestry.mixin.feature.bottlefillcauldron;

import net.minecraft.block.CauldronBlock;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.world.World;
import net.nullspace_mc.tapestry.settings.Settings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CauldronBlock.class)
public abstract class CauldronBlockMixin {
    @Shadow public abstract void setLevel(World world, int x, int y, int z, int level);

    @Inject(
            method = "use",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerInventory;getMainHandStack()Lnet/minecraft/item/ItemStack;"
            )
    )
    private void onUse(World world, int x, int y, int z, PlayerEntity player, int dir, float dx, float dy, float dz, CallbackInfoReturnable<Boolean> cir) {
        ItemStack stack = player.inventory.getMainHandStack();
        if (Settings.bottleFillCauldron && stack != null && stack.getItem() == Items.POTION && stack.getDamage() == 0) {
            int metadata = world.getBlockMetadata(x, y, z);
            if (metadata < 3) {
                this.setLevel(world, x, y, z, ++metadata);
            }
            if (!player.abilities.creativeMode) {
                stack.size--;
                if (stack.size <= 0) player.inventory.setStack(player.inventory.selectedSlot, null);

                ItemStack result = new ItemStack(Items.GLASS_BOTTLE, 1, 0);
                if (!player.inventory.insertStack(result)) world.addEntity(new ItemEntity(world, x + 0.5D, y + 0.5D, z + 0.5D, result));
                else ((ServerPlayerEntity)player).setMenu(player.playerMenu);
            }
        }
    }
}
