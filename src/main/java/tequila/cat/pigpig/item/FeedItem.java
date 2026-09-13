package tequila.cat.pigpig.item;

import net.minecraft.world.item.Item;

/**
 * 饲料物品。类别决定适用范围与饲养度收益（喂食逻辑在事件中统一处理）。
 */
public class FeedItem extends Item {
    public enum FeedType {
        /** 家畜：牛羊猪等 */
        LIVESTOCK(1),
        /** 家禽：鸡兔等 */
        POULTRY(1),
        /** 通用精饲料：所有可驯养动物，收益更高 */
        PREMIUM(3);

        private final int gain;

        FeedType(int gain) {
            this.gain = gain;
        }

        public int gain() {
            return this.gain;
        }
    }

    private final FeedType type;

    public FeedItem(FeedType type, Properties properties) {
        super(properties);
        this.type = type;
    }

    public FeedType getType() {
        return this.type;
    }
}
