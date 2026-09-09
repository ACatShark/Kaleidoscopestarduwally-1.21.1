package tequila.cat.pigpig.block;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class ModCropBlock extends CropBlock {
    private final int maxAge;
    private final ItemLike seed;

    public ModCropBlock(int maxAge, ItemLike seed, BlockBehaviour.Properties properties) {
        super(properties);
        this.maxAge = maxAge;
        this.seed = seed;
    }

    @Override
    public int getMaxAge() {
        return this.maxAge;
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return this.seed;
    }
}
