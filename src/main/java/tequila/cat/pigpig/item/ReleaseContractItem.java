package tequila.cat.pigpig.item;

import java.util.List;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

/**
 * 解除契约：右键已驯养牲畜，将其恢复为野生（主人可再次伤害）。
 */
public class ReleaseContractItem extends Item {
    public ReleaseContractItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.literal("\u00a77右键驯养的牲畜以解除绑定（将恢复为野生）"));
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
