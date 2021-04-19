package me.jrm_wrm.mob_gems.items;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.tuple.ImmutableTriple;

import me.jrm_wrm.mob_gems.gui.BraceletScreenHandler;
import me.jrm_wrm.mob_gems.registry.ModItems;
import me.jrm_wrm.mob_gems.util.ImplementedInventory;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import top.theillusivec4.curios.api.CuriosApi;

public class BraceletItem extends Item {
    
    public boolean isAugmenter;

    public BraceletItem(Settings settings, boolean isAugmenter) {
        super(settings);
        this.isAugmenter = isAugmenter;
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        // gets the tooltip text from tha lang file and appends the entity name
        String key = isAugmenter ? "item.mob_gems.augmenter_bracelet.tooltip" : "item.mob_gems.diminisher_bracelet.tooltip";
        tooltip.add(new TranslatableText(key).formatted(Formatting.YELLOW));
        
        DefaultedList<ItemStack> items = getStoredItems(itemStack);
        for (ItemStack i : items) {
            if (i.getItem() instanceof MobGemItem) {
                MobGemItem mobGem = (MobGemItem) i.getItem();
                tooltip.add(Text.of("- Mob Gem: " + mobGem.getTypeName().getString()));
            }
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (world.isClient) return;

        DefaultedList<ItemStack> items = getStoredItems(stack);

        // call onBraceletTick for all mob gems stored in the bracelet
        for (ItemStack i : items) {
            if (i.getItem() instanceof MobGemItem) {
                MobGemItem mobGem = (MobGemItem) i.getItem();
                mobGem.onBraceletTick(stack, i, world, entity, items.indexOf(i));
            }
        }
        
        /*if (world.random.nextDouble() < ModItems.MOB_GEM_DRAIN_FACTOR * count && stack.getDamage() < stack.getMaxDamage()) {       
            System.out.println("before: "+stack.getTag());        
            stack.setDamage(stack.getDamage() + 1);
            System.out.println("after: "+stack.getDamage());
        }*/
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        super.use(world, player, hand);
        if (world.isClient) {
            player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1.0F, 1.0F);
            return TypedActionResult.success(player.getStackInHand(hand));
        }
        
        ItemStack stack = player.getStackInHand(hand);
        DefaultedList<ItemStack> items = getStoredItems(stack);

        // create an inventory object for the screen
        ImplementedInventory screenInventory = new ImplementedInventory(){
            @Override
            public DefaultedList<ItemStack> getItems() {
                return items;
            }   
        };

        // create a screen handler factory wth the screen inventory
        NamedScreenHandlerFactory screenHandlerFactory = new NamedScreenHandlerFactory(){
            @Override
            public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
                return new BraceletScreenHandler(syncId, playerInventory, screenInventory, stack);
            }
        
            @Override
            public Text getDisplayName() {
                return new TranslatableText(getTranslationKey());
            }
        };
        
        // open the screen
        player.openHandledScreen(screenHandlerFactory);

        return TypedActionResult.success(stack);
    }

    // static util methods
    //#region

    // read stored items from nbt
    public static DefaultedList<ItemStack> getStoredItems(ItemStack stack) {
        DefaultedList<ItemStack> items = DefaultedList.ofSize(5, ItemStack.EMPTY);
        CompoundTag invTag = stack.getOrCreateSubTag("Inventory");
        
        if (!invTag.isEmpty()) {
            Inventories.fromTag(invTag, items);
        }

        return items;
    }

    // write stored items to nbt
    public static void setStoredItems(ItemStack stack, DefaultedList<ItemStack> items) { 
        CompoundTag invTag = stack.getOrCreateSubTag("Inventory");
        stack.putSubTag("Inventory", Inventories.toTag(invTag, items));
    }

    // check if the bracelet stores a specific mob gem
    public static boolean hasMobGem(ItemStack stack, MobGemItem item) {
        for (ItemStack storedItem : getStoredItems(stack)) {
            System.out.println("Stored: " + storedItem.getItem() + ", " + item + " - " + (storedItem.getItem() == item));
            if (storedItem.getItem() == item) return true;
        }
        return false;
    }

    public static ItemStack getEquippedBracelet(LivingEntity entity) {
        ItemStack bracelet = ItemStack.EMPTY;
        for (ItemStack equippedItem : entity.getItemsEquipped()) {
            if (equippedItem.getItem() instanceof BraceletItem) bracelet = equippedItem;
        }
        
        Optional<ImmutableTriple<String,Integer,ItemStack>> optional = CuriosApi.getCuriosHelper().findEquippedCurio(ModItems.IRON_BRACELET, entity);
        if (!optional.isEmpty()) bracelet = optional.get().getRight();
        
        optional = CuriosApi.getCuriosHelper().findEquippedCurio(ModItems.GOLDEN_BRACELET, entity);
        if (!optional.isEmpty()) bracelet = optional.get().getRight();

        return bracelet;
    }

    // check if an entity is wearing a bracelet with a specific mob gem
    public static boolean hasMobGemEquipped(LivingEntity entity, MobGemItem mobGem) {
        ItemStack bracelet = getEquippedBracelet(entity);
        if (!bracelet.isEmpty()) return hasMobGem(bracelet, mobGem);
        else return false;
    }
    //#endregion
}
