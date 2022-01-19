package net.nullspace_mc.tapestry.mixin.feature.creativenoclip;

import net.minecraft.server.entity.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.nullspace_mc.tapestry.settings.Settings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {
    @Redirect(method = "onPlayerMove", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/entity/ServerPlayerEntity;isSleeping()Z", ordinal = 3))
    private boolean addCreativeNoClipCheck(ServerPlayerEntity player) {
        return player.isSleeping() || (Settings.creativeNoClip && player.abilities.creativeMode);
    }
}
