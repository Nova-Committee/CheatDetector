package top.infsky.cheatdetector;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.malilib.impl.ConfigHandler;
import top.hendrixshen.magiclib.malilib.impl.ConfigManager;
import top.infsky.cheatdetector.utils.TRSelf;
import top.infsky.cheatdetector.impl.modules.ClickGUI;
import top.infsky.cheatdetector.config.ModuleConfig;
import top.infsky.cheatdetector.config.utils.ConfigPredicate;
import top.infsky.cheatdetector.utils.PlayerManager;

import java.util.Objects;

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
        ModuleConfig.clickGUIEnabled = false;
        ClickGUI.getConfigGui().addKeybindChangeListener(ClickGUI::update);
    }

    private void onClientStopping(Minecraft minecraft) {
        CONFIG_HANDLER.saveToFile();
    }

    private void onJoinWorld(ClientPacketListener clientPacketListener, PacketSender packetSender, @NotNull Minecraft minecraft) {
        CLIENT = minecraft;
        inWorld = true;
        manager = null;
        manager = new PlayerManager();
        try {
            String ip = Objects.requireNonNull(clientPacketListener.getServerData()).ip.split(":")[0];
            ConfigPredicate.onJoinWorld(ip, CONFIG_HANDLER.configManager);
        } catch (NullPointerException ignored) {}
    }

    private void onDisconnect(ClientPacketListener clientPacketListener, Minecraft minecraft) {
        CLIENT = minecraft;
        inWorld = false;
        manager = null;
        TRSelf.onDisconnect();
        CONFIG_HANDLER.configManager.setValue("clickGUIEnabled", false);
        CONFIG_HANDLER.configManager.setValue("noteBotEnabled", false);
        CONFIG_HANDLER.configManager.setValue("sayHackerEnabled", false);
    }

    private void onTick(ClientLevel clientLevel) {
        if (clientLevel != null && CLIENT.getCurrentServer() != null) {
            inWorld = true;
            if (manager == null)
                manager = new PlayerManager();
        } else {
            inWorld = false;
            manager = null;
            return;
        }

        if (inWorld) {
            manager.update(CLIENT);
        }
    }
}
