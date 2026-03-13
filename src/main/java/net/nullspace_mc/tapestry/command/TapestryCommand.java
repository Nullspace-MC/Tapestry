package net.nullspace_mc.tapestry.command;

import java.util.Arrays;
import java.util.List;

import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.server.command.AbstractCommand;
import net.minecraft.server.command.source.CommandSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.HitResult;

public abstract class TapestryCommand extends AbstractCommand {

    /**
     * Parse an x, y, z triple from command arguments
     *
     * @param source    The command sender
     * @param args      The command arguments
     * @param startIdx  The index in args[] of the x coordinate of the desired coordinate triple
     * @return Returns a BlockPos corresponding to the coordinates specified in the command
     */
    public static BlockPos parseBlockPos(CommandSource source, String[] args, int startIdx) {
        int x = source.getCommandSourceBlockPos().x;
        int y = source.getCommandSourceBlockPos().y;
        int z = source.getCommandSourceBlockPos().z;
        x = MathHelper.floor(parseCoordinate(source, (double)x, args[startIdx]));
        y = MathHelper.floor(parseCoordinate(source, (double)y, args[startIdx + 1], 0, 255));
        z = MathHelper.floor(parseCoordinate(source, (double)z, args[startIdx + 2]));
        return new BlockPos(x, y, z);
    }

    /**
     * Get tab completions for coordinates based on the block that the player is looking at
     *
     * @param source    The command sender
     * @param args      The command arguments
     * @param idx       The index in args[] where the x coordinate of the desired coordinate triple is expected to be
     * @return Returna a List containing the coordinate to suggest
     */
    public static List<String> suggestCoordinates(CommandSource source, String[] args, int idx) {
        HitResult hit = null;
        if (source instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity)source;
            double range = player.abilities.creativeMode ? 5.0D : 4.5D;
            Vec3d playerLook = player.getLookVector().normalize();
            Vec3d playerPos = source.getCommandSourceWorld().getVec3dPool().create(player.x, player.y + player.getEyeHeight(), player.z);
            Vec3d targetPos = source.getCommandSourceWorld().getVec3dPool().create(playerPos.x + range*playerLook.x, playerPos.y + range*playerLook.y, playerPos.z + range*playerLook.z);
            hit = source.getCommandSourceWorld().rayTrace(playerPos, targetPos, false, true, false);
        }

        if (hit == null) {
            return Arrays.asList("~");
        } else {
            int i = args.length - 1;
            String s;

            if (i == idx) {
                s = Integer.toString(hit.x);
            } else if (i == idx + 1) {
                s = Integer.toString(hit.y);
            } else if (i == idx + 2) {
                s = Integer.toString(hit.z);
            } else {
                return null;
            }

            return Arrays.asList(s);
        }
    }
}
