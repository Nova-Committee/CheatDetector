package top.infsky.cheatdetector.impl.modules.common;

import com.mojang.authlib.GameProfile;
import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import lombok.SneakyThrows;
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

import javax.naming.NameNotFoundException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class AntiVanish extends Module {
    @Getter
    @Nullable
    private static Module instance = null;

    private final Map<GameProfile, Integer> playerLeaveCache = new HashMap<>();
    public AntiVanish(@NotNull TRSelf player) {
        super("AntiVanish", player);
        instance = this;
    }

    @SneakyThrows
    @Override
    public boolean _onPacketReceive(@NotNull Packet<ClientGamePacketListener> basePacket, Connection connection, ChannelHandlerContext channelHandlerContext, CallbackInfo ci) {
        if (isDisabled()) return false;
        if (basePacket instanceof ClientboundPlayerInfoRemovePacket packet) {
            packet.profileIds().forEach(uuid -> {
                GameProfile profile = getProfile(uuid);
                if (playerLeaveCache.containsKey(profile)) {
                    playerLeaveCache.remove(profile);
                } else {
                    playerLeaveCache.put(profile, 1);
                    player.timeTask.schedule(() -> {
                        if (playerLeaveCache.getOrDefault(profile, 2) < 2) {
                            customMsg(Component.translatable("cheatdetector.chat.alert.foundVanish").append(profile.getName()).getString());
                        }
                        playerLeaveCache.remove(profile);
                    }, player.getLatency() + 50, TimeUnit.MILLISECONDS);
                }
            });
        }
        if (basePacket instanceof ClientboundSystemChatPacket packet && packet.content().getString().endsWith("退出了游戏")) {
            String msg = packet.content().getString();
            GameProfile profile = getProfile(msg.substring(0, msg.length() - 6));
            if (profile != null) {
                playerLeaveCache.put(profile, playerLeaveCache.getOrDefault(profile, 0));

                if (playerLeaveCache.containsKey(profile)) {
                    playerLeaveCache.remove(profile);
                } else {
                    playerLeaveCache.put(profile, 1);
                    player.timeTask.schedule(() -> {
                        if (playerLeaveCache.getOrDefault(profile, 2) < 2) {
                            customMsg(Component.translatable("cheatdetector.chat.alert.foundVanish").append(profile.getName()).getString());
                        }
                        playerLeaveCache.remove(profile);
                    }, player.getLatency() + 50, TimeUnit.MILLISECONDS);
                }
            } else {
                throw new NameNotFoundException("receive invalid player leave msg: %s".formatted(msg));
            }
        }
        return false;
    }

    private @NotNull GameProfile getProfile(UUID uuid) {
        TRPlayer trPlayer = CheatDetector.manager.getDataMap().getOrDefault(uuid, null);
        if (trPlayer != null) {
            return trPlayer.fabricPlayer.getGameProfile();
        }

        PlayerInfo playerInfo = player.fabricPlayer.connection.getPlayerInfo(uuid);
        if (playerInfo != null) {
            return playerInfo.getProfile();
        }

        return new GameProfile(uuid, uuid.toString());
    }

    private @Nullable GameProfile getProfile(String name) {
        for (TRPlayer trPlayer : CheatDetector.manager.getDataMap().values()) {
            if (trPlayer.fabricPlayer.getName().getString().equals(name)) {
                return trPlayer.fabricPlayer.getGameProfile();
            }
        }


        PlayerInfo playerInfo = player.fabricPlayer.connection.getPlayerInfo(name);
        if (playerInfo != null) {
            return playerInfo.getProfile();
        }

        return null;
    }

    @Override
    public boolean isDisabled() {
        return !ModuleConfig.antiVanishEnabled;
    }
}
