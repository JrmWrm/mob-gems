package me.jrm_wrm.mob_gems.items.mob_gem_items;

import java.util.ArrayList;

import me.jrm_wrm.mob_gems.items.MobGemItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class CowMobGem extends MobGemItem {

    /**
     * Cow Mob Gem
     * - augmenter: remove all status effects
     * - diminiser: slowly increase all status effects to max level 5
     * - gem cage: all mobs in range are milkable
     */
    public <T extends MobEntity> CowMobGem(EntityType<T> type, int tint) {
        super(type, tint);
    }

    @Override
    public void onAugmenterTick(ItemStack bracelet, ItemStack gemStack, World world, LivingEntity livingEntity, int slot) {
        if (world.isClient) return;

        ArrayList<StatusEffect> appliedEffects = new ArrayList<StatusEffect>();
        // get all status effect on the entity
        for (StatusEffectInstance effectInstance : livingEntity.getStatusEffects()) {
            appliedEffects.add(effectInstance.getEffectType());    
        }
        // remove all status effects from the entity
        for (StatusEffect effect : appliedEffects) {
            livingEntity.removeStatusEffect(effect);
        }
    }
    
    @Override
    public void onDiminisherTick(ItemStack bracelet, ItemStack gemStack, World world, LivingEntity livingEntity, int slot) {
        for (StatusEffectInstance effect : livingEntity.getStatusEffects()) {
            // slowly increase to max level 5
            if (world.random.nextDouble() < 0.01)
                effect.upgrade(new StatusEffectInstance( effect.getEffectType(), effect.getDuration(), 
                    MathHelper.clamp(effect.getAmplifier() + 1, -4, 4)));    
        }
    }

    // called via InteractionListener when right clicking an entity in the range of a gem cage with a cow mob gem
    public static ActionResult milkEntity(PlayerEntity player, Hand hand, Entity entity) {
        if (!(entity instanceof MobEntity)) return ActionResult.PASS;
        
        ItemStack handStack = player.getStackInHand(hand);
        MobEntity mob = (MobEntity) entity;

        // if the player is weilding a bucket, use ItemUsage to fill the bucket with milk
        if (handStack.getItem() == Items.BUCKET && !mob.isBaby()) {
            player.playSound(SoundEvents.ENTITY_COW_MILK, 1.0F, 1.0F);
            ItemStack filledBucket = ItemUsage.method_30012(handStack, player, Items.MILK_BUCKET.getDefaultStack());
            player.setStackInHand(hand, filledBucket);
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }
}
