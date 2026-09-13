package tequila.cat.pigpig.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

/**
 * 大黄酱：用 kaleidoscopecookery 汤锅（水作汤底）烹制，由 kaleidoscopetavern 的空酒瓶承接。
 * 手持饮用（DRINK 动画），饮用后返还空酒瓶。
 */
public class RhubarbJamItem extends Item {
    // 饱食度设定：营养 4，饱和度 = 4 * (1/8) * 2 = 1.0（可按需调整）
    private static final int NUTRITION = 4;
    private static final float SATURATION_MODIFIER = 1.0F / 8.0F;

    public RhubarbJamItem(Properties properties) {
        super(properties);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    /**
     * 饮用音效。原版默认返回 {@link SoundEvents#GENERIC_DRINK}（通用喝水声），
     * 这里返回与蜂蜜瓶一致的 {@link SoundEvents#HONEY_DRINK}。
     * 该方法由 LivingEntity 在饮用动画结束时调用，音量/音调与蜂蜜瓶完全相同。
     */
    @Override
    public SoundEvent getDrinkingSound() {
        return SoundEvents.HONEY_DRINK;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 32;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return ItemUtils.startUsingInstantly(level, player, hand);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (entity instanceof ServerPlayer serverPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, stack);
            serverPlayer.awardStat(Stats.ITEM_USED.get(this));
        }
        if (entity instanceof Player player) {
            player.getFoodData().eat(NUTRITION, SATURATION_MODIFIER);
            if (!player.isCreative()) {
                stack.shrink(1);
            }
            // 饮用后返还承接它的空酒瓶
            ItemStack bottle = new ItemStack(BuiltInRegistries.ITEM.get(
                    ResourceLocation.fromNamespaceAndPath("kaleidoscopetavern", "empty_bottle")));
            if (!bottle.isEmpty()) {
                if (!player.addItem(bottle)) {
                    player.spawnAtLocation(bottle);
                }
            }
        }
        return stack;
    }
}
