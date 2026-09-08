package tequila.cat.pigpig.block;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

/**
 * 多生长阶段农田作物的公共基类。
 * <p>
 * 原版 {@link CropBlock} 把 AGE 属性写死为 0..7，这里改为由各作物子类
 * 提供自己的 "age" 属性（0..各自 maxAge），并重写 age/种子相关钩子。
 * 生长速度仍走原版概率随机刻（种满 8 邻 + 水源可加速），骨粉默认 +1 阶段。
 */
public abstract class ModCropBlock extends CropBlock {

    public ModCropBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    /** 各作物自己的 age 属性（静态常量）。 */
    protected abstract IntegerProperty getCropAgeProperty();

    /** 最大阶段序号（如 3 表示 4 个阶段：age 0..3）。 */
    public abstract int getCropMaxAge();

    /** 种植/拾取所用的种子物品。 */
    protected abstract ItemLike getCropSeed();

    @Override
    protected IntegerProperty getAgeProperty() {
        return this.getCropAgeProperty();
    }

    @Override
    public int getMaxAge() {
        return this.getCropMaxAge();
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return this.getCropSeed();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(this.getCropAgeProperty());
    }
}
