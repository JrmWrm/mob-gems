package me.jrm_wrm.mob_gems.registry;

import me.jrm_wrm.mob_gems.MobGems;
import me.jrm_wrm.mob_gems.enchantments.CaptureEnchantment;
import me.jrm_wrm.mob_gems.status_effects.BlastResistanceStatusEffect;
import me.jrm_wrm.mob_gems.status_effects.CombustionStatusEffect;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeInfo.BuildScheme;
import top.theillusivec4.curios.api.SlotTypePreset;

public class ModMisc {

    // Enchantments
    public static final Enchantment CAPTURE_ENCHANTMENT = new CaptureEnchantment();

    // Status effects
    public static final StatusEffect COMBUSTION_EFFECT = new CombustionStatusEffect();
    public static final StatusEffect BLAST_RESISTANCE_EFFECT = new BlastResistanceStatusEffect();

    public static void registerMiscellanious() {
        // Register enchantments
        Registry.register(Registry.ENCHANTMENT, new Identifier(MobGems.MOD_ID, "capture"), CAPTURE_ENCHANTMENT);

        // Register status effects
        Registry.register(Registry.STATUS_EFFECT, new Identifier(MobGems.MOD_ID, "combustion"), COMBUSTION_EFFECT);
        Registry.register(Registry.STATUS_EFFECT, new Identifier(MobGems.MOD_ID, "blast_resistance"), BLAST_RESISTANCE_EFFECT);

        // Register curios slot
        CuriosApi.enqueueSlotType(BuildScheme.REGISTER, SlotTypePreset.BRACELET.getInfoBuilder().build());
    }
    
}
