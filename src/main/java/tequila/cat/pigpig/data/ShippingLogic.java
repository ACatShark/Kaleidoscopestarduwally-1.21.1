package tequila.cat.pigpig.data;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;

import tequila.cat.pigpig.attachment.LivestockData;
import tequila.cat.pigpig.attachment.ModAttachments;
import tequila.cat.pigpig.block.ShippingBinBlockEntity;
import tequila.cat.pigpig.component.BoundLivestock;
import tequila.cat.pigpig.component.ModDataComponents;

/**
 * 交易站售出结算核心。
 * 只接受"已签订且主人仍匹配"的契约；牲畜须在玩家附近且位于同维度。
 */
public final class ShippingLogic {
    private static final double MAX_DISTANCE_SQ = 32.0 * 32.0;

    /** @return true 表示成功完成一笔售出（此时应停止后续交互） */
    public static boolean sellAtStation(ServerLevel level, BlockPos stationPos, ServerPlayer player, ItemStack contract) {
        BoundLivestock bound = contract.get(ModDataComponents.BOUND_LIVESTOCK);
        if (bound == null) {
            return false;
        }

        // 1. 定位牲畜
        Entity entity = level.getEntity(bound.targetUuid());
        if (!(entity instanceof Animal animal) || entity.isRemoved()) {
            player.sendSystemMessage(Component.literal("\u00a7c牲畜不在附近或已消失，无法出售"));
            return false;
        }

        // 2. 距离 & 归属校验
        if (entity.distanceToSqr(player) > MAX_DISTANCE_SQ) {
            player.sendSystemMessage(Component.literal("\u00a7c牲畜离你太远了，请靠近后再出售"));
            return false;
        }
        LivestockData data = animal.getData(ModAttachments.LIVESTOCK);
        if (!data.isTamed() || !data.owner().equals(player.getUUID())) {
            player.sendSystemMessage(Component.literal("\u00a7c只能出售本人驯养的牲畜"));
            return false;
        }

        // 3. 结算：找零入箱，放不下直接掉落（绝不吞钱）
        List<ItemStack> coins = CurrencyUtil.toStacks(bound.priceFen());
        if (level.getBlockEntity(stationPos) instanceof ShippingBinBlockEntity bin) {
            for (ItemStack coin : coins) {
                ItemStack rest = CurrencyUtil.insertInto(bin, coin);
                if (!rest.isEmpty()) {
                    CurrencyUtil.dropStack(level, stationPos, rest);
                }
            }
        } else {
            for (ItemStack coin : coins) {
                CurrencyUtil.dropStack(level, stationPos, coin);
            }
        }

        // 4. 移除牲畜
        animal.discard();

        // 5. 契约可重复使用：清空绑定
        contract.remove(ModDataComponents.BOUND_LIVESTOCK);

        player.sendSystemMessage(Component.literal("\u00a7a已售出：\u00a7f" + bound.displayName()
                + "\u00a77，收入 \u00a7e").append(CurrencyUtil.format(bound.priceFen()))
                .append(Component.literal("\u00a77，已存入交易站")));
        return true;
    }

    private ShippingLogic() {
    }
}
