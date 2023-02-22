package net.nullspace_mc.tapestry.mixin.core;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.Bootstrap;

import net.nullspace_mc.tapestry.Tapestry;

@Mixin(Bootstrap.class)
public class BootstrapMixin {

    @Shadow private static boolean initialized = false;
    private static boolean tapestry_alreadyInitialized = false;

    @Inject(
        method = "init",
        at = @At(
            value = "HEAD"
        )
    )
    private static void preInit(CallbackInfo ci) {
        tapestry_alreadyInitialized = initialized;
    }

    @Inject(
        method = "init",
        at = @At(
            value = "TAIL"
        )
    )
    private static void postInit(CallbackInfo ci) {
        if (!tapestry_alreadyInitialized) {
            Tapestry.initialize();
        }
    }
}
