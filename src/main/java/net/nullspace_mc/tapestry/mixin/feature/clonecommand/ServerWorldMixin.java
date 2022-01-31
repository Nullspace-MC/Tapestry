package net.nullspace_mc.tapestry.mixin.feature.clonecommand;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ScheduledTick;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.SaveHandler;
import net.minecraft.world.World;
import net.minecraft.world.WorldInfo;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.gen.structure.StructureBox;
import net.nullspace_mc.tapestry.helpers.ServerWorldHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World implements ServerWorldHelper {
    @Shadow
    private TreeSet scheduledTicksInOrder;

    @Shadow
    private List currentScheduledTicks;

    @Environment(EnvType.CLIENT)
    public ServerWorldMixin(SaveHandler saveHandler, String levelName, Dimension dimension, WorldInfo levelInfo, Profiler profiler) {
        super(saveHandler, levelName, dimension, levelInfo, profiler);
    }

    @Environment(EnvType.SERVER)
    public ServerWorldMixin(SaveHandler saveHandler, String levelName, WorldInfo levelInfo, Dimension dimension, Profiler profiler) {
        super(saveHandler, levelName, levelInfo, dimension, profiler);
    }

    public List<ScheduledTick>getScheduledTicksInBox(StructureBox box) {
        ArrayList<ScheduledTick> ticks = null;

        for (int v = 0; v < 2; ++v) {
            Iterator iter;
            if (v == 0) {
                iter = this.scheduledTicksInOrder.iterator();
            } else {
                iter = this.currentScheduledTicks.iterator();
            }

            while (iter.hasNext()) {
                ScheduledTick tick = (ScheduledTick)iter.next();
                if (tick.x >= box.minX && tick.x <= box.maxX && tick.y >= box.minY && tick.y <= box.maxY && tick.z >= box.minZ && tick.z <= box.maxZ) {
                    if (ticks == null) {
                        ticks = new ArrayList<ScheduledTick>();
                    }

                    ticks.add(tick);
                }
            }
        }

        return ticks;
    }
}
