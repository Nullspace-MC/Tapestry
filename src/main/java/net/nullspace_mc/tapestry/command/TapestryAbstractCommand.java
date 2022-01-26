package net.nullspace_mc.tapestry.command;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.command.AbstractCommand;
import net.minecraft.command.Command;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class TapestryAbstractCommand extends AbstractCommand {
    public BlockPos parseBlockPos(CommandSource source, String[] args, int startIdx) {
        int x = source.getBlockPos().x;
        int y = source.getBlockPos().y;
        int z = source.getBlockPos().z;
        x = MathHelper.floor(parseCoordinate(source, (double)x, args[startIdx]));
        y = MathHelper.floor(parseCoordinate(source, (double)y, args[startIdx + 1], 0, 255));
        z = MathHelper.floor(parseCoordinate(source, (double)z, args[startIdx + 2]));
        return new BlockPos(x, y, z);
    }

    public List<String> getCoordinateSuggestions(CommandSource source, String[] args, int idx) {
        HitResult hit = null;
        if (source instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity)source;
            double range = player.abilities.creativeMode ? 5.0D : 4.5D;
            Vec3d playerLook = player.getCameraRotation().normalize();
            Vec3d playerPos = source.getWorld().getVec3dPool().create(player.x, player.y + player.getEyeHeight(), player.z);
            Vec3d targetPos = source.getWorld().getVec3dPool().create(playerPos.x + range*playerLook.x, playerPos.y + range*playerLook.y, playerPos.z + range*playerLook.z);
            hit = source.getWorld().rayTrace(playerPos, targetPos, false, true, false);
        }

        if (hit == null) {
            return Lists.newArrayList("~");
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

            return Lists.newArrayList(new String[] {s});
        }
    }

    /**
     * Added so the compiler would stop crying
     */
    public int compareTo(Object object) {
        if(object instanceof Command) {
            return super.compareTo((Command)object);
        } else {
            return this.toString().compareTo(object.toString());
        }
    }
}
