package net.nullspace_mc.tapestry.loggers.helpers;

import com.google.common.collect.Lists;
import net.minecraft.text.Formatting;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.nullspace_mc.tapestry.loggers.LoggerRegistry;
import net.nullspace_mc.tapestry.loggers.ProjectileLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectileLoggerHelper {

    private final ProjectileLogger logger;
    private final ArrayList<Vec3d> positions = new ArrayList<>();
    private final ArrayList<Vec3d> motions = new ArrayList<>();

    public ProjectileLoggerHelper() {
        logger = (ProjectileLogger)LoggerRegistry.getLoggerFromName("projectiles");
    }

    public void tick(double x, double y, double z, double motionX, double motionY, double motionZ) {
        if (logger.getPlayersSubscribed().isEmpty()) return;
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
        full.forEach(line -> logger.log(line, "full"));

        List<Text> finalBrief = Lists.partition(brief, 128) /* Splits the message into smaller chunks */
                .stream()
                .map(subList -> subList.stream().reduce(new LiteralText(""), Text::append))
                .collect(Collectors.toList());
        finalBrief.forEach(text -> logger.log(text, "brief"));

        positions.clear();
        motions.clear();
    }

    public Text formatFull(int tick, double x, double y, double z, double vx, double vy, double vz) {
        Text t = new LiteralText("[Tick " + tick + "] ");
        t.setStyle(t.getStyle().setColor(Formatting.GRAY));
        Text t2 = new LiteralText(
                String.format("pos[ %.1f, %.1f, %.1f ] mot[ %.1f, %.1f, %.1f ]",
                        x, y, z,
                        vx, vy, vz));
        t2.setStyle(t2.getStyle().setColor(Formatting.RESET));
        t.append(t2);
        return t;
    }

    public Text formatBrief(int tick, double x, double y, double z, double vx, double vy, double vz) {
        Text t = new LiteralText("x ");
        Text t2 = new LiteralText(
                String.format("Tick: %s\n-----\nx: %.8f\ny: %.8f\nz: %.8f\n-----\nmx: %.8f\nmy: %.8f\nmz: %.8f",
                        tick,
                        x, y, z,
                        vx, vy, vz));
        t.setStyle(t.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, t2)));
        return t;
    }

    public List<Vec3d> getPositions() {
        return this.positions;
    }
}
