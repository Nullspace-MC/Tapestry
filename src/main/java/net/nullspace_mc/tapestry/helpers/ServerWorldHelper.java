package net.nullspace_mc.tapestry.helpers;

import java.util.List;

import net.minecraft.server.world.ScheduledTick;
import net.minecraft.world.gen.structure.StructureBox;

public interface ServerWorldHelper {
    public List<ScheduledTick> collectScheduledTicks(StructureBox bounds);
}
