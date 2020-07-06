package com.msvdaamen.blocks;

import com.msvdaamen.Drawbridges;
import com.msvdaamen.tileentities.DrawbridgeTileEntity;;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
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

//    void handleTick(ServerWorld world, BlockPos pos, BlockState state) {
//        if (world.getTileEntity(pos) instanceof DrawbridgeTileEntity) {
//            DrawbridgeTileEntity tile = (DrawbridgeTileEntity) world.getTileEntity(pos);
//
//            if (state.get(BlockStateProperties.POWERED)) {
//                tile.extend();
//            } else {
//                tile.retract();
//            }
//
//        }
//    }

    private VoxelShape generatedShape(IBlockReader world, BlockPos pos) {
        if (world.getTileEntity(pos) instanceof DrawbridgeTileEntity) {
            DrawbridgeTileEntity te = (DrawbridgeTileEntity) world.getTileEntity(pos);
            if (te.getCamo() != null) {
                BlockState camoState = te.getCamo();
                VoxelShape shape = camoState.getShape(world, pos);
                if (!shape.isEmpty()) {
                    return shape;
                }
                return Block.makeCuboidShape(0, 0, 0, 16, 16, 16);
            }
        }
        return SHAPE;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
        if (world.getTileEntity(pos) instanceof DrawbridgeTileEntity) {
            DrawbridgeTileEntity te = (DrawbridgeTileEntity) world.getTileEntity(pos);
            te.syncTimer(world, pos, state.get(BlockStateProperties.FACING));
        }
        super.onBlockPlacedBy(world, pos, state, entity, stack);
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

    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (oldState.getBlock() != state.getBlock()) {
            this.updateState(worldIn, pos, state);
        }
    }

    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        this.updateState(worldIn, pos, state);
    }

    protected void updateState(World worldIn, BlockPos pos, BlockState state) {
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
