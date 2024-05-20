package top.infsky.cheatdetector.impl.modules.pas;

import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class AntiBot extends Module {
    @Getter
    @Nullable
    private static Module instance = null;

    private static final String botJoinMsg = " joined the game";
    private static final String botLeftMsg = " joined the game";

    @Getter
    private static List<PlayerInfo> botList = new ArrayList<>();

    public AntiBot(@NotNull TRSelf player) {
        super("AntiBot", player);
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

        if (Advanced3Config.antiBotMessage && basePacket instanceof ClientboundSystemChatPacket packet) {
            String msg = packet.content().getString();
            try {
                if (msg.endsWith(botJoinMsg)) {
                    final String name = msg.substring(0, msg.length() - botJoinMsg.length());
                    addBotVisual(player.fabricPlayer.connection.getPlayerInfo(name));
                } else if (msg.endsWith(botLeftMsg)) {
                    final String name = msg.substring(0, msg.length() - botLeftMsg.length());
                    botList.remove(player.fabricPlayer.connection.getPlayerInfo(name));
                }
            } catch (NullPointerException e) {
                customMsg("receive invalid player join msg: %s".formatted(msg));
            }
        }
        if (Advanced3Config.antiBotLatency && basePacket instanceof ClientboundPlayerInfoUpdatePacket packet) {
            Collection<PlayerInfo> players = player.fabricPlayer.connection.getOnlinePlayers();
            packet.entries().forEach(entry -> {
                try {
                    PlayerInfo playerInfo = Objects.requireNonNull(player.fabricPlayer.connection.getPlayerInfo(entry.profileId()));
                    players.remove(playerInfo);
                    removeBotVisual(playerInfo);
                } catch (NullPointerException ignored) {}
            });

            players.forEach(this::addBotVisual);
        }
        return false;
    }

    private void addBotVisual(PlayerInfo playerInfo) {
        if (botList.contains(playerInfo)) return;
        player.timeTask.schedule(() -> {
            Component displayName = Objects.requireNonNullElse(playerInfo.getTabListDisplayName(), Component.literal(playerInfo.getProfile().getName())).copy();
            if (displayName.getString().startsWith(Component.translatable("cheatdetector.overlay.tab.bot").withStyle(ChatFormatting.AQUA).getString())) return;
            playerInfo.setTabListDisplayName(
                    Component.translatable("cheatdetector.overlay.tab.bot")
                            .withStyle(ChatFormatting.AQUA)
                            .append(displayName)
            );
            botList.add(playerInfo);
            if (Advanced3Config.antiBotDebug) {
                customMsg("remove bot :)");
            }
        }, player.getLatency() + 150, TimeUnit.MILLISECONDS);
    }

    private void removeBotVisual(PlayerInfo playerInfo) {
        if (!botList.contains(playerInfo)) return;
        player.timeTask.schedule(() -> {
            Component displayName = Objects.requireNonNullElse(playerInfo.getTabListDisplayName(), Component.literal(playerInfo.getProfile().getName())).copy();
            if (!displayName.getString().startsWith(Component.translatable("cheatdetector.overlay.tab.bot").withStyle(ChatFormatting.AQUA).getString())) return;
            playerInfo.setTabListDisplayName(Component.literal(
                    displayName.getString().substring(Component.translatable("cheatdetector.overlay.tab.bot").withStyle(ChatFormatting.AQUA).getString().length())
            ));
            botList.remove(playerInfo);
        }, player.getLatency() + 150, TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean isDisabled() {
        return !ModuleConfig.antiBotEnabled || !ModuleConfig.aaaPASModeEnabled;
    }
}
