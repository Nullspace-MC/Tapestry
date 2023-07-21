package net.nullspace_mc.tapestry.mixin.feature.tcpnodelay;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.nullspace_mc.tapestry.settings.Settings;

@Mixin(targets = "net.minecraft.server.network.ConnectionListener$1")
public class RemoteChannelHandlerMixin {
    
    @SuppressWarnings("unchecked")
    @ModifyArg(
            method = "initChannel",
            at = @At(
                    value = "INVOKE",
                    target = "Lio/netty/channel/ChannelConfig;setOption(Lio/netty/channel/ChannelOption;Ljava/lang/Object;)Z",
                    ordinal = 1
            ),
            index = 1,
            remap = false
    )
    private <T> T setTCPNoDelay(T value) {
        if(value instanceof Boolean) {
            return (T)Boolean.valueOf(Settings.tcpNoDelay);
        } else {
            return value;
        }
    }
}
