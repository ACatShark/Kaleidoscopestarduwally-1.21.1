package tequila.cat.pigpig.init;

import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import tequila.cat.pigpig.KaleidoscopeStarduwally;
import tequila.cat.pigpig.block.CauliflowerCropBlock;
import tequila.cat.pigpig.block.GarlicCropBlock;
import tequila.cat.pigpig.block.KaleCropBlock;

public final class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(KaleidoscopeStarduwally.MODID);

    public static final DeferredBlock<GarlicCropBlock> GARLIC_CROP =
            BLOCKS.register("garlic_crop", GarlicCropBlock::new);
    public static final DeferredBlock<CauliflowerCropBlock> CAULIFLOWER_CROP =
            BLOCKS.register("cauliflower_crop", CauliflowerCropBlock::new);
    public static final DeferredBlock<KaleCropBlock> KALE_CROP =
            BLOCKS.register("kale_crop", KaleCropBlock::new);

    private ModBlocks() {
    }
}
