package net.nullspace_mc.tapestry.mixin.feature.randomredstonedust;

import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.client.texture.ISprite;
import net.minecraft.world.World;
import net.nullspace_mc.tapestry.settings.Settings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.ArrayList;
import java.util.Collections;

@Mixin(RedstoneWireBlock.class)
public class RedstoneWireBlockMixin {
    @Inject(
            method = "update",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Set;clear()V"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void MakeRedstoneDustRandom(World x, int y, int z, int par4, CallbackInfo ci, ArrayList list) {
        if (Settings.randomRedstoneDust) Collections.shuffle(list);
    }
}
