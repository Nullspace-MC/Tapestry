package net.nullspace_mc.tapestry.mixin.feature.kabovillagemarker;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.SaveHandler;
import net.minecraft.world.World;
import net.minecraft.world.WorldInfo;
import net.minecraft.world.dimension.Dimension;
import net.nullspace_mc.tapestry.helpers.KaboVillageMarker;
import net.nullspace_mc.tapestry.helpers.VillageMarkerAccessor;
import net.nullspace_mc.tapestry.settings.Settings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World implements VillageMarkerAccessor {
    private final KaboVillageMarker villageMarker = new KaboVillageMarker((ServerWorld)((World)this));

    public KaboVillageMarker getVillageMarker() {
        return this.villageMarker;
    }

    @Environment(EnvType.CLIENT)
    public ServerWorldMixin(SaveHandler saveHandler, String levelName, Dimension dimension, WorldInfo levelInfo, Profiler profiler) {
        super(saveHandler, levelName, dimension, levelInfo, profiler);
    }

    @Environment(EnvType.SERVER)
    public ServerWorldMixin(SaveHandler saveHandler, String levelName, WorldInfo levelInfo, Dimension dimension, Profiler profiler) {
        super(saveHandler, levelName, levelInfo, dimension, profiler);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void updateKVM(CallbackInfo ci) {
        if(this.getTime() % 400L == 0L && Settings.kaboVillageMarker) {
            this.villageMarker.flagForUpdate();
        }

        if(this.villageMarker.needsUpdate() && Settings.kaboVillageMarker) {
            this.villageMarker.updateClients();
        }
    }
}
