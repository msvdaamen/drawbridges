package com.msvdaamen.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Random;

public abstract class BasicDrawbridge extends Block {

    private VoxelShape SHAPE_BOTTOM_FRONT = Block.makeCuboidShape(0, 0, 0, 16, 2, 2);
    private VoxelShape SHAPE_BOTTOM_LEFT = Block.makeCuboidShape(0, 0, 0, 2, 2, 16);
    private VoxelShape SHAPE_BOTTOM_RIGHT = Block.makeCuboidShape(14, 0, 0, 16, 2, 16);
    private VoxelShape SHAPE_BOTTOM_BACK = Block.makeCuboidShape(0, 0, 14, 16, 2, 16);

    private VoxelShape SHAPE_TOP_FRONT = Block.makeCuboidShape(0, 14, 0, 16, 16, 2);
    private VoxelShape SHAPE_TOP_LEFT = Block.makeCuboidShape(0, 14, 0, 2, 16, 16);
    private VoxelShape SHAPE_TOP_RIGHT = Block.makeCuboidShape(14, 14, 0, 16, 16, 16);
    private VoxelShape SHAPE_TOP_BACK = Block.makeCuboidShape(0, 14, 14, 16, 16, 16);

    private VoxelShape SHAPE_LEFT_FRONT = Block.makeCuboidShape(0, 2, 0, 2, 14, 2);
    private VoxelShape SHAPE_LEFT_BACK = Block.makeCuboidShape(0, 2, 14, 2, 14, 16);
    private VoxelShape SHAPE_RIGHT_FRONT = Block.makeCuboidShape(14, 2, 0, 16, 14, 2);
    private VoxelShape SHAPE_RIGHT_BACK = Block.makeCuboidShape(14, 2, 14, 16, 14, 16);

    private VoxelShape SHAPE_CENTER = Block.makeCuboidShape(1, 1, 1, 15, 15, 15);

    protected VoxelShape SHAPE = VoxelShapes.or(
            SHAPE_BOTTOM_FRONT,
            SHAPE_BOTTOM_LEFT,
            SHAPE_BOTTOM_RIGHT,
            SHAPE_BOTTOM_BACK,
            SHAPE_TOP_FRONT,
            SHAPE_TOP_LEFT,
            SHAPE_TOP_RIGHT,
            SHAPE_TOP_BACK,
            SHAPE_LEFT_FRONT,
            SHAPE_LEFT_BACK,
            SHAPE_RIGHT_FRONT,
            SHAPE_RIGHT_BACK,
            SHAPE_CENTER
    ).simplify();

    public BasicDrawbridge() {
        super(
            net.minecraft.block.Block.Properties.create(Material.IRON)
            .hardnessAndResistance(2.0f)
        );
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return false;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult trace) {
        if (!world.isRemote) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof INamedContainerProvider) {
                NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) tileEntity, tileEntity.getPos());
            } else {
                throw new IllegalStateException("Our named container provider is missing!");
            }
            return ActionResultType.SUCCESS;
        }
        return super.onBlockActivated(state, world, pos, player, hand, trace);
    }

    @Override
    public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return  Block.makeCuboidShape(1, 1, 1, 15, 15, 15);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
        if (entity != null) {
            world.setBlockState(pos, state.with(BlockStateProperties.FACING, getFacingFromEntity(pos, entity)).with(BlockStateProperties.POWERED, false), 2);
        }
        world.getPendingBlockTicks().scheduleTick(pos, this, this.tickRate(world));
    }

    public static Direction getFacingFromEntity(BlockPos clickedBlock, LivingEntity entity) {
        Vec3d vec = entity.getPositionVec();
        return Direction.getFacingFromVector((float) (vec.x - clickedBlock.getX()), (float) (vec.y - clickedBlock.getY()), (float) (vec.z - clickedBlock.getZ()));
    }

    @Override
    public boolean canProvidePower(BlockState state) {
        return false;
    }

    @Override
    public boolean ticksRandomly(BlockState state) {
        return false;
    }

    abstract void handleTick(ServerWorld world, BlockPos pos, BlockState state);

    @Deprecated
    public void tick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
        if(!world.isRemote) {
            handleTick(world, pos, state);
        }
        world.getPendingBlockTicks().scheduleTick(pos, this, this.tickRate(world));
    }


    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.FACING, BlockStateProperties.POWERED);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public abstract TileEntity createTileEntity(BlockState state, IBlockReader world);
}
