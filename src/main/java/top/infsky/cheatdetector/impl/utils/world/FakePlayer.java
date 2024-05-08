package top.infsky.cheatdetector.impl.utils.world;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import top.infsky.cheatdetector.utils.TRPlayer;
import top.infsky.cheatdetector.mixins.LookAtPacketInvoker;
import top.infsky.cheatdetector.utils.LogUtils;

import java.util.UUID;

public class FakePlayer extends RemotePlayer {
    private boolean isRemoved = true;

    public FakePlayer(Player player) {
        super(LevelUtils.getClientLevel(), new GameProfile(UUID.randomUUID(), player.getName().getString()));

        copyPosition(player);

        yRotO = getYRot();
        xRotO = getXRot();
        yHeadRot = player.yHeadRot;
        yHeadRotO = yHeadRot;
        yBodyRot = player.yBodyRot;
        yBodyRotO = yBodyRot;

        Byte playerModel = player.getEntityData().get(Player.DATA_PLAYER_MODE_CUSTOMISATION);
        entityData.set(Player.DATA_PLAYER_MODE_CUSTOMISATION, playerModel);

        getAttributes().assignValues(EntityUtils.getAttributes(player));
        setPose(player.getPose());

        xCloak = getX();
        yCloak = getY();
        zCloak = getZ();

        if (getHealth() <= 20) {
            setHealth(getHealth());
        } else {
            setHealth(getHealth());
            setAbsorptionAmount(getHealth() - 20);
        }

        getInventory().replaceWith(player.getInventory());

        LogUtils.custom("create fakePlayer");
    }

    public void show() {
        if (!isRemoved) return;
        LevelUtils.getClientLevel().addEntity(this);
        isRemoved = false;
    }

    public void hide() {
        if (isRemoved) return;
        LevelUtils.getClientLevel().removeEntity(getId(), RemovalReason.DISCARDED);
        isRemoved = true;
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
            connection.handleDamageEvent(new ClientboundDamageEventPacket(this, packet.getSource(this.level())));
        else if (basePacket instanceof ClientboundAnimatePacket packet)
            connection.handleAnimate(new ClientboundAnimatePacket(this, packet.getAction()));
        else if (basePacket instanceof ClientboundHurtAnimationPacket)
            connection.handleHurtAnimation(new ClientboundHurtAnimationPacket(this));
        else if (basePacket instanceof ClientboundSetEntityMotionPacket packet)
            connection.handleSetEntityMotion(new ClientboundSetEntityMotionPacket(this.getId(), new Vec3(packet.getXa() * 8000, packet.getYa() * 8000, packet.getZa() * 8000)));
    }
}
