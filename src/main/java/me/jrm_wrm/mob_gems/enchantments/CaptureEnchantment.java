package me.jrm_wrm.mob_gems.enchantments;

import java.util.Map;

import me.jrm_wrm.mob_gems.registry.ModItems;
import me.jrm_wrm.mob_gems.registry.ModMisc;
import me.jrm_wrm.mob_gems.registry.ModNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class CaptureEnchantment extends Enchantment {

    public static final float BASE_CAPTURE_CHANCE = 0.30f;
    public static final float CAPTURE_CHANCE_LVL_FACTOR = 0.20f;

    public CaptureEnchantment() {
        super(Enchantment.Rarity.VERY_RARE, EnchantmentTarget.WEAPON, new EquipmentSlot[] {EquipmentSlot.MAINHAND});
    }

    // some nice juicy feedback
    public static void provideFeedback(PlayerEntity player, double mobX, double mobY, double mobZ) {
        player.playSound(SoundEvents.BLOCK_BEACON_POWER_SELECT, 1.0f, 1.0f);
        player.world.addParticle(ParticleTypes.FLASH, mobX, mobY, mobZ, 0.0D, 0.0D, 0.0D);

        for (float i = 0; i < 10; i++) {
            double d = mobX + i/10 * (player.getPos().getX() - mobX);
            double e = mobY + i/10 * (player.getPos().getY() - mobY) + 1;
            double f = mobZ + i/10 * (player.getPos().getZ() - mobZ);
            player.world.addParticle(ParticleTypes.SOUL, d, e, f, 0.0D, 0.0D, 0.0D);
            player.world.addParticle(ParticleTypes.ASH, d, e, f, 0.0D, 0.0D, 0.0D);
        }

    }

    // gets called when an entitity is killed by a player wielding the capture enchantment
    public static ActionResult onKilled(World world, PlayerEntity player, LivingEntity target,  Map<Enchantment, Integer> itemEnchants) {
        
        int level = itemEnchants.get(ModMisc.CAPTURE_ENCHANTMENT);
        float captureChance = CaptureEnchantment.BASE_CAPTURE_CHANCE + (level-1) * CaptureEnchantment.CAPTURE_CHANCE_LVL_FACTOR;
        MobEntity mob = (MobEntity) target;
        
        // check if diamonds in off-hand
        // check if there exists a mob gem for the killed mob
        // rngesus decides
        if (player.getOffHandStack().getItem() == Items.DIAMOND) {
            if (ModItems.hasMobGem(mob.getType())) {
                if (Math.random() < captureChance) {                    
                    // remove one diamond and give one appropriate mob gem
                    player.setStackInHand(Hand.OFF_HAND, 
                        ItemUsage.method_30012(player.getOffHandStack(), player, ModItems.getMobGemFromMob(mob.getType()).getDefaultStack()));
                    

                    // send position information to the client for some juicy feedback
                    PacketByteBuf buf = PacketByteBufs.create();
                    buf.writeDouble(mob.getPos().getX());
                    buf.writeDouble(mob.getPos().getY());
                    buf.writeDouble(mob.getPos().getZ());                            
                    ServerPlayNetworking.send((ServerPlayerEntity) player, ModNetworking.CAPTURE_FEEDBACK_PACKET_ID, buf);
                }
            }
        };

        return ActionResult.PASS;
    }

    // Capture has 3 levels
    @Override
    public int getMaxLevel() {
        return 3;
    }

    // True: Can not be found in enchantment table
    @Override
    public boolean isTreasure() {
        return true;
    }

    // True: can be found in the enchantment table or on loot
    @Override
    public boolean isAvailableForRandomSelection() {
        return true;
    }

    // Power (at what level found in enchant. table) is same as fortune
    @Override
    public int getMinPower(int level) {
        return 15 + (level - 1) * 9;
    }
  
    @Override
    public int getMaxPower(int level) {
        return super.getMinPower(level) + 50;
    }

    // Enchantment is not compatible with mending, looting or multishot
    @Override
    public boolean canAccept(Enchantment other) {
        return super.canAccept(other) && 
        other != Enchantments.LOOTING && 
        other != Enchantments.MENDING &&
        other != Enchantments.MULTISHOT;
    }

}
