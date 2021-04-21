package me.jrm_wrm.mob_gems.events;

import me.jrm_wrm.mob_gems.blocks.GemCageBlockEntity;
import me.jrm_wrm.mob_gems.items.mob_gem_items.CowMobGem;
import me.jrm_wrm.mob_gems.util.WorldUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class InteractionListener {

    // called when the player right clicks an entity
    public static ActionResult onUseEntity(PlayerEntity player, World world, Hand hand, Entity entity, EntityHitResult hitResult) {
        if (world.isClient) return ActionResult.PASS;

        BlockPos pos = entity.getBlockPos();

        // get gem cages in range
        WorldUtil.getGemCageBlockEntitiesInRange(world, pos).forEach( (GemCageBlockEntity e) -> {
            
            // cow mob gem code
            if (entity instanceof LivingEntity && e.getGemStack().getItem() instanceof CowMobGem) {
                CowMobGem.milkEntity(player, hand, (LivingEntity) entity);
            }

        }); 

        return ActionResult.PASS;
    }
    
}
