package net.nullspace_mc.tapestry.mixin.feature.kabovillagemarker;

import net.minecraft.village.Village;
import net.minecraft.world.World;
import net.nullspace_mc.tapestry.helpers.KaboVillageMarker;
import net.nullspace_mc.tapestry.helpers.VillageMarkerAccessor;
import net.nullspace_mc.tapestry.settings.Settings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Village.class)
public abstract class VillageMixin {
    @Shadow
    private World world;

    private boolean shouldUpdateKaboVillageMarker = false;

    @Inject(method = "setTicks", at = @At("TAIL"))
    private void updateKVM(int ticks, CallbackInfo ci) {
        if(this.shouldUpdateKaboVillageMarker && Settings.kaboVillageMarker) {
            ((VillageMarkerAccessor)this.world).getVillageMarker().flagForUpdate();
            this.shouldUpdateKaboVillageMarker = false;
        }
    }

    @Inject(method = "tickDoors", at = @At(value = "INVOKE", target = "Lnet/minecraft/village/Village;updateRadius()V"))
    private void scheduleKVMUpdate(CallbackInfo ci) {
        if(Settings.kaboVillageMarker) {
            this.shouldUpdateKaboVillageMarker = true;
        }
    }
}
