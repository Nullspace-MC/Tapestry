package net.nullspace_mc.tapestry.mixin.feature.alwayseatcake;

import net.minecraft.block.CakeBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.nullspace_mc.tapestry.settings.Settings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CakeBlock.class)
public class CakeBlockMixin {
    @Redirect(
            method = "tryEatCake",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;canEat(Z)Z"
            )
    )
    private boolean alwaysAllowCakeEating(PlayerEntity instance, boolean b) {
        return Settings.alwaysEatCake || b;
    }
}
