package tequila.cat.pigpig.init;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;

import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import tequila.cat.pigpig.KaleidoscopeStarduwally;

public final class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(KaleidoscopeStarduwally.MODID);

    // Cauliflower: seeds + harvest.
    private static final FoodProperties CAULIFLOWER_FOOD = new FoodProperties.Builder()
            .nutrition(2)
            .saturationModifier(0.25F)
            .build();
    public static final DeferredItem<ItemNameBlockItem> CAULIFLOWER_SEEDS = ITEMS.register("cauliflower_seeds",
            () -> new ItemNameBlockItem(ModBlocks.CAULIFLOWER_CROP.get(), new Item.Properties()));
    public static final DeferredItem<Item> CAULIFLOWER = ITEMS.register("cauliflower",
            () -> new Item(new Item.Properties().food(CAULIFLOWER_FOOD)));

    // Parsnip: carrot-style (no separate seed, plant the parsnip itself). Edible 3 hunger / 1 saturation.
    // Actual saturation = nutrition * saturationModifier * 2, so 3 * (1/6) * 2 = 1.
    private static final FoodProperties PARSNIP_FOOD = new FoodProperties.Builder()
            .nutrition(3)
            .saturationModifier(1.0F / (3.0F * 2.0F))
            .build();
    public static final DeferredItem<ItemNameBlockItem> PARSNIP = ITEMS.register("parsnip",
            () -> new ItemNameBlockItem(ModBlocks.PARSNIP_CROP.get(), new Item.Properties().food(PARSNIP_FOOD)));

    // Rhubarb + rhubarb seeds. Edible 1 hunger / 1 saturation. 1 * (1/2) * 2 = 1.
    private static final FoodProperties RHUBARB_FOOD = new FoodProperties.Builder()
            .nutrition(1)
            .saturationModifier(1.0F / (1.0F * 2.0F))
            .build();
    public static final DeferredItem<ItemNameBlockItem> RHUBARB_SEEDS = ITEMS.register("rhubarb_seeds",
            () -> new ItemNameBlockItem(ModBlocks.RHUBARB_CROP.get(), new Item.Properties()));
    public static final DeferredItem<Item> RHUBARB = ITEMS.register("rhubarb",
            () -> new Item(new Item.Properties().food(RHUBARB_FOOD)));

    // Melon + melon slice + melon seeds (watermelon-style). Slice edible 4 hunger / 0.5 saturation.
    // 4 * (0.5/8) * 2 = 0.5.
    private static final FoodProperties MELON_SLICE_FOOD = new FoodProperties.Builder()
            .nutrition(4)
            .saturationModifier(0.5F / (4.0F * 2.0F))
            .build();
    public static final DeferredItem<ItemNameBlockItem> MELON_SEEDS = ITEMS.register("melon_seeds",
            () -> new ItemNameBlockItem(ModBlocks.MELON_STEM.get(), new Item.Properties()));
    public static final DeferredItem<Item> MELON_SLICE = ITEMS.register("melon_slice",
            () -> new Item(new Item.Properties().food(MELON_SLICE_FOOD)));

    // Amaranth + amaranth seeds (wheat-style). Not directly edible.
    public static final DeferredItem<ItemNameBlockItem> AMARANTH_SEEDS = ITEMS.register("amaranth_seeds",
            () -> new ItemNameBlockItem(ModBlocks.AMARANTH_CROP.get(), new Item.Properties()));
    public static final DeferredItem<Item> AMARANTH = ITEMS.register("amaranth",
            () -> new Item(new Item.Properties()));

    // Frost melon + frost melon seeds. Edible 6 hunger / 1 saturation. 6 * (1/12) * 2 = 1.
    private static final FoodProperties FROST_MELON_FOOD = new FoodProperties.Builder()
            .nutrition(6)
            .saturationModifier(1.0F / (6.0F * 2.0F))
            .build();
    public static final DeferredItem<ItemNameBlockItem> FROST_MELON_SEEDS = ITEMS.register("frost_melon_seeds",
            () -> new ItemNameBlockItem(ModBlocks.FROST_MELON_CROP.get(), new Item.Properties()));
    public static final DeferredItem<Item> FROST_MELON = ITEMS.register("frost_melon",
            () -> new Item(new Item.Properties().food(FROST_MELON_FOOD)));

    private ModItems() {
    }
}
