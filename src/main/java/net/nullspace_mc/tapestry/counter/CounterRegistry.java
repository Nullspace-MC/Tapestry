package net.nullspace_mc.tapestry.counter;

import net.minecraft.item.DyeItem;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CounterRegistry {
    private static final Map<String /* Counter color */, Counter> counters = new HashMap<>();
    public static Counter getCounter(String counterColor) {
        return counters.get(counterColor);
    }

    public static Set<String> getCounterColors() {
        return counters.keySet();
    }

    public static void setupCounters() {
        for (String color : DyeItem.colors) { // wtf mojang why are the colors THERE ?????
            counters.put(color, new Counter());
        }
    }
}
