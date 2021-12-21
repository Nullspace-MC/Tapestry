package net.nullspace_mc.tapestry.mixin.feature.fortressspawningfix;

import java.util.Iterator;
import java.util.Map;
import net.minecraft.world.Generatable;
import net.minecraft.world.World;
import net.minecraft.world.gen.GeneratorConfig;
import net.minecraft.structure.StructureFeature;
import net.nullspace_mc.tapestry.settings.Settings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StructureFeature.class)
public abstract class StructureFeatureMixin extends Generatable {
    @Shadow
    private Map field_9695;

    @Invoker("method_1688")
    abstract void invokeMethod_1688(World world);
    
    /**
     * Fixes the check for intersection with a structure bounding
     * box for the purpose of fortress mob nether brick spawning
     */
    @Inject(method = "method_9012", at = @At("HEAD"), cancellable = true)
    public void fixBBCheck(final int x, final int y, final int z, final CallbackInfoReturnable<Boolean> cir) {
        if(Settings.fortressSpawningFix) {
            this.invokeMethod_1688(this.field_1850);
            final Iterator structIter = this.field_9695.values().iterator();
            GeneratorConfig struct;
            boolean intersects = false;

            while(structIter.hasNext()) {
                struct = (GeneratorConfig)structIter.next();
                if(struct.method_1751() && struct.getBox().method_8986(x, z, x, z)) {
                    intersects = true;
                    break;
                }
            }

            cir.setReturnValue(intersects);
        }
    }
}
