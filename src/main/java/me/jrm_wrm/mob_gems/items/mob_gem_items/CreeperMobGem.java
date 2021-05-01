package me.jrm_wrm.mob_gems.items.mob_gem_items;

import me.jrm_wrm.mob_gems.items.MobGemItem;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class CreeperMobGem extends MobGemItem {

    /**
     * Creeper Mob Gem
     * - augmenter: chance of exploding when attacked
     * - diminiser: blast resistence
     * - gem cage: all mobs in range can be ignited
     */
    public <T extends MobEntity> CreeperMobGem(EntityType<T> type, int tint) {
        super(type, tint);
    }

    @Override
    public void onDiminisherTick(ItemStack bracelet, ItemStack stack, World world, LivingEntity livingEntity, int slot) {
        if(world.isClient) return;
        
        //livingEntity.applyStatusEffect(new StatusEffectInstance(ModMisc.BLAST_RESISTANCE_EFFECT, 10, -4, true, false));
    }
    
}
