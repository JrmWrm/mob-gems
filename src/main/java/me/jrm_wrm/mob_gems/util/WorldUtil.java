package me.jrm_wrm.mob_gems.util;

import java.util.ArrayList;
import java.util.List;

import me.jrm_wrm.mob_gems.blocks.GemCageBlockEntity;
import me.jrm_wrm.mob_gems.items.MobGemItem;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldUtil {
    
    public static List<GemCageBlockEntity> getGemCageBlockEntitiesInRange(World world, BlockPos pos) {
        ArrayList<GemCageBlockEntity> list = new ArrayList<GemCageBlockEntity>();
        
        // loop through all blockentities to find any gem cages in range of the specified location
        for (BlockEntity e : world.blockEntities) {
            if (MobGemItem.getRangeBox(pos).contains(e.getPos().getX(), e.getPos().getY(), e.getPos().getZ()) && 
                e instanceof GemCageBlockEntity) list.add((GemCageBlockEntity) e);
        }

        return list;
    }

}
