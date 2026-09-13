package tequila.cat.pigpig.attachment;

import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import tequila.cat.pigpig.KaleidoscopeStarduwally;

public final class ModAttachments {
    public static final DeferredRegister<AttachmentType<?>> TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, KaleidoscopeStarduwally.MODID);

    /** 牲畜驯养状态。序列化到磁盘，实体死亡不复制（死亡即牲畜消失）。 */
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<LivestockData>> LIVESTOCK =
            TYPES.register("livestock",
                    () -> AttachmentType.builder(() -> LivestockData.EMPTY)
                            .serialize(LivestockData.CODEC)
                            .build());

    private ModAttachments() {
    }
}
