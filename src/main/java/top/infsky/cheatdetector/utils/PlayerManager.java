package top.infsky.cheatdetector.utils;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import org.jetbrains.annotations.NotNull;

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
            for (AbstractClientPlayer player : client.level.players()) {
                final UUID uuid = player.getUUID();
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
                    if (player.getUUID() == client.player.getUUID()) {
                        dataMap.get(uuid).update(player);
                    } else dataMap.get(uuid).update(player);
                } catch (NullPointerException e) {
                    LogUtils.LOGGER.error(String.format("玩家 %s 的数据不存在！丢弃玩家。", player.getName()), e);
                    activeMap.remove(uuid);
                }
            }
        } catch (ConcurrentModificationException e) {
            LogUtils.custom(e.getLocalizedMessage());
        }
    }
}
