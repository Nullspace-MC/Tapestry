package net.nullspace_mc.tapestry.loggers;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.nullspace_mc.tapestry.util.MathUtil;

public class TPSLogger extends Logger {
    
    public String getName() {
        return "tps";
    }

    public Text tickLogger(MinecraftServer server, ServerPlayerEntity player) {
        double mspt = MathUtil.round(2, MathHelper.average(server.serverTickTimes) * 1.0E-6D);
        double tps = MathUtil.round(2, 1000 / mspt);
        if (tps > 20) tps = 20;
        return new LiteralText(String.format("TPS: %.1f MSPT: %.2f", tps, mspt));
    }
}
