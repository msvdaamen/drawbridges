package com.msvdaamen.client.gui.drawbridge_extended;

import com.mojang.blaze3d.systems.RenderSystem;
import com.msvdaamen.Drawbridges;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class DrawbridgeExtendedScreen extends ContainerScreen<DrawbridgeExtendedContainer> {
    public static final int WIDTH = 176;
    public static final int HEIGHT = 166;
    protected ResourceLocation GUI = new ResourceLocation(Drawbridges.MODID, "textures/gui/drawbridge.png");

    public DrawbridgeExtendedScreen(DrawbridgeExtendedContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    protected void init() {
        super.init();
//        BlockState state = this.te.getWorld().getBlockState(this.te.getPos());
        String name = "Redstone mode";
//        if(state.getValue(DrawbridgeExtended.PULSE_MODE)) {
//            name = "Pulse mode";
//        }
        this.addButton(new Button((guiLeft + WIDTH / 2) - 10, (guiTop + (HEIGHT / 3)), 90, 20, name, e -> {
            Drawbridges.LOGGER.info("Hallo redstone mode");
        }));
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
