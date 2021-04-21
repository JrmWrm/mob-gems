package me.jrm_wrm.mob_gems.gui;

import com.mojang.blaze3d.systems.RenderSystem;

import me.jrm_wrm.mob_gems.MobGems;
import me.jrm_wrm.mob_gems.blocks.GemCageBlockEntity;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class GemCageScreen extends HandledScreen<GemCageScreenHandler> {

    private static final Identifier TEXTURE = new Identifier(MobGems.MOD_ID, "textures/gui/gem_cage.png");

    public GemCageScreen(GemCageScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        client.getTextureManager().bindTexture(TEXTURE);

        GemCageScreenHandler screenHandler = (GemCageScreenHandler) this.handler;

        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        int w = (int) (18 * ((float)screenHandler.getFuelLevel()/(float)GemCageBlockEntity.FUEL_CAPACITY));

        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);
        drawTexture(matrices, x + 79, y + 54, 176, 0, w, 4);

        //textRenderer.draw(matrices, "Fuel: " + screenHandler.getFuelLevel() + " - " + w, 120, 28, 0);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }
    
}
