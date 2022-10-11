package net.nullspace_mc.tapestry.mixin.feature.tcpnodelay;

import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelOption;
import net.nullspace_mc.tapestry.settings.Settings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(targets = "net.minecraft.server.network.RemoteChannelHandler")
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
