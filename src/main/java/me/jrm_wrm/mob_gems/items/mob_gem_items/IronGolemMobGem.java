package me.jrm_wrm.mob_gems.items.mob_gem_items;

import java.util.List;

import me.jrm_wrm.mob_gems.items.MobGemItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class IronGolemMobGem extends MobGemItem {

    public <T extends MobEntity> IronGolemMobGem(EntityType<T> type, int tint) {
        super(type, tint);
    }
    
    @Override
    public void onAugmenterTick(ItemStack bracelet, ItemStack stack, World world, Entity entity, int slot) {
        if(world.isClient) return;
        
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) entity;
            if (livingEntity.hasStatusEffect(StatusEffects.HEALTH_BOOST)) {
                livingEntity.getStatusEffect(StatusEffects.HEALTH_BOOST).upgrade(new StatusEffectInstance(StatusEffects.HEALTH_BOOST, 10, 2, true, false));
            } else {
                livingEntity.applyStatusEffect(new StatusEffectInstance(StatusEffects.HEALTH_BOOST, 10, 2, true, false));
            }
        }
    }

    @Override
    public void onDiminisherTick(ItemStack bracelet, ItemStack stack, World world, Entity entity, int slot) {
        if(world.isClient) return;
        
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) entity;
            livingEntity.applyStatusEffect(new StatusEffectInstance(StatusEffects.HEALTH_BOOST, 10, -4, true, false));
        }
    }

    @Override
    public void onCageTick(ItemStack stack, World world, BlockPos pos) {
        if(world.isClient) return;

        // get entities in range
        List<Entity> entities = world.getOtherEntities(null, getRangeBox(pos));

        // push away all hostile entities
        for (Entity entity : entities) {
            if (entity instanceof HostileEntity || entity instanceof ArrowEntity) {  
                double deltaX = MathHelper.clamp(entity.getVelocity().getX() + (entity.getX() - pos.getX()), -0.4, 0.4);
                double deltaZ = MathHelper.clamp(entity.getVelocity().getZ() + (entity.getZ() - pos.getZ()), -0.4, 0.4);
                entity.addVelocity(deltaX, 0, deltaZ);
            }
        }      
    }
    
    
}
