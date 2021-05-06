package me.jrm_wrm.mob_gems.items.mob_gem_items;

import java.util.ArrayList;
import java.util.function.BiConsumer;

import me.jrm_wrm.mob_gems.items.MobGemItem;
import me.jrm_wrm.mob_gems.registry.ModPackets;
import me.jrm_wrm.mob_gems.util.WorldUtil;
import me.jrm_wrm.mob_gems.util.mixin.ItemUsageContextMixin;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
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
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
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

    // Bracelet code
    //#region
    @Override
    public void onAugmenterTick(ItemStack bracelet, ItemStack gemStack, World world, LivingEntity wearer, int slot) {      
        beeBraceletTick(world, wearer, ParticleTypes.FALLING_NECTAR, (sourcePos, targetPos) -> {      
            // only bonemeal every now and then
            if (world.random.nextDouble() > 0.004) return;
            bonemeal(new Vec3d(sourcePos.getX(), sourcePos.getY(), sourcePos.getZ()), targetPos, world);
        });
    }

    @Override
    public void onDiminisherTick(ItemStack bracelet, ItemStack gemStack, World world, LivingEntity wearer, int slot) {
        beeBraceletTick(world, wearer, ParticleTypes.ASH, (sourcePos, targetPos) -> {        
            // only destroy every now and then
            if (world.random.nextDouble() > 0.05) return;

            Block block = world.getBlockState(targetPos).getBlock();
            boolean flower = block.isIn(TagRegistry.block(new Identifier("minecraft", "flowers"))) && block != Blocks.WITHER_ROSE;

            // if it's a flower or grass remove block
            // if it's a flower, small chance of turning into a wither rose
            if (flower || isGrowable(block) || block == Blocks.GRASS || block == Blocks.TALL_GRASS) {
                world.removeBlock(targetPos, false);
                world.updateNeighbors(targetPos, block);
                if (flower && world.random.nextDouble() < 0.1) world.setBlockState(targetPos, Blocks.WITHER_ROSE.getDefaultState());
            }
        });
    }

    // the universal code for both augmenter and diminisher logic
    private void beeBraceletTick(World world, LivingEntity wearer, ParticleEffect particle, BiConsumer<Vec3d, BlockPos> blockHandler) {
        Vec3d pos = wearer.getPos();
        BlockPos blockPos = wearer.getBlockPos();

        if (world.isClient) {
            
            if (wearer.getVelocity().getX() == 0 && wearer.getVelocity().getZ() == 0) {
                // if standing still, remove from the moving entities list
                if (movedEntities.contains(wearer)) {
                    movedEntities.remove(wearer);
                    sendMovingPacket(false, wearer.getEntityId());
                }
            } else {  
                // else, if moving, add to the movig entities list
                if (!movedEntities.contains(wearer)) {
                    movedEntities.add(wearer);
                    sendMovingPacket(true, wearer.getEntityId());
                }

                // and render particles at the entity's butt
                double effectX = pos.getX() + (world.random.nextDouble() - 0.5)/3;
                double effectY = wearer.getBodyY(0.5d);
                double effectZ = pos.getZ() + (world.random.nextDouble() - 0.5)/3;
                world.addParticle(particle, effectX, effectY, effectZ, 0.0D, 0.0D, 0.0D);
            }

            // act as guard clause
            return;
        }
        
        // check if the entity has moved
        if (movedEntities.contains(wearer)) {

            // loop through all blocks in range
            for (int x = blockPos.getX() - braceletRange; x < blockPos.getX() + braceletRange; x++) {
                for (int y = blockPos.getY() - braceletRange; y < blockPos.getY() + braceletRange; y++) {
                    for (int z = blockPos.getZ() - braceletRange; z < blockPos.getZ() + braceletRange; z++) {

                        // handle the augmenter/diminisher code
                        BlockPos targetPos = new BlockPos(x, y, z);
                        blockHandler.accept(pos, targetPos);
                    }
                }
            }
        }
    }
    //#endregion

    @Override
    public void onCageTick(ItemStack stack, World world, BlockPos pos) {
        Box box = WorldUtil.getRangeBox(pos, CAGE_RANGE);

        // loop through all blocks in range
        for (int x = (int) box.minX; x <= box.maxX; x++) {
            for (int y = (int) box.minY; y <= box.maxY; y++) {
                for (int z = (int) box.minZ; z <= box.maxZ; z++) {

                    BlockPos blockPos2 = new BlockPos(x, y, z);
                    Block block = world.getBlockState(blockPos2).getBlock();

                    // only bonemeal every now and then
                    if (world.random.nextDouble() < 0.003 && isGrowable(block)) {
                        bonemeal(new Vec3d(pos.getX(), pos.getY(), pos.getZ()), blockPos2, world);
                    }
                }
            }
        }
        
    }

    private boolean isGrowable(Block block) {
        return block.isIn(TagRegistry.block(new Identifier("minecraft", "bee_growables")))
            || block.isIn(TagRegistry.block(new Identifier("minecraft", "saplings")));
    }

    private void bonemeal(Vec3d sourcePos, BlockPos bonemealPos, World world) {
        PlayerEntity closestPlayer = world.getClosestPlayer(sourcePos.getX(), sourcePos.getY(), sourcePos.getZ(), 128, false);

        // trick the game into thinking a block was bonemealed
        if (closestPlayer != null) {
            BlockHitResult hit = new BlockHitResult(sourcePos, Direction.UP, bonemealPos, false);
            ItemUsageContext context = new ItemUsageContext(closestPlayer, Hand.MAIN_HAND, hit);
            ((ItemUsageContextMixin) context).setStack(ItemStack.EMPTY);
            Items.BONE_MEAL.useOnBlock(context);
        }
    }

    // Networking code
    //#region
    private void sendMovingPacket(boolean moving, int entityId) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBoolean(moving);
        buf.writeInt(entityId);
        ClientPlayNetworking.send(ModPackets.SET_MOVING_TAG_PACKET_ID, buf);
    }

    public void receiveMovingPacket(boolean moving, int entityId,  PlayerEntity player) {
        LivingEntity entity = (LivingEntity) player.world.getEntityById(entityId);
    
        System.out.println(moving);

        if (moving) movedEntities.add(entity);
        else movedEntities.remove(entity);
    }
    //#endregion
    
}
