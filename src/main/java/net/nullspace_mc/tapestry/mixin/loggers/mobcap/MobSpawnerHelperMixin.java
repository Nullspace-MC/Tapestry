package net.nullspace_mc.tapestry.mixin.loggers.mobcap;

import java.util.HashMap;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.living.mob.MobSpawnerHelper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;

import net.nullspace_mc.tapestry.helpers.MobSpawnerHelperHelper;

@Mixin(MobSpawnerHelper.class)
public abstract class MobSpawnerHelperMixin {

    @Shadow
    private HashMap<ChunkPos, Boolean> spawnedMobs;

    @Inject(
            method = "spawnEntities",
            at = @At("RETURN")
    )
    public void saveSpawnableChunksCount(ServerWorld serverWorld, boolean allowAnimals, boolean allowMonsters, boolean shouldSpawnAnimals, CallbackInfoReturnable<Integer> cir) {
        MobSpawnerHelperHelper.setSpawnableChunksCount(serverWorld, this.spawnedMobs.size());
    }
}
