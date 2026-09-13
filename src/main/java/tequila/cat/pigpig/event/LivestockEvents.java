package tequila.cat.pigpig.event;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import net.minecraft.network.chat.Component;

import tequila.cat.pigpig.attachment.LivestockData;
import tequila.cat.pigpig.attachment.ModAttachments;
import tequila.cat.pigpig.component.BoundLivestock;
import tequila.cat.pigpig.component.ModDataComponents;
import tequila.cat.pigpig.data.CurrencyUtil;
import tequila.cat.pigpig.data.FeedRegistry;
import tequila.cat.pigpig.data.LivestockPrices;
import tequila.cat.pigpig.item.FeedItem;
import tequila.cat.pigpig.item.ReleaseContractItem;
import tequila.cat.pigpig.item.SaleContractItem;

/**
 * 养殖玩法事件（均在 NeoForge 游戏总线上）：
 *  1) 手持饲料右键动物 → 驯养绑定 / 投喂饲养度
 *  2) 手持出售契约右键已驯养动物 → 签订待售契约（锁定价格）
 *  3) 手持解除契约右键已驯养动物 → 恢复野生（主人免伤随之失效）
 *  4) 主人伤害自己驯养的牲畜 → 免伤（LivingIncomingDamageEvent）
 */
public class LivestockEvents {

    @SubscribeEvent
    public void onInteractEntity(PlayerInteractEvent.EntityInteract event) {
        InteractionHand hand = event.getHand();
        if (hand != InteractionHand.MAIN_HAND) {
            return;
        }
        Player player = event.getEntity();
        Level level = player.level();
        if (level.isClientSide) {
            return;
        }
        Entity target = event.getTarget();
        if (!(target instanceof Animal animal)) {
            return;
        }
        ItemStack stack = player.getItemInHand(hand);

        if (stack.getItem() instanceof FeedItem feed) {
            if (tryFeed(player, animal, stack, feed)) {
                event.setCanceled(true);
            }
        } else if (stack.getItem() instanceof SaleContractItem) {
            if (trySign(player, animal, stack)) {
                event.setCanceled(true);
            }
        } else if (stack.getItem() instanceof ReleaseContractItem) {
            if (tryRelease(player, animal)) {
                event.setCanceled(true);
            }
        }
    }

    // ---- 喂食 / 驯养 ----

    private boolean tryFeed(Player player, Animal animal, ItemStack stack, FeedItem feed) {
        if (!FeedRegistry.canEat(feed.getType(), animal)) {
            return false; // 类别不符：放行，让其他原版交互继续
        }
        LivestockData data = animal.getData(ModAttachments.LIVESTOCK);
        if (data.isTamed() && !data.owner().equals(player.getUUID())) {
            player.sendSystemMessage(Component.literal(
                    "\u00a77这是 \u00a7e" + data.ownerName() + "\u00a77 驯养的牲畜"));
            return true;
        }
        Level level = player.level();
        int day = (int) (level.getDayTime() / 24000L);
        if (data.isTamed() && data.lastFedDay() == day) {
            player.sendSystemMessage(Component.literal("\u00a77今天已投喂过，明天再来吧"));
            return true;
        }

        boolean firstTame = !data.isTamed();
        LivestockData next = data.feed(player.getUUID(), player.getName().getString(), day, feed.getType().gain());
        animal.setData(ModAttachments.LIVESTOCK, next);

        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.HEART,
                    animal.getX(), animal.getY() + animal.getBbHeight() * 0.5 + 0.3, animal.getZ(),
                    5, 0.2, 0.1, 0.2, 0.0);
        }
        player.swing(InteractionHand.MAIN_HAND);
        if (firstTame) {
            player.sendSystemMessage(Component.literal("\u00a7a已驯养 \u00a7e" + animal.getName().getString()
                    + "\u00a7a，它现在是你的牲畜了（主人不会伤害它）"));
        } else {
            player.sendSystemMessage(Component.literal("\u00a7a已投喂 " + animal.getName().getString() + "，饲养度 +" + feed.getType().gain()));
        }
        return true;
    }

    // ---- 签订出售契约 ----

    private boolean trySign(Player player, Animal animal, ItemStack stack) {
        LivestockData data = animal.getData(ModAttachments.LIVESTOCK);
        if (!data.isTamed() || !data.owner().equals(player.getUUID())) {
            player.sendSystemMessage(Component.literal("\u00a7c只能对本人物品栏里已驯养的牲畜签订契约"));
            return true;
        }
        int baseFen = LivestockPrices.priceFor(animal.getType());
        if (baseFen <= 0) {
            player.sendSystemMessage(Component.literal("\u00a7c这种牲畜暂时没有回收定价"));
            return true;
        }
        int feedCount = Math.min(data.feedCount(), 20);
        double multiplier = (animal.isBaby() ? 0.4 : 1.0) * (1.0 + 0.1 * feedCount);
        int priceFen = CurrencyUtil.snap(Math.max(1, (int) Math.round(baseFen * multiplier)));

        BoundLivestock bound = new BoundLivestock(
                animal.getUUID(),
                BuiltInRegistries.ENTITY_TYPE.getKey(animal.getType()),
                animal.getName().getString(),
                priceFen);
        stack.set(ModDataComponents.BOUND_LIVESTOCK, bound);

        player.swing(InteractionHand.MAIN_HAND);
        player.sendSystemMessage(Component.literal("\u00a7a已签订待售契约：\u00a7e" + animal.getName().getString()
                + "\u00a77 预计收入 \u00a7a").append(CurrencyUtil.format(priceFen))
                .append(Component.literal("\u00a77，去交易站右键即可售出")));
        return true;
    }

    // ---- 解除契约 ----

    private boolean tryRelease(Player player, Animal animal) {
        LivestockData data = animal.getData(ModAttachments.LIVESTOCK);
        if (!data.isTamed() || !data.owner().equals(player.getUUID())) {
            player.sendSystemMessage(Component.literal("\u00a77这头牲畜并没有绑定在你名下"));
            return true;
        }
        animal.setData(ModAttachments.LIVESTOCK, LivestockData.untame());
        player.swing(InteractionHand.MAIN_HAND);
        player.sendSystemMessage(Component.literal("\u00a7a已解除驯养，牲畜恢复为野生状态"));
        return true;
    }

    // ---- 主人免伤 ----

    @SubscribeEvent
    public void onIncomingDamage(LivingIncomingDamageEvent event) {
        if (event.getEntity().level().isClientSide) {
            return;
        }
        if (!(event.getEntity() instanceof Animal animal)) {
            return;
        }
        LivestockData data = animal.getData(ModAttachments.LIVESTOCK);
        if (!data.isTamed()) {
            return;
        }
        Entity attacker = event.getSource().getEntity();
        if (attacker != null && data.owner().equals(attacker.getUUID())) {
            event.setCanceled(true);
        }
    }
}
