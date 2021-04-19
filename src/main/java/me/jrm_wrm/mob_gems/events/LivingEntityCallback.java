package me.jrm_wrm.mob_gems.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;

public class LivingEntityCallback {
    
    public static Event<OnDeath> ON_DEATH = EventFactory.createArrayBacked(OnDeath.class,
        (listeners) -> (entity, source) -> {
            for (OnDeath listener : listeners) {
                ActionResult result = listener.onDeath(entity, source);
 
                if(result != ActionResult.PASS) {
                    return result;
                }
            }
 
        return ActionResult.PASS;
    });

    public static Event<OnEat> ON_EAT = EventFactory.createArrayBacked(OnEat.class,
        (listeners) -> (entity, food, info) -> {
            for (OnEat listener : listeners) {
                ActionResult result = listener.onEat(entity, food, info);
    
                if(result != ActionResult.PASS) {
                    return result;
                }
            }
    
        return ActionResult.PASS;
    });
 
    @FunctionalInterface
    public interface OnDeath {
        ActionResult onDeath(LivingEntity entity, DamageSource source);
    }
 
    @FunctionalInterface
    public interface OnEat {
        ActionResult onEat(World world, LivingEntity entity, ItemStack food);
    }
}
