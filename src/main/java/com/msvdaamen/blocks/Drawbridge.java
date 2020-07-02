package com.msvdaamen.blocks;

import com.msvdaamen.tileentities.DrawbridgeTileEntity;;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;

public class Drawbridge extends BasicDrawbridge {

    public Drawbridge() {
        super();
    }

    @Override
    void handleTick(ServerWorld world, BlockPos pos, BlockState state) {
        if (world.getTileEntity(pos) instanceof DrawbridgeTileEntity) {
            DrawbridgeTileEntity tile = (DrawbridgeTileEntity) world.getTileEntity(pos);

            if (state.get(BlockStateProperties.POWERED)) {
                tile.placeBlock();
            } else {
                tile.removeBlock();
            }

        }
    }

    private VoxelShape generatedShape(IBlockReader world, BlockPos pos) {
        if (world.getTileEntity(pos) instanceof DrawbridgeTileEntity) {
            DrawbridgeTileEntity te = (DrawbridgeTileEntity) world.getTileEntity(pos);
            if (te.getCamo() != null) {
                BlockState camoState = te.getCamo();
                VoxelShape shape = camoState.getShape(world, pos);
                return shape;
            }
        }
        return SHAPE;
    }

    @Override
    public boolean canStickTo(BlockState state, BlockState other) {
        return false;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return generatedShape(worldIn, pos);
    }

    @Override
    public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos)
    {
        return generatedShape(worldIn, pos);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos,
                                        ISelectionContext context) {
        return generatedShape(worldIn, pos);
    }


    @Override
    public int tickRate(IWorldReader worldIn) {
        return 20;
    }

    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (oldState.getBlock() != state.getBlock()) {
            this.updateState(worldIn, pos, state);
        }
    }

    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        this.updateState(worldIn, pos, state);
    }

    private void updateState(World worldIn, BlockPos pos, BlockState state) {
        boolean powered = worldIn.isBlockPowered(pos);
        if (powered != state.get(BlockStateProperties.POWERED)) {
            worldIn.setBlockState(pos, state.with(BlockStateProperties.POWERED, Boolean.valueOf(powered)));
        }
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new DrawbridgeTileEntity();
    }
}
