package me.jrm_wrm.mob_gems.items.mob_gem_items;

import java.util.Random;
import java.util.function.Predicate;

import me.jrm_wrm.mob_gems.items.MobGemItem;
import me.jrm_wrm.mob_gems.registry.ModMisc;
import me.jrm_wrm.mob_gems.util.WorldUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public class CreeperMobGem extends MobGemItem {

    private final int augmenterRange = 5;

    /**
     * Creeper Mob Gem
     * - augmenter: when hostile mobs approach and not on full health the wearer explodes
     * - diminiser: blast resistence
     * - gem cage: all mobs in range can be ignited
     */
    public <T extends MobEntity> CreeperMobGem(EntityType<T> type, int tint) {
        super(type, tint);
    }

    @Override
    public void onAugmenterTick(ItemStack bracelet, ItemStack gemStack, World world, LivingEntity wearer, int slot) {
        if (world.isClient) return;
        if (wearer.getHealth() == wearer.getMaxHealth()) return;

        // get all entities in range
        Box rangeBox = WorldUtil.getRangeBox(wearer.getPos(), augmenterRange);

        // either the wearer is not hostile and the igniting entity should be
        // or the wearer is hostile and the igniting entity shouldn't be
        Predicate<Entity> ignitionFilter = 
            entity -> (!(wearer instanceof HostileEntity) && entity instanceof HostileEntity)
                || (wearer instanceof HostileEntity && entity instanceof LivingEntity && !(entity instanceof HostileEntity));

        // if there is a hostile mob in range & the wearer doesn't have the combustion effect
        // apply the combustion effect
        if (world.getOtherEntities(wearer, rangeBox).stream().anyMatch(ignitionFilter)) {
            if (!wearer.hasStatusEffect(ModMisc.COMBUSTION_EFFECT))
                wearer.applyStatusEffect(new StatusEffectInstance(ModMisc.COMBUSTION_EFFECT, 20*3, 1, true, false, true));
        }
        // else, if there is no hostile entity 
        // and the combustion effect is ambient (if the wearer gets the combustion effect through other means, it shouldn't be negated)
        // remove the combustion effect 
        else if (wearer.hasStatusEffect(ModMisc.COMBUSTION_EFFECT)) {
            if (wearer.getStatusEffect(ModMisc.COMBUSTION_EFFECT).isAmbient())
                wearer.removeStatusEffect(ModMisc.COMBUSTION_EFFECT);
        }
    }

    @Override
    public void onDiminisherTick(ItemStack bracelet, ItemStack stack, World world, LivingEntity wearer, int slot) {
        if (world.isClient) return;
        
        wearer.applyStatusEffect(new StatusEffectInstance(ModMisc.BLAST_RESISTANCE_EFFECT, 10, 2, true, false, true));
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
