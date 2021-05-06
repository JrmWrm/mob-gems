package me.jrm_wrm.mob_gems.registry;

import me.jrm_wrm.mob_gems.MobGems;
import net.minecraft.util.Identifier;

public interface ModPackets {
    
    // Packet ID's
    public static final Identifier CAPTURE_FEEDBACK_PACKET_ID = new Identifier(MobGems.MOD_ID, "capture_feedback");
    public static final Identifier SET_IGNITED_PACKET_ID = new Identifier(MobGems.MOD_ID, "set_ignited");

}
