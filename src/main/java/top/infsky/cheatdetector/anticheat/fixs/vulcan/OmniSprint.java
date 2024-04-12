package top.infsky.cheatdetector.anticheat.fixs.vulcan;

import net.minecraft.ChatFormatting;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.anticheat.Fix;
import top.infsky.cheatdetector.anticheat.TRPlayer;

import static top.infsky.cheatdetector.CheatDetector.CONFIG;

public class OmniSprint extends Fix {

    public OmniSprint(@NotNull TRPlayer player) {
        super("Omni-Sprint", player);
    }

    @Override
    public void _onTick() {
        if (TRPlayer.CLIENT.getConnection() == null) return;
        if (player.lastPos == player.currentPos) return;

        // copy from rise client :D
        if (CONFIG().getFixes().isVulcanOmniSprintEnabled()) {
            TRPlayer.CLIENT.getConnection().send(
                    new ServerboundPlayerCommandPacket(
                            player.fabricPlayer, ServerboundPlayerCommandPacket.Action.START_SPRINTING
                    )
            );
            TRPlayer.CLIENT.getConnection().send(
                    new ServerboundPlayerCommandPacket(
                            player.fabricPlayer, ServerboundPlayerCommandPacket.Action.STOP_SPRINTING
                    )
            );

            if (CONFIG().getAdvanced2().isOmniSprintShowPacketSend())
                moduleMsg(ChatFormatting.GRAY + "send packet.");
        }
    }
}
