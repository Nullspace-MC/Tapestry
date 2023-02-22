package net.nullspace_mc.tapestry.mixin.feature.clonecommand;

import java.util.ArrayList;
import java.util.Collection;
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
    private List<ScheduledTick> scheduledTicksThisTick;

    public List<ScheduledTick> collectScheduledTicks(StructureBox bounds) {
        ArrayList<ScheduledTick> ticks = null;

        collectScheduledTicks(this.scheduledTicksInOrder, ticks, bounds);
        collectScheduledTicks(this.scheduledTicksThisTick, ticks, bounds);

        return ticks;
    }

    private static void collectScheduledTicks(Iterable<ScheduledTick> src, Collection<ScheduledTick> dst, StructureBox bounds) {
        for (ScheduledTick tick : src) {
            if (tick.x >= bounds.minX && tick.x <= bounds.maxX && tick.y >= bounds.minY && tick.y <= bounds.maxY && tick.z >= bounds.minZ && tick.z <= bounds.maxZ) {
                dst.add(tick);
            }
        }
    }
}
