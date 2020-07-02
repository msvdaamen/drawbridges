package com.msvdaamen.tileentities;

import com.msvdaamen.client.gui.drawbridge_extended.DrawbridgeExtendedContainer;
import com.msvdaamen.setup.Registration;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;

import javax.annotation.Nullable;

public class DrawbridgeExtendedTileEntity extends BasicDrawbridgeTileEntity {

    public DrawbridgeExtendedTileEntity() {
        super(Registration.DRAWBRIDGE_EXTENDED_TILE.get());
    }

    @Nullable
    @Override
    public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new DrawbridgeExtendedContainer(i, world, pos, playerInventory, playerEntity);
    }
}
