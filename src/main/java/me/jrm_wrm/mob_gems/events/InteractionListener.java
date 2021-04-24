package me.jrm_wrm.mob_gems.events;

import me.jrm_wrm.mob_gems.blocks.GemCageBlockEntity;
import me.jrm_wrm.mob_gems.items.BraceletItem;
import me.jrm_wrm.mob_gems.items.mob_gem_items.CowMobGem;
import me.jrm_wrm.mob_gems.items.mob_gem_items.SkeletonMobGem;
import me.jrm_wrm.mob_gems.registry.ModItems;
import me.jrm_wrm.mob_gems.util.WorldUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class InteractionListener {

    // called when the player right clicks an entity
    public static ActionResult onUseEntity(PlayerEntity player, World world, Hand hand, Entity entity, EntityHitResult hitResult) {
        BlockPos pos = entity.getBlockPos();
        ActionResult actionResult = ActionResult.PASS;

        // get gem cages in range
        for (GemCageBlockEntity e : WorldUtil.getGemCageBlockEntitiesInRange(world, pos)) {
            
            // cow mob gem code
            if (entity instanceof LivingEntity && e.getGemStack().getItem() instanceof CowMobGem) {
                actionResult = CowMobGem.milkEntity(player, hand, (LivingEntity) entity);
            }
            
        } 

        return actionResult;
    }

    // called when the player attacks an entity
    public static ActionResult onAttackEntity(PlayerEntity player, World world, Hand hand, Entity entity, EntityHitResult hitResult) {
        ActionResult actionResult = ActionResult.PASS;

        ItemStack bracelet = BraceletItem.getEquippedBracelet(player);
        if (BraceletItem.hasMobGem(bracelet, ModItems.SKELETON_MOB_GEM) && entity instanceof WolfEntity) SkeletonMobGem.onAttackWolf(player, (WolfEntity) entity, bracelet);

        return actionResult;
    }
    
}
