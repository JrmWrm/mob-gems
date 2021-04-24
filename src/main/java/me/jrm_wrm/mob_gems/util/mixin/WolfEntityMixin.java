package me.jrm_wrm.mob_gems.util.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.jrm_wrm.mob_gems.items.BraceletItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.item.ItemStack;

@Mixin(WolfEntity.class)
public class WolfEntityMixin {
    
    @Inject(method = "tickMovement", at = @At(value = "INVOKE", target = "tickAngerLogic"))
    public void stopAngerOnTickMove(CallbackInfo info) {
        
        WolfEntity wolf = (WolfEntity) (Object) this;
        LivingEntity target = wolf.getTarget();
        
        // TODO: remove debug logging
        System.out.println("start: \n" + wolf.world.isClient + " - " + wolf.getAngerTime() + " - " + wolf.getAngryAt());

        if (target != null) {
            ItemStack bracelet = BraceletItem.getEquippedBracelet(target);
            if (bracelet != ItemStack.EMPTY) {
                if (!((BraceletItem) bracelet.getItem()).isAugmenter) {
                    wolf.stopAnger();
                }
            }
        }

        // TODO: remove debug logging
        System.out.println(wolf.world.isClient + " - " + wolf.getAngerTime() + " - " + wolf.getAngryAt());
    }

}
