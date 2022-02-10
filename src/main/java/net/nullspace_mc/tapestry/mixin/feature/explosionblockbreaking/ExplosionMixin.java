package net.nullspace_mc.tapestry.mixin.feature.explosionblockbreaking;

import java.util.List;
import net.minecraft.world.explosion.Explosion;
import net.nullspace_mc.tapestry.settings.Settings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Explosion.class)
public abstract class ExplosionMixin {

    @Shadow
    private List affectedBlocks;

    @Inject(
            method = "affectWorld",
            at = @At("HEAD")
    )
    private void clearAffectedBlocks(boolean bl, CallbackInfo ci) {
        if (!Settings.explosionBlockBreaking) {
            affectedBlocks.clear();
        }
    }
}
