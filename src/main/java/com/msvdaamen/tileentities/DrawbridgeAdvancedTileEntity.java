package com.msvdaamen.tileentities;

import com.msvdaamen.client.gui.drawbridge_advanced.DrawbridgeAdvancedContainer;
import com.msvdaamen.setup.Registration;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;

import javax.annotation.Nullable;

public class DrawbridgeAdvancedTileEntity extends BasicDrawbridgeTileEntity {

    public DrawbridgeAdvancedTileEntity() {
        super(Registration.DRAWBRIDGE_ADVANCED_TILE.get());
    }

    @Nullable
    @Override
    public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new DrawbridgeAdvancedContainer(i, world, pos, playerInventory, playerEntity);
    }
}
