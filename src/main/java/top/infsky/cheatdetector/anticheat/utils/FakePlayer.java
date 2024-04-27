package top.infsky.cheatdetector.anticheat.utils;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.anticheat.TRPlayer;
import top.infsky.cheatdetector.mixins.LookAtPacketInvoker;
import top.infsky.cheatdetector.utils.LogUtils;

public class FakePlayer {
    @NotNull
    public Player player;

    public FakePlayer(Level level, BlockPos position, GameProfile gameProfile) {
        this.player = new Player(level, position, 0, gameProfile) {
            @Override
            public boolean isSpectator() {
                return false;
            }

            @Override
            public boolean isCreative() {
                return false;
            }
        };
        LogUtils.custom("create fakePlayer");
    }

    public void show() {
        if (TRPlayer.CLIENT.getConnection() == null) return;
        TRPlayer.CLIENT.getConnection().handleAddPlayer(new ClientboundAddPlayerPacket(player));
    }

    public void hide() {
        if (TRPlayer.CLIENT.getConnection() == null) return;
        TRPlayer.CLIENT.getConnection().handleRemoveEntities(new ClientboundRemoveEntitiesPacket(player.getId()));
    }

    public void handlePacket(Packet<?> basePacket) {
        final ClientPacketListener connection = TRPlayer.CLIENT.getConnection();
        if (connection == null) return;

        if (basePacket instanceof ClientboundPlayerPositionPacket packet)
            connection.handleMovePlayer(new ClientboundPlayerPositionPacket(
                    packet.getX(), packet.getY(), packet.getZ(), packet.getYRot(), packet.getXRot(), packet.getRelativeArguments(), packet.getId()));
        else if (basePacket instanceof ClientboundPlayerLookAtPacket packet) {
            LookAtPacketInvoker packet1 = (LookAtPacketInvoker) packet;
            connection.handleLookAt(new ClientboundPlayerLookAtPacket(packet1.getFromAnchor(), packet1.getX(), packet1.getY(), packet1.getZ()));
        } else if (basePacket instanceof ClientboundDamageEventPacket packet)
            connection.handleDamageEvent(new ClientboundDamageEventPacket(player, packet.getSource(player.level())));
        else if (basePacket instanceof ClientboundAnimatePacket packet)
            connection.handleAnimate(new ClientboundAnimatePacket(player, packet.getAction()));
        else if (basePacket instanceof ClientboundHurtAnimationPacket)
            connection.handleHurtAnimation(new ClientboundHurtAnimationPacket(player));
        else if (basePacket instanceof ClientboundSetEntityMotionPacket packet)
            connection.handleSetEntityMotion(new ClientboundSetEntityMotionPacket(player.getId(), new Vec3(packet.getXa() * 8000, packet.getYa() * 8000, packet.getZa() * 8000)));
    }
}
