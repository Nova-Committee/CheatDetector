package top.infsky.cheatdetector.utils;

import net.minecraft.network.chat.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.infsky.cheatdetector.CheatDetector;

import static top.infsky.cheatdetector.CheatDetector.CONFIG;

public class LogUtils {
    public static final Logger LOGGER = LoggerFactory.getLogger("CheatDetector");

    public static void alert(String player, String module, String extraMsg) {
        if (!CONFIG().isAllowAlert()) return;
        LOGGER.info(String.format("TR> %s %s %s | %s", player, Component.translatable("text.cheatdetector.alert.fail").getString(), module, extraMsg));
        if (CheatDetector.CLIENT.player != null) {
            CheatDetector.CLIENT.player.sendSystemMessage(Component.literal(
                    String.format("§b§lTR§r§l> §r%s§r %s§r %s§r | %s§r", player, Component.translatable("text.cheatdetector.alert.fail").getString(), module, extraMsg)
            ));
        }
    }
}
