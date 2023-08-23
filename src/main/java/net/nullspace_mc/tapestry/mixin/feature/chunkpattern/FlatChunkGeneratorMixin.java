package net.nullspace_mc.tapestry.mixin.feature.chunkpattern;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.chunk.WorldChunkSection;
import net.minecraft.world.gen.chunk.FlatChunkGenerator;

import net.nullspace_mc.tapestry.settings.Settings;

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
                    target = "Lnet/minecraft/world/gen/chunk/FlatWorldLayer;getBlockMetadata()I"
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
                    target = "Lnet/minecraft/world/chunk/WorldChunkSection;setBlock(IIILnet/minecraft/block/Block;)V"
            )
    )
    private void swapSetBlock(int chunkX, int chunkZ, CallbackInfoReturnable<WorldChunk> cir, WorldChunk chunk, int y, Block block, int csIdx, WorldChunkSection cs, int x, int z) {
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
                    target = "Lnet/minecraft/world/chunk/WorldChunkSection;setBlock(IIILnet/minecraft/block/Block;)V"
            )
    )
    private void disableSetBlock(WorldChunkSection cs, int x, int y, int z, Block b) {}

    /**
     * Swap layers for setBlockMeta call
     */
    @Inject(
            method = "getChunk",
            locals = LocalCapture.CAPTURE_FAILEXCEPTION,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/chunk/WorldChunkSection;setBlockMetadata(IIII)V"
            )
    )
    private void swapSetBlockMetadata(int chunkX, int chunkZ, CallbackInfoReturnable<WorldChunk> cir, WorldChunk chunk, int y, Block block, int csIdx, WorldChunkSection cs, int x, int z) {
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
                    target = "Lnet/minecraft/world/chunk/WorldChunkSection;setBlockMetadata(IIII)V"
            )
    )
    private void disableSetBlockMetadata(WorldChunkSection cs, int x, int y, int z, int m) {}
}
