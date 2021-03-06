package com.msvdaamen.client.gui.drawbridge_advanced;

import com.mojang.blaze3d.systems.RenderSystem;
import com.msvdaamen.Drawbridges;
import com.msvdaamen.client.gui.drawbridge.DrawbridgeContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class DrawbridgeAdvancedScreen extends ContainerScreen<DrawbridgeAdvancedContainer> {

    protected ResourceLocation GUI = new ResourceLocation(Drawbridges.MODID, "textures/gui/drawbridge_advanced.png");

    public DrawbridgeAdvancedScreen(DrawbridgeAdvancedContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(GUI);
        int relX = (this.width - this.xSize) / 2;
        int relY = (this.height - this.ySize) / 2;
        this.blit(relX, relY, 0, 0, this.xSize, this.ySize);
    }
}
