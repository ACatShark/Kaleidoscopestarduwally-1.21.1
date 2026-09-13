package tequila.cat.pigpig.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

import tequila.cat.pigpig.KaleidoscopeStarduwally;
import tequila.cat.pigpig.init.ModMenuTypes;
import tequila.cat.pigpig.ui.ShippingBinScreen;

/** 客户端专属事件（mod 总线）：菜单屏幕注册。 */
@EventBusSubscriber(modid = KaleidoscopeStarduwally.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientModEvents {
    @SubscribeEvent
    public static void onRegisterMenuScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.SHIPPING_BIN.get(), ShippingBinScreen::new);
    }

    private ClientModEvents() {
    }
}
