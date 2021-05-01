package me.jrm_wrm.mob_gems.util.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import me.jrm_wrm.mob_gems.items.BraceletItem;
import me.jrm_wrm.mob_gems.registry.ModItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@Mixin(BowItem.class)
public class BowItemMixin {

    /**
     *  skeleton mob gem augmenter code 
     *  {@SkeletonMobGem.java}
     */ 
    @ModifyVariable(method = "onStoppedUsing", at = @At(value = "INVOKE", target="Lnet/minecraft/entity/player/PlayerEntity;getArrowType"))
    public boolean onShoot(boolean bl, ItemStack stack, World world, LivingEntity user) {

        PlayerEntity player = (PlayerEntity) user;

        if (!player.getArrowType(stack).isEmpty() && BraceletItem.hasMobGemEquipped(player, ModItems.SKELETON_MOB_GEM, true)) {
            return true;
        }

        return false;
    }
    
}
