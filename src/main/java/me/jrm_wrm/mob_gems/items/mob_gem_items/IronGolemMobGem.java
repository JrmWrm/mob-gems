package me.jrm_wrm.mob_gems.items.mob_gem_items;

import java.util.List;

import me.jrm_wrm.mob_gems.items.MobGemItem;
import me.jrm_wrm.mob_gems.util.WorldUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class IronGolemMobGem extends MobGemItem {

    /**
     * Iron Golem Mob Gem
     * - augmenter: health boost
     * - diminiser: negative health boost (less hearts)
     * - gem cage: push away hostile mobs and arrows
     */
    public <T extends MobEntity> IronGolemMobGem(EntityType<T> type, int tint) {
        super(type, tint);
    }
    
    @Override
    public void onAugmenterTick(ItemStack bracelet, ItemStack stack, World world, LivingEntity wearer, int slot) {
        if(world.isClient) return;
        
        // if the entity already has health boost, upgrade it, otherwise apply it
        if (wearer.hasStatusEffect(StatusEffects.HEALTH_BOOST)) {
            wearer.getStatusEffect(StatusEffects.HEALTH_BOOST).upgrade(new StatusEffectInstance(StatusEffects.HEALTH_BOOST, 10, 2, true, false));
        } else {
            wearer.addStatusEffect(new StatusEffectInstance(StatusEffects.HEALTH_BOOST, 10, 2, true, false, true));
        }
    }

    @Override
    public void onDiminisherTick(ItemStack bracelet, ItemStack stack, World world, LivingEntity wearer, int slot) {
        if(world.isClient) return;
        
        wearer.addStatusEffect(new StatusEffectInstance(StatusEffects.HEALTH_BOOST, 10, -4, true, false, false));
    }

    @Override
    public void onCageTick(ItemStack stack, World world, BlockPos pos) {
        if(world.isClient) return;

        // get entities in range
        List<Entity> entities = world.getOtherEntities(null, WorldUtil.getRangeBox(pos, CAGE_RANGE));

        // push away all hostile entities
        for (Entity entity : entities) {
            if (entity instanceof HostileEntity) {  
                double deltaX = MathHelper.clamp(entity.getVelocity().getX() + (entity.getX() - pos.getX()), -0.4, 0.4);
                double deltaZ = MathHelper.clamp(entity.getVelocity().getZ() + (entity.getZ() - pos.getZ()), -0.4, 0.4);
                entity.addVelocity(deltaX, 0, deltaZ);
            }
        }      
    }
    
    
}
