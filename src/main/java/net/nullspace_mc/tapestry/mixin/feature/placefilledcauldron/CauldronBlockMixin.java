package net.nullspace_mc.tapestry.mixin.feature.placefilledcauldron;

import net.minecraft.block.Block;
import net.minecraft.block.CauldronBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.living.LivingEntity;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.nullspace_mc.tapestry.settings.Settings;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CauldronBlock.class)
public abstract class CauldronBlockMixin extends Block {

    protected CauldronBlockMixin(Material material) {
        super(material);
    }

    @Override
    public void onPlaced(World world, int x, int y, int z, LivingEntity entity, ItemStack stack) {
        if (Settings.placeFilledCauldron && entity instanceof PlayerEntity) {
            try {
                int i = Integer.parseInt(stack.getHoverName());
                if (i > 0 && i < 16) {
                    world.setBlockMetadata(x, y, z, i, 3);
                }
            } catch (NumberFormatException ignored){}
        }
    }
}
