package net.nullspace_mc.tapestry.mixin.feature.kabovillagemarker;

import net.minecraft.village.VillageState;
import net.minecraft.world.World;
import net.nullspace_mc.tapestry.helpers.KaboVillageMarker;
import net.nullspace_mc.tapestry.helpers.VillageMarkerAccessor;
import net.nullspace_mc.tapestry.settings.Settings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VillageState.class)
public abstract class VillageStateMixin {
    @Shadow
    private World world;

    private boolean shouldUpdateKaboVillageMarker = false;

    @Inject(method = "tick", at = @At("TAIL"))
    private void updateKVM(CallbackInfo ci) {
        if(this.shouldUpdateKaboVillageMarker && Settings.kaboVillageMarker) {
            ((VillageMarkerAccessor)this.world).getVillageMarker().flagForUpdate();
            this.shouldUpdateKaboVillageMarker = false;
        }
    }

    @Inject(method = "removeVillagesWithNoDoors", at = @At(value = "INVOKE", target = "Lnet/minecraft/village/VillageState;markDirty()V"))
    private void scheduleKVMUpdateRemoveVillagesWithNoDoors(CallbackInfo ci) {
        if(Settings.kaboVillageMarker) {
            this.shouldUpdateKaboVillageMarker = true;
        }
    }

    @Inject(method = "addDoorsToVillages", at = @At(value = "INVOKE", target = "Lnet/minecraft/village/Village;addDoor(Lnet/minecraft/village/VillageDoor;)V"))
    private void scheduleKVMUpdateAddDoorsToVillages(CallbackInfo ci) {
        if(Settings.kaboVillageMarker) {
            this.shouldUpdateKaboVillageMarker = true;
        }
    }

    @Inject(method = "addDoor", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"))
    private void scheduleKVMUpdateAddDoor(int x, int y, int z, CallbackInfo ci) {
        if(Settings.kaboVillageMarker) {
            this.shouldUpdateKaboVillageMarker = true;
        }
    }
}
