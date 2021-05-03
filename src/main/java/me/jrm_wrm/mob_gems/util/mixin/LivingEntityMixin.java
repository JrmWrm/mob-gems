package me.jrm_wrm.mob_gems.util.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import me.jrm_wrm.mob_gems.events.LivingEntityCallback;
import me.jrm_wrm.mob_gems.registry.ModMisc;
import me.jrm_wrm.mob_gems.util.LivingEntityAccess;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.world.World;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements LivingEntityAccess {

    public boolean ignited = false;

    @Override
    public boolean isIgnited() {
        return ignited;
    }

    @Override
    public void setIgnited(boolean ignited) {
        this.ignited = ignited;
    }

    @Inject(method = "onDeath", at = @At("RETURN"))
    private void onDeath(DamageSource source, CallbackInfo info) {
        LivingEntityCallback.ON_DEATH.invoker().onDeath((LivingEntity) (Object) this, source);
    }

    @Inject(method = "eatFood", 
        at = @At(value = "INVOKE", 
            target = "Lnet/minecraft/entity/LivingEntity;applyFoodEffects(Lnet/minecraft/item/ItemStack; Lnet/minecraft/world/World; Lnet/minecraft/entity/LivingEntity;)V", 
            shift = Shift.AFTER))
    private void eatFood(World world, ItemStack stack, CallbackInfoReturnable<ItemStack> info) {
        LivingEntityCallback.ON_EAT.invoker().onEat(world, (LivingEntity) (Object) this, stack);
    }

    @Inject(method = "applyEnchantmentsToDamage", at = @At("TAIL"), cancellable = true)
    private void applyBlastResistanceToDamage(DamageSource source, float amount, CallbackInfoReturnable<Float> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;

        // if the damage source is an explosion, and the entity has blast resistance increase the dmg protection
        // code yanked from the resistance effect (from this same method)
        if (entity.hasStatusEffect(ModMisc.BLAST_RESISTANCE_EFFECT) && source.isExplosive()) {
            
            int k = (entity.getStatusEffect(ModMisc.BLAST_RESISTANCE_EFFECT).getAmplifier() + 1) * 5;
            int j = 25 - k;
            float f = amount * (float)j;
            float g = amount;
            amount = Math.max(f / 25.0F, 0.0F);
            float h = g - amount;

            if (h > 0.0F && h < 3.4028235E37F) {
               if (entity instanceof ServerPlayerEntity) {
                  ((ServerPlayerEntity)entity).increaseStat(Stats.DAMAGE_RESISTED, Math.round(h * 10.0F));
               } else if (source.getAttacker() instanceof ServerPlayerEntity) {
                  ((ServerPlayerEntity)source.getAttacker()).increaseStat(Stats.DAMAGE_DEALT_RESISTED, Math.round(h * 10.0F));
               }
            }

            cir.setReturnValue(amount);
        }
    }

}
