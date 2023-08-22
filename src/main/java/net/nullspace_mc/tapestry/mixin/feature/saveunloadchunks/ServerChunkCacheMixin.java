package net.nullspace_mc.tapestry.mixin.feature.saveunloadchunks;

import net.minecraft.server.world.chunk.ServerChunkCache;
import net.nullspace_mc.tapestry.helpers.ServerChunkCacheHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(ServerChunkCache.class)
public abstract class ServerChunkCacheMixin implements ServerChunkCacheHelper {

	@Shadow
	private List chunks;

	@Override
	public List getChunks() {
		return chunks;
	}
}
