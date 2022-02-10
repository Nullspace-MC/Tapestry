package net.nullspace_mc.tapestry.mixin.feature.kabovillagemarker;

import net.minecraft.server.PlayerManager;
import net.minecraft.server.entity.ServerPlayerEntity;
import net.nullspace_mc.tapestry.helpers.KaboVillageMarker;
import net.nullspace_mc.tapestry.settings.Settings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
