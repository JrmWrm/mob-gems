package me.jrm_wrm.mob_gems.util.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import me.jrm_wrm.mob_gems.util.LivingEntityAccess;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.LivingEntity;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {

    @Inject(method="getAnimationCounter", at = @At("RETURN"), cancellable = true)
    private void renderCombustionEffect(LivingEntity entity, float tickDelta, CallbackInfoReturnable<Float> cir) {
        if (!((LivingEntityAccess) entity).isIgnited()) return;

        float returnVal = (float) Math.sin(entity.world.getTime() * 0.8) / 2 + 0.5f;
        returnVal = returnVal < 0.5f ? 0.0f : returnVal;
        cir.setReturnValue(returnVal);
        cir.cancel();
    }
    
}
