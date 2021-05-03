package me.jrm_wrm.mob_gems.items.mob_gem_items;

import java.util.Random;

import me.jrm_wrm.mob_gems.items.MobGemItem;
import me.jrm_wrm.mob_gems.registry.ModMisc;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
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
        
        livingEntity.applyStatusEffect(new StatusEffectInstance(ModMisc.BLAST_RESISTANCE_EFFECT, 10, 2, true, false));
    }

    public static ActionResult igniteEntity(PlayerEntity player, Hand hand, Entity entity) {
        if (!(entity instanceof MobEntity)) return ActionResult.PASS;
        
        ItemStack handStack = player.getStackInHand(hand);
        MobEntity mob = (MobEntity) entity;

        // if the player is weilding flint and steel, apply the combustion effect to the mob
        if (handStack.getItem() == Items.FLINT_AND_STEEL) {
            mob.applyStatusEffect(new StatusEffectInstance(ModMisc.COMBUSTION_EFFECT, 20*4, 1, true, false));
            
            // if the player is not in creative, damage the flint and steel
            if (!player.isCreative() && !player.world.isClient) 
                handStack.damage(1, new Random(), (ServerPlayerEntity) player);
            
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    } 
    
}
