package top.infsky.cheatdetector.impl.modules.pas;

import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.infsky.cheatdetector.config.Advanced3Config;
import top.infsky.cheatdetector.config.ModuleConfig;
import top.infsky.cheatdetector.impl.Module;
import top.infsky.cheatdetector.utils.TRSelf;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class AntiBot extends Module {
    @Getter
    @Nullable
    private static Module instance = null;

    private static final String botJoinMsg = " joined the game";
    private static final String botLeftMsg = " left the game";

    @Getter
    private static List<UUID> botList = new LinkedList<>();

    public AntiBot(@NotNull TRSelf player) {
        super("AntiBot", player);
        botList = new LinkedList<>();
        instance = this;
    }

    @Override
    public void _onTick() {
        if (isDisabled()) {
            botList.clear();
        }
    }

    @Override
    public boolean _onPacketReceive(@NotNull Packet<ClientGamePacketListener> basePacket, Connection connection, ChannelHandlerContext channelHandlerContext, CallbackInfo ci) {
        if (isDisabled()) return false;

        try {
            if (Advanced3Config.antiBotMessage && basePacket instanceof ClientboundSystemChatPacket packet) {
                String msg = packet.content().getString();
                try {
                    if (msg.endsWith(botJoinMsg)) {
                        String name = msg.substring(0, msg.length() - botJoinMsg.length());
                        addBotVisual(player.fabricPlayer.connection.getPlayerInfo(name));
                    } else if (msg.endsWith(botLeftMsg)) {
                        String name = msg.substring(0, msg.length() - botLeftMsg.length());
                        removeBotVisual(player.fabricPlayer.connection.getPlayerInfo(name));
                    }
                } catch (NullPointerException e) {
                    customMsg("receive invalid player join msg: %s".formatted(msg));
                }
            }
            if (Advanced3Config.antiBotLatency && basePacket instanceof ClientboundPlayerInfoUpdatePacket packet) {
                if (packet.actions().stream().allMatch(ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LATENCY::equals)) {
                    List<PlayerInfo> players = new LinkedList<>(player.fabricPlayer.connection.getOnlinePlayers());
                    packet.entries().forEach(entry -> {
                        try {
                            PlayerInfo playerInfo = Objects.requireNonNull(player.fabricPlayer.connection.getPlayerInfo(entry.profileId()));
                            players.remove(playerInfo);
                            removeBotVisual(playerInfo);
                        } catch (NullPointerException ignored) {
                        }
                    });

                    players.forEach(this::addBotVisual);
                }
            }
        } catch (Exception ignored) {}  // 我不知道为什么，但是bug修复了！
        return false;
    }

    private void addBotVisual(PlayerInfo playerInfo) {
        if (playerInfo == null) return;
        if (botList.contains(playerInfo.getProfile().getId())) return;
        player.timeTask.schedule(() -> {
            if (getBotList().contains(playerInfo.getProfile().getId())) return;
            getBotList().add(playerInfo.getProfile().getId());
            if (Advanced3Config.antiBotDebug) {
                customMsg("remove bot :)");
            }
        }, player.getLatency() + 150, TimeUnit.MILLISECONDS);
    }

    private void removeBotVisual(PlayerInfo playerInfo) {
        if (playerInfo == null) return;
        if (!botList.contains(playerInfo.getProfile().getId())) return;
        player.timeTask.schedule(() -> {
            if (getBotList().contains(playerInfo.getProfile().getId())) return;
            getBotList().remove(playerInfo.getProfile().getId());
        }, player.getLatency() + 150, TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean isDisabled() {
        return !ModuleConfig.antiBotEnabled || !ModuleConfig.aaaPASModeEnabled;
    }
}
