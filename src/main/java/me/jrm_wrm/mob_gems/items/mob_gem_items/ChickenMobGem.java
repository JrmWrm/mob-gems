package me.jrm_wrm.mob_gems.items.mob_gem_items;

import java.util.List;

import me.jrm_wrm.mob_gems.items.MobGemItem;
import me.jrm_wrm.mob_gems.util.WorldUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ChickenMobGem extends MobGemItem {

    /**
     * Chicken Mob Gem
     * - augmenter: slow falling
     * - diminiser: stronger gravity
     * - gem cage: entities in range drop eggs
     */
    public <T extends MobEntity> ChickenMobGem(EntityType<T> type, int tint) {
        super(type, tint);
    }

    @Override
    public void onAugmenterTick(ItemStack bracelet, ItemStack stack, World world, LivingEntity wearer, int slot) {
        if(world.isClient) return;
        
        wearer.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 10, 2, true, false, true));
    }

    @Override
    public void onDiminisherTick(ItemStack bracelet, ItemStack stack, World world, LivingEntity wearer, int slot) {
        if(world.isClient) return;
        
        wearer.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 10, -3, true, false, false));
    }

    @Override
    public void onCageTick(ItemStack stack, World world, BlockPos pos) {
        if(world.isClient) return;
        if(world.random.nextDouble() > 0.1) return;

        // get entities in range
        List<Entity> entities = world.getOtherEntities(null, WorldUtil.getRangeBox(pos, CAGE_RANGE));

        // drop eggs
        for (Entity entity : entities) {
            if (entity instanceof MobEntity && world.random.nextDouble() < 0.008) {  
                world.playSoundFromEntity(null, entity, SoundEvents.ENTITY_CHICKEN_EGG, 
                    SoundCategory.NEUTRAL, 1.0F, (world.random.nextFloat() - world.random.nextFloat()) * 0.2F + 1.0F);
                entity.dropItem(Items.EGG);
            }
        }      
    }
    
}
