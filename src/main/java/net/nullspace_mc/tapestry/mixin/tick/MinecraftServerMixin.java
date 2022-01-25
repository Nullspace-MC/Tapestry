package net.nullspace_mc.tapestry.mixin.tick;

import net.minecraft.server.MinecraftServer;
import net.nullspace_mc.tapestry.helpers.TickSpeedHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
    @Shadow
    private boolean loading;

    @Shadow
    public abstract void tick();

    @ModifyConstant(
            method = "run",
            constant = @Constant(
                    longValue = 50L
            )
    )
    private long setTickSpeed(long constant) {
        return TickSpeedHelper.mspt;
    }

    @ModifyConstant(
            method = "run",
            constant = @Constant(
                    longValue = 2000L
            )
    )
    private long setWarnTimer(long constant) {
        return 40*TickSpeedHelper.mspt;
    }

    @Redirect(
            method = "run",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/MinecraftServer;getTimeMillis()J",
                    ordinal = 1
            )
    )
    private long tickWarp() {
        while (TickSpeedHelper.warping) {
            if (TickSpeedHelper.ticksToWarp != 0L) {
                this.tick();
                ++TickSpeedHelper.warpedTicks;
                if (TickSpeedHelper.ticksToWarp > 0L) {
                    --TickSpeedHelper.ticksToWarp;
                }
                this.loading = true;
            } else {
                TickSpeedHelper.stopTickWarp();
            }
        }

        return MinecraftServer.getTimeMillis();
    }
}
