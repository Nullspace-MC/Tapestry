package net.nullspace_mc.tapestry.counter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.minecraft.item.DyeItem;

public class CounterRegistry {

    private static final Map<String /* Counter color */, Counter> counters = new HashMap<>();

    public static Counter getCounter(String counterColor) {
        return counters.get(counterColor.toLowerCase());
    }

    public static Set<String> getCounterColors() {
        return counters.keySet();
    }

    public static void setupCounters() {
        for (String color : DyeItem.TRANSLATION_KEYS) { // wtf mojang why are the colors THERE ?????
            counters.put(color.toLowerCase(), new Counter());
        }
    }
}
