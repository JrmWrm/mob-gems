package me.jrm_wrm.mob_gems.blocks;

import me.jrm_wrm.mob_gems.gui.GemCageScreenHandler;
import me.jrm_wrm.mob_gems.items.MobGemItem;
import me.jrm_wrm.mob_gems.registry.ModBlocks;
import me.jrm_wrm.mob_gems.registry.ModItems;
import me.jrm_wrm.mob_gems.util.ImplementedInventory;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;

public class GemCageBlockEntity extends BlockEntity implements Tickable, BlockEntityClientSerializable, ImplementedInventory, NamedScreenHandlerFactory {

    // current values: ~15 minutes per soul powder
    private static final int POWDER_FUEL_AMOUNT = 3600;
    private static final float MOB_GEM_DRAIN_FACTOR = 0.2f;
    public static final int FUEL_CAPACITY = 2 * POWDER_FUEL_AMOUNT;

    // call markDirty() whenever changing variables
    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(2, ItemStack.EMPTY);  
    private int fuelLevel = 0;

    // property delegate stores fuel level so the screen can draw a fuel bar
    //#region
    private final PropertyDelegate propertyDelegate = new PropertyDelegate() {
        @Override
        public int get(int index) {
            return fuelLevel;
        }
 
        @Override
        public void set(int index, int value) {
            fuelLevel = value;
        }
 
        @Override
        public int size() {
            return 1;
        }
    };
    //#endregion

    // constructor
    public GemCageBlockEntity() {
        super(ModBlocks.GEM_CAGE_ENTITY);
    }
    
    // accessors
    //#region  
    @Override
    public DefaultedList<ItemStack> getItems() {
        return items;
    }

    public ItemStack getGemStack() {
        return getStack(0);
    }

    public ItemStack getFuelStack() {
        return getStack(1);
    }

    public boolean hasFuelRemaining() {
        return fuelLevel > 0;
    }

    public boolean hasMobGem() {
        return getStack(0).getItem() instanceof MobGemItem;
    }
    //#endregion

    // tick
    @Override
    public void tick() {
        
        // Client side rendering code
        if (world.isClient) { 
            if (world.random.nextDouble() < 0.5 && hasMobGem() && fuelLevel > 0) {
                double d = (double)pos.getX() + world.random.nextDouble();
                double e = (double)pos.getY() + world.random.nextDouble();
                double f = (double)pos.getZ() + world.random.nextDouble();
                world.addParticle(ParticleTypes.SOUL, d, e, f, 0.0D, 0.0D, 0.0D);
            }

        // Server side code
        } else {
            if (hasMobGem()) {
                
                if (fuelLevel < 0 ) fuelLevel = 0;
                else if (fuelLevel > 0) {
                    if (world.random.nextDouble() < MOB_GEM_DRAIN_FACTOR) fuelLevel -= 1;
                    MobGemItem mobGem = (MobGemItem) getGemStack().getItem();
                    mobGem.onCageTick(getGemStack(), world, pos);
                }

                markDirty();
            }

            if (fuelLevel <= FUEL_CAPACITY - POWDER_FUEL_AMOUNT && getFuelStack().getCount() > 0) {
                fuelLevel += POWDER_FUEL_AMOUNT;
                getFuelStack().setCount(getFuelStack().getCount() - 1);
                markDirty();
            }

            sync();
        }
    }

    // screen handler
    //#region

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new GemCageScreenHandler(syncId, playerInventory, this, propertyDelegate);
    }
 
    @Override
    public Text getDisplayName() {
        return new TranslatableText(ModItems.GEM_CAGE.getTranslationKey());
    }

    //#endregion
    
    // Serialize the BlockEntity
    //#region


    public void serialize(CompoundTag tag) {
        // rewriten Inventories.toTag(), because it doesn't serialize empty slots
        ListTag listTag = new ListTag();

        for(int i = 0; i < items.size(); ++i) {
            ItemStack itemStack = items.get(i);
            CompoundTag compoundTag = new CompoundTag();
            compoundTag.putByte("Slot", (byte)i);
            itemStack.toTag(compoundTag);
            listTag.add(compoundTag);
        }
        
        tag.put("Items", listTag);
        tag.putInt("Fuel", fuelLevel);
    }
    
    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        serialize(tag);
        return tag;
    }

    @Override
    public CompoundTag toClientTag(CompoundTag tag) {
        serialize(tag);
        return tag;
    }
    
    // Deserialize the BlockEntity

    public void deserialize(CompoundTag tag) {
        Inventories.fromTag(tag, items);
        fuelLevel = tag.getInt("Fuel");
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        deserialize(tag);
    }

    @Override
    public void fromClientTag(CompoundTag tag) {
        deserialize(tag);
    }

    //#endregion

}
