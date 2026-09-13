package tequila.cat.pigpig.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import tequila.cat.pigpig.KaleidoscopeStarduwally;
import tequila.cat.pigpig.ui.ShippingBinMenu;

public final class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
            DeferredRegister.create(Registries.MENU, KaleidoscopeStarduwally.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<ShippingBinMenu>> SHIPPING_BIN =
            MENU_TYPES.register("shipping_bin",
                    () -> new MenuType<>(ShippingBinMenu::new, FeatureFlags.DEFAULT_FLAGS));

    private ModMenuTypes() {
    }
}
