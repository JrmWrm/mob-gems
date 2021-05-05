package me.jrm_wrm.mob_gems.registry;

import me.jrm_wrm.mob_gems.MobGems;
import me.jrm_wrm.mob_gems.blocks.GemCageBlock;
import me.jrm_wrm.mob_gems.blocks.GemCageBlockEntity;
import me.jrm_wrm.mob_gems.blocks.GemCageBlockEntityRenderer;
import me.jrm_wrm.mob_gems.gui.GemCageScreenHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlocks {
    
    // Blocks
    public static final GemCageBlock GEM_CAGE = new GemCageBlock(FabricBlockSettings
        .of(Material.METAL)
        .breakByTool(FabricToolTags.PICKAXES, 0)
        .strength(0.5f, 0.5f)
        .sounds(BlockSoundGroup.METAL)
        .nonOpaque());

    // Block entities
    public static BlockEntityType<GemCageBlockEntity> GEM_CAGE_ENTITY;

    // Block screen handlers
    public static ScreenHandlerType<GemCageScreenHandler> GEM_CAGE_SCREEN_HANDLER;
                            

    public static void registerBlocks() {
        // register blocks
        Registry.register(Registry.BLOCK, new Identifier(MobGems.MOD_ID, "gem_cage"), GEM_CAGE);

        // register block entities
        GEM_CAGE_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, MobGems.MOD_ID + ":gem_cage", 
            BlockEntityType.Builder.create(GemCageBlockEntity::new, GEM_CAGE).build(null));
        
        // register screen handlers
        GEM_CAGE_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(new Identifier(MobGems.MOD_ID, "gem_cage"), GemCageScreenHandler::new);
    }

    /**
     * Must be called from the client initializer!
     */
    @Environment(EnvType.CLIENT)
    public static void registerBlockRendering() {
        // make Gem Cage transparent
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.GEM_CAGE, RenderLayer.getCutout());

        // register Block Entity Renderers
        BlockEntityRendererRegistry.INSTANCE.register(GEM_CAGE_ENTITY, GemCageBlockEntityRenderer::new);      
    }
}
