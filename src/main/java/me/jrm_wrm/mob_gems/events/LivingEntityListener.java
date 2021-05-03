package me.jrm_wrm.mob_gems.events;

import java.util.Map;

import me.jrm_wrm.mob_gems.blocks.GemCageBlockEntity;
import me.jrm_wrm.mob_gems.enchantments.CaptureEnchantment;
import me.jrm_wrm.mob_gems.items.BraceletItem;
import me.jrm_wrm.mob_gems.items.MobGemItem;
import me.jrm_wrm.mob_gems.items.mob_gem_items.ZombieMobGem;
import me.jrm_wrm.mob_gems.registry.ModItems;
import me.jrm_wrm.mob_gems.registry.ModMisc;
import me.jrm_wrm.mob_gems.util.WorldUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LivingEntityListener {

    // ON_DEATH
    public static ActionResult onDeath(LivingEntity entity, DamageSource source) {
        World world = entity.getEntityWorld();
        BlockPos pos = entity.getBlockPos();

        if (world.isClient) return ActionResult.PASS;

        // get gem cages in range
        WorldUtil.getGemCageBlockEntitiesInRange(world, pos, MobGemItem.CAGE_RANGE).forEach( (GemCageBlockEntity gemCageEntity) -> {
            
            // zombie mob gem code
            if (entity instanceof VillagerEntity && gemCageEntity.getGemStack().getItem() instanceof ZombieMobGem) {
                ZombieMobGem.convertVillager(world, (VillagerEntity) entity, entity.getPos(), gemCageEntity);
            }            
        }); 

        return ActionResult.PASS;
    }

    // AFTER_KILLED_OTHER_ENTITY
    public static ActionResult onKilled(World world, Entity killer, LivingEntity target) {
        if (world.isClient) return ActionResult.PASS;

        if (killer instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) killer;
            ItemStack item = player.getMainHandStack();
            Map<Enchantment, Integer> itemEnchants = EnchantmentHelper.get(item);
    
            // ENCHANTMENT LOGIC \\
            // Capture Enchantment
            if (itemEnchants.containsKey(ModMisc.CAPTURE_ENCHANTMENT) && target instanceof MobEntity) {
                CaptureEnchantment.onKilled(world, player, target, itemEnchants);
            }
        }

        return ActionResult.PASS;
    }

    // ON_EAT
    public static ActionResult onEat(World world, LivingEntity entity, ItemStack food) {  
        if (world.isClient) return ActionResult.PASS;;

        ItemStack bracelet = BraceletItem.getEquippedBracelet(entity);
        if (BraceletItem.hasMobGem(bracelet, ModItems.ZOMBIE_MOB_GEM)) ZombieMobGem.onEat(entity, bracelet, food);

        return ActionResult.PASS;
    }

}
