package tequila.cat.pigpig.block;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import tequila.cat.pigpig.init.ModItems;

/**
 * 防风草：胡萝卜式，4 个生长阶段（age 0..3）。种子即防风草本身（直接种植）。
 * 掉落规则（由 loot 表实现）：age 0-2 破坏掉 1 个防风草；age 3 掉 1-4 个防风草。
 */
public class ParsnipCropBlock extends ModCropBlock {

    public static final int MAX_AGE = 3;
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, MAX_AGE);

    public ParsnipCropBlock() {
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
        return ModItems.PARSNIP.get();
    }
}
