package me.jrm_wrm.mob_gems.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class WorldUtil {
    
    /**
     * gets all blockentities in a specified range with a specified type
     * @param <T> BlockEntity type
     * @param world world
     * @param pos central position to check from
     * @param range range to check
     * @param filter filter to check with
     * @return a list of blockentities
     */
    @SuppressWarnings("unchecked") // it IS checked
    public static <T extends BlockEntity> List<T> getBlockEntitiesInRange(World world, Vec3d pos, double range, Predicate<BlockEntity> filter) {
        ArrayList<T> list = new ArrayList<T>();
        
        // loop through the box and add the blockentity if it passes the test.
        forEachBlockInBox(getRangeBox(pos, range), blockPos -> {
            BlockEntity blockEntity = world.getBlockEntity(blockPos);
            if (blockEntity != null && filter.test(blockEntity)) list.add((T) blockEntity);
        });

        return list;
    }

    /**
     * gets all blockentities in a specified range with a specified type
     * @param <T> BlockEntity type
     * @param world world
     * @param pos central position to check from
     * @param range range to check
     * @param type type to check for, pass null if looking for all blockentities
     * @return a list of blockentities
     */
    public static <T extends BlockEntity> List<T> getBlockEntitiesInRange(World world, Vec3d pos, double range, @Nullable BlockEntityType<T> type) {
        // filter block entities based on type
        Predicate<BlockEntity> filter = blockEntity -> type != null && blockEntity.getType().equals(type);
        return getBlockEntitiesInRange(world, pos, range, filter);
    }

    /**
     * gets all blockentities in a specified range with a specified type
     * @param <T> BlockEntity type
     * @param world world
     * @param pos central position to check from
     * @param range range to check
     * @param filter filter to check with
     * @return a list of blockentities
     */
    public static <T extends BlockEntity> List<T> getBlockEntitiesInRange(World world, BlockPos pos, double range, Predicate<BlockEntity> filter) {
        return getBlockEntitiesInRange(world, fromBlockPos(pos), range, filter);
    }

    /**
     * gets all blockentities in a specified range with a specified type
     * @param <T> BlockEntity type
     * @param world world
     * @param pos central position to check from
     * @param range range to check
     * @param type type to check for, pass null if looking for all blockentities
     * @return a list of blockentities
     */
    public static <T extends BlockEntity> List<T> getBlockEntitiesInRange(World world, BlockPos pos, double range, @Nullable BlockEntityType<T> type) {
        return getBlockEntitiesInRange(world, fromBlockPos(pos), range, type);
    }

    /**
     * loop through all block positions inside a box
     * @param box
     * @param consumer called for each blockpos
     */
    public static void forEachBlockInBox(Box box, Consumer<BlockPos> consumer) {
        for (double x = box.minX; x <= box.maxX; x++) {
            for (double y = box.minX; x <= box.maxX; x++) {
                for (double z = box.minX; x <= box.maxX; x++) {
                    consumer.accept(new BlockPos(x, y, z));
                }
            }
        }
    }

    /**
     * get a box that extends in a range around a positon
     */ 
    public static Box getRangeBox(Vec3d pos, double range) {
        return new Box(
            pos.getX() - range, pos.getY() - range, pos.getZ() - range, 
            pos.getX() + range, pos.getY() + range, pos.getZ() + range);
    }

    /**
     * get a box that extends in a range around a positon
     */ 
    public static Box getRangeBox(BlockPos pos, double range) {
        return getRangeBox(fromBlockPos(pos), range);
    }

    /**
     * convert BlockPos to Vec3d
     */ 
    public static Vec3d fromBlockPos(BlockPos pos) {
        return new Vec3d(pos.getX(), pos.getY(), pos.getZ());
    }

}
