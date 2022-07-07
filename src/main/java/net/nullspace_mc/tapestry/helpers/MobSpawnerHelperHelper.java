package net.nullspace_mc.tapestry.helpers;

import java.util.HashMap;
import net.minecraft.entity.MobSpawnerHelper;
import net.minecraft.server.world.ServerWorld;

public class MobSpawnerHelperHelper {
    private static HashMap<ServerWorld, Integer> spawnableChunksCount = new HashMap<ServerWorld, Integer>();

    public static int getSpawnableChunksCount(ServerWorld world) {
        Integer count = MobSpawnerHelperHelper.spawnableChunksCount.get(world);
        return count != null ? count.intValue() : 0;
    }

    public static void setSpawnableChunksCount(ServerWorld world, int count) {
        MobSpawnerHelperHelper.spawnableChunksCount.put(world, count);
    }
}
