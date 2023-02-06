package net.nullspace_mc.tapestry.loggers;

import java.util.Arrays;
import java.util.LinkedHashSet;

import net.minecraft.entity.living.mob.MobCategory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Formatting;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.world.NaturalSpawner;

import net.nullspace_mc.tapestry.mixin.loggers.mobcap.NaturalSpawnerAccessor;
import net.nullspace_mc.tapestry.mixin.loggers.mobcap.ServerWorldAccessor;

public class MobCapLogger extends Logger {

    public MobCapLogger() {
        super(new LinkedHashSet<String>(Arrays.asList(new String[]{"dynamic", "overworld", "nether", "end"})), false);
    }

    public String getName() {
        return "mobcap";
    }

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

        Text mobCapMsg = genMobTally("H", MobCategory.MONSTER, world, Formatting.DARK_RED);
        mobCapMsg.append(new LiteralText(" "));
        mobCapMsg.append(genMobTally("P", MobCategory.CREATURE, world, Formatting.GREEN));
        mobCapMsg.append(new LiteralText(" "));
        mobCapMsg.append(genMobTally("W", MobCategory.WATER_CREATURE, world, Formatting.AQUA));
        mobCapMsg.append(new LiteralText(" "));
        mobCapMsg.append(genMobTally("A", MobCategory.WATER_CREATURE, world, Formatting.YELLOW));

        return mobCapMsg;
    }

    private Text genMobTally(String label, MobCategory category, ServerWorld world, Formatting color) {
        int mobCount = this.getMobCount(category, world);
        int mobCap = this.getMobCap(category, world);
        Text tally = new LiteralText(String.format("%s: %d/%d", label, mobCount, mobCap));
        tally.getStyle().setColor(color);
        return tally;
    }

    private int getMobCount(MobCategory category, ServerWorld world) {
        return world.getEntityCount(category.getType());
    }

    private int getMobCap(MobCategory category, ServerWorld world) {
        return category.getCap() * getMobSpawningChunkCount(world) / 256;
    }

    private int getMobSpawningChunkCount(ServerWorld world) {
        NaturalSpawner spawner = ((ServerWorldAccessor)world).getNaturalSpawner();
        return ((NaturalSpawnerAccessor)(Object)spawner).getMobSpawningChunks().size();
    }
}
