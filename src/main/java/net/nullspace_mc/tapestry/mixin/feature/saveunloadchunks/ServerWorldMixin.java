package net.nullspace_mc.tapestry.mixin.feature.saveunloadchunks;

import net.minecraft.server.ChunkMap;
import net.minecraft.server.world.ServerWorld;

import net.minecraft.server.world.chunk.ServerChunkCache;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.storage.WorldStorage;
import net.nullspace_mc.tapestry.settings.Settings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World {

	public ServerWorldMixin(WorldStorage storage, String name, Dimension dimension, WorldSettings settings, Profiler profiler) {
		super(storage, name, dimension, settings, profiler);
	}

	@Shadow
	public ServerChunkCache chunkCache;

	@Shadow
	public ChunkMap chunkMap;

	@Inject(method = "save", at = @At(value = "TAIL"))
	public void redirectSave(CallbackInfo ci) {
		if (Settings.saveUnloadChunks) {
			if (this.chunkSource.canSave()) {
				List<WorldChunk> chunks = ((ServerChunkCacheAccessor) this.chunkCache).getChunks();

				for (WorldChunk chunk : chunks) {
					if (!isLoaded(chunk.chunkX, chunk.chunkZ)) {
						this.chunkCache.unloadChunk(chunk.chunkX, chunk.chunkZ);
					}
				}
			}
		}
	}

	private boolean isLoaded(int x, int z) {
		long chunk = (long)x + 2147483647L | (long)z + 2147483647L << 32;
		return ((ChunkMapAccessor)this.chunkMap).getChunks().get(chunk) != null;
	}
}
