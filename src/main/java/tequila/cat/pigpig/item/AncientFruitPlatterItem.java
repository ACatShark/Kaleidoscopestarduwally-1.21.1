package tequila.cat.pigpig.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

/**
 * 上古水果拼盘：3 个上古水果 + 1 个碗合成。食用回复 9 饱食度 / 1 饱和度，食用后返还碗。
 */
public class AncientFruitPlatterItem extends Item {
    public AncientFruitPlatterItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        super.finishUsingItem(stack, level, entity);
        return entity instanceof Player player && !player.isCreative() ? new ItemStack(Items.BOWL) : stack;
    }
}
