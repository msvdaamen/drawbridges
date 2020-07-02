package com.msvdaamen.client.blocks.drawbridge;

import com.google.common.collect.ImmutableList;
import com.msvdaamen.Drawbridges;
import com.msvdaamen.blocks.Drawbridge;
import com.msvdaamen.tileentities.DrawbridgeTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.GrassBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.TransformationMatrix;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.pipeline.BakedQuadBuilder;
import net.minecraftforge.client.model.pipeline.TRSRTransformer;
import net.minecraftforge.common.model.TransformationHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class DrawbridgeBakedModel implements IDynamicBakedModel {

    private final IBakedModel drawbridge;
    protected TransformationMatrix transformation;

    public DrawbridgeBakedModel(IBakedModel drawbridge) {
        this.drawbridge = drawbridge;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
        Direction direction = state.get(BlockStateProperties.FACING);

        BlockState camo = extraData.getData(DrawbridgeTileEntity.CAMO);
        if (camo != null && !(camo.getBlock() instanceof Drawbridge)) {
            if (camo.equals(Blocks.GRASS_BLOCK.getDefaultState())) {
                Drawbridges.LOGGER.info(camo);
            }
            ModelResourceLocation location = BlockModelShapes.getModelLocation(camo);
            if (location != null) {
                IBakedModel model = Minecraft.getInstance().getModelManager().getModel(location);
                if (model != null) {
                    List<BakedQuad> quads = model.getQuads(camo, side, rand, extraData);
//                    if (quads.size() > 0) {
                        return quads;
//                    }
                }
            }
        }

        if (side != null) {
            return Collections.emptyList();
        }

        Quaternion quaternion;
        if (direction == Direction.UP) {
            quaternion = TransformationHelper.quatFromXYZ(new Vector3f(90, 0, 0), true);
        } else if (direction == Direction.DOWN) {
            quaternion = TransformationHelper.quatFromXYZ(new Vector3f(270, 0, 0), true);
        } else {
            double r = Math.PI * (360 - direction.getOpposite().getHorizontalIndex() * 90) / 180d;

            quaternion = TransformationHelper.quatFromXYZ(new Vector3f(0, (float) r, 0), false);
        }
        this.transformation = new TransformationMatrix(null, quaternion, null, null);

        ImmutableList.Builder<BakedQuad> quads = ImmutableList.builder();
        for (BakedQuad quad : this.drawbridge.getQuads(state, side, rand, EmptyModelData.INSTANCE)) {
            BakedQuadBuilder builder = new BakedQuadBuilder(quad.func_187508_a());

            TRSRTransformer transformer = new TRSRTransformer(builder, transformation.blockCenterToCorner());
            quad.pipe(transformer);
            quads.add(builder.build());
        }
        return quads.build();
    }

    @Override
    public boolean isAmbientOcclusion() {
        return drawbridge.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return drawbridge.isGui3d();
    }

    @Override
    public boolean func_230044_c_() {
        return true;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return drawbridge.isBuiltInRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return drawbridge.getParticleTexture();
    }

    @Override
    public ItemOverrideList getOverrides() {
        return drawbridge.getOverrides();
    }
}
