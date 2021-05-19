package me.jrm_wrm.mob_gems.items.mob_gem_items;

import java.util.List;

import me.jrm_wrm.mob_gems.items.MobGemItem;
import me.jrm_wrm.mob_gems.util.InvUtil;
import me.jrm_wrm.mob_gems.util.WorldUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FoxMobGem extends MobGemItem {

    /**
     * Fox Mob Gem
     * - augmenter: able to wear any items in the head slot
     * - diminiser: no longer able to attack chickens, rabbits & fish
     * - gem cage: all items in range will be collected
     */
    public <T extends MobEntity> FoxMobGem(EntityType<T> type, int tint) {
        super(type, tint);
    }

    @Override
    public void onCageTick(ItemStack stack, World world, BlockPos pos) {
        if(world.isClient) return;

        // get item entities in range
        List<ItemEntity> items = world.getEntitiesByType(EntityType.ITEM, WorldUtil.getRangeBox(pos, CAGE_RANGE), T -> true);

        // move items closer
        for (ItemEntity item : items) {
            Vec3d diff = WorldUtil.fromBlockPos(pos).subtract(item.getPos());
            Vec3d dir = diff.normalize();
            item.setVelocity(dir.multiply(0.2f));

            // collect items in range
            if (diff.length() < 1) collectItem(item, pos, world);
        }
    }

    private void collectItem(ItemEntity itemEntity, BlockPos cagePos, World world)  {
        ItemStack stack = itemEntity.getStack();

        System.out.println("Collect!");

        WorldUtil.forEachBlockInBox(WorldUtil.getRangeBox(cagePos, 1), blockPos -> {
            Inventory inventory = InvUtil.getInventoryAt(world, blockPos);
            if (inventory == null) return;

            Direction direction = Direction.fromVector(
                blockPos.getX() - cagePos.getX(), 
                blockPos.getY() - cagePos.getY(), 
                blockPos.getZ() - cagePos.getZ());
            
            if (InvUtil.isInventoryFull(inventory, direction)) return;
            
            InvUtil.transfer(null, inventory, stack, direction);
        });
    }

    

}
