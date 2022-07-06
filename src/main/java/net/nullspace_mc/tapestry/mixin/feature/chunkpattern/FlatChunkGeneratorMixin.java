package net.nullspace_mc.tapestry.mixin.feature.chunkpattern;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.server.world.chunk.FlatChunkGenerator;
import net.minecraft.world.gen.layer.FlatWorldLayer;
import net.nullspace_mc.tapestry.settings.Settings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(FlatChunkGenerator.class)
public abstract class FlatChunkGeneratorMixin {

    @Shadow
    private Block[] blocks;

    @Shadow
    private byte[] blockMetadata;

    private int fullLayers = 0;

    @Inject(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/gen/layer/FlatWorldLayer;method_8983()I"
            )
    )
    private void countFullLayer(World world, long l, boolean bl, String string, CallbackInfo ci) {
        ++fullLayers;
    }

    /**
     * Swap layers for setBlock call
     */
    @Inject(
            method = "getChunk",
            locals = LocalCapture.CAPTURE_FAILEXCEPTION,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/chunk/ChunkSection;setBlock(IIILnet/minecraft/block/Block;)V"
            )
    )
    private void swapSetBlock(int chunkX, int chunkZ, CallbackInfoReturnable cir, Chunk chunk, int y, Block block, int csIdx, ChunkSection cs, int x, int z) {
        if (Settings.chunkPattern && fullLayers == 2 && y < 2 && ((chunkX ^ chunkZ) & 1) != 0) {
            cs.setBlock(x, y & 15, z, this.blocks[1 - y]);
        } else {
            cs.setBlock(x, y & 15, z, block);
        }
    }

    /**
     * Disable original setBlock call
     */
    @Redirect(
            method = "getChunk",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/chunk/ChunkSection;setBlock(IIILnet/minecraft/block/Block;)V"
            )
    )
    private void disableSetBlock(ChunkSection cs, int x, int y, int z, Block b) {}

    /**
     * Swap layers for setBlockMeta call
     */
    @Inject(
            method = "getChunk",
            locals = LocalCapture.CAPTURE_FAILEXCEPTION,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/chunk/ChunkSection;setBlockMetadata(IIII)V"
            )
    )
    private void swapSetBlockMetadata(int chunkX, int chunkZ, CallbackInfoReturnable cir, Chunk chunk, int y, Block block, int csIdx, ChunkSection cs, int x, int z) {
        if (Settings.chunkPattern && fullLayers == 2 && y < 2 && ((chunkX ^ chunkZ) & 1) != 0) {
            cs.setBlockMetadata(x, y & 15, z, this.blockMetadata[1 - y]);
        } else {
            cs.setBlockMetadata(x, y & 15, z, this.blockMetadata[y]);
        }
    }

    /**
     * Disable original setBlockMeta call
     */
    @Redirect(
            method = "getChunk",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/chunk/ChunkSection;setBlockMetadata(IIII)V"
            )
    )
    private void disableSetBlockMetadata(ChunkSection cs, int x, int y, int z, int m) {}
}
