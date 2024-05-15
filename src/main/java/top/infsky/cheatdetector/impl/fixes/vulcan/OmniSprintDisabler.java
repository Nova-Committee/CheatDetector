package top.infsky.cheatdetector.impl.fixes.vulcan;

import net.minecraft.ChatFormatting;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.impl.Fix;
import top.infsky.cheatdetector.utils.TRPlayer;
import top.infsky.cheatdetector.utils.TRSelf;
import top.infsky.cheatdetector.config.Advanced2Config;
import top.infsky.cheatdetector.config.FixesConfig;

public class OmniSprintDisabler extends Fix {

    public OmniSprintDisabler(@NotNull TRSelf player) {
        super("Omni-Sprint", player);
    }

    @Override
    public void _onTick() {
        if (TRPlayer.CLIENT.getConnection() == null) return;
        if (isDisabled()) return;
        if (player == null) return;

        // copy from rise client :D
        if (player.lastPos != player.currentPos) {
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

            if (Advanced2Config.omniSprintShowPacketSend)
                moduleMsg(ChatFormatting.GRAY + "send packet.");
        }
    }

    @Override
    public boolean isDisabled() {
        return !FixesConfig.vulcanOmniSprintEnabled;
    }
}
