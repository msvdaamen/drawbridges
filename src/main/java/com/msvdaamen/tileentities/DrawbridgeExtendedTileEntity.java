package com.msvdaamen.tileentities;

import com.msvdaamen.client.gui.drawbridge_extended.DrawbridgeExtendedContainer;
import com.msvdaamen.setup.Registration;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DrawbridgeExtendedTileEntity extends BasicDrawbridgeTileEntity {

    private LazyOptional<IItemHandler> handler = LazyOptional.of(this::createHandler);
    public static final int SIZE = 2;
    private int timer = 20;
    private int blocksPlaced = 0;
    private int maxBlocks = 64;
    private ItemStack placedBlock = ItemStack.EMPTY;

    public DrawbridgeExtendedTileEntity() {
        super(Registration.DRAWBRIDGE_EXTENDED_TILE.get());
    }

    private IItemHandler createHandler() {
        return new ItemStackHandler(SIZE) {

            @Override
            protected void onContentsChanged(int slot) {
                if (slot == 0) {
                    if (!getStackInSlot(slot).isEmpty()) {
                        BlockState camo = Block.getBlockFromItem(getStackInSlot(slot).getItem()).getDefaultState();
                        if (camo.has(BlockStateProperties.FACING)) {
                            camo = camo.with(BlockStateProperties.FACING, getWorld().getBlockState(getPos()).get(BlockStateProperties.FACING));
                        }
                        if(camo.has(BlockStateProperties.HORIZONTAL_FACING)) {
                            Direction dir = getWorld().getBlockState(getPos()).get(BlockStateProperties.FACING);
                            if (dir == Direction.DOWN || dir == Direction.UP) {
                                dir = Direction.NORTH;
                            }
                            camo = camo.with(BlockStateProperties.HORIZONTAL_FACING, dir);
                        }
                        setCamo(camo);

                    } else {
                        setCamo(null);
                    }
                }
                if (slot == 1) {
                    ItemStack copy = getStackInSlot(1).copy();
                    if (!copy.isEmpty()) {
                        copy.setCount(1);
                        placedBlock = copy;
                    }
                }
                markDirty();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return true;
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                if (slot == 1) {
                    if (!stack.isEmpty()) {
                        ItemStack copy = stack.copy();
                        copy.setCount(1);
                        placedBlock = copy;
                    }
                }
                return super.insertItem(slot, stack, simulate);
            }

            @Override
            public int getSlotLimit(int slot) {
                if (slot == 0) {
                    return 1;
                }
                if (slot == 1) {
                    return maxBlocks;
                }
                return super.getSlotLimit(slot);
            }

            @Override
            protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
                if (slot == 0) {
                    return 1;
                }
                if (slot == 1) {
                    return maxBlocks;
                }
                return super.getStackLimit(slot, stack);
            }
        };
    }

