package tequila.cat.pigpig.block;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import tequila.cat.pigpig.init.ModItems;

/**
 * 花椰菜：5 个生长阶段（age 0..4）。
 * 掉落规则（由 loot 表实现）：age 0-3 破坏掉 1 花椰菜种子；age 4 掉 1 花椰菜 + 1-2 花椰菜种子。
 */
public class CauliflowerCropBlock extends ModCropBlock {

    public static final int MAX_AGE = 4;
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, MAX_AGE);

    public CauliflowerCropBlock() {
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
        return ModItems.CAULIFLOWER_SEEDS.get();
    }
}
