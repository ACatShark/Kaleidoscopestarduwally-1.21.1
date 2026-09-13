package tequila.cat.pigpig.component;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import tequila.cat.pigpig.KaleidoscopeStarduwally;

public final class ModDataComponents {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, KaleidoscopeStarduwally.MODID);

    /** 出售契约上绑定的牲畜 */
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<BoundLivestock>> BOUND_LIVESTOCK =
            DATA_COMPONENT_TYPES.register("bound_livestock",
                    () -> DataComponentType.<BoundLivestock>builder()
                            .persistent(BoundLivestock.CODEC)
                            .networkSynchronized(BoundLivestock.STREAM_CODEC)
                            .build());

    private ModDataComponents() {
    }
}
