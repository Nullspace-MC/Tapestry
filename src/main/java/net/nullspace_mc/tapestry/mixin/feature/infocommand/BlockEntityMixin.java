package net.nullspace_mc.tapestry.mixin.feature.infocommand;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.block.entity.BlockEntity;

@Mixin(BlockEntity.class)
public interface BlockEntityMixin {

    @Accessor("TYPE_TO_ID")
    static Map<Class<?>, String> getTypeToId() {
        return null;
    }
}
