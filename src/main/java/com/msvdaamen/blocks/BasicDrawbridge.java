package com.msvdaamen.blocks;

import com.msvdaamen.tileentities.DrawbridgeTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
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

    public static final BooleanProperty PULSE_MODE = BooleanProperty.create("pulse_mode");

    public BasicDrawbridge() {
        super(
            net.minecraft.block.Block.Properties.create(Material.IRON)
            .hardnessAndResistance(2.0f)
                .notSolid()
        );
    }

    @Override
    public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity entity) {
        super.onBlockHarvested(world, pos, state, entity);
        if (world.getTileEntity(pos) instanceof DrawbridgeTileEntity) {
            DrawbridgeTileEntity te = (DrawbridgeTileEntity) world.getTileEntity(pos);
            for(int i = 0; i < DrawbridgeTileEntity.SIZE; i++) {
                ItemStack copy = te.getItemHandler().getStackInSlot(i);
                dropItemIntoWorld(world, pos, copy);
            }
        }
    }

    public final void dropItemIntoWorld(World world, BlockPos pos, ItemStack item) {
        Random rand = new Random();

        if (item != null && item.getCount() > 0) {
            float rx = rand.nextFloat() * 0.8F + 0.1F;
            float ry = rand.nextFloat() * 0.8F + 0.1F;
            float rz = rand.nextFloat() * 0.8F + 0.1F;

            ItemEntity entityItem = new ItemEntity(world,
                    pos.getX() + rx, pos.getY() + ry, pos.getZ() + rz,
                    item.copy());

            if (item.hasTag()) {
                entityItem.getItem().setTag(item.getTag().copy());
            }

            float factor = 0.05F;
            entityItem.setMotion(
                    rand.nextGaussian() * factor,
                    rand.nextGaussian() * factor + 0.2F,
                    rand.nextGaussian() * factor);
            world.addEntity(entityItem);
            item.setCount(0);
        }
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
            world.setBlockState(pos, state.with(BlockStateProperties.FACING, getFacingFromEntity(pos, entity)).with(BlockStateProperties.POWERED, false).with(PULSE_MODE, false), 2);
        }
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


    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.FACING, BlockStateProperties.POWERED, PULSE_MODE);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public abstract TileEntity createTileEntity(BlockState state, IBlockReader world);
}
