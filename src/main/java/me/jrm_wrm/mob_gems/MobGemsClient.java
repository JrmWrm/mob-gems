package me.jrm_wrm.mob_gems;

import me.jrm_wrm.mob_gems.gui.BraceletScreen;
import me.jrm_wrm.mob_gems.gui.GemCageScreen;
import me.jrm_wrm.mob_gems.registry.ModBlocks;
import me.jrm_wrm.mob_gems.registry.ModItems;
import me.jrm_wrm.mob_gems.registry.ModClientNetworking;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

public class MobGemsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {       
        ModBlocks.registerBlockRendering();
        ModItems.registerMobGemColors();
        ModClientNetworking.registerClientReceivers();

        // register screens
        ScreenRegistry.register(ModBlocks.GEM_CAGE_SCREEN_HANDLER, GemCageScreen::new);
        ScreenRegistry.register(ModItems.BRACELET_SCREEN_HANDLER, BraceletScreen::new);
    }
    
}
