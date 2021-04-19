package me.jrm_wrm.mob_gems.util.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import me.jrm_wrm.mob_gems.events.LivingEntityCallback;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "onDeath", at = @At("RETURN"))
    private void onDeath(DamageSource source, CallbackInfo info) {
        LivingEntityCallback.ON_DEATH.invoker().onDeath((LivingEntity) (Object) this, source);
    }

    @Inject(method = "eatFood", at = @At("RETURN"))
    private void eatFood(World world, ItemStack stack, CallbackInfoReturnable<ItemStack> info) {
        LivingEntityCallback.ON_EAT.invoker().onEat(world, (LivingEntity) (Object) this, stack);
    }
    
}
