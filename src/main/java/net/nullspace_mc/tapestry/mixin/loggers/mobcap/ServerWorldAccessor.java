package net.nullspace_mc.tapestry.mixin.loggers.mobcap;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.NaturalSpawner;

@Mixin(ServerWorld.class)
public interface ServerWorldAccessor {

    @Accessor("naturalSpawner")
    NaturalSpawner getNaturalSpawner();

}
