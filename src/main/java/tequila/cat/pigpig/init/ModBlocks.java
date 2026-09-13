package tequila.cat.pigpig.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.AttachedStemBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import tequila.cat.pigpig.KaleidoscopeStarduwally;
import tequila.cat.pigpig.block.ModCropBlock;
import tequila.cat.pigpig.block.ModTrellisCropBlock;
import tequila.cat.pigpig.block.ShippingBinBlock;

public final class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(KaleidoscopeStarduwally.MODID);

    public static final DeferredBlock<ModCropBlock> CAULIFLOWER_CROP =
            BLOCKS.register("cauliflower_crop", () -> new ModCropBlock(4, lazyItem(ModItems.CAULIFLOWER_SEEDS::get), cropProperties()));
    public static final DeferredBlock<ModCropBlock> PARSNIP_CROP =
            BLOCKS.register("parsnip_crop", () -> new ModCropBlock(3, lazyItem(ModItems.PARSNIP::get), cropProperties()));
    public static final DeferredBlock<ModCropBlock> RHUBARB_CROP =
            BLOCKS.register("rhubarb_crop", () -> new ModCropBlock(4, lazyItem(ModItems.RHUBARB_SEEDS::get), cropProperties()));
    public static final DeferredBlock<ModCropBlock> AMARANTH_CROP =
            BLOCKS.register("amaranth_crop", () -> new ModCropBlock(7, lazyItem(ModItems.AMARANTH_SEEDS::get), cropProperties()));
    public static final DeferredBlock<ModCropBlock> FROST_MELON_CROP =
            BLOCKS.register("frost_melon_crop", () -> new ModCropBlock(4, lazyItem(ModItems.FROST_MELON_SEEDS::get), cropProperties()));

    // 藤架作物：生长方式与 tavern 的葡萄一致，必须依附藤架（上方为藤架）才能存活。
    public static final DeferredBlock<ModTrellisCropBlock> ANCIENT_FRUIT_CROP =
            BLOCKS.register("ancient_fruit_crop", () -> new ModTrellisCropBlock(
                    ModItems.ANCIENT_FRUIT::get, 2, vineCropProperties()));
    public static final DeferredBlock<ModTrellisCropBlock> HOPS_CROP =
            BLOCKS.register("hops_crop", () -> new ModTrellisCropBlock(
                    ModItems.HOPS::get, 3, vineCropProperties()));

    // Shipping bin (trade station): a 27-slot box for coins.
    public static final DeferredBlock<ShippingBinBlock> SHIPPING_BIN =
            BLOCKS.register("shipping_bin", () -> new ShippingBinBlock(
                    BlockBehaviour.Properties.of()
                            .strength(2.5F, 6.0F)
                            .sound(SoundType.WOOD)
                            .noOcclusion()));

    /**
     * 惰性 ItemLike：注册阶段不触碰物品 DeferredHolder（避免不同注册表事件
     * 顺序不确定时触发 "Trying to access unbound value"），仅在运行时按需解析。
     */
    private static ItemLike lazyItem(Supplier<? extends Item> supplier) {
        return supplier::get;
    }

    private static BlockBehaviour.Properties cropProperties() {
        return BlockBehaviour.Properties.of()
                .noCollission()
                .randomTicks()
                .instabreak()
                .sound(SoundType.CROP);
    }

    /** 藤架作物属性：与 tavern 的 GrapeCropBlock 保持一致 */
    private static BlockBehaviour.Properties vineCropProperties() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
                .noCollission()
                .randomTicks()
                .instabreak()
                .sound(SoundType.CROP)
                .offsetType(BlockBehaviour.OffsetType.XYZ)
                .pushReaction(PushReaction.DESTROY);
    }

    private ModBlocks() {
    }
}
