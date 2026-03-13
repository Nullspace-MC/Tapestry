package net.nullspace_mc.tapestry.mixin.feature.fasteritemframemaps;

import net.minecraft.server.entity.EntityTrackerEntry;
import net.nullspace_mc.tapestry.settings.Settings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityTrackerEntry.class)
public class EntityTrackerEntryMixin {

    @Shadow public int ticks;

    @Redirect(
            method = "notifyNewLocation",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/server/entity/EntityTrackerEntry;ticks:I",
                    ordinal = 1
            )
    )
    private int setTicks(EntityTrackerEntry instance) {
        if (Settings.fasterItemFrameMaps) {
            return 0;
        }
        return this.ticks;
    }
}