package net.nullspace_mc.tapestry.mixin.feature.arrowspersist;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.world.World;
import net.nullspace_mc.tapestry.settings.Settings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ArrowEntity.class)
public abstract class ArrowEntityMixin extends Entity {
    @Shadow
    private boolean inGround;

    @Shadow
    private int lifeTicks;

    protected ArrowEntityMixin(World world) {
        super(world);
    }

    /**
     * Makes the removal of arrows that have been stuck in the
     * ground for too long depend on the arrowsPersist setting
     */
    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/ArrowEntity;remove()V"))
    private void conditionalRemove(ArrowEntity a) {
        if(!this.inGround || (this.lifeTicks == 1200 && !Settings.arrowsPersist)) {
            this.remove();
        }
    }
}
