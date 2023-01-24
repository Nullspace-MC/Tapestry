package net.nullspace_mc.tapestry.mixin.feature.kabovillagemarker;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.PlayerManager;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;

import net.nullspace_mc.tapestry.helpers.KaboVillageMarker;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {

    @Inject(
            method = "remove",
            at = @At("TAIL")
    )
    private void removeKVMPlayer(ServerPlayerEntity player, CallbackInfo ci) {
        KaboVillageMarker.removePlayerFromList(player.getUuid().toString());
    }
}
