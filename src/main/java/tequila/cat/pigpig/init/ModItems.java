package tequila.cat.pigpig.init;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;

import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import tequila.cat.pigpig.KaleidoscopeStarduwally;

public final class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(KaleidoscopeStarduwally.MODID);

    // 蒜：蒜瓣是种子，蒜是收获物
    private static final FoodProperties GARLIC_FOOD = new FoodProperties.Builder()
            .nutrition(2)
            .saturationModifier(0.25F)
            .build();
    public static final DeferredItem<ItemNameBlockItem> GARLIC_CLOVE = ITEMS.register("garlic_clove",
            () -> new ItemNameBlockItem(ModBlocks.GARLIC_CROP.get(), new Item.Properties()));
    public static final DeferredItem<Item> GARLIC = ITEMS.register("garlic",
            () -> new Item(new Item.Properties().food(GARLIC_FOOD)));

    // 花椰菜：种子 + 收获物
    private static final FoodProperties CAULIFLOWER_FOOD = new FoodProperties.Builder()
            .nutrition(2)
            .saturationModifier(0.25F)
            .build();
    public static final DeferredItem<ItemNameBlockItem> CAULIFLOWER_SEEDS = ITEMS.register("cauliflower_seeds",
            () -> new ItemNameBlockItem(ModBlocks.CAULIFLOWER_CROP.get(), new Item.Properties()));
    public static final DeferredItem<Item> CAULIFLOWER = ITEMS.register("cauliflower",
            () -> new Item(new Item.Properties().food(CAULIFLOWER_FOOD)));

    // 甘蓝：种子 + 收获物
    private static final FoodProperties KALE_FOOD = new FoodProperties.Builder()
            .nutrition(1)
            .saturationModifier(0.0F)
            .build();
    public static final DeferredItem<ItemNameBlockItem> KALE_SEEDS = ITEMS.register("kale_seeds",
            () -> new ItemNameBlockItem(ModBlocks.KALE_CROP.get(), new Item.Properties()));
    public static final DeferredItem<Item> KALE = ITEMS.register("kale",
            () -> new Item(new Item.Properties().food(KALE_FOOD)));

    public static void addCreativeTabEntries(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {
            event.accept(GARLIC.get());
            event.accept(CAULIFLOWER.get());
            event.accept(KALE.get());
        } else if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
            event.accept(GARLIC_CLOVE.get());
            event.accept(CAULIFLOWER_SEEDS.get());
            event.accept(KALE_SEEDS.get());
        }
    }

    private ModItems() {
    }
}
