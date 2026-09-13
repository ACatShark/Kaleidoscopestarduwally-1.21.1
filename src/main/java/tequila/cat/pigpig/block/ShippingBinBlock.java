package tequila.cat.pigpig.block;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import tequila.cat.pigpig.KaleidoscopeStarduwally;
import tequila.cat.pigpig.component.ModDataComponents;
import tequila.cat.pigpig.data.CurrencyUtil;
import tequila.cat.pigpig.data.ShippingLogic;
public class ShippingBinBlock extends Block implements EntityBlock {
    public static final MapCodec<ShippingBinBlock> CODEC = simpleCodec(ShippingBinBlock::new);

    public static final Component TITLE =
            Component.translatable("block." + KaleidoscopeStarduwally.MODID + ".shipping_bin");

    public ShippingBinBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ShippingBinBlockEntity(pos, state);
    }

    // 手持已签订契约：售出；其余交给默认流程
    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                              Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!level.isClientSide
                && hand == InteractionHand.MAIN_HAND
                && stack.has(ModDataComponents.BOUND_LIVESTOCK)
                && player instanceof ServerPlayer serverPlayer
                && level instanceof ServerLevel serverLevel) {
            boolean ok = ShippingLogic.sellAtStation(serverLevel, pos, serverPlayer, stack);
            return ok ? ItemInteractionResult.SUCCESS : ItemInteractionResult.FAIL;
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    // 空手/非放置类物品右键：打开箱子界面
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof MenuProvider menuProvider) {
                player.openMenu(menuProvider);
                return InteractionResult.CONSUME;
            }
        }
        return InteractionResult.SUCCESS;
    }

    // 拆毁时掉落容器内货币
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            if (level.getBlockEntity(pos) instanceof ShippingBinBlockEntity bin) {
                for (int i = 0; i < bin.getContainerSize(); i++) {
                    ItemStack stack = bin.getItem(i);
                    if (!stack.isEmpty()) {
                        CurrencyUtil.dropStack(level, pos, stack);
                    }
                }
            }
            super.onRemove(state, level, pos, newState, movedByPiston);
        }
    }
}
