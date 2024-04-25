package top.infsky.cheatdetector;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import top.hendrixshen.magiclib.malilib.impl.ConfigHandler;
import top.hendrixshen.magiclib.malilib.impl.ConfigManager;
import top.infsky.cheatdetector.anticheat.TRSelf;
import top.infsky.cheatdetector.anticheat.modules.ClickGUI;
import top.infsky.cheatdetector.config.FixesConfig;
import top.infsky.cheatdetector.config.ModuleConfig;
import top.infsky.cheatdetector.utils.PlayerManager;

public class CheatDetector implements ClientModInitializer {
    public static final String MOD_ID = "cheatdetector";
    public static Minecraft CLIENT = null;
    public static PlayerManager manager = null;
    public static ConfigHandler CONFIG_HANDLER;
    public static boolean inWorld = false;

    @Override
    public void onInitializeClient() {
        // config
        final ConfigManager configManager = ConfigManager.get(MOD_ID);
        ClickGUI.register(configManager);
        CONFIG_HANDLER = new ConfigHandler(MOD_ID, configManager, 1);
        ConfigHandler.register(CONFIG_HANDLER);

        ClientLifecycleEvents.CLIENT_STARTED.register(this::onClientStarted);
        ClientLifecycleEvents.CLIENT_STOPPING.register(this::onClientStopping);
        ClientPlayConnectionEvents.JOIN.register(this::onJoinWorld);
        ClientPlayConnectionEvents.DISCONNECT.register(this::onDisconnect);
        ClientTickEvents.END_WORLD_TICK.register(this::onTick);
    }

    private void onClientStarted(Minecraft minecraft) {
        CLIENT = minecraft;
        ClickGUI.getInstance().addKeybindChangeListener(() -> ClickGUI.getInstance().reDraw());

    }

    private void onClientStopping(Minecraft minecraft) {
        ModuleConfig.clickGUIEnabled = false;
        FixesConfig.vulcanDisablerEnabled = false;
        CONFIG_HANDLER.saveToFile();
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
        TRSelf.onDisconnect();
    }

    private void onTick(ClientLevel clientLevel) {
//        CONFIG_HANDLER.load();
        if (inWorld && manager != null && CLIENT != null) {
            manager.update(CLIENT);
        }
//        CONFIG_HANDLER.save();
//        ClickGUI.getInstance().reDraw();
    }
}
