package com.msvdaamen.blocks;


import com.msvdaamen.tileentities.DrawbridgeExtendedTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;

public class DrawbridgeExtended extends BasicDrawbridge {

    public DrawbridgeExtended() {
        super();
    }

    @Override
    void handleTick(ServerWorld world, BlockPos pos, BlockState state) {

    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        if (worldIn.getTileEntity(pos) instanceof DrawbridgeExtendedTileEntity) {
            DrawbridgeExtendedTileEntity te = (DrawbridgeExtendedTileEntity) worldIn.getTileEntity(pos);
            if (te.getCamo() != null) {
                BlockState camoState = te.getCamo();
                VoxelShape shape = camoState.getShape(worldIn, pos);
                return shape;
            }
        }
        return SHAPE;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new DrawbridgeExtendedTileEntity();
    }
}
