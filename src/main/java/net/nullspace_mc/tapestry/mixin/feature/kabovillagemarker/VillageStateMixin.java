package net.nullspace_mc.tapestry.mixin.feature.kabovillagemarker;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.World;
import net.minecraft.world.village.SavedVillageData;

import net.nullspace_mc.tapestry.helpers.VillageMarkerAccessor;
import net.nullspace_mc.tapestry.settings.Settings;

@Mixin(SavedVillageData.class)
public abstract class VillageStateMixin {

    @Shadow
    private World world;

    private boolean shouldUpdateKaboVillageMarker = false;

    @Inject(
            method = "tick",
            at = @At("TAIL")
    )
    private void updateKVM(CallbackInfo ci) {
        if (this.shouldUpdateKaboVillageMarker && Settings.kaboVillageMarker) {
            ((VillageMarkerAccessor)this.world).getVillageMarker().flagForUpdate();
            this.shouldUpdateKaboVillageMarker = false;
        }
    }

    @Inject(
            method = "removeVillagesWithoutDoors",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/village/SavedVillageData;markDirty()V"
            )
    )
    private void scheduleKVMUpdateRemoveVillagesWithNoDoors(CallbackInfo ci) {
        if (Settings.kaboVillageMarker) {
            this.shouldUpdateKaboVillageMarker = true;
        }
    }

    @Inject(
            method = "addDoorsToVillages",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/village/Village;addDoor(Lnet/minecraft/world/village/VillageDoor;)V"
            )
    )
    private void scheduleKVMUpdateAddDoorsToVillages(CallbackInfo ci) {
        if (Settings.kaboVillageMarker) {
            this.shouldUpdateKaboVillageMarker = true;
        }
    }

    @Inject(
            method = "addNewDoor",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/List;add(Ljava/lang/Object;)Z"
            )
    )
    private void scheduleKVMUpdateAddDoor(int x, int y, int z, CallbackInfo ci) {
        if (Settings.kaboVillageMarker) {
            this.shouldUpdateKaboVillageMarker = true;
        }
    }
}
