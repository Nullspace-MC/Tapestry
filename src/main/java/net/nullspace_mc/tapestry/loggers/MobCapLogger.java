package net.nullspace_mc.tapestry.loggers;

import java.util.Arrays;
import java.util.LinkedHashSet;
import net.minecraft.entity.living.mob.MobSpawnerHelper;
import net.minecraft.entity.living.mob.MobSpawnGroup;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Formatting;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.nullspace_mc.tapestry.helpers.MobSpawnerHelperHelper;

public class MobCapLogger extends Logger {

    public MobCapLogger() {
        super(new LinkedHashSet<String>(Arrays.asList(new String[]{"dynamic", "overworld", "nether", "end"})), false);
    }

    public String getName() {
        return "mobcap";
    }

    @SuppressWarnings("unchecked")
    public Text tickLogger(MinecraftServer server, ServerPlayerEntity player) {
        // get world
        String worldString = this.getChannelSubscriptions().get(player.getName());
        if(worldString == null) worldString = "dynamic";

        ServerWorld world;
        if(worldString.equals("overworld")) {
            world = server.worlds[0];
        } else if(worldString.equals("nether")) {
            world = server.worlds[1];
        } else if(worldString.equals("end")) {
            world = server.worlds[2];
        } else {
            world = (ServerWorld)player.world;
        }

        Text mobCapMsg = genMobTally("H", MobSpawnGroup.MONSTER, world, Formatting.DARK_RED);
        mobCapMsg.append(new LiteralText(" "));
        mobCapMsg.append(genMobTally("P", MobSpawnGroup.CREATURE, world, Formatting.GREEN));
        mobCapMsg.append(new LiteralText(" "));
        mobCapMsg.append(genMobTally("W", MobSpawnGroup.WATER_CREATURE, world, Formatting.AQUA));
        mobCapMsg.append(new LiteralText(" "));
        mobCapMsg.append(genMobTally("A", MobSpawnGroup.WATER_CREATURE, world, Formatting.YELLOW));

        return mobCapMsg;
    }

    private Text genMobTally(String label, MobSpawnGroup mobType, ServerWorld world, Formatting color) {
        int mobCount = this.getMobCount(mobType, world);
        int mobCap = this.getMobCap(mobType, world);
        Text tally = new LiteralText(String.format("%s: %d/%d", label, mobCount, mobCap));
        tally.getStyle().setFormatting(color);
        return tally;
    }

    private int getMobCount(MobSpawnGroup mobType, ServerWorld world) {
        return world.getSpawnCap(mobType.getType());
    }

    private int getMobCap(MobSpawnGroup mobType, ServerWorld world) {
        return mobType.getCapacity() * MobSpawnerHelperHelper.getSpawnableChunksCount(world) / 256;
    }
}
