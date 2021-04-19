package me.jrm_wrm.mob_gems;

import me.jrm_wrm.mob_gems.registry.ModBlocks;
import me.jrm_wrm.mob_gems.registry.ModEvents;
import me.jrm_wrm.mob_gems.registry.ModItems;
import me.jrm_wrm.mob_gems.registry.ModLoot;
import me.jrm_wrm.mob_gems.registry.ModMisc;
import me.jrm_wrm.mob_gems.registry.ModNetworking;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class MobGems implements ModInitializer {

	// Mod ID
	public static final String MOD_ID = "mob_gems";
	
	// Item group
    public static final ItemGroup MOD_ITEM_GROUP = FabricItemGroupBuilder.build(new Identifier(MobGems.MOD_ID, "mod_items"), 
        () -> new ItemStack(ModItems.GEM_CAGE));

	@Override
	public void onInitialize() {
		System.out.println("Mob Gems is installed! :D");

		ModItems.registerItems();
		ModBlocks.registerBlocks();
		ModMisc.registerMiscellanious();
		ModEvents.registerEvents();
		ModNetworking.registerReceivers();
		ModLoot.modifyLootTables();
	}

}
