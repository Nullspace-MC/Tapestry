package net.nullspace_mc.tapestry.mixin.loggers.tnt;

import net.minecraft.entity.Entity;
import net.minecraft.entity.PrimedTntEntity;
import net.minecraft.entity.living.LivingEntity;
import net.minecraft.world.World;
import net.nullspace_mc.tapestry.loggers.helpers.TNTLoggerHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PrimedTntEntity.class)
public abstract class PrimedTntEntityMixin extends Entity {

    TNTLoggerHelper helper;

    public PrimedTntEntityMixin(World world) {
        super(world);
    }

    @Inject(method = "<init>(Lnet/minecraft/world/World;DDDLnet/minecraft/entity/living/LivingEntity;)V", at = @At("RETURN"))
    private void addLogger(World world, double x, double y, double z, LivingEntity igniter, CallbackInfo ci) {
        helper = new TNTLoggerHelper();
        helper.onPrimed(x, y, z, this.velocityX, this.velocityY, this.velocityZ);
    }

    @Inject(method = "explode", at = @At(value = "HEAD"))
    private void onExplode(CallbackInfo ci) {
        if (helper != null) {
            helper.onExploded(this.x, this.y, this.z);
        }
    }
}
