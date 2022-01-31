package net.nullspace_mc.tapestry.helpers;

import java.util.List;
import net.minecraft.util.ScheduledTick;
import net.minecraft.world.gen.structure.StructureBox;

public interface ServerWorldHelper {
    public List<ScheduledTick> getScheduledTicksInBox(StructureBox box);
}
