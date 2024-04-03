package top.infsky.cheatdetector;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import top.infsky.cheatdetector.config.ModConfig;
import top.infsky.cheatdetector.utils.PlayerManager;

public class CheatDetector implements ClientModInitializer {
    public static final String MOD_ID = "cheatdetector";
    public static Minecraft CLIENT = null;
    public static ModConfig CONFIG() { return AutoConfig.getConfigHolder(ModConfig.class).getConfig(); }
    public static PlayerManager manager = null;
    public static boolean inWorld = false;

    @Override
    public void onInitializeClient() {
        // config
        AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);

        ClientLifecycleEvents.CLIENT_STARTED.register(this::onClientStarted);
        ClientPlayConnectionEvents.JOIN.register(this::onJoinWorld);
        ClientPlayConnectionEvents.DISCONNECT.register(this::onDisconnect);
        ClientTickEvents.END_WORLD_TICK.register(this::onTick);
    }

    private void onClientStarted(Minecraft minecraft) {
        CLIENT = minecraft;
    }

    private void onJoinWorld(ClientPacketListener clientPacketListener, PacketSender packetSender, Minecraft minecraft) {
        CLIENT = minecraft;
        inWorld = true;
        manager = null;
        manager = new PlayerManager();
    }

    private void onDisconnect(ClientPacketListener clientPacketListener, Minecraft minecraft) {
        CLIENT = minecraft;
        inWorld = false;
        manager = null;
    }

    private void onTick(ClientLevel clientLevel) {
        if (inWorld && CONFIG().isEnabled()) manager.update(CLIENT);
    }
}
