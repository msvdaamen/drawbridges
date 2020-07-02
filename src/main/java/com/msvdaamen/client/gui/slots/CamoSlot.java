package com.msvdaamen.client.gui.slots;

import com.msvdaamen.setup.Registration;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class CamoSlot extends SlotItemHandler {

    public CamoSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public int getItemStackLimit(@Nonnull ItemStack stack) {
        return 1;
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack) {
        if(stack.getItem() instanceof BlockItem) {
            Block block = ((BlockItem) stack.getItem()).getBlock();
            if (!block.equals(Registration.DRAWBRIDGE.get()) && !block.equals(Registration.DRAWBRIDGE_EXTENDED.get()) && !block.equals(Registration.DRAWBRIDGE_ADVANCED.get())) {
                return true;
            }
        }
        return false;
    }
}
