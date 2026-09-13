package tequila.cat.pigpig;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

import tequila.cat.pigpig.attachment.ModAttachments;
import tequila.cat.pigpig.component.ModDataComponents;
import tequila.cat.pigpig.data.CurrencyUtil;
import tequila.cat.pigpig.data.LivestockPrices;
import tequila.cat.pigpig.event.LivestockEvents;
import tequila.cat.pigpig.init.ModBlockEntities;
import tequila.cat.pigpig.init.ModBlocks;
import tequila.cat.pigpig.init.ModCreativeModeTabs;
import tequila.cat.pigpig.init.ModItems;
import tequila.cat.pigpig.init.ModMenuTypes;

@Mod(KaleidoscopeStarduwally.MODID)
public class KaleidoscopeStarduwally {
    public static final String MODID = "kaleidoscope_starduwally";
    public static final Logger LOGGER = LogUtils.getLogger();

    public KaleidoscopeStarduwally(IEventBus modEventBus, ModContainer modContainer) {
        // 注册表（mod 总线）
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModCreativeModeTabs.CREATIVE_MODE_TABS.register(modEventBus);
        ModAttachments.TYPES.register(modEventBus);
        ModDataComponents.DATA_COMPONENT_TYPES.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITY_TYPES.register(modEventBus);
        ModMenuTypes.MENU_TYPES.register(modEventBus);

        // 数据映射（数据包配置，/reload 生效）：牲畜回收定价 + 货币面额
        modEventBus.addListener(LivestockPrices::registerDataMapTypes);
        modEventBus.addListener(CurrencyUtil::registerDataMapTypes);

        // 运行时事件（NeoForge 游戏总线）
        NeoForge.EVENT_BUS.register(new LivestockEvents());
        NeoForge.EVENT_BUS.addListener(CurrencyUtil::onDataMapsUpdated);
    }
}
