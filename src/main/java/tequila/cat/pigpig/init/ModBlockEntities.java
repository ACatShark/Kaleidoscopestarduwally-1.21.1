package tequila.cat.pigpig.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import tequila.cat.pigpig.KaleidoscopeStarduwally;
import tequila.cat.pigpig.block.ShippingBinBlockEntity;

public final class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, KaleidoscopeStarduwally.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ShippingBinBlockEntity>> SHIPPING_BIN =
            BLOCK_ENTITY_TYPES.register("shipping_bin",
                    () -> BlockEntityType.Builder.of(ShippingBinBlockEntity::new, ModBlocks.SHIPPING_BIN.get()).build(null));

    private ModBlockEntities() {
    }
}
