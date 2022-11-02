package net.nullspace_mc.tapestry.mixin.feature.kabovillagemarker;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.World;
import net.minecraft.world.village.Village;

import net.nullspace_mc.tapestry.helpers.VillageMarkerAccessor;
import net.nullspace_mc.tapestry.settings.Settings;

@Mixin(Village.class)
public abstract class VillageMixin {

    @Shadow
    private World world;

    private boolean shouldUpdateKaboVillageMarker = false;

    @Inject(
            method = "tick",
            at = @At("TAIL")
    )
    private void updateKVM(int ticks, CallbackInfo ci) {
        if (this.shouldUpdateKaboVillageMarker && Settings.kaboVillageMarker) {
            ((VillageMarkerAccessor)this.world).getVillageMarker().flagForUpdate();
            this.shouldUpdateKaboVillageMarker = false;
        }
    }

    @Inject(
            method = "cleanUpDoors",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/village/Village;updateCenterAndRadius()V"
            )
    )
    private void scheduleKVMUpdate(CallbackInfo ci) {
        if (Settings.kaboVillageMarker) {
            this.shouldUpdateKaboVillageMarker = true;
        }
    }
}
