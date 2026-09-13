package tequila.cat.pigpig.ui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * 交易站界面：先直接复用原版潜影盒贴图（176x166，3x9 布局一致），占位。
 */
public class ShippingBinScreen extends AbstractContainerScreen<ShippingBinMenu> {
    // 潜影盒 GUI：尺寸 176x166，与 3x9 容器布局完全一致
    private static final ResourceLocation TEXTURE =
            ResourceLocation.withDefaultNamespace("textures/gui/container/shulker_box.png");

    public ShippingBinScreen(ShippingBinMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        int x = this.titleLabelX;
        int y = this.titleLabelY;
        guiGraphics.drawString(this.font, this.title, x, y, 0x404040, false);
    }
}
