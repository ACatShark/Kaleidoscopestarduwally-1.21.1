package tequila.cat.pigpig.init;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;

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

    // 防风草：胡萝卜式——无独立种子，直接种防风草本身；可食 3 饱食度 / 1 饱和度
    // 实际饱和度 = nutrition * saturationModifier * 2，故 3 * (1/6) * 2 = 1
    private static final FoodProperties PARSNIP_FOOD = new FoodProperties.Builder()
            .nutrition(3)
            .saturationModifier(1.0F / (3.0F * 2.0F))
            .build();
    public static final DeferredItem<ItemNameBlockItem> PARSNIP = ITEMS.register("parsnip",
            () -> new ItemNameBlockItem(ModBlocks.PARSNIP_CROP.get(), new Item.Properties().food(PARSNIP_FOOD)));

    // 大黄 + 大黄种子：5 阶段；可食 1 饱食度 / 1 饱和度
    // 1 * (1/2) * 2 = 1
    private static final FoodProperties RHUBARB_FOOD = new FoodProperties.Builder()
            .nutrition(1)
            .saturationModifier(1.0F / (1.0F * 2.0F))
            .build();
    public static final DeferredItem<ItemNameBlockItem> RHUBARB_SEEDS = ITEMS.register("rhubarb_seeds",
            () -> new ItemNameBlockItem(ModBlocks.RHUBARB_CROP.get(), new Item.Properties()));
    public static final DeferredItem<Item> RHUBARB = ITEMS.register("rhubarb",
            () -> new Item(new Item.Properties().food(RHUBARB_FOOD)));

    // 甜瓜 + 甜瓜片 + 甜瓜种子：西瓜式（茎结瓜方块，破坏掉甜瓜片）；甜瓜片可食 4 / 0.5
    // 4 * (0.5/8) * 2 = 0.5
    private static final FoodProperties MELON_SLICE_FOOD = new FoodProperties.Builder()
            .nutrition(4)
            .saturationModifier(0.5F / (4.0F * 2.0F))
            .build();
    public static final DeferredItem<ItemNameBlockItem> MELON_SEEDS = ITEMS.register("melon_seeds",
            () -> new ItemNameBlockItem(ModBlocks.MELON_STEM.get(), new Item.Properties()));
    public static final DeferredItem<Item> MELON_SLICE = ITEMS.register("melon_slice",
            () -> new Item(new Item.Properties().food(MELON_SLICE_FOOD)));

    // 苋菜 + 苋菜种子：小麦式；不可直接食用
    public static final DeferredItem<ItemNameBlockItem> AMARANTH_SEEDS = ITEMS.register("amaranth_seeds",
            () -> new ItemNameBlockItem(ModBlocks.AMARANTH_CROP.get(), new Item.Properties()));
    public static final DeferredItem<Item> AMARANTH = ITEMS.register("amaranth",
            () -> new Item(new Item.Properties()));

    // 霜瓜 + 霜瓜种子：5 阶段；可食 6 饱食度 / 1 饱和度
    // 6 * (1/12) * 2 = 1
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
