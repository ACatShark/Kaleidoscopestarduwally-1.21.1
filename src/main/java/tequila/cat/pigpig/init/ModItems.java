package tequila.cat.pigpig.init;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;

import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import tequila.cat.pigpig.KaleidoscopeStarduwally;
import tequila.cat.pigpig.item.AncientFruitPlatterItem;
import tequila.cat.pigpig.item.FeedItem;
import tequila.cat.pigpig.item.ReleaseContractItem;
import tequila.cat.pigpig.item.RhubarbJamItem;
import tequila.cat.pigpig.item.SaleContractItem;

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

    // Leek: edible 2 hunger / 1 saturation. 2 * (1/4) * 2 = 1.
    private static final FoodProperties LEEK_FOOD = new FoodProperties.Builder()
            .nutrition(2)
            .saturationModifier(1.0F / (2.0F * 2.0F))
            .build();
    public static final DeferredItem<Item> LEEK = ITEMS.register("leek",
            () -> new Item(new Item.Properties().food(LEEK_FOOD)));

    // Ancient fruit: edible 4 hunger / 1 saturation. 4 * (1/8) * 2 = 1.
    private static final FoodProperties ANCIENT_FRUIT_FOOD = new FoodProperties.Builder()
            .nutrition(4)
            .saturationModifier(1.0F / (4.0F * 2.0F))
            .build();

    // 上古水果：藤架式作物（生长方式同 tavern 的葡萄），种子播种。
    public static final DeferredItem<ItemNameBlockItem> ANCIENT_FRUIT_SEEDS = ITEMS.register("ancient_fruit_seeds",
            () -> new ItemNameBlockItem(ModBlocks.ANCIENT_FRUIT_CROP.get(), new Item.Properties()));
    public static final DeferredItem<Item> ANCIENT_FRUIT = ITEMS.register("ancient_fruit",
            () -> new Item(new Item.Properties().food(ANCIENT_FRUIT_FOOD)));

    // 啤酒花：藤架式作物，种子播种。不可直接食用。
    public static final DeferredItem<ItemNameBlockItem> HOPS_SEEDS = ITEMS.register("hops_seeds",
            () -> new ItemNameBlockItem(ModBlocks.HOPS_CROP.get(), new Item.Properties()));
    public static final DeferredItem<Item> HOPS = ITEMS.register("hops",
            () -> new Item(new Item.Properties()));

    // ============ 加工食品 ============

    // 大黄酱：用 kaleidoscopecookery 汤锅（水作汤底）烹制，kaleidoscopetavern 空酒瓶承接，可饮用。
    public static final DeferredItem<Item> RHUBARB_JAM = ITEMS.register("rhubarb_jam",
            () -> new RhubarbJamItem(new Item.Properties().stacksTo(16)));

    // 上古水果拼盘：3 个上古水果 + 1 个碗合成，食用回复 9 饱食度 / 1 饱和度。
    // 最终饱和度 = nutrition * saturationModifier * 2 = 9 * (1/18) * 2 = 1.0。
    private static final FoodProperties ANCIENT_FRUIT_PLATTER_FOOD = new FoodProperties.Builder()
            .nutrition(9)
            .saturationModifier(1.0F / (9.0F * 2.0F))
            .build();
    public static final DeferredItem<Item> ANCIENT_FRUIT_PLATTER = ITEMS.register("ancient_fruit_platter",
            () -> new AncientFruitPlatterItem(new Item.Properties().food(ANCIENT_FRUIT_PLATTER_FOOD)));

    // ============ 养殖：经济系统（占位物品，后续可替换实现） ============

    // ---- 饲料 ----
    public static final DeferredItem<FeedItem> LIVESTOCK_FEED = ITEMS.register("livestock_feed",
            () -> new FeedItem(FeedItem.FeedType.LIVESTOCK, new Item.Properties()));
    public static final DeferredItem<FeedItem> POULTRY_FEED = ITEMS.register("poultry_feed",
            () -> new FeedItem(FeedItem.FeedType.POULTRY, new Item.Properties()));
    public static final DeferredItem<FeedItem> PREMIUM_FEED = ITEMS.register("premium_feed",
            () -> new FeedItem(FeedItem.FeedType.PREMIUM, new Item.Properties()));

    // ---- 契约 ----
    public static final DeferredItem<SaleContractItem> SALE_CONTRACT = ITEMS.register("sale_contract",
            () -> new SaleContractItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<ReleaseContractItem> RELEASE_CONTRACT = ITEMS.register("release_contract",
            () -> new ReleaseContractItem(new Item.Properties().stacksTo(1)));

    // ---- 交易站方块物品 ----
    public static final DeferredItem<BlockItem> SHIPPING_BIN = ITEMS.register("shipping_bin",
            () -> new BlockItem(ModBlocks.SHIPPING_BIN.get(), new Item.Properties()));

    private ModItems() {
    }
}
