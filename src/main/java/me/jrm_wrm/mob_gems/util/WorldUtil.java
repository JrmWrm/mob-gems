package me.jrm_wrm.mob_gems.util;

import java.util.ArrayList;
import java.util.List;

import me.jrm_wrm.mob_gems.blocks.GemCageBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class WorldUtil {
    
    /**
     * get all gem cages in range
     */ 
    public static List<GemCageBlockEntity> getGemCageBlockEntitiesInRange(World world, Vec3d pos, double range) {
        ArrayList<GemCageBlockEntity> list = new ArrayList<GemCageBlockEntity>();
        
        // loop through all blockentities to find any gem cages in range of the specified location
        for (BlockEntity e : world.blockEntities) {
            if (getRangeBox(pos, range).contains(e.getPos().getX(), e.getPos().getY(), e.getPos().getZ()) && 
                e instanceof GemCageBlockEntity) list.add((GemCageBlockEntity) e);
        }

        return list;
    }

    /**
     * get all gem cages in range
     */ 
    public static List<GemCageBlockEntity> getGemCageBlockEntitiesInRange(World world, BlockPos pos, double range) {
        return getGemCageBlockEntitiesInRange(world, fromBlockPos(pos), range);
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
