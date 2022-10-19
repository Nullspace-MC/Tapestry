package net.nullspace_mc.tapestry.mixin.feature.clonecommand;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.server.world.ScheduledTick;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.gen.structure.StructureBox;
import net.minecraft.world.storage.WorldStorage;

import net.nullspace_mc.tapestry.helpers.ServerWorldHelper;

import net.ornithemc.api.EnvType;
import net.ornithemc.api.Environment;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World implements ServerWorldHelper {

    @Shadow
    private TreeSet scheduledTicksInOrder;

    @Shadow
    private List currentScheduledTicks;

    @Environment(EnvType.CLIENT)
    public ServerWorldMixin(WorldStorage storage, String name, Dimension dimension, WorldSettings settings, Profiler profiler) {
        super(storage, name, dimension, settings, profiler);
    }

    @Environment(EnvType.SERVER)
    public ServerWorldMixin(WorldStorage storage, String name, WorldSettings settings, Dimension dimension, Profiler profiler) {
        super(storage, name, settings, dimension, profiler);
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
