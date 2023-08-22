package net.nullspace_mc.tapestry.mixin.feature.saveunloadchunks;

import net.minecraft.server.ChunkMap;
import net.minecraft.util.Long2ObjectHashMap;
import net.nullspace_mc.tapestry.helpers.ChunkMapHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ChunkMap.class)
public abstract class ChunkMapMixin implements ChunkMapHelper {

	@Shadow
	private Long2ObjectHashMap chunks;

	@Override
	public Long2ObjectHashMap getChunks() {
		return chunks;
	}
}
