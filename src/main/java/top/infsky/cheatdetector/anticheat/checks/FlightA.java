package top.infsky.cheatdetector.anticheat.checks;

import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.phys.Vec3;
import top.infsky.cheatdetector.anticheat.Check;
import top.infsky.cheatdetector.anticheat.TRPlayer;

import static top.infsky.cheatdetector.CheatDetector.CONFIG;

public class FlightA extends Check {
    public short jumpTick = 0;
    public short liquidTick = 0;
    public short disableTick = 0;

    public Vec3 setbackPos;

    public FlightA(TRPlayer player) {
        super("FlightA", player);
        setbackPos = player.currentPos;
    }

    @Override
    public void _onTick() {
        if (player.fabricPlayer.getUseItem().getItem() instanceof TridentItem) return;
        if (player.fabricPlayer.isPassenger()) return;
        if (player.fabricPlayer.getInventory().getArmor(1).getItem() instanceof ElytraItem) return;

        if (disableTick > 0) {
            disableTick--;
            return;
        }

        if (player.fabricPlayer.onGround()) {
            jumpTick = 1;  // MC原版OnGround不可靠。方块边缘会误判。
            setbackPos = player.lastOnGroundPos;
        }

        // fix liquid
        if (player.fabricPlayer.isInWater() || player.fabricPlayer.isInLava()) {
            liquidTick = 8;
            setbackPos = player.lastInLiquidPos;
        }

        // fix hurt
        if (player.fabricPlayer.hurtTime > 0) {
            jumpTick = 6;
            setbackPos = player.currentPos;
        }


        if (!player.fabricPlayer.onGround() && jumpTick > 0
                && player.currentPos.y() - player.lastOnGroundPos.y() < 1.25219 * (1 + player.fabricPlayer.getJumpBoostPower()) + CONFIG().getThreshold()
//                && player.currentPos.distanceTo(player.lastPos) < 5.612 * (1 + player.fabricPlayer.getSpeed()) + CONFIG().getThreshold()  // 警惕跳跃弱检测
        ) {
            jumpTick--;
        } else if ((!player.fabricPlayer.isInWater() || !player.fabricPlayer.isInLava()) && liquidTick > 0
                && player.currentPos.y() - player.lastInLiquidPos.y() < 0.5 + CONFIG().getThreshold()  // 瞎写的0.5
//                && (lastPos.y() - lastPos2.y() + CONFIG().getThreshold()) > (player.position().y() - lastPos.y())  // 警惕出水弱检测
        ) {
            liquidTick--;
        } else if (player.posHistory.get(1) != null && !player.fabricPlayer.onGround() && (!player.fabricPlayer.isInWater() && !player.fabricPlayer.isInLava())) {
            jumpTick = 0;
            liquidTick = 0;
            if (player.lastPos.y() - player.currentPos.y() < CONFIG().getThreshold() &&
                    player.posHistory.get(1).y() - player.lastPos.y() <= CONFIG().getThreshold()) {
                flag();
            }
        }
    }

    @Override
    public void _onTeleport() {
        disableTick = 2;
    }

    @Override
    public void _onJump() {
        // fix jump
        jumpTick = 14;
    }
}
