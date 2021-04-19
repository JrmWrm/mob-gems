package me.jrm_wrm.mob_gems.registry;

import me.jrm_wrm.mob_gems.MobGems;
import me.jrm_wrm.mob_gems.enchantments.CaptureEnchantment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class ModNetworking {

    // Packet ID's
    public static final Identifier CAPTURE_FEEDBACK_PACKET_ID = new Identifier(MobGems.MOD_ID, "capture_feedback");
    public static final Identifier GEM_CAGE_SET_STACK_PACKET_ID = new Identifier(MobGems.MOD_ID, "capture_feedback");

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

    }
    
}
