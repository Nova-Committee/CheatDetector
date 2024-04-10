package top.infsky.cheatdetector.anticheat.checks;

import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.TridentItem;
import top.infsky.cheatdetector.anticheat.Check;
import top.infsky.cheatdetector.anticheat.TRPlayer;

import static top.infsky.cheatdetector.CheatDetector.CONFIG;

public class FlightA extends Check {
    public short jumpTick = 0;
    public short liquidTick = 0;
    public short disableTick = 0;

    public FlightA(TRPlayer player) {
        super("FlightA", player);
    }

    @Override
    public void _onTick() {
        if (player.fabricPlayer.getMainHandItem().getItem() instanceof TridentItem
                || player.fabricPlayer.getOffhandItem().getItem() instanceof TridentItem) return;
        if (player.fabricPlayer.isPassenger()) return;
        if (ElytraItem.isFlyEnabled(player.fabricPlayer.getInventory().getArmor(2))) return;

        if (disableTick > 0) {
            disableTick--;
            return;
        }

        if (player.fabricPlayer.onGround()) {
            jumpTick = CONFIG().getAdvanced().getFlightAOnGroundJumpTick();  // MC原版OnGround不可靠。方块边缘会误判。
        }

        // fix liquid
        if (player.fabricPlayer.isInWater() || player.fabricPlayer.isInLava()) {
            liquidTick = CONFIG().getAdvanced().getFlightAInLiquidLiquidTick();
        }

        // fix hurt
        if (player.fabricPlayer.hurtTime > 0) {
            jumpTick = CONFIG().getAdvanced().getFlightAInHurtJumpTick();
        }


        if (!player.fabricPlayer.onGround() && jumpTick > 0
                && player.currentPos.y() - player.lastOnGroundPos.y()
                < CONFIG().getAdvanced().getFlightAJumpDistance() * (1 + player.fabricPlayer.getJumpBoostPower()) + CONFIG().getAntiCheat().getThreshold()
//                && player.currentPos.distanceTo(player.lastPos) < 5.612 * (1 + player.fabricPlayer.getSpeed()) + CONFIG().getThreshold()  // 警惕跳跃弱检测
        ) {
            jumpTick--;
        } else if ((!player.fabricPlayer.isInWater() || !player.fabricPlayer.isInLava()) && liquidTick > 0
                && player.currentPos.y() - player.lastInLiquidPos.y()
                < CONFIG().getAdvanced().getFlightAFromWaterYDistance() + CONFIG().getAntiCheat().getThreshold()  // 瞎写的0.5 (getFlightAFromWaterYDistance)
//                && (lastPos.y() - lastPos2.y() + CONFIG().getThreshold()) > (player.position().y() - lastPos.y())  // 警惕出水弱检测
        ) {
            liquidTick--;
        } else if (player.posHistory.get(1) != null && !player.fabricPlayer.onGround() && (!player.fabricPlayer.isInWater() && !player.fabricPlayer.isInLava())) {
            jumpTick = 0;
            liquidTick = 0;
            if (player.lastPos.y() - player.currentPos.y() < CONFIG().getAntiCheat().getThreshold() &&
                    player.posHistory.get(1).y() - player.lastPos.y() <= CONFIG().getAntiCheat().getThreshold()) {
                flag();
            }
        }
    }

    @Override
    public void _onTeleport() {
        disableTick = CONFIG().getAdvanced().getFlightAOnTeleportDisableTick();
    }

    @Override
    public void _onJump() {
        // fix jump
        jumpTick = CONFIG().getAdvanced().getFlightAOnJumpJumpTick();
    }

    @Override
    protected long getAlertBuffer() {
        return CONFIG().getAdvanced().getFlightAAlertBuffer();
    }

    @Override
    protected boolean isDisabled() {
        return !CONFIG().getAdvanced().isFlightACheck();
    }
}
