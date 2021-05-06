package me.jrm_wrm.mob_gems.registry;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;

public class ModServerNetworking {
    
    public static void registerServerReceivers() {
        
        // register receiver for setting the moving tag of a mob gem item stack
        ServerPlayNetworking.registerGlobalReceiver(ModPackets.SET_MOVING_TAG_PACKET_ID, (server, player, handler, buf, responseSender) -> {
            boolean moving = buf.readBoolean();
            int entityId = buf.readInt();

            server.execute(() -> {
                ModItems.BEE_MOB_GEM.receiveMovingPacket(moving, entityId, (PlayerEntity) player);
            });
        });

    }

}
