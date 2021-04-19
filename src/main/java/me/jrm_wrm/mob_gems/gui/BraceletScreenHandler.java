package me.jrm_wrm.mob_gems.gui;

import me.jrm_wrm.mob_gems.items.BraceletItem;
import me.jrm_wrm.mob_gems.items.MobGemItem;
import me.jrm_wrm.mob_gems.registry.ModItems;
import me.jrm_wrm.mob_gems.util.ImplementedInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class BraceletScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final ItemStack stack;
 
    // This constructor gets called on the client when the server wants it to open the screenHandler,
    // The client will call the other constructor with an empty Inventory and the screenHandler will automatically
    // sync this empty inventory with the inventory on the server.
    public BraceletScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(5), ItemStack.EMPTY);
    }

    // This constructor gets called from the BlockEntity on the server without calling the other constructor first, the server knows the inventory of the container
    // and can therefore directly provide it as an argument. This inventory will then be synced to the client.
    public BraceletScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, ItemStack stack) {
        super(ModItems.BRACELET_SCREEN_HANDLER, syncId);
        checkSize(inventory, 5);
        this.inventory = inventory;
        this.stack = stack;
 
        // render slots

        // bracelet inventory
        for(int x = 0; x < 5; x++) {
            this.addSlot(new BraceletSlot(inventory, x, 44 + x * 18, 20));
        }
        
        // player inventory
        for (int m = 0; m < 3; ++m) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 51 + m * 18));
            }
        }
        // player Hotbar
        for (int m = 0; m < 9; ++m) {
            this.addSlot(new Slot(playerInventory, m, 8 + m * 18, 109));
        }
 
    }

    // write the screen inventory to the bracelet NBT
    @Override
    public void close(PlayerEntity player) {
        super.close(player);
        if (inventory instanceof ImplementedInventory) {
            ImplementedInventory braceletInv = (ImplementedInventory) inventory;
            BraceletItem.setStoredItems(stack, braceletInv.getItems());
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }
 
    // shift clicking code
    @Override
    public ItemStack transferSlot(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);

        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();

            if (invSlot < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }
 
            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }
 
        return newStack;
    }
    
}

class BraceletSlot extends Slot {

    public BraceletSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return stack.getItem() instanceof MobGemItem || stack.getItem() == ModItems.SOUL_POWDER;
    }
    
}
