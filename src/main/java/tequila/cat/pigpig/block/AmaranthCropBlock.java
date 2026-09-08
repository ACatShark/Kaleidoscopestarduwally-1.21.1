package tequila.cat.pigpig.block;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import tequila.cat.pigpig.init.ModItems;

/**
 * 苋菜：小麦式，8 个生长阶段（age 0..7）。种子为苋菜种子，苋菜本身不可直接食用。
 * 掉落规则（由 loot 表实现）：age 0-6 破坏掉 1 个苋菜种子；age 7 掉 1 个苋菜 + 0-3 个苋菜种子。
 */
public class AmaranthCropBlock extends ModCropBlock {

    public static final int MAX_AGE = 7;
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, MAX_AGE);

    public AmaranthCropBlock() {
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
        return ModItems.AMARANTH_SEEDS.get();
    }
}
