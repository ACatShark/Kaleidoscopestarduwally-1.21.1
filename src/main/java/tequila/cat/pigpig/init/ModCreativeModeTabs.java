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

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> STARDUWALLY_TAB =
            CREATIVE_MODE_TABS.register("starduwally_tab", () -> CreativeModeTab.builder()
                    .withTabsBefore(CreativeModeTabs.REDSTONE_BLOCKS)
                    .icon(() -> new ItemStack(ModItems.CAULIFLOWER.get()))
                    .title(Component.translatable("itemGroup.kaleidoscope_starduwally"))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.CAULIFLOWER.get());
                        output.accept(ModItems.CAULIFLOWER_SEEDS.get());
                        output.accept(ModItems.PARSNIP.get());
                        output.accept(ModItems.RHUBARB.get());
                        output.accept(ModItems.RHUBARB_SEEDS.get());
                        output.accept(ModItems.MELON_SLICE.get());
                        output.accept(ModItems.MELON_SEEDS.get());
                        output.accept(ModItems.AMARANTH.get());
                        output.accept(ModItems.AMARANTH_SEEDS.get());
                        output.accept(ModItems.FROST_MELON.get());
                        output.accept(ModItems.FROST_MELON_SEEDS.get());
                    })
                    .build());

    private ModCreativeModeTabs() {
    }
}
