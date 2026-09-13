package tequila.cat.pigpig.ui;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import tequila.cat.pigpig.block.ShippingBinBlockEntity;
import tequila.cat.pigpig.init.ModMenuTypes;

/**
 * 交易站 27 格容器菜单。
 * 服务端：绑定方块实体容器；客户端：哑容器，内容由原版容器同步机制填充。
 */
public class ShippingBinMenu extends AbstractContainerMenu {
    private static final int CONTAINER_ROWS = 3;
    private static final int CONTAINER_COLS = 9;

    private final Container container;

    // 服务端：绑定真实容器
    public ShippingBinMenu(int containerId, Inventory playerInventory, Container container) {
        super(ModMenuTypes.SHIPPING_BIN.get(), containerId);
        this.container = container;
        checkContainerSize(container, ShippingBinBlockEntity.SIZE);
        container.startOpen(playerInventory.player);

        // 交易站容器（3x9），位于 y=18..54，与 shulker_box 贴图一致
        int index = 0;
        for (int row = 0; row < CONTAINER_ROWS; row++) {
            for (int col = 0; col < CONTAINER_COLS; col++) {
                this.addSlot(new Slot(container, index++, 8 + col * 18, 18 + row * 18));
            }
        }
        this.addPlayerSlots(playerInventory, 84);
    }

    // 客户端：哑容器（仅占位，槽位内容由服务端同步）
    public ShippingBinMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(ShippingBinBlockEntity.SIZE));
    }

    private void addPlayerSlots(Inventory playerInventory, int startY) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, startY + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, startY + 58));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);
        if (slot != null && slot.hasItem()) {
            ItemStack current = slot.getItem();
            result = current.copy();
            if (slotIndex < CONTAINER_ROWS * CONTAINER_COLS) {
                // 交易站 -> 玩家背包/快捷栏
                if (!this.moveItemStackTo(current, 27, 63, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(current, 0, CONTAINER_ROWS * CONTAINER_COLS, false)) {
                return ItemStack.EMPTY;
            }
            if (current.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            if (current.getCount() == result.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(player, current);
        }
        return result;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.container.stopOpen(player);
    }
}
