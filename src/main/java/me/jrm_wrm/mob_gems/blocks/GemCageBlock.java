package me.jrm_wrm.mob_gems.blocks;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class GemCageBlock extends BlockWithEntity {

    public GemCageBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new GemCageBlockEntity();
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient || hand == Hand.OFF_HAND) return ActionResult.PASS;

        //GemCageBlockEntity entity = (GemCageBlockEntity) world.getBlockEntity(pos);
        NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);

        if (screenHandlerFactory != null) {
            player.openHandledScreen(screenHandlerFactory);
        }

        /*if (entity.getStack(0).isEmpty()) {
            if (player.getMainHandStack().getItem() instanceof MobGemItem) {
                entity.setStack(0, player.getMainHandStack().copy());
                player.getMainHandStack().setCount(0);
            } else {
                player.sendMessage(Text.of("Gem cage currently empty"), false);
            }
        } else {
            if (player.getMainHandStack().isEmpty()) {
                player.giveItemStack(entity.getStack(0).copy());
                entity.setStack(0, ItemStack.EMPTY);
            } else {
                player.sendMessage(Text.of("Gem cage currently holding: " + entity.getStack(0)), false);
            }
        }*/

        return ActionResult.SUCCESS;
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBreak(world, pos, state, player);
        if (world.isClient) return;

        GemCageBlockEntity entity = (GemCageBlockEntity) world.getBlockEntity(pos);

        for (ItemStack item : entity.getItems()) {
            if (item != ItemStack.EMPTY) {
                world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), item));
            }
        }

    }

}
