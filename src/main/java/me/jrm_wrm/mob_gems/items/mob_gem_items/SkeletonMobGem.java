package me.jrm_wrm.mob_gems.items.mob_gem_items;

import java.util.List;

import me.jrm_wrm.mob_gems.items.MobGemItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SkeletonMobGem extends MobGemItem {

    /**
     * Skeleton Mob Gem
     * - augmenter: don't consume arrows like the bow has infinity {@BowItemMixin.java}
     * - diminiser: wolves will never attack you
     * - gem cage: repel projectiles
     */
    public <T extends MobEntity> SkeletonMobGem(EntityType<T> type, int tint) {
        super(type, tint);
    }

    // Augmenter: see BowItemMixin

    @Override
    public void onCageTick(ItemStack stack, World world, BlockPos pos) {
        if(world.isClient) return;

        // get entities in range
        List<Entity> entities = world.getOtherEntities(null, getRangeBox(pos));

        // repel all projectiles
        for (Entity entity : entities) {
            if (entity instanceof ProjectileEntity) { 
                // TODO: if the projectile was shot from inside the box, make sure it doesn't get affected
                Vec3d difference = entity.getPos().subtract(new Vec3d(pos.getX(), pos.getY(), pos.getZ()));
                Vec3d direction = difference.normalize();
                double force = entity.getVelocity().length();
                Vec3d delta = direction.multiply(force);
                entity.addVelocity(delta.x, delta.y/8, delta.z);
            }
        } 
    }
    
}
