package net.nullspace_mc.tapestry.mixin.feature.kabovillagemarker;

import org.apache.commons.io.Charsets;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.network.Connection;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.server.network.handler.ServerLoginNetworkHandler;

import net.nullspace_mc.tapestry.settings.Settings;

@Mixin(ServerLoginNetworkHandler.class)
public abstract class ServerLoginNetworkHandlerMixin {

    @Redirect(
            method = "acceptLogin",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/PlayerManager;onLogin(Lnet/minecraft/network/Connection;Lnet/minecraft/server/entity/living/player/ServerPlayerEntity;)V"
            )
    )
    private void pollKVMPlayer(PlayerManager pm, Connection connection, ServerPlayerEntity player) {
        pm.onLogin(connection, player);

        if (Settings.kaboVillageMarker) {
            try {
                CustomPayloadS2CPacket packet = new CustomPayloadS2CPacket("KVM|Poll", "".getBytes(Charsets.UTF_8));
                if (player != null && packet != null) {
                    player.networkHandler.sendPacket(packet);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
