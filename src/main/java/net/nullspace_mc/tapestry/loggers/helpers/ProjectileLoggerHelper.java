package net.nullspace_mc.tapestry.loggers.helpers;

import net.minecraft.text.Formatting;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.nullspace_mc.tapestry.loggers.LoggerRegistry;
import net.nullspace_mc.tapestry.loggers.ProjectileLogger;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ProjectileLoggerHelper {

    private final ProjectileLogger logger;
    private final ArrayList<Vec3d> positions = new ArrayList<>();
    private final ArrayList<Vec3d> motions = new ArrayList<>();

    public ProjectileLoggerHelper() {
        logger = (ProjectileLogger)LoggerRegistry.getLoggerFromName("projectiles");
    }

    public void tick(double x, double y, double z, double motionX, double motionY, double motionZ) {
        if (logger.getPlayersSubscribed().size() < 1) return;
        positions.add(Vec3d.of(x, y, z));
        motions.add(Vec3d.of(motionX, motionY, motionZ));
    }

    public void onFinish() {
        List<Text> full = new ArrayList<>();
        List<Text> brief = new ArrayList<>();

        full.add(new LiteralText(" "));
        for (int i = 0; i < positions.size(); i++) {
            Vec3d pos = positions.get(i);
            Vec3d mot = motions.get(i);
            full.add(formatFull(i, pos.x, pos.y, pos.z, mot.x, mot.y, mot.z));
            brief.add(formatBrief(i, pos.x, pos.y, pos.z, mot.x, mot.y, mot.z));
        }
        full.add(new LiteralText(" "));

        Text t = new LiteralText("");
        brief.forEach(t::append);
        logger.log(t, "brief");
        full.forEach(line -> logger.log(line, "full"));

        positions.clear();
        motions.clear();
    }

    public Text formatFull(int tick, double x, double y, double z, double vx, double vy, double vz) {
        DecimalFormat df = new DecimalFormat("#.#");
        Text t = new LiteralText("[Tick " + tick + "] ");
        t.setStyle(t.getStyle().setColor(Formatting.GRAY));
        Text t2 = new LiteralText(
                String.format("pos[ %s, %s, %s ] mot[ %s, %s, %s ]",
                        df.format(x), df.format(y), df.format(z),
                        df.format(vx), df.format(vy), df.format(vz)));
        t2.setStyle(t2.getStyle().setColor(Formatting.RESET));
        t.append(t2);
        return t;
    }

    public Text formatBrief(int tick, double x, double y, double z, double vx, double vy, double vz) {
        DecimalFormat df = new DecimalFormat("#.########");
        Text t = new LiteralText("x ");
        Text t2 = new LiteralText(
                String.format("Tick: %s\n-----\nx: %s\ny: %s\nz: %s\n-----\nmx: %s\nmy: %s\nmz: %s", tick,
                        df.format(x), df.format(y), df.format(z),
                        df.format(vx), df.format(vy), df.format(vz)));
        t.setStyle(t.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, t2)));
        return t;
    }
}
