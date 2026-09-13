package tequila.cat.pigpig.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.core.UUIDUtil;

/**
 * 驯养牲畜数据（挂在实体附件上，服务端权威）。
 * 所有新增字段务必用 optionalFieldOf，保证旧存档读档兼容。
 */
public record LivestockData(
        @Nullable UUID owner,
        String ownerName,
        int feedCount,
        int lastFedDay
) {
    /** 未驯养默认值 */
    public static final LivestockData EMPTY = new LivestockData(null, "", 0, 0);

    public static final Codec<LivestockData> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            UUIDUtil.CODEC.optionalFieldOf("owner").forGetter(d -> Optional.ofNullable(d.owner())),
            Codec.STRING.optionalFieldOf("owner_name", "").forGetter(LivestockData::ownerName),
            Codec.INT.optionalFieldOf("feed_count", 0).forGetter(LivestockData::feedCount),
            Codec.INT.optionalFieldOf("last_fed_day", 0).forGetter(LivestockData::lastFedDay)
    ).apply(inst, (owner, name, count, day) -> new LivestockData(owner.orElse(null), name, count, day)));

    public boolean isTamed() {
        return this.owner != null;
    }

    /** 喂一次（首次同时完成驯养绑定）。feedGain 为该次获得的饲养度。 */
    public LivestockData feed(UUID ownerUuid, String ownerDisplayName, int day, int feedGain) {
        return new LivestockData(ownerUuid, ownerDisplayName, this.feedCount + feedGain, day);
    }

    /** 解除契约：清空绑定 */
    public static LivestockData untame() {
        return EMPTY;
    }
}
