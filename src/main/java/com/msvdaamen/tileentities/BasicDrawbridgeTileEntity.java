package com.msvdaamen.tileentities;

import net.minecraft.block.BlockState;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.client.model.ModelDataManager;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class BasicDrawbridgeTileEntity extends TileEntity implements ITickableTileEntity, INamedContainerProvider {

    public static final ModelProperty<BlockState> CAMO = new ModelProperty<>();

    private BlockState camo;

    public BasicDrawbridgeTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public void setCamo(BlockState camo) {
        this.camo = camo;
        markDirty();
        world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
    }

    public BlockState getCamo() {
        return camo;
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tag = super.getUpdateTag();
        if (camo != null) {
            tag.put("camo", NBTUtil.writeBlockState(camo));
        }
        return tag;
    }


    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(pos, 1, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        ModelDataManager.requestModelDataRefresh(this);
        world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
//        BlockState oldMimic = camo;
//        CompoundNBT tag = pkt.getNbtCompound();
//        if (tag.contains("camo")) {
//            camo = NBTUtil.readBlockState(tag.getCompound("camo"));
//            Drawbridges.LOGGER.error(camo);
////            if (!Objects.equals(oldMimic, camo)) {
//                ModelDataManager.requestModelDataRefresh(this);
//                world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
//            }
//        }
    }

    @Nonnull
    @Override
    public IModelData getModelData() {
        return new ModelDataMap.Builder()
                .withInitial(CAMO, camo)
                .build();
    }

    @Override
    public void read(CompoundNBT tag) {
        if (tag.contains("camo")) {
            camo = NBTUtil.readBlockState(tag.getCompound("camo"));
        }
        super.read(tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        if (camo != null) {
            tag.put("camo", NBTUtil.writeBlockState(camo));
        }
        return super.write(tag);
    }


    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent(getType().getRegistryName().getPath());
    }
}
