package net.nullspace_mc.tapestry.mixin.tick;

import net.minecraft.server.MinecraftServer;
import net.nullspace_mc.tapestry.helpers.TickSpeedHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @ModifyConstant(
            method = "run",
            constant = @Constant(
                    longValue = 50L
            )
    )
    private long setTickSpeed(long constant) {
        return TickSpeedHelper.getTickLength();
    }
}
