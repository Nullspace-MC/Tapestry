package net.nullspace_mc.tapestry.mixin.feature.infocommand;

import java.util.Map;
import net.minecraft.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlockEntity.class)
public interface BlockEntityMixin {
    @Accessor("typeToId")
    static Map getTypeToId() {
        return null;
    }
}
