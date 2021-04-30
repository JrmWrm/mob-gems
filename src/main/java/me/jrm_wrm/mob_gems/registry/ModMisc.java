package me.jrm_wrm.mob_gems.registry;

import me.jrm_wrm.mob_gems.MobGems;
import me.jrm_wrm.mob_gems.enchantments.CaptureEnchantment;
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

    public static void registerMiscellanious() {
        // Register enchantment
        Registry.register(Registry.ENCHANTMENT, new Identifier(MobGems.MOD_ID, "capture"), CAPTURE_ENCHANTMENT);

        // Register curios slot
        CuriosApi.enqueueSlotType(BuildScheme.REGISTER, SlotTypePreset.BRACELET.getInfoBuilder().build());
    }
    
}
