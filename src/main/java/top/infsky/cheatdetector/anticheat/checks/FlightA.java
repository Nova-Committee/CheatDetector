package top.infsky.cheatdetector.anticheat.checks;

import net.minecraft.world.item.TridentItem;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.anticheat.Check;
import top.infsky.cheatdetector.anticheat.TRPlayer;
import top.infsky.cheatdetector.anticheat.utils.PlayerMove;
import top.infsky.cheatdetector.config.AdvancedConfig;
import top.infsky.cheatdetector.config.AntiCheatConfig;

public class FlightA extends Check {
    public short jumpTick = 0;
    public short liquidTick = 0;
    public short disableTick = 0;

    public FlightA(@NotNull TRPlayer player) {
        super("FlightA", player);
    }

    @Override
    public void _onTick() {
        if (player.fabricPlayer.getMainHandItem().getItem() instanceof TridentItem
                || player.fabricPlayer.getOffhandItem().getItem() instanceof TridentItem) return;
        if (player.fabricPlayer.isPassenger()) return;
        if (player.fabricPlayer.isFallFlying()) {
            jumpTick = Short.MAX_VALUE;
            return;
        }

        if (disableTick > 0) {
            disableTick--;
            return;
        }

        if (player.currentOnGround) {
            jumpTick = AdvancedConfig.getFlightAOnGroundJumpTick();  // MC原版OnGround不可靠。方块边缘会误判。
        }

        // fix liquid
        if (player.fabricPlayer.isInWater() || player.fabricPlayer.isInLava()) {
            liquidTick = AdvancedConfig.getFlightAInLiquidLiquidTick();
        }

        // fix hurt
        if (player.fabricPlayer.hurtTime > 0) {
            jumpTick = AdvancedConfig.getFlightAInHurtJumpTick();
        }


        if (!player.currentOnGround && jumpTick > 0
                && player.currentPos.y() - player.lastOnGroundPos.y()
                < PlayerMove.getJumpDistance(player.fabricPlayer) + AntiCheatConfig.threshold
//                && player.currentPos.distanceTo(player.lastPos) < 5.612 *
//                (1 + player.fabricPlayer.getSpeed()) + ModConfig.getThreshold()  // 警惕跳跃弱检测
        ) {
            jumpTick--;
        } else if ((!player.fabricPlayer.isInWater() || !player.fabricPlayer.isInLava()) && liquidTick > 0
                && player.currentPos.y() - player.lastInLiquidPos.y()
                < AdvancedConfig.flightAFromWaterYDistance + AntiCheatConfig.threshold  // 瞎写的0.5 (getFlightAFromWaterYDistance)
//                && (lastPos.y() - lastPos2.y() + ModConfig.getThreshold()) > (player.position().y() - lastPos.y())  // 警惕出水弱检测
        ) {
            liquidTick--;
        } else if (player.posHistory.get(1) != null && !player.currentOnGround && (!player.fabricPlayer.isInWater() && !player.fabricPlayer.isInLava())) {
            jumpTick = 0;
            liquidTick = 0;
            if (player.lastPos.y() - player.currentPos.y() < AntiCheatConfig.threshold &&
                    player.posHistory.get(1).y() - player.lastPos.y() <= AntiCheatConfig.threshold) {
                flag();
            }
        }
    }

    @Override
    public void _onTeleport() {
        disableTick = AdvancedConfig.getFlightAOnTeleportDisableTick();
    }

    @Override
    public void _onJump() {
        // fix jump
        jumpTick = AdvancedConfig.getFlightAOnJumpJumpTick();
    }

    @Override
    protected int getAlertBuffer() {
        return AdvancedConfig.flightAAlertBuffer;
    }

    @Override
    protected boolean isDisabled() {
        return !AdvancedConfig.flightACheck;
    }
}
