package net.nullspace_mc.tapestry.mixin.feature.kabovillagemarker;

import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.entity.ServerPlayerEntity;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import net.nullspace_mc.tapestry.helpers.KaboVillageMarker;
import net.nullspace_mc.tapestry.settings.Settings;
import org.apache.commons.io.Charsets;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerLoginNetworkHandler.class)
public abstract class ServerLoginNetworkHandlerMixin {

    @Redirect(
            method = "setUuid",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/PlayerManager;onPlayerConnect(Lnet/minecraft/network/ClientConnection;Lnet/minecraft/server/entity/ServerPlayerEntity;)V"
            )
    )
    private void pollKVMPlayer(PlayerManager pm, ClientConnection connection, ServerPlayerEntity player) {
        pm.onPlayerConnect(connection, player);

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
