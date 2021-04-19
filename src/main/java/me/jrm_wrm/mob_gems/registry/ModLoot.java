package me.jrm_wrm.mob_gems.registry;

import me.jrm_wrm.mob_gems.MobGems;
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.loot.ConstantLootTableRange;
import net.minecraft.loot.entry.LootTableEntry;
import net.minecraft.util.Identifier;

public class ModLoot {
    
    private static final Identifier STRONGHOLD_LIBRARY_LOOT_TABLE_ID = new Identifier("minecraft", "chests/stronghold_library");
	private static final Identifier STRONGHOLD_LIBRARY_INJ_LOOT_TABLE_ID = new Identifier(MobGems.MOD_ID, "chests/stronghold_library_injection");
    
	public static void modifyLootTables() {
		LootTableLoadingCallback.EVENT.register((resourceManager, lootManager, id, supplier, setter) -> {

			// Stronghold library injection
			if (id.equals(STRONGHOLD_LIBRARY_LOOT_TABLE_ID)) {
				FabricLootPoolBuilder poolBuilder = FabricLootPoolBuilder.builder()
				.rolls(ConstantLootTableRange.create(1))
				.with(LootTableEntry.builder(STRONGHOLD_LIBRARY_INJ_LOOT_TABLE_ID));

				supplier.withPool(poolBuilder.build());
			}

		});
	}

}
