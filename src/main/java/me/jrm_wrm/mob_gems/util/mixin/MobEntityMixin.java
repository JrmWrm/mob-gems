package me.jrm_wrm.mob_gems.util.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.jrm_wrm.mob_gems.items.BraceletItem;
import net.minecraft.entity.mob.MobEntity;

@Mixin(MobEntity.class)
public class MobEntityMixin {
    
    @Inject(method="mobTick", at = @At("HEAD"))
    private void mobTick(CallbackInfo info) {
        MobEntity mob = (MobEntity) (Object) this;

        // if this mob is wearing a bracelet, call the inventoryTick method!
        mob.getItemsEquipped().forEach(stack -> {
            if (stack.getItem() instanceof BraceletItem) {
                int slot = MobEntity.getPreferredEquipmentSlot(stack).getEntitySlotId();
                boolean active = mob.getActiveItem() == stack;
                ((BraceletItem) stack.getItem()).inventoryTick(stack, mob.world, mob, slot, active);
            }
        });
    }
}
