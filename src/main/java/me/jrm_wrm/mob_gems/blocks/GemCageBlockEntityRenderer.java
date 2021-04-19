package me.jrm_wrm.mob_gems.blocks;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.Quaternion;

public class GemCageBlockEntityRenderer extends BlockEntityRenderer<GemCageBlockEntity> {

    public GemCageBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(GemCageBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        
        matrices.translate(0.5, 0.5, 0.5);
        matrices.multiply(new Quaternion(Vector3f.POSITIVE_Y, entity.getWorld().getTime() + tickDelta, true));
        MinecraftClient.getInstance().getItemRenderer().renderItem(entity.getStack(0), ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers);

        matrices.pop();
    }
    
}
