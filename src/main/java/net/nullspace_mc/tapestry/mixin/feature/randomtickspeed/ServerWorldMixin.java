package net.nullspace_mc.tapestry.mixin.feature.randomtickspeed;

import net.minecraft.server.world.ServerWorld;
import net.nullspace_mc.tapestry.settings.Settings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {

    @ModifyConstant(
            method = "tickChunks",
            constant = @Constant(
                    intValue = 3,
                    ordinal = 2
            )
    )
    private int randomTickSpeed(int value) {
        return Settings.randomTickSpeed;
    }
}
