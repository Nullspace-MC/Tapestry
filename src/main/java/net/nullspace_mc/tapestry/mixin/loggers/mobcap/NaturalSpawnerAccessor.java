package net.nullspace_mc.tapestry.mixin.loggers.mobcap;

import java.util.HashMap;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.NaturalSpawner;

@Mixin(NaturalSpawner.class)
public interface NaturalSpawnerAccessor {

    @Accessor("mobSpawningChunks")
    HashMap<ChunkPos, Boolean> getMobSpawningChunks();

}
