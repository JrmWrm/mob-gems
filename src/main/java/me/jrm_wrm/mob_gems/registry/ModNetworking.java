package me.jrm_wrm.mob_gems.registry;

import me.jrm_wrm.mob_gems.MobGems;
import me.jrm_wrm.mob_gems.enchantments.CaptureEnchantment;
import me.jrm_wrm.mob_gems.util.LivingEntityAccess;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class ModNetworking {

    // Packet ID's
    public static final Identifier CAPTURE_FEEDBACK_PACKET_ID = new Identifier(MobGems.MOD_ID, "capture_feedback");
    public static final Identifier SET_IGNITED_PACKET_ID = new Identifier(MobGems.MOD_ID, "set_ignited");

    /**
     * Should be called from the client initializer!
     */
    @Environment(EnvType.CLIENT)
    public static void registerClientReceivers() {

        // register the capture feedback receiver
        ClientPlayNetworking.registerGlobalReceiver(CAPTURE_FEEDBACK_PACKET_ID, (client, handler, buf, responseSender) -> {
            
            double mobX = buf.readDouble();
            double mobY = buf.readDouble();
            double mobZ = buf.readDouble();

            client.execute(() -> {
                CaptureEnchantment.provideFeedback(client.player, mobX, mobY, mobZ);
            });
        });

        // register the combustion apply receiver
        ClientPlayNetworking.registerGlobalReceiver(SET_IGNITED_PACKET_ID, (client, handler, buf, responseSender) -> {
            
            int entityId = buf.readInt();
            boolean ignited = buf.readBoolean();
            
            client.execute(() -> {
                LivingEntity entity = (LivingEntity) ((World) client.world).getEntityById(entityId);
                ((LivingEntityAccess) entity).setIgnited(ignited);
            });
        });
    }
    
}
