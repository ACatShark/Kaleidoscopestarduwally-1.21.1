package tequila.cat.pigpig.block;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import tequila.cat.pigpig.init.ModItems;

/**
 * 霜瓜：5 个生长阶段（age 0..4）。种子为霜瓜种子。
 * 掉落规则（由 loot 表实现）：age 0-3 破坏掉 1 个霜瓜种子；age 4 掉 1 个霜瓜 + 1 个霜瓜种子。
 */
public class FrostMelonCropBlock extends ModCropBlock {

    public static final int MAX_AGE = 4;
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, MAX_AGE);

    public FrostMelonCropBlock() {
        super(cropProperties());
    }

    public static BlockBehaviour.Properties cropProperties() {
        return BlockBehaviour.Properties.of()
                .noCollission()
                .randomTicks()
                .instabreak()
                .sound(SoundType.CROP);
    }

    @Override
    protected IntegerProperty getCropAgeProperty() {
        return AGE;
    }

    @Override
    public int getCropMaxAge() {
        return MAX_AGE;
    }

    @Override
    protected ItemLike getCropSeed() {
        return ModItems.FROST_MELON_SEEDS.get();
    }
}
