package net.nullspace_mc.tapestry.mixin.feature.explosionblockbreaking;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.util.math.Vec3i;
import net.minecraft.world.explosion.Explosion;

import net.nullspace_mc.tapestry.settings.Settings;

@Mixin(Explosion.class)
public class ExplosionMixin {

    @Shadow
    private List<Vec3i> damagedBlocks;

    @Inject(
            method = "damageBlocks",
            at = @At("HEAD")
    )
    private void clearAffectedBlocks(boolean bl, CallbackInfo ci) {
        if (!Settings.explosionBlockBreaking) {
            damagedBlocks.clear();
        }
    }
}
