package net.nullspace_mc.tapestry.mixin.loggers.projectiles;

import net.minecraft.entity.Entity;
import net.minecraft.entity.living.LivingEntity;
import net.minecraft.entity.thrown.ThrownEntity;
import net.minecraft.world.World;
import net.nullspace_mc.tapestry.loggers.helpers.ProjectileLoggerHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownEntity.class)
public abstract class ThrownEntityMixin extends Entity {

    ProjectileLoggerHelper helper;

    public ThrownEntityMixin(World world) {
        super(world);
    }

    @Inject(method = "<init>(Lnet/minecraft/world/World;Lnet/minecraft/entity/living/LivingEntity;)V", at = @At("RETURN"))
    private void addLogger(World world, LivingEntity thrower, CallbackInfo ci) {
        helper = new ProjectileLoggerHelper();
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tickCheck(CallbackInfo ci) {
        if (helper != null) {
            helper.tick(this.x, this.y, this.z, this.velocityX, this.velocityY, this.velocityZ);
        }
    }

    @Override
    public void remove() {
        super.remove();
        if (helper != null) {
            helper.onFinish();
        }
    }
}