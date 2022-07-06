package net.nullspace_mc.tapestry.mixin.feature.fillupdates;

import net.minecraft.block.Block;
import net.minecraft.server.command.AbstractCommand;
import net.minecraft.server.command.SetBlockCommand;
import net.minecraft.world.World;
import net.nullspace_mc.tapestry.helpers.SetBlockHelper;
import net.nullspace_mc.tapestry.settings.Settings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SetBlockCommand.class)
public abstract class SetBlockCommandMixin extends AbstractCommand {

    @Redirect(
            method = "execute",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;setBlockWithMetadata(IIILnet/minecraft/block/Block;II)Z"
            )
    )
    private boolean disableUpdate(World world, int x, int y, int z, Block block, int metadata, int flags) {
        SetBlockHelper.applyFillUpdatesRule = true;
        return world.setBlockWithMetadata(x, y, z, block, metadata, Settings.fillUpdates ? 3 : 2);
    }
}
