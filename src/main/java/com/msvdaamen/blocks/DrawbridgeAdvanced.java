package com.msvdaamen.blocks;

import com.msvdaamen.tileentities.DrawbridgeAdvancedTileEntity;
import com.msvdaamen.tileentities.DrawbridgeTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;

public class DrawbridgeAdvanced extends BasicDrawbridge {

    public DrawbridgeAdvanced() {
        super();
    }

//    @Override
//    void handleTick(ServerWorld world, BlockPos pos, BlockState state) {
//
//    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        if (worldIn.getTileEntity(pos) instanceof DrawbridgeAdvancedTileEntity) {
            DrawbridgeAdvancedTileEntity te = (DrawbridgeAdvancedTileEntity) worldIn.getTileEntity(pos);
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
        return new DrawbridgeAdvancedTileEntity();
    }
}
