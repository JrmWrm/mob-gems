package me.jrm_wrm.mob_gems.items.mob_gem_items;

import java.util.ArrayList;

import me.jrm_wrm.mob_gems.items.MobGemItem;
import me.jrm_wrm.mob_gems.util.mixin.ItemUsageContextMixin;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class BeeMobGem extends MobGemItem {

    private int braceletRange = 2;
    private ArrayList<LivingEntity> movedEntities = new ArrayList<LivingEntity>();

     /**
     * Bee Mob Gem
     * - augmenter: randomly bonemeal blocks near the player
     * - diminiser: randomly destroy grass/flowers near the player, very low chance of spawning wither rose
     * - gem cage: all crops grow faster in the area
     */
    public <T extends MobEntity> BeeMobGem(EntityType<T> type, int tint) {
        super(type, tint);
    }

    @Override
    public void onAugmenterTick(ItemStack bracelet, ItemStack gemStack, World world, LivingEntity livingEntity, int slot) {      
        Vec3d pos = livingEntity.getPos();
        BlockPos blockPos = livingEntity.getBlockPos();

        PlayerEntity closestPlayer = livingEntity instanceof PlayerEntity ? 
            (PlayerEntity) livingEntity : world.getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 128, false);

        // check if there is a player near, otherwise ItemUsageContext#new will break
        // check if the entity has moved
        if (closestPlayer != null && movedEntities.contains(livingEntity)) {

            // loop through all blocks in range
            for (int x = blockPos.getX() - braceletRange; x < blockPos.getX() + braceletRange; x++) {
                for (int y = blockPos.getY() - braceletRange; y < blockPos.getY() + braceletRange; y++) {
                    for (int z = blockPos.getZ() - braceletRange; z < blockPos.getZ() + braceletRange; z++) {

                        // only every bonemeal every now and then
                        if (world.random.nextDouble() > 0.005) continue;

                        // trick the game into thinking a block was bonemealed
                        BlockHitResult hit = new BlockHitResult(pos, Direction.UP, new BlockPos(x, y, z), false);
                        ItemUsageContext context = new ItemUsageContext(closestPlayer, Hand.MAIN_HAND, hit);
                        ((ItemUsageContextMixin) context).setStack(ItemStack.EMPTY);
                        Items.BONE_MEAL.useOnBlock(context);
                    }
                }
            }
        }

        // check velocity and register as moved
        if (world.isClient) {
            
            if (livingEntity.getVelocity().getX() == 0 && livingEntity.getVelocity().getZ() == 0) {
                if (movedEntities.contains(livingEntity)) movedEntities.remove(livingEntity);
            } else {
                if (!movedEntities.contains(livingEntity)) movedEntities.add(livingEntity);
                // render pollination particles at the entity's butt
                double effectX = pos.getX() + (world.random.nextDouble() - 0.5)/3;
                double effectY = livingEntity.getBodyY(0.5d);
                double effectZ = pos.getZ() + (world.random.nextDouble() - 0.5)/3;
                world.addParticle(ParticleTypes.FALLING_NECTAR, effectX, effectY, effectZ, 0.0D, 0.0D, 0.0D);
            }

        }
    }

    @Override
    public void onDiminisherTick(ItemStack bracelet, ItemStack gemStack, World world, LivingEntity livingEntity, int slot) {
        Vec3d pos = livingEntity.getPos();
        BlockPos blockPos = livingEntity.getBlockPos();

        // check if the entity has moved
        if (movedEntities.contains(livingEntity)) {

            // loop through all blocks in range
            for (int x = blockPos.getX() - braceletRange; x < blockPos.getX() + braceletRange; x++) {
                for (int y = blockPos.getY() - braceletRange; y < blockPos.getY() + braceletRange; y++) {
                    for (int z = blockPos.getZ() - braceletRange; z < blockPos.getZ() + braceletRange; z++) {

                        // only destroy every now and then
                        if (world.random.nextDouble() > 0.05) continue;

                        BlockPos blockPos2 = new BlockPos(x, y, z);
                        Block block = world.getBlockState(blockPos2).getBlock();
                        boolean flower = block.isIn(TagRegistry.block(new Identifier("minecraft", "flowers"))) && block != Blocks.WITHER_ROSE;

                        // if it's a flower or grass remove block
                        // if it's a flower, small chance of turning into a wither rose
                        if (flower || block == Blocks.GRASS || block == Blocks.TALL_GRASS) {
                            world.removeBlock(blockPos2, false);
                            if (flower && world.random.nextDouble() < 0.1) world.setBlockState(blockPos2, Blocks.WITHER_ROSE.getDefaultState());
                        }
                    }
                }
            }
        }

        // check velocity and register as moved
        if (world.isClient) {
            
            if (livingEntity.getVelocity().getX() == 0 && livingEntity.getVelocity().getZ() == 0) {
                if (movedEntities.contains(livingEntity)) movedEntities.remove(livingEntity);
            } else {
                if (!movedEntities.contains(livingEntity)) movedEntities.add(livingEntity);
                // render ash particles at the entity's butt
                double effectX = pos.getX() + (world.random.nextDouble() - 0.5)/3;
                double effectY = livingEntity.getBodyY(0.5d);
                double effectZ = pos.getZ() + (world.random.nextDouble() - 0.5)/3;
                world.addParticle(ParticleTypes.ASH, effectX, effectY, effectZ, 0.0D, 0.0D, 0.0D);
            }

        }
    }

    @Override
    public void onCageTick(ItemStack stack, World world, BlockPos pos) {
        
    }
    
}
