package net.nullspace_mc.tapestry.mixin.feature.clonecommand;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.server.world.ScheduledTick;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.gen.structure.StructureBox;

import net.nullspace_mc.tapestry.helpers.ServerWorldHelper;

@Mixin(ServerWorld.class)
public class ServerWorldMixin implements ServerWorldHelper {

    @Shadow
    private TreeSet<ScheduledTick> scheduledTicksInOrder;

    @Shadow
    private List<ScheduledTick> currentScheduledTicks;

    public List<ScheduledTick>getScheduledTicksInBox(StructureBox box) {
        ArrayList<ScheduledTick> ticks = null;

        for (int v = 0; v < 2; ++v) {
            Iterator<ScheduledTick> iter;
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
