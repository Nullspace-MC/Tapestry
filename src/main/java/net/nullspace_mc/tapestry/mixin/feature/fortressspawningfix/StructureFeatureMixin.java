package net.nullspace_mc.tapestry.mixin.feature.fortressspawningfix;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.World;
import net.minecraft.world.gen.Generator;
import net.minecraft.world.gen.structure.StructureFeature;
import net.minecraft.world.gen.structure.StructureStart;

import net.nullspace_mc.tapestry.settings.Settings;

@Mixin(StructureFeature.class)
public abstract class StructureFeatureMixin extends Generator {

    @Shadow
    private Map<Long, StructureStart> structures;

    @Invoker("loadSavedData")
    protected abstract void invokeLoadSavedData(World world);
    
    /**
     * Fixes the check for intersection with a structure bounding
     * box for the purpose of fortress mob nether brick spawning
     */
    @Inject(
            method = "isInsideStructureBounds",
            at = @At("HEAD"),
            cancellable = true
    )
    private void fixBBCheck(final int x, final int y, final int z, final CallbackInfoReturnable<Boolean> cir) {
        if(Settings.fortressSpawningFix) {
            this.invokeLoadSavedData(this.world);

            boolean intersects = false;

            for (StructureStart struct : this.structures.values()) {
                if(struct.isValid() && struct.getBounds().intersects(x, z, x, z)) {
                    intersects = true;
                    break;
                }
            }

            cir.setReturnValue(intersects);
        }
    }
}
