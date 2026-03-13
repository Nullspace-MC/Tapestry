package net.nullspace_mc.tapestry.mixin.loggers.projectiles;

import net.minecraft.entity.Entity;
import net.minecraft.entity.living.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.world.World;
import net.nullspace_mc.tapestry.loggers.helpers.ProjectileLoggerHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArrowEntity.class)
public abstract class ArrowEntityMixin extends Entity {

    @Unique ProjectileLoggerHelper helper;

    @Shadow private boolean inGround;

    public ArrowEntityMixin(World world) {
        super(world);
    }

    @Inject(method = "<init>(Lnet/minecraft/world/World;Lnet/minecraft/entity/living/LivingEntity;F)V", at = @At("RETURN"))
    private void addLogger(World world, LivingEntity shooter, float speed, CallbackInfo ci) {
        helper = new ProjectileLoggerHelper();
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tickCheck(CallbackInfo ci) {
        if (helper == null) {
            return;
        }

        if (!inGround) {
            helper.tick(this.x, this.y, this.z, this.velocityX, this.velocityY, this.velocityZ);
        } else if (!helper.getPositions().isEmpty()) {
            helper.onFinish();
        }
    }
}
