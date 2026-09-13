package tequila.cat.pigpig.data;

import com.mojang.serialization.Codec;

import java.util.Optional;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

import tequila.cat.pigpig.KaleidoscopeStarduwally;

/**
 * 牲畜基础回收售价（单位：分）。货币本体由 {@code currency} 数据映射配置，默认 1 绿宝石 = 100 分。
 * <p>
 * 价格通过 NeoForge <b>数据映射（Data Map）</b> 配置，可按数据包覆盖、{@code /reload} 即时生效。
 * 文件位置（可放在任意数据包命名空间下，多个文件/数据包会自动合并）：
 * <pre>{@code
 * data/<命名空间>/data_maps/entity_type/livestock_price.json
 * }</pre>
 * 文件格式：
 * <pre>{@code
 * {
 *   "replace": false,                    // 可选：true 则清空此前合并的全部价格
 *   "values": {
 *     "minecraft:cow": 1200,             // 精确指定实体
 *     "#kaleidoscope_starduwally:livestock": 1000   // 也可用实体 tag 批量定价
 *   },
 *   "remove": ["minecraft:pig"]          // 可选：移除（覆盖低优先级数据包）指定实体
 * }
 * }</pre>
 * <b>未列出的实体视为不可回收（价格为 0）。</b>数值须为正整数（0 或负值会导致解析报错）。
 * <p>
 * 注意：这里只配置「基础价」；幼崽折扣与饲养度加成（售价系数）仍在 {@code LivestockEvents} 中计算。
 */
public final class LivestockPrices {
    /** 数据映射 ID：kaleidoscope_starduwally:livestock_price，作用于 minecraft:entity_type 注册表 */
    public static final DataMapType<EntityType<?>, Integer> PRICE = DataMapType.builder(
                    ResourceLocation.fromNamespaceAndPath(KaleidoscopeStarduwally.MODID, "livestock_price"),
                    Registries.ENTITY_TYPE,
                    Codec.intRange(1, Integer.MAX_VALUE))
            .build();

    /** 在 mod 总线注册数据映射类型 */
    public static void registerDataMapTypes(RegisterDataMapTypesEvent event) {
        event.register(PRICE);
    }

    /** 返回基础售价（分）；未配置或数据包尚未加载时返回 0（不可售） */
    public static int priceFor(EntityType<?> type) {
        Optional<ResourceKey<EntityType<?>>> key = BuiltInRegistries.ENTITY_TYPE.getResourceKey(type);
        if (key.isEmpty()) {
            return 0;
        }
        Integer price = BuiltInRegistries.ENTITY_TYPE.getData(PRICE, key.get());
        return price == null ? 0 : price;
    }

    private LivestockPrices() {
    }
}
