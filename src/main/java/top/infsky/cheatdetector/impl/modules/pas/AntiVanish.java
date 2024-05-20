package top.infsky.cheatdetector.impl.modules.pas;

import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.infsky.cheatdetector.impl.Module;
import top.infsky.cheatdetector.impl.utils.packet.NetUtils;
import top.infsky.cheatdetector.impl.utils.world.EntityUtils;
import top.infsky.cheatdetector.utils.TRSelf;
import top.infsky.cheatdetector.config.ModuleConfig;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class AntiVanish extends Module {
    @Getter
    @Nullable
    private static Module instance = null;

    public static final List<String> possiblePlayerLeaveMsg = List.of(
            "退出了游戏",
            " left the game"
    );

    private final Map<String, Integer> playerLeaveCache = new HashMap<>();

    private final List<String> currentVanish = new LinkedList<>();
    public AntiVanish(@NotNull TRSelf player) {
        super("AntiVanish", player);
        instance = this;
    }

    @Override
    public boolean _onPacketReceive(@NotNull Packet<ClientGamePacketListener> basePacket, Connection connection, ChannelHandlerContext channelHandlerContext, CallbackInfo ci) {
        if (isDisabled()) return false;
        if (basePacket instanceof ClientboundPlayerInfoUpdatePacket packet) {
            packet.newEntries().stream()
                    .map(entry -> entry.profile().getName())
                    .forEach(this::onPlayerJoin);
        }
        if (basePacket instanceof ClientboundPlayerInfoRemovePacket packet) {
            packet.profileIds().forEach(uuid -> EntityUtils.getName(uuid).ifPresentOrElse(
                    this::onPlayerLeave,
                    () -> player.timeTask.execute(() -> onPlayerLeave(NetUtils.get("https://api.mojang.com/user/profile/%s".formatted(uuid), "name")))));
        }
        if (basePacket instanceof ClientboundSystemChatPacket packet) {
            for (String end : possiblePlayerLeaveMsg) {
                final String msg = packet.content().getString();
                if (msg.endsWith(end)) {
                    final String name = msg.substring(0, msg.length() - end.length());
                    playerLeaveCache.put(name, playerLeaveCache.getOrDefault(name, 1) + 1);
                    currentVanish.remove(name);
                    break;
                }
            }
        }
        return false;
    }

    private void onPlayerLeave(String name) {
        if (name == null) return;
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
    }

    private void onPlayerJoin(String name) {
        if (name == null) return;
        if (currentVanish.contains(name)) {
            currentVanish.remove(name);
            customMsg(Component.translatable("cheatdetector.chat.alert.stopVanish").getString().formatted(name));
        }
    }

    @Override
    public boolean isDisabled() {
        return !ModuleConfig.antiVanishEnabled || !ModuleConfig.aaaPASModeEnabled;
    }
}
