package me.jrm_wrm.mob_gems.items;

import me.jrm_wrm.mob_gems.util.ImplementedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

public class BraceletInventory implements ImplementedInventory {
    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(5, ItemStack.EMPTY);

    public BraceletInventory() {

    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return items;
    }
    
}
