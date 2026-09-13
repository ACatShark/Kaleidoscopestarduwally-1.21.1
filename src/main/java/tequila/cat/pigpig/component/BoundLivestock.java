package tequila.cat.pigpig.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

/**
 * 出售契约上绑定的牲畜信息。
 * 价格在签订契约瞬间锁定，避免签订后再喂食刷价。
 */
public record BoundLivestock(
        @Nullable UUID targetUuid,
        ResourceLocation entityType,
        String displayName,
        int priceFen
) {
    public static final StreamCodec<ByteBuf, UUID> UUID_STREAM = UUIDUtil.STREAM_CODEC;

    public static final StreamCodec<RegistryFriendlyByteBuf, BoundLivestock> STREAM_CODEC =
            StreamCodec.composite(
                    UUID_STREAM, BoundLivestock::targetUuid,
                    ResourceLocation.STREAM_CODEC, BoundLivestock::entityType,
                    ByteBufCodecs.STRING_UTF8, BoundLivestock::displayName,
                    ByteBufCodecs.VAR_INT, BoundLivestock::priceFen,
                    BoundLivestock::new);

    public static final Codec<BoundLivestock> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            UUIDUtil.CODEC.optionalFieldOf("target_uuid").forGetter(b -> Optional.ofNullable(b.targetUuid())),
            ResourceLocation.CODEC.fieldOf("entity_type").forGetter(BoundLivestock::entityType),
            Codec.STRING.fieldOf("display_name").forGetter(BoundLivestock::displayName),
            Codec.INT.fieldOf("price_fen").forGetter(BoundLivestock::priceFen)
    ).apply(inst, (uuid, type, name, price) -> new BoundLivestock(uuid.orElse(null), type, name, price)));
}
