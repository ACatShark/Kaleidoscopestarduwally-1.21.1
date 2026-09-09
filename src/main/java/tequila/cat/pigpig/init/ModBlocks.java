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
import tequila.cat.pigpig.block.ModCropBlock;

public final class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(KaleidoscopeStarduwally.MODID);

    // NeoForge 1.21.1 StemBlock/AttachedStemBlock constructors require ResourceKey args.
    private static final ResourceKey<Block> MELON_KEY = ResourceKey.create(Registries.BLOCK,
            ResourceLocation.fromNamespaceAndPath(KaleidoscopeStarduwally.MODID, "melon"));
    private static final ResourceKey<Block> ATTACHED_MELON_STEM_KEY = ResourceKey.create(Registries.BLOCK,
            ResourceLocation.fromNamespaceAndPath(KaleidoscopeStarduwally.MODID, "attached_melon_stem"));
    private static final ResourceKey<Block> MELON_STEM_KEY = ResourceKey.create(Registries.BLOCK,
            ResourceLocation.fromNamespaceAndPath(KaleidoscopeStarduwally.MODID, "melon_stem"));
    private static final ResourceKey<Item> MELON_SEEDS_KEY = ResourceKey.create(Registries.ITEM,
            ResourceLocation.fromNamespaceAndPath(KaleidoscopeStarduwally.MODID, "melon_seeds"));

    public static final DeferredBlock<ModCropBlock> GARLIC_CROP =
            BLOCKS.register("garlic_crop", () -> new ModCropBlock(3, ModItems.GARLIC_CLOVE.get(), cropProperties()));
    public static final DeferredBlock<ModCropBlock> CAULIFLOWER_CROP =
            BLOCKS.register("cauliflower_crop", () -> new ModCropBlock(4, ModItems.CAULIFLOWER_SEEDS.get(), cropProperties()));
    public static final DeferredBlock<ModCropBlock> PARSNIP_CROP =
            BLOCKS.register("parsnip_crop", () -> new ModCropBlock(3, ModItems.PARSNIP.get(), cropProperties()));
    public static final DeferredBlock<ModCropBlock> RHUBARB_CROP =
            BLOCKS.register("rhubarb_crop", () -> new ModCropBlock(4, ModItems.RHUBARB_SEEDS.get(), cropProperties()));
    public static final DeferredBlock<ModCropBlock> AMARANTH_CROP =
            BLOCKS.register("amaranth_crop", () -> new ModCropBlock(7, ModItems.AMARANTH_SEEDS.get(), cropProperties()));
    public static final DeferredBlock<ModCropBlock> FROST_MELON_CROP =
            BLOCKS.register("frost_melon_crop", () -> new ModCropBlock(4, ModItems.FROST_MELON_SEEDS.get(), cropProperties()));

    // Melon (watermelon-style: stem grows a melon block).
    public static final DeferredBlock<Block> MELON =
            BLOCKS.register("melon", () -> new Block(melonProperties()));
    public static final DeferredBlock<AttachedStemBlock> ATTACHED_MELON_STEM =
            BLOCKS.register("attached_melon_stem",
                    () -> new AttachedStemBlock(MELON_KEY, MELON_STEM_KEY, MELON_SEEDS_KEY, cropProperties()));
    public static final DeferredBlock<StemBlock> MELON_STEM =
            BLOCKS.register("melon_stem",
                    () -> new StemBlock(MELON_KEY, ATTACHED_MELON_STEM_KEY, MELON_SEEDS_KEY, cropProperties()));

    private static BlockBehaviour.Properties cropProperties() {
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
