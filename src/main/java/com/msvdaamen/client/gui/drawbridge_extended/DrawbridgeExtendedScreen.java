package com.msvdaamen.client.gui.drawbridge_extended;

import com.mojang.blaze3d.systems.RenderSystem;
import com.msvdaamen.Drawbridges;
import com.msvdaamen.blocks.DrawbridgeExtended;
import com.msvdaamen.network.Networking;
import com.msvdaamen.network.packages.PacketUpdateBlockstate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;

import static com.msvdaamen.blocks.BasicDrawbridge.PULSE_MODE;

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
        BlockState state = container.getTileEntity().getBlockState();
        String name = getButtonText(state.get(PULSE_MODE));
        this.addButton(new Button((guiLeft + WIDTH / 2) - 10, (guiTop + (HEIGHT / 3)), 90, 20, name, e -> {
            BlockPos pos = container.getTileEntity().getPos();
            BlockState oldState = container.getTileEntity().getBlockState();
            BlockState redstoneState = state.with(PULSE_MODE, !oldState.get(PULSE_MODE));
            if (!redstoneState.get(PULSE_MODE)) {
                boolean powered = container.getTileEntity().getWorld().isBlockPowered(pos);
                redstoneState = redstoneState.with(BlockStateProperties.POWERED, powered);
            }

            e.setMessage(getButtonText(redstoneState.get(PULSE_MODE)));

            container.getTileEntity().getWorld().setBlockState(pos, redstoneState);
            Networking.INSTANCE.sendToServer(new PacketUpdateBlockstate(getMinecraft().player.dimension, pos, Block.getStateId(redstoneState)));
        }));
    }

    private String getButtonText(boolean pulseMode) {
        if(pulseMode) {
            return "Pulse mode";
        }
        return "Redstone mode";
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
