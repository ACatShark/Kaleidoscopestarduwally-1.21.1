package tequila.cat.pigpig.data;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;

import tequila.cat.pigpig.KaleidoscopeStarduwally;
import tequila.cat.pigpig.item.FeedItem.FeedType;

/**
 * 饲料类别 ↔ 生物 tag 映射。
 * 对应 datapack: data/kaleidoscope_starduwally/tags/entity_type/{livestock,poultry}.json
 */
public final class FeedRegistry {
    public static final TagKey<EntityType<?>> LIVESTOCK = tag("livestock");
    public static final TagKey<EntityType<?>> POULTRY = tag("poultry");

    private static TagKey<EntityType<?>> tag(String name) {
        return TagKey.create(Registries.ENTITY_TYPE,
                ResourceLocation.fromNamespaceAndPath(KaleidoscopeStarduwally.MODID, name));
    }

    /** 该饲料是否能喂该动物 */
    public static boolean canEat(FeedType type, Animal animal) {
        return switch (type) {
            case PREMIUM -> true; // 通吃
            case LIVESTOCK -> animal.getType().is(LIVESTOCK);
            case POULTRY -> animal.getType().is(POULTRY);
        };
    }

    private FeedRegistry() {
    }
}
