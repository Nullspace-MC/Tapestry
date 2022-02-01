package net.nullspace_mc.tapestry.helpers;

import net.minecraft.inventory.Inventory;

public class InventoryHelper {
    public static void clearInventory(Inventory inv) {
        for (int s = 0; s < inv.getInvSize(); ++s) {
            inv.removeInvStack(s);
        }
    }
}
