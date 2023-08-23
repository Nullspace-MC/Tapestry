package net.nullspace_mc.tapestry.mixin.feature.instantfall;

import net.minecraft.block.FallingBlock;
import net.minecraft.world.World;
import net.nullspace_mc.tapestry.settings.Settings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FallingBlock.class)
public class FallingBlockMixin {

    @Redirect(
            method = "tryFall",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;isAreaLoaded(IIIIII)Z"
            )
    )
    public boolean forceInstantFall(World instance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        return !(Settings.instantFall || !instance.isAreaLoaded(minX, minY, minZ, maxX, maxY, maxZ));
    }
}
