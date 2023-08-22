package net.nullspace_mc.tapestry.mixin.feature.saveunloadchunks;

import net.minecraft.server.world.chunk.ServerChunkCache;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(ServerChunkCache.class)
public interface ServerChunkCacheAccessor {

	@Accessor("chunks")
	List<WorldChunk> getChunks();
}
