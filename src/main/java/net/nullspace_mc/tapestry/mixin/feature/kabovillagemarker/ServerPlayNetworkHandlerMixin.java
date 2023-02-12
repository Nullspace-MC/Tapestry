package net.nullspace_mc.tapestry.mixin.feature.kabovillagemarker;

import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.server.network.handler.ServerPlayNetworkHandler;
import net.nullspace_mc.tapestry.helpers.KaboVillageMarker;
import net.nullspace_mc.tapestry.settings.Settings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {

    @Shadow
    private MinecraftServer server;

    @Inject(
            method = "handleCustomPayload",
            at = @At("HEAD")
    )
    private void processKVMAnswer(CustomPayloadC2SPacket packet, CallbackInfo ci) {
        if ("KVM|Answer".equals(packet.getChannel()) && Settings.kaboVillageMarker) {
            try {
                String data = new String(packet.getData());
                Object[] players = this.server.getPlayerManager().players.toArray();
                int numPlayers = players.length;

                for (int i = 0; i < numPlayers; ++i) {
                    Object playerObj = players[i];
                    ServerPlayerEntity player = null;
                    
                    if (playerObj instanceof ServerPlayerEntity) {
                        player = (ServerPlayerEntity)playerObj;
                    }

                    if (player != null && player.getUuid().toString().equals(data)) {
                        KaboVillageMarker.addPlayerToList(data, player);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
