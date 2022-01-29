package net.nullspace_mc.tapestry.mixin.core;

import net.minecraft.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(BlockEntity.class)
public interface BlockEntityMixin {
    @Accessor("typeToId")
    Map getTypeToId();
}
