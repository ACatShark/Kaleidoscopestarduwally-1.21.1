package tequila.cat.pigpig.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.AttachedStemBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import tequila.cat.pigpig.KaleidoscopeStarduwally;
import tequila.cat.pigpig.block.AmaranthCropBlock;
import tequila.cat.pigpig.block.CauliflowerCropBlock;
import tequila.cat.pigpig.block.FrostMelonCropBlock;
import tequila.cat.pigpig.block.GarlicCropBlock;
import tequila.cat.pigpig.block.KaleCropBlock;
import tequila.cat.pigpig.block.ParsnipCropBlock;
import tequila.cat.pigpig.block.RhubarbCropBlock;

public final class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(KaleidoscopeStarduwally.MODID);

    // NeoForge 1.21.1 的 StemBlock/AttachedStemBlock 构造器需要 ResourceKey，
    // 这里先准备好，避免在字段初始化器里相互前向引用。
    private static final ResourceKey<Block> MELON_KEY = ResourceKey.create(Registries.BLOCK,
            ResourceLocation.fromNamespaceAndPath(KaleidoscopeStarduwally.MODID, "melon"));
    private static final ResourceKey<Block> ATTACHED_MELON_STEM_KEY = ResourceKey.create(Registries.BLOCK,
            ResourceLocation.fromNamespaceAndPath(KaleidoscopeStarduwally.MODID, "attached_melon_stem"));
    private static final ResourceKey<Block> MELON_STEM_KEY = ResourceKey.create(Registries.BLOCK,
            ResourceLocation.fromNamespaceAndPath(KaleidoscopeStarduwally.MODID, "melon_stem"));
    private static final ResourceKey<Item> MELON_SEEDS_KEY = ResourceKey.create(Registries.ITEM,
            ResourceLocation.fromNamespaceAndPath(KaleidoscopeStarduwally.MODID, "melon_seeds"));

    public static final DeferredBlock<GarlicCropBlock> GARLIC_CROP =
            BLOCKS.register("garlic_crop", GarlicCropBlock::new);
    public static final DeferredBlock<CauliflowerCropBlock> CAULIFLOWER_CROP =
            BLOCKS.register("cauliflower_crop", CauliflowerCropBlock::new);
    public static final DeferredBlock<KaleCropBlock> KALE_CROP =
            BLOCKS.register("kale_crop", KaleCropBlock::new);

    // 防风草（胡萝卜式，无种子）
    public static final DeferredBlock<ParsnipCropBlock> PARSNIP_CROP =
            BLOCKS.register("parsnip_crop", ParsnipCropBlock::new);
    // 大黄
    public static final DeferredBlock<RhubarbCropBlock> RHUBARB_CROP =
            BLOCKS.register("rhubarb_crop", RhubarbCropBlock::new);
    // 苋菜（小麦式）
    public static final DeferredBlock<AmaranthCropBlock> AMARANTH_CROP =
            BLOCKS.register("amaranth_crop", AmaranthCropBlock::new);
    // 霜瓜
    public static final DeferredBlock<FrostMelonCropBlock> FROST_MELON_CROP =
            BLOCKS.register("frost_melon_crop", FrostMelonCropBlock::new);

    // 甜瓜（西瓜式：茎结瓜方块）
    public static final DeferredBlock<Block> MELON =
            BLOCKS.register("melon", () -> new Block(melonProperties()));
    public static final DeferredBlock<AttachedStemBlock> ATTACHED_MELON_STEM =
            BLOCKS.register("attached_melon_stem",
                    () -> new AttachedStemBlock(MELON_KEY, MELON_STEM_KEY, MELON_SEEDS_KEY, stemProperties()));
    public static final DeferredBlock<StemBlock> MELON_STEM =
            BLOCKS.register("melon_stem",
                    () -> new StemBlock(MELON_KEY, ATTACHED_MELON_STEM_KEY, MELON_SEEDS_KEY, stemProperties()));

    private static BlockBehaviour.Properties stemProperties() {
        return BlockBehaviour.Properties.of()
                .noCollission()
                .randomTicks()
                .instabreak()
                .sound(SoundType.CROP);
    }

    private static BlockBehaviour.Properties melonProperties() {
        return BlockBehaviour.Properties.of()
                .sound(SoundType.WOOD)
                .strength(1.0F);
    }

    private ModBlocks() {
    }
}
