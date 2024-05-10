package top.infsky.cheatdetector.utils;

import lombok.Getter;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.NotNull;

public class TRSelf extends TRPlayer {
    @Getter
    private static TRSelf instance = null;
    public LocalPlayer fabricPlayer;
    public Vec2 rotation;
    public boolean lastLeftPressed = false;
    public boolean currentLeftPressed = false;

    public TRSelf(@NotNull LocalPlayer player) {
        super(player, true);
        this.fabricPlayer = player;
        instance = this;
    }

    @Override
    public void update(AbstractClientPlayer player) {
        if (player instanceof LocalPlayer localPlayer) {
            this.fabricPlayer = localPlayer;
        } else throw new RuntimeException("Trying update TRSelf with non-local player!");
        lastLeftPressed = currentLeftPressed;
        currentLeftPressed = CLIENT.mouseHandler.isLeftPressed();

        super.update(player);
    }

    public static void onDisconnect() {
        instance = null;
    }
}
