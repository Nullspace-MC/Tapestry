package net.nullspace_mc.tapestry.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.Charsets;

import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.village.SavedVillageData;
import net.minecraft.world.village.Village;
import net.minecraft.world.village.VillageDoor;

public class KaboVillageMarker {

    private static HashMap<String, ServerPlayerEntity> players = new HashMap<String, ServerPlayerEntity>();
    private static int id = 0;
    private boolean shouldUpdateClients = false;
    private MinecraftServer mc = MinecraftServer.getInstance();
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
        boolean parts = true;
        ArrayList<String> dataStringList = new ArrayList<String>();
        String dim = this.dataString.split(":")[0];

        int numSubstrings = (int)Math.ceil((double)this.dataString.length() / 10000.0D);
        for (int i = 0; i < numSubstrings; ++i) {
                dataStringList.add(id + "<" + dim + ":" + (i+1) + ":" + numSubstrings + ">" + this.dataString.substring(10000*i, Math.min(10000*(i+1), this.dataString.length())));
        }

        for (String data : dataStringList) {
            try {
                CustomPayloadS2CPacket packet = new CustomPayloadS2CPacket("KVM|Data", data.getBytes(Charsets.UTF_8));

                for (String playerName : this.players.keySet()) {
                    ServerPlayerEntity player = this.players.get(playerName);

                    if (player != null && packet != null) {
                        player.networkHandler.sendPacket(packet);
                    } else if (player == null) {
                        players.remove(playerName);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.shouldUpdateClients = false;
    }

    public String buildDataString() {
        String data = "";
        SavedVillageData villageData = this.world.villageData;
        List villages = villageData.getVillages();
        data = data + this.dimension + ":";

        for (Iterator i = villages.iterator(); i.hasNext();) {
            Object villageObj = i.next();
            if (villageObj instanceof Village) {
                Village village = (Village)villageObj;
                data = data + village.getRadius() + ";" + village.getCenter().x + "," + village.getCenter().y + "," + village.getCenter().z + ";";
                List doors = village.getDoors();

                for (Iterator j = doors.iterator(); j.hasNext();) {
                    Object doorObj  = j.next();
                    if (doorObj instanceof VillageDoor) {
                        VillageDoor door = (VillageDoor)doorObj;
                        data = data + door.x + "," + door.y + "," + door.z + ";";
                    }
                }

                data = data.substring(0, data.length()-1) + ":";
            }
        }

        data = data.substring(0, data.length()-1);
        return data;
    }

    public static synchronized void addPlayerToList(String playerName, ServerPlayerEntity player) {
        players.put(playerName, player);
    }

    public static synchronized void removePlayerFromList(String playerName) {
        players.remove(playerName);
    }
}
