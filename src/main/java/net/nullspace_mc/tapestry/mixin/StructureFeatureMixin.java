package net.nullspace_mc.tapestry.mixin;

import java.util.Iterator;
import java.util.Map;
import net.minecraft.world.Generatable;
import net.minecraft.world.World;
import net.minecraft.world.gen.GeneratorConfig;
import net.minecraft.structure.StructureFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StructureFeature.class)
public abstract class StructureFeatureMixin extends Generatable {
    // Remove once TapestrySettings is added
    private static final boolean fix = true;

    @Shadow
    private Map field_9695;

    @Invoker("method_1688")
    abstract void invokeMethod_1688(World world);
    
    // Fixes the check for intersection with a structure bounding box for the
    // purpose of fortress mob nether brick spawning
    @Inject(method = "method_9012(III)Z", at = @At("HEAD"), cancellable = true)
    public void fixBBCheck(final int x, final int y, final int z, final CallbackInfoReturnable<Boolean> cir) {
        if(this.fix) {
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
