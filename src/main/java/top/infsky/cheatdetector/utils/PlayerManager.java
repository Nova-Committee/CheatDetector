package top.infsky.cheatdetector.utils;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.impl.modules.common.AntiBot;
import top.infsky.cheatdetector.impl.utils.world.LevelUtils;

import java.util.*;

@Getter
public class PlayerManager {
    public HashMap<UUID, Boolean> activeMap;  // 实时活动玩家(可被检查)列表

    public HashMap<UUID, TRPlayer> dataMap;  // 玩家数据列表

    public PlayerManager() {
        activeMap = new HashMap<>();
        dataMap = new HashMap<>();
    }

    public void update(@NotNull Minecraft client) {
        if (client.level == null || client.player == null) return;
        activeMap.forEach((uuid, aBoolean) -> activeMap.replace(uuid, false));

        // 遍历活动玩家
        try {
            for (AbstractClientPlayer player : LevelUtils.getClientLevel().players()) {
                final UUID uuid = player.getUUID();
                if (AntiBot.getBotList().contains(uuid)) {
                    activeMap.remove(uuid);
                    continue;
                }

                if (!activeMap.containsKey(uuid)) {
                    final TRPlayer trPlayer;
                    if (client.player.equals(player)) {
                        trPlayer = new TRSelf(client.player);
                    } else {
                        trPlayer = TRPlayer.create(player);
                    }
                    activeMap.put(uuid, true);
                    dataMap.put(uuid, trPlayer);
                }

                // 更新
                activeMap.replace(uuid, true);
                try {
                    dataMap.get(uuid).update(player);
                } catch (Exception e) {
                    LogUtils.custom("Exception in console.");
                    LogUtils.LOGGER.error(String.format("遇到了异常，丢弃玩家 %s 数据。", player.getName().getString()), e);
                    activeMap.remove(uuid);
                }
            }
        } catch (ConcurrentModificationException e) {
            LogUtils.custom(e.getLocalizedMessage());
        }
    }
}