//    @Nonnull
//    @Override
//    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
//        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
//            return handler.cast();
//        }
//        return super.getCapability(cap, side);
//    }

    public void syncTimer(World world, BlockPos pos, Direction facing) {
        for(Direction dir: Direction.values()) {
            if (world.getTileEntity(pos.offset(dir)) instanceof DrawbridgeExtendedTileEntity) {
                DrawbridgeExtendedTileEntity tile = (DrawbridgeExtendedTileEntity) world.getTileEntity(pos.offset(dir));
                this.setTimer(tile.getTimer());
                break;
            }
        }

//        if (world.getBlockState(neighbor).getBlock().equals(getBlockState().getBlock())) {
//
//        }
    }

    public IItemHandler getItemHandler() {
        return handler.orElseThrow(RuntimeException::new);
    }

    @Override
    public void tick() {
        if (getWorld().isRemote()) {
            return;
        }
        if (timer == 0) {
            timer = 20;
            if (getBlockState().get(BlockStateProperties.POWERED)) {
                extend();
            } else {
                retract();
            }
        }
        timer--;
    }

    public boolean retract() {
        if (!hasBlocksPlaced()) {
            return false;
        }
        if (!hasInvSpace()) {
            return false;
        }

        if (!getItemHandler().getStackInSlot(1).isItemEqual(placedBlock) && !getItemHandler().getStackInSlot(1).isEmpty()) {
            return false;
        }
        Direction dir = getBlockState().get(BlockStateProperties.FACING);

        int offset = 1;
        for(int i = 1; i <= blocksPlaced; i++) {
            BlockPos pos = getPos().offset(dir, i);
            BlockState state = getWorld().getBlockState(pos);
            if (state.isAir(getWorld(), pos) || !state.getBlock().equals(Block.getBlockFromItem(placedBlock.getItem()))) {
                break;
            }
            offset = i;
        }
        BlockPos pos = getPos().offset(dir, offset);
        if (!getWorld().getBlockState(pos).getBlock().equals(Block.getBlockFromItem(placedBlock.getItem()))) {
            return false;
        }
        blocksPlaced = offset;
        getWorld().setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
        removeBlocksPlaced();
        addItemToInv();
        return true;
    }

    public boolean extend() {
        if (this.hasMaxedPlaced()) {
            return false;
        }
        BlockState placeBlockState = this.getPlaceBlockState();
        if (placeBlockState == null) {
            return false;
        }
        Direction dir = getBlockState().get(BlockStateProperties.FACING);
        BlockPos pos = getExtendPosition(dir);
        if (pos == null) {
            return false;
        }
        getWorld().setBlockState(pos, placeBlockState, 3);
        blocksPlaced++;
        this.removeFromInv();
        return true;
    }

    private BlockPos getExtendPosition(Direction dir) {
        for(int i = 1; i <= (blocksPlaced + 1); i++) {
            BlockPos pos = getPos().offset(dir, i);
            BlockState state = getWorld().getBlockState(pos);

            if (state.getBlock().equals(Block.getBlockFromItem(placedBlock.getItem()))) {
                continue;
            }
            if ((state.isAir(getWorld(), pos) || isFluid(state.getBlock())) && state.isValidPosition(getWorld(), pos)) {
                return pos;
            } else {
                return null;
            }
        }
        return null;
    }

    public boolean isFluid(Block b) {
        return b instanceof IFluidBlock || b instanceof FlowingFluidBlock;
    }

    @Nullable
    @Override
    public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new DrawbridgeExtendedContainer(i, world, pos, playerInventory, playerEntity);
    }

    @Override
    public void read(CompoundNBT tag) {
        if(tag.contains("placedBlock")) {
            this.placedBlock = ItemStack.read(tag.getCompound("placedBlock"));
        }
        blocksPlaced = tag.getInt("blocksPlaced");
        CompoundNBT invTag = tag.getCompound("inv");
        handler.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(invTag));
        super.read(tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        if(!this.placedBlock.isEmpty()) {
            tag.put("placedBlock", placedBlock.serializeNBT());
        }
        tag.putInt("blocksPlaced", blocksPlaced);
        handler.ifPresent(h -> {
            CompoundNBT compound = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
            tag.put("inv", compound);
        });
        return super.write(tag);
    }

    private BlockState getPlaceBlockState() {
        if (!getItemHandler().getStackInSlot(1).isEmpty()) {
            return Block.getBlockFromItem(getItemHandler().getStackInSlot(1).getItem()).getDefaultState();
        }
        return null;
    }

    private void removeFromInv() {
        this.getItemHandler().getStackInSlot(1).shrink(1);
    }

    private void addItemToInv() {
        if (this.getItemHandler().getStackInSlot(1).isEmpty()) {
            ItemStack itemStack = placedBlock.copy();
            itemStack.setCount(1);
            this.getItemHandler().insertItem(1, itemStack, false);
        } else {
            this.getItemHandler().getStackInSlot(1).grow(1);
        }
    }

    private boolean hasInvSpace() {
        if (getItemHandler().getStackInSlot(1).isEmpty()) {
            return true;
        }
        return getItemHandler().getStackInSlot(0).getMaxStackSize() != getItemHandler().getStackInSlot(1).getCount();
    }

    private boolean hasMaxedPlaced() {
        return this.blocksPlaced == this.maxBlocks;
    }

    private void addBlocksPlaced() {
        this.blocksPlaced++;
    }

    private void removeBlocksPlaced() {
        this.blocksPlaced--;
    }

    private boolean hasBlocksPlaced() {
        return this.blocksPlaced != 0;
    }

    public int getTimer() {
        return timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }
}
