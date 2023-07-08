package net.nullspace_mc.tapestry.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.Charsets;

import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.village.SavedVillageData;
import net.minecraft.world.village.Village;
import net.minecraft.world.village.VillageDoor;

import net.nullspace_mc.tapestry.settings.Settings;

import net.ornithemc.osl.networking.api.server.ServerConnectionEvents;
import net.ornithemc.osl.networking.api.server.ServerPlayNetworking;

public class KaboVillageMarker {

    private static final String POLL_CHANNEL = "KVM|Poll";
    private static final String ANSWER_CHANNEL = "KVM|Answer";
    private static final String DATA_CHANNEL = "KVM|Data";

    private static HashMap<String, ServerPlayerEntity> players = new HashMap<>();
    private static int id = 0;
    private boolean shouldUpdateClients = false;
    private ServerWorld world;
    private int dimension;
    private String dataString;

    public KaboVillageMarker(ServerWorld serverWorld) {
        this.world = serverWorld;
        this.dimension = this.world.dimension.id;
    }

    public void flagForUpdate() {
        this.shouldUpdateClients = true;
    }

    public boolean needsUpdate() {
        return this.shouldUpdateClients;
    }

    public synchronized void updateClients() {
        id = id >= 999 ? 0 : id + 1;
        this.dataString = this.buildDataString();
        ArrayList<String> dataStringList = new ArrayList<String>();
        String dim = this.dataString.split(":")[0];

        int numSubstrings = (int)Math.ceil((double)this.dataString.length() / 10000.0D);
        for (int i = 0; i < numSubstrings; ++i) {
                dataStringList.add(id + "<" + dim + ":" + (i+1) + ":" + numSubstrings + ">" + this.dataString.substring(10000*i, Math.min(10000*(i+1), this.dataString.length())));
        }

        for (String dataString : dataStringList) {
            ServerPlayNetworking.doSend(players.values(), DATA_CHANNEL, dataString.getBytes(Charsets.UTF_8));
        }

        this.shouldUpdateClients = false;
    }

    public String buildDataString() {
        String data = "";
        SavedVillageData villageData = this.world.villages;
        @SuppressWarnings("unchecked")
        List<Village> villages = villageData.getVillages();
        data += this.dimension + ":";

        for (Village village : villages) {
            data += village.getRadius() + ";" + village.getCenter().x + "," + village.getCenter().y + "," + village.getCenter().z + ";";
            @SuppressWarnings("unchecked")
            List<VillageDoor> doors = village.getDoors();
            
            for (VillageDoor door : doors) {
                data = data + door.x + "," + door.y + "," + door.z + ";";
            }
            
            data = data.substring(0, data.length() - 1) + ":";
        }

        return data.substring(0, data.length() - 1);
    }

    public static void init() {
        ServerConnectionEvents.LOGIN.register((server, player) -> {
            if (Settings.kaboVillageMarker) {
                ServerPlayNetworking.doSend(player, POLL_CHANNEL, new byte[0]);
            }
        });
        ServerConnectionEvents.DISCONNECT.register((server, player) -> {
            players.remove(player.getUuid().toString(), player);
        });
        ServerPlayNetworking.registerListenerRaw(ANSWER_CHANNEL, (server, handler, player, data) -> {
            if (!Settings.kaboVillageMarker) {
                return false;
            }

            String playerName = new String(data);

            for (Object o : server.getPlayerManager().players) {
                ServerPlayerEntity p = (ServerPlayerEntity)o; // thanks proguard

                if (p.getUuid().toString().equals(playerName)) {
                    players.put(playerName, player);
                }
            }

            return true;
        });
    }
}
