package me.jrm_wrm.mob_gems.registry;

import me.jrm_wrm.mob_gems.MobGems;
import me.jrm_wrm.mob_gems.enchantments.CaptureEnchantment;
import me.jrm_wrm.mob_gems.util.LivingEntityAccess;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class ModNetworking {

    // Packet ID's
    public static final Identifier CAPTURE_FEEDBACK_PACKET_ID = new Identifier(MobGems.MOD_ID, "capture_feedback");
    public static final Identifier SET_IGNITED_PACKET_ID = new Identifier(MobGems.MOD_ID, "set_ignited");

    public static void registerReceivers() {

        // register the capture feedback receiver
        ClientPlayNetworking.registerGlobalReceiver(CAPTURE_FEEDBACK_PACKET_ID, (client, handler, buf, responseSender) -> {
            
            PlayerEntity player = client.player;
            double mobX = buf.readDouble();
            double mobY = buf.readDouble();
            double mobZ = buf.readDouble();

            client.execute(() -> {
                CaptureEnchantment.provideFeedback(player, mobX, mobY, mobZ);
            });
        });

        // register the combustion apply receiver
        ClientPlayNetworking.registerGlobalReceiver(SET_IGNITED_PACKET_ID, (client, handler, buf, responseSender) -> {
            
            LivingEntity entity = (LivingEntity) ((World) client.world).getEntityById(buf.readInt());
            boolean ignited = buf.readBoolean();

            client.execute(() -> {
                ((LivingEntityAccess) entity).setIgnited(ignited);
            });
        });

    }
    
}
