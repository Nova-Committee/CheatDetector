package top.infsky.cheatdetector.compat;

import com.replaymod.recording.packet.PacketListener;
import net.minecraft.network.PacketSendListener;

public class ReplaymodHelper {
    public static boolean isFromReplayMod(PacketSendListener listener) {
        return listener instanceof PacketListener;
    }
}
