package tequila.cat.pigpig.data;

import com.mojang.serialization.Codec;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.DataMapsUpdatedEvent;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

import tequila.cat.pigpig.KaleidoscopeStarduwally;

/**
 * 货币系统与结算工具。
 * <p>
 * 内部记账单位是「分」（fen）。货币本体由 NeoForge <b>数据映射（Data Map）</b> 配置，
 * 默认只有绿宝石（{@code minecraft:emerald} = 100 分）。文件位置：
 * <pre>{@code
 * data/<命名空间>/data_maps/item/currency.json
 * }</pre>
 * 格式（值 = 该物品等于多少分，正整数）：
 * <pre>{@code
 * {
 *   "replace": false,
 *   "values": {
 *     "minecraft:emerald": 100,
 *     "minecraft:emerald_block": 900
 *   }
 * }
 * }</pre>
 * 支持任意多种面额；结算时按分从大到小贪心找零。移除默认文件即无货币可用。
 */
public final class CurrencyUtil {
    /** 数据映射 ID：kaleidoscope_starduwally:currency，作用于 minecraft:item 注册表 */
    public static final DataMapType<Item, Integer> CURRENCY = DataMapType.builder(
                    ResourceLocation.fromNamespaceAndPath(KaleidoscopeStarduwally.MODID, "currency"),
                    Registries.ITEM,
                    Codec.intRange(1, Integer.MAX_VALUE))
            // 客户端缩放 Tooltip 需要面额信息，故同步到客户端（非强制，缺失时不阻断连接）
            .synced(Codec.intRange(1, Integer.MAX_VALUE), false)
            .build();

    /** 面额：某物品值多少分 */
    public record Denomination(Item item, int fen) {
    }

    private static List<Denomination> cache;

    /** 在 mod 总线注册数据映射类型 */
    public static void registerDataMapTypes(RegisterDataMapTypesEvent event) {
        event.register(CURRENCY);
    }

    /** 在游戏总线监听：数据映射重载/同步后重建面额缓存 */
    public static void onDataMapsUpdated(DataMapsUpdatedEvent event) {
        event.ifRegistry(Registries.ITEM, registry -> rebuild());
    }

    /** 重建面额缓存（按分降序） */
    public static void rebuild() {
        List<Denomination> list = new ArrayList<>();
        Map<ResourceKey<Item>, Integer> map = BuiltInRegistries.ITEM.getDataMap(CURRENCY);
        for (Map.Entry<ResourceKey<Item>, Integer> entry : map.entrySet()) {
            Item item = BuiltInRegistries.ITEM.get(entry.getKey());
            Integer fen = entry.getValue();
            if (item != null && fen != null && fen > 0) {
                list.add(new Denomination(item, fen));
            }
        }
        list.sort(Comparator.comparingInt(Denomination::fen).reversed());
        cache = List.copyOf(list);
    }

    /** 当前配置的面额（按分降序）；未初始化时惰性构建 */
    public static List<Denomination> denominations() {
        List<Denomination> local = cache;
        if (local == null) {
            rebuild();
            local = cache;
        }
        return local;
    }

    /** 最小面额（分）；无货币时返回 0 */
    public static int smallestDenomination() {
        List<Denomination> list = denominations();
        return list.isEmpty() ? 0 : list.get(list.size() - 1).fen();
    }

    /**
     * 把分数对齐到「可精确支付」的值：即最小面额的整数倍（向上至少 1 个）。
     * 无货币时原样返回。
     */
    public static int snap(int fen) {
        int unit = smallestDenomination();
        if (unit <= 0) {
            return fen;
        }
        if (fen <= 0) {
            return unit;
        }
        int units = Math.max(1, (int) Math.round((double) fen / unit));
        return units * unit;
    }

    /** 把（已对齐的）分数拆成货币物品堆叠，每叠不超过 64 */
    public static List<ItemStack> toStacks(int fen) {
        List<ItemStack> out = new ArrayList<>();
        int remaining = fen;
        for (Denomination denom : denominations()) {
            if (remaining < denom.fen()) {
                continue;
            }
            int n = remaining / denom.fen();
            remaining -= n * denom.fen();
            while (n > 0) {
                int c = Math.min(n, 64);
                out.add(new ItemStack(denom.item(), c));
                n -= c;
            }
        }
        return out;
    }

    /** 把分数格式化为可翻译文本，如 "12 绿宝石"、"1 绿宝石块 36 绿宝石" */
    public static Component format(int fen) {
        if (fen <= 0) {
            return Component.literal("0");
        }
        MutableComponent out = Component.empty();
        int remaining = fen;
        boolean any = false;
        for (Denomination denom : denominations()) {
            int n = remaining / denom.fen();
            if (n <= 0) {
                continue;
            }
            remaining -= n * denom.fen();
            if (any) {
                out.append(" ");
            }
            out.append(Component.literal(n + " ")).append(denom.item().getDescription());
            any = true;
        }
        if (!any) {
            return Component.literal(Integer.toString(fen));
        }
        return out;
    }

    /** 向容器插入一堆物品，返回剩余（放不下部分） */
    public static ItemStack insertInto(Container container, ItemStack stack) {
        ItemStack remain = stack.copy();
        for (int i = 0; i < container.getContainerSize() && !remain.isEmpty(); i++) {
            ItemStack slot = container.getItem(i);
            if (slot.isEmpty()) {
                int put = Math.min(remain.getCount(), container.getMaxStackSize());
                container.setItem(i, remain.copyWithCount(put));
                remain.shrink(put);
            } else if (ItemStack.isSameItemSameComponents(slot, remain)) {
                int room = container.getMaxStackSize() - slot.getCount();
                if (room > 0) {
                    int put = Math.min(room, remain.getCount());
                    slot.grow(put);
                    remain.shrink(put);
                }
            }
        }
        return remain;
    }

    /** 在地面生成掉落物 */
    public static void dropStack(Level level, BlockPos pos, ItemStack stack) {
        if (!stack.isEmpty()) {
            level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack));
        }
    }

    private CurrencyUtil() {
    }
}
