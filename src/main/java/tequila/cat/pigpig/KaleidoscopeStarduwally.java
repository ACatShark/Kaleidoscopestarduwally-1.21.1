package tequila.cat.pigpig;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

import tequila.cat.pigpig.init.ModBlocks;
import tequila.cat.pigpig.init.ModItems;

@Mod(KaleidoscopeStarduwally.MODID)
public class KaleidoscopeStarduwally {
    public static final String MODID = "kaleidoscopestarduwally";
    public static final Logger LOGGER = LogUtils.getLogger();

    public KaleidoscopeStarduwally(IEventBus modEventBus, ModContainer modContainer) {
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        modEventBus.addListener(ModItems::addCreativeTabEntries);
    }
}
