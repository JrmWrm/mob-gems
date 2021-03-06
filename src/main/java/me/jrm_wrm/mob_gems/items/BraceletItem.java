package me.jrm_wrm.mob_gems.items;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.google.common.base.Predicate;

import me.jrm_wrm.mob_gems.MobGems;
import me.jrm_wrm.mob_gems.gui.BraceletScreenHandler;
import me.jrm_wrm.mob_gems.util.ImplementedInventory;
import net.fabricmc.fabric.api.item.v1.EquipmentSlotProvider;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
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
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import top.theillusivec4.curios.api.CuriosApi;

public class BraceletItem extends Item {
    
    public boolean isAugmenter;

    public BraceletItem(FabricItemSettings settings, boolean isAugmenter) {
        super(settings.equipmentSlot(new EquipmentSlotProvider(){
            @Override
            public EquipmentSlot getPreferredEquipmentSlot(ItemStack stack) {
                return EquipmentSlot.CHEST;
            }
        }));
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

        DefaultedList<ItemStack> items = getStoredItems(stack);

        // call onBraceletTick for all mob gems stored in the bracelet
        for (ItemStack i : items) {
            if (i.getItem() instanceof MobGemItem) {
                MobGemItem mobGem = (MobGemItem) i.getItem();
                mobGem.onBraceletTick(stack, i, world, entity, items.indexOf(i));
            }
        }
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

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (entity.world.isClient) return ActionResult.SUCCESS;

        // when used on an entity, try to equip it
        if (entity instanceof MobEntity) {
            if (((MobEntity) entity).tryEquip(stack.copy()))
                stack.decrement(1);
            System.out.println(entity.getItemsEquipped());
        } 
        return ActionResult.SUCCESS;
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
    public static <M extends MobGemItem> boolean hasMobGem(ItemStack stack, Object gemObj) {
        for (ItemStack storedItem : getStoredItems(stack)) {
            // complicated version of instanceof because it's in a static method and an indirect check
            // doing it this way so that multiple mob gems that are of the same mob gem class will all return true 
            // (e.g zombie villager & zombie)
            if (storedItem.getItem().getClass().getName().equals(gemObj.getClass().getName())) return true;
        }
        return false;
    }

    public static ItemStack getEquippedBracelet(LivingEntity entity) {
        Predicate<ItemStack> filter = stack -> stack.getItem() instanceof BraceletItem;

        // get the bracelet from equipped items
        Stream<ItemStack> equippedStream = StreamSupport.stream(entity.getItemsEquipped().spliterator(), false);
        Optional<ItemStack> braceletOptional = equippedStream.filter(filter).findFirst();

        // if curios is installed and bracelet is null, search for bracelet in curios
        if (FabricLoader.getInstance().isModLoaded(MobGems.CURIOS_MOD_ID)) {
            if (!braceletOptional.isPresent()) 
                braceletOptional = CuriosApi.getCuriosHelper()
                    .findEquippedCurio(filter, entity)
                    .map(triple -> triple.getRight());
        }

        // return bracelet or empty stack if no bracelet found
        return braceletOptional.orElse(ItemStack.EMPTY);
    }

    // check if an entity is wearing a augmenter/diminisher bracelet with a specific mob gem
    public static boolean hasMobGemEquipped(LivingEntity entity, MobGemItem mobGem, boolean augmenter) {
        ItemStack bracelet = getEquippedBracelet(entity);
        if (!bracelet.isEmpty()) return hasMobGem(bracelet, mobGem) && ((BraceletItem) bracelet.getItem()).isAugmenter == augmenter;
        else return false;
    }
    //#endregion
}
