package top.infsky.cheatdetector.impl.modules.common;

import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.infsky.cheatdetector.CheatDetector;
import top.infsky.cheatdetector.impl.Module;
import top.infsky.cheatdetector.utils.TRPlayer;
import top.infsky.cheatdetector.utils.TRSelf;
import top.infsky.cheatdetector.config.ModuleConfig;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class AntiVanish extends Module {
    @Getter
    @Nullable
    private static Module instance = null;

    private final Map<String, Integer> playerLeaveCache = new HashMap<>();

    private final List<String> currentVanish = new LinkedList<>();
    public AntiVanish(@NotNull TRSelf player) {
        super("AntiVanish", player);
        instance = this;
    }

    @Override
    public boolean _onPacketReceive(@NotNull Packet<ClientGamePacketListener> basePacket, Connection connection, ChannelHandlerContext channelHandlerContext, CallbackInfo ci) {
        if (isDisabled()) return false;
        if (basePacket instanceof ClientboundPlayerInfoRemovePacket packet) {
            packet.profileIds().forEach(uuid -> getName(uuid).ifPresentOrElse(name -> {
                if (currentVanish.contains(name)) {
                    currentVanish.remove(name);
                    customMsg(Component.translatable("cheatdetector.chat.alert.stopVanish").getString().formatted(name));
                }

                if (playerLeaveCache.containsKey(name)) {
                    playerLeaveCache.remove(name);
                } else {
                    playerLeaveCache.put(name, 1);
                    player.timeTask.schedule(() -> {
                        if (playerLeaveCache.getOrDefault(name, 1) < 2) {
                            customMsg(Component.translatable("cheatdetector.chat.alert.startVanish").getString().formatted(name));
                            currentVanish.add(name);
                        }
                        playerLeaveCache.remove(name);
                    }, 150, TimeUnit.MILLISECONDS);
                }
            }, () -> customMsg("receive invalid player leave packet: %s".formatted(uuid))));
        }
        if (basePacket instanceof ClientboundSystemChatPacket packet && packet.content().getString().endsWith("退出了游戏")) {
            String msg = packet.content().getString();
            String name = msg.substring(0, msg.length() - 5);
            playerLeaveCache.put(name, playerLeaveCache.getOrDefault(name, 1) + 1);
        }
        return false;
    }

    private @NotNull Optional<String> getName(UUID uuid) {
        TRPlayer trPlayer = CheatDetector.manager.getDataMap().getOrDefault(uuid, null);
        if (trPlayer != null) {
            return Optional.of(trPlayer.fabricPlayer.getGameProfile().getName());
        }

        PlayerInfo playerInfo = player.fabricPlayer.connection.getPlayerInfo(uuid);
        if (playerInfo != null) {
            return Optional.of(playerInfo.getProfile().getName());
        }

        return Optional.empty();
    }

    @Override
    public boolean isDisabled() {
        return !ModuleConfig.antiVanishEnabled;
    }
}
