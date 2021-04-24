package me.jrm_wrm.mob_gems.registry;

import me.jrm_wrm.mob_gems.events.InteractionListener;
import me.jrm_wrm.mob_gems.events.LivingEntityCallback;
import me.jrm_wrm.mob_gems.events.LivingEntityListener;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;

public class ModEvents {
    
    public static void registerEvents() { 
        // Register event listeners
        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register(LivingEntityListener::onKilled);
        UseEntityCallback.EVENT.register(InteractionListener::onUseEntity);
        AttackEntityCallback.EVENT.register(InteractionListener::onAttackEntity);
        LivingEntityCallback.ON_DEATH.register(LivingEntityListener::onDeath);
        LivingEntityCallback.ON_EAT.register(LivingEntityListener::onEat);
    }

}
