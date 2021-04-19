package me.jrm_wrm.mob_gems.registry;

import me.jrm_wrm.mob_gems.MobGems;
import me.jrm_wrm.mob_gems.enchantments.CaptureEnchantment;
import me.jrm_wrm.mob_gems.events.LivingEntityCallback;
import me.jrm_wrm.mob_gems.events.LivingEntityListener;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypePreset;
import top.theillusivec4.curios.api.SlotTypeInfo.BuildScheme;

public class ModMisc {

    // Enchantments
    public static final Enchantment CAPTURE = new CaptureEnchantment();

    public static void registerMiscellanious() {
        // Register enchantment
        Registry.register(Registry.ENCHANTMENT, new Identifier(MobGems.MOD_ID, "capture"), CAPTURE);

        // Register curios slot
        CuriosApi.enqueueSlotType(BuildScheme.REGISTER, SlotTypePreset.BRACELET.getInfoBuilder().build());

        // Register event listeners
        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register(LivingEntityListener::onKilled);
        LivingEntityCallback.ON_DEATH.register(LivingEntityListener::onDeath);
        LivingEntityCallback.ON_EAT.register(LivingEntityListener::onEat);
    }
    
}
