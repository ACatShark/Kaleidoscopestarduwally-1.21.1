package tequila.cat.pigpig.item;

import java.util.List;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import tequila.cat.pigpig.component.BoundLivestock;
import tequila.cat.pigpig.component.ModDataComponents;
import tequila.cat.pigpig.data.CurrencyUtil;

/**
 * 出售契约：右键驯养牲畜后绑定（写入 DataComponent），
 * 再手持契约右键交易站即可售出。契约可重复使用。
 */
public class SaleContractItem extends Item {
    public SaleContractItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        BoundLivestock bound = stack.get(ModDataComponents.BOUND_LIVESTOCK);
        if (bound != null) {
            tooltipComponents.add(Component.literal("\u00a7e待售：\u00a7f" + bound.displayName()));
            tooltipComponents.add(Component.literal("\u00a7e价格：\u00a7a").append(CurrencyUtil.format(bound.priceFen())));
            tooltipComponents.add(Component.literal("\u00a77对交易站右键以售出"));
        } else {
            tooltipComponents.add(Component.literal("\u00a77右键已驯养的牲畜以签订契约"));
        }
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
