package net.nullspace_mc.tapestry.mixin.feature.fasteritemframemaps;

import net.minecraft.server.TrackedEntity;
import net.nullspace_mc.tapestry.settings.Settings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TrackedEntity.class)
public class TrackedEntityMixin {

    @Shadow public int ticks;

    @Redirect(
            method = "tick",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/server/TrackedEntity;ticks:I",
                    ordinal = 1
            )
    )
    private int setTicks(TrackedEntity instance) {
        if (Settings.fasterItemFrameMaps) {
            return 0;
        }
        return this.ticks;
    }
}
