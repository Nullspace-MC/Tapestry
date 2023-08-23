package net.nullspace_mc.tapestry.mixin.feature.instantscheduling;

import net.minecraft.server.world.ServerWorld;
import net.nullspace_mc.tapestry.settings.Settings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {

    @Redirect(
            method = "Lnet/minecraft/server/world/ServerWorld;scheduleTick(IIILnet/minecraft/block/Block;II)V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/server/world/ServerWorld;doTicksImmediately:Z"
            )
    )
    public boolean forceInstantScheduling(ServerWorld instance) {
        return instance.doTicksImmediately || Settings.instantScheduling;
    }
}
