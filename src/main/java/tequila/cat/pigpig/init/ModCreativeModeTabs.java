package tequila.cat.pigpig.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;

import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import tequila.cat.pigpig.KaleidoscopeStarduwally;

public final class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, KaleidoscopeStarduwally.MODID);

    /**
     * NeoForge 每页最多放 10 个标签页，前 5 个在上排。这里排在 REDSTONE_BLOCKS 之前，
     * 使本标签页落在第一页上排的末位，即创造模式物品栏的右上角。
     */
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> STARDUWALLY_TAB =
            CREATIVE_MODE_TABS.register("starduwally_tab", () -> CreativeModeTab.builder()
                    .withTabsBefore(CreativeModeTabs.REDSTONE_BLOCKS)
                    .icon(() -> new ItemStack(ModItems.CAULIFLOWER.get()))
                    .title(Component.translatable("itemGroup.kaleidoscope_starduwally"))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.GARLIC.get());
                        output.accept(ModItems.GARLIC_CLOVE.get());
                        output.accept(ModItems.CAULIFLOWER.get());
                        output.accept(ModItems.CAULIFLOWER_SEEDS.get());
                        output.accept(ModItems.KALE.get());
                        output.accept(ModItems.KALE_SEEDS.get());
                    })
                    .build());

    private ModCreativeModeTabs() {
    }
}
