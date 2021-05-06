package me.jrm_wrm.mob_gems.items.mob_gem_items;

import java.util.List;

import com.mojang.datafixers.util.Pair;

import me.jrm_wrm.mob_gems.blocks.GemCageBlockEntity;
import me.jrm_wrm.mob_gems.items.BraceletItem;
import me.jrm_wrm.mob_gems.items.MobGemItem;
import me.jrm_wrm.mob_gems.util.WorldUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ZombieMobGem extends MobGemItem {

    /**
     * Zombie Mob Gem
     * - augmenter: every food gives you hunger
     * - diminiser: no food gives you hunger
     * - gem cage: turns villagers into zombie villagers
     */
    public <T extends MobEntity> ZombieMobGem(EntityType<T> type, int tint) {
        super(type, tint);
    }

    @Override
    public void onCageTick(ItemStack stack, World world, BlockPos pos) {
        if(world.isClient) return;

        // get entities in range
        List<Entity> entities = world.getOtherEntities(null, WorldUtil.getRangeBox(pos, CAGE_RANGE));

        // wither away all villagers
        for (Entity entity : entities) {
            if (entity instanceof VillagerEntity) { 
                VillagerEntity villager = (VillagerEntity) entity;
                villager.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 10, 5));
            }
        }
        
    }

    // called from LivingEntityListener#onDeath
    public static void convertVillager(World world, VillagerEntity entity, Vec3d pos, GemCageBlockEntity gemCageEntity) {
        // if there is fuel left, spawn a zombie at the location the villager died
        if (gemCageEntity.hasFuelRemaining()) {
            ZombieVillagerEntity zombie = (ZombieVillagerEntity) EntityType.ZOMBIE_VILLAGER.create(world);
            zombie.setVillagerData(entity.getVillagerData());
            zombie.refreshPositionAndAngles(pos.getX(), pos.getY(), pos.getZ(), 0.0f, 0.0f);
            world.spawnEntity(zombie);       
        }
    }

    // called from LivingEntityListener#onEat
    public static void onEat(LivingEntity wearer, ItemStack bracelet, ItemStack food) {
        BraceletItem braceletItem = (BraceletItem) bracelet.getItem();
        
        // check if the consumed item induces hunger
        boolean induceHunger = false;
        for (Pair<StatusEffectInstance, Float> effectPair : food.getItem().getFoodComponent().getStatusEffects()) {
            if (effectPair.getFirst().getEffectType() == StatusEffects.HUNGER) induceHunger = true;
        }
        
        // diminishers remove hunger from rotten flesh
        // augmenters add hunger to everything (currently same duration and amplifier as rotten flesh)
        if (!braceletItem.isAugmenter && induceHunger) {
            wearer.removeStatusEffect(StatusEffects.HUNGER);
        } else if (braceletItem.isAugmenter) {
            wearer.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 30*20, 0)); 
        }
    }
    
}
