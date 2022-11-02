package net.nullspace_mc.tapestry.mixin.feature.kabovillagemarker;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.storage.WorldStorage;

import net.nullspace_mc.tapestry.helpers.KaboVillageMarker;
import net.nullspace_mc.tapestry.helpers.VillageMarkerAccessor;
import net.nullspace_mc.tapestry.settings.Settings;

import net.ornithemc.api.EnvType;
import net.ornithemc.api.Environment;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World implements VillageMarkerAccessor {

    private final KaboVillageMarker villageMarker = new KaboVillageMarker((ServerWorld)((World)this));

    public KaboVillageMarker getVillageMarker() {
        return this.villageMarker;
    }

    @Environment(EnvType.CLIENT)
    public ServerWorldMixin(WorldStorage storage, String name, Dimension dimension, WorldSettings settings, Profiler profiler) {
        super(storage, name, dimension, settings, profiler);
    }

    @Environment(EnvType.SERVER)
    public ServerWorldMixin(WorldStorage storage, String name, WorldSettings settings, Dimension dimension, Profiler profiler) {
        super(storage, name, settings, dimension, profiler);
    }

    @Inject(
            method = "tick",
            at = @At("TAIL")
    )
    private void updateKVM(CallbackInfo ci) {
        if (this.getTime() % 400L == 0L && Settings.kaboVillageMarker) {
            this.villageMarker.flagForUpdate();
        }

        if (this.villageMarker.needsUpdate() && Settings.kaboVillageMarker) {
            this.villageMarker.updateClients();
        }
    }
}
