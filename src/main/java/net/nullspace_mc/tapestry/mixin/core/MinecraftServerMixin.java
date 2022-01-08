package net.nullspace_mc.tapestry.mixin.core;

import net.nullspace_mc.tapestry.Tapestry;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
    
    @Inject(method = "start", at = @At("HEAD"))
    public void onStart(CallbackInfo ci) {
        Tapestry.onStart();
    }
}
