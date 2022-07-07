package net.nullspace_mc.tapestry.loggers;

import java.util.Arrays;
import java.util.LinkedHashSet;
import net.minecraft.entity.MobSpawnerHelper;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.ServerPlayerEntity;
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
        String worldString = this.getChannelSubscriptions().get(player.getName().getString());
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

        Text mobCapMsg = genMobTally("H", SpawnGroup.MONSTER, world, Formatting.DARK_RED);
        mobCapMsg.append(new LiteralText(" "));
        mobCapMsg.append(genMobTally("P", SpawnGroup.CREATURE, world, Formatting.GREEN));
        mobCapMsg.append(new LiteralText(" "));
        mobCapMsg.append(genMobTally("W", SpawnGroup.WATER_CREATURE, world, Formatting.AQUA));
        mobCapMsg.append(new LiteralText(" "));
        mobCapMsg.append(genMobTally("A", SpawnGroup.WATER_CREATURE, world, Formatting.YELLOW));

        return mobCapMsg;
    }

    private Text genMobTally(String label, SpawnGroup mobType, ServerWorld world, Formatting color) {
        int mobCount = this.getMobCount(mobType, world);
        int mobCap = this.getMobCap(mobType, world);
        Text tally = new LiteralText(String.format("%s: %d/%d", label, mobCount, mobCap));
        tally.getStyle().setFormatting(color);
        return tally;
    }

    private int getMobCount(SpawnGroup mobType, ServerWorld world) {
        return world.getSpawnCap(mobType.getGroupClass());
    }

    private int getMobCap(SpawnGroup mobType, ServerWorld world) {
        return mobType.getCapacity() * MobSpawnerHelperHelper.getSpawnableChunksCount(world) / 256;
    }
}
