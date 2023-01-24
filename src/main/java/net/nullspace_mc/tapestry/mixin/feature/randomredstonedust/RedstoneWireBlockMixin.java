package net.nullspace_mc.tapestry.mixin.feature.randomredstonedust;

import java.util.ArrayList;
import java.util.Collections;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import net.nullspace_mc.tapestry.settings.Settings;

@Mixin(RedstoneWireBlock.class)
public class RedstoneWireBlockMixin {

    @Inject(
            method = "updatePower",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Set;clear()V"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void MakeRedstoneDustRandom(World x, int y, int z, int par4, CallbackInfo ci, ArrayList<Vec3i> list) {
        if (Settings.randomRedstoneDust) Collections.shuffle(list);
    }
}
