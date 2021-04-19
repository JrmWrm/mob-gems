package me.jrm_wrm.mob_gems.registry;

import java.util.HashMap;

import me.jrm_wrm.mob_gems.MobGems;
import me.jrm_wrm.mob_gems.gui.BraceletScreenHandler;
import me.jrm_wrm.mob_gems.items.BraceletItem;
import me.jrm_wrm.mob_gems.items.MobGemItem;
import me.jrm_wrm.mob_gems.items.mob_gem_items.IronGolemMobGem;
import me.jrm_wrm.mob_gems.items.mob_gem_items.ZombieMobGem;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItems {

    // Items
    public static final Item IRON_BRACELET = new BraceletItem(new FabricItemSettings().group(MobGems.MOD_ITEM_GROUP).maxCount(1), false);
    public static final Item GOLDEN_BRACELET = new BraceletItem(new FabricItemSettings().group(MobGems.MOD_ITEM_GROUP).maxCount(1), true);
    public static final Item SOUL_POWDER = new Item(new FabricItemSettings().group(MobGems.MOD_ITEM_GROUP));
    
    // Block items
    public static final BlockItem GEM_CAGE = new BlockItem(ModBlocks.GEM_CAGE, new Item.Settings().group(MobGems.MOD_ITEM_GROUP));
    
    // Mob Gem items
    private static HashMap<String, MobGemItem> mobGemItems = new HashMap<String, MobGemItem>(); 
    
    public static final MobGemItem ZOMBIE_MOB_GEM = addMobGem(new ZombieMobGem(EntityType.ZOMBIE, 0xadfc03));
    public static final MobGemItem ENDERMAN_MOB_GEM = addMobGem(new MobGemItem(EntityType.ENDERMAN, 0x78126e));
    public static final MobGemItem IRON_GOLEM_MOB_GEM = addMobGem(new IronGolemMobGem(EntityType.IRON_GOLEM, 0x7a7a7a));

    // Item variables
    public static final int POWDER_FUEL_AMOUNT = 500;
    public static final float MOB_GEM_DRAIN_FACTOR = 0.2f;

    // Item screen handlers
    public static ScreenHandlerType<BraceletScreenHandler> BRACELET_SCREEN_HANDLER;


    public static void registerItems() {
        Registry.register(Registry.ITEM, new Identifier(MobGems.MOD_ID, "iron_bracelet"), IRON_BRACELET);
        Registry.register(Registry.ITEM, new Identifier(MobGems.MOD_ID, "golden_bracelet"), GOLDEN_BRACELET);
        Registry.register(Registry.ITEM, new Identifier(MobGems.MOD_ID, "soul_powder"), SOUL_POWDER);
        Registry.register(Registry.ITEM, new Identifier(MobGems.MOD_ID, "gem_cage"), GEM_CAGE);
        
        // register all mob gem items
        for (MobGemItem item : mobGemItems.values()) {
            String path = item.getTypeKey().replace("entity.", "").replace('.', '_') + "_mob_gem";
            
            Registry.register(Registry.ITEM, new Identifier(MobGems.MOD_ID, path), item);
            ColorProviderRegistry.ITEM.register((stack, tintIndex) -> item.getTint(), item);
            
            System.out.println("Registered mob gem: " + path + " with hue: " + item.getTint());
        }
        
        // register screen handlers
        BRACELET_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(new Identifier(MobGems.MOD_ID, "bracelet"), BraceletScreenHandler::new);
    }
    
    // Mob Gem util methods
    private static MobGemItem addMobGem(MobGemItem mobGemItem) {
        mobGemItems.put(mobGemItem.getTypeKey(), mobGemItem);
        return mobGemItem;
    }

    public static boolean hasMobGem(EntityType<?> type) {
        return mobGemItems.get(type.getTranslationKey()) != null;
    }

    public static MobGemItem getMobGemFromMob(EntityType<?> type) {
        return getMobGemFromMob(type.getTranslationKey());
    }

    public static MobGemItem getMobGemFromMob(String translationKey) {
        return mobGemItems.get(translationKey);
    }
}