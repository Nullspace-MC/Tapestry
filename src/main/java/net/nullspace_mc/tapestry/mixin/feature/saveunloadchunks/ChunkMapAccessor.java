package net.nullspace_mc.tapestry.mixin.feature.saveunloadchunks;

import net.minecraft.server.ChunkMap;
import net.minecraft.util.Long2ObjectHashMap;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ChunkMap.class)
public interface ChunkMapAccessor {

	@Accessor("chunksByPos")
	Long2ObjectHashMap<WorldChunk> getChunks();
}
