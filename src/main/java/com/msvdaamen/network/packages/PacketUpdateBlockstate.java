package com.msvdaamen.network.packages;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketUpdateBlockstate {

    private final DimensionType type;
    private BlockPos blockPos;

    public PacketUpdateBlockstate(DimensionType type, BlockPos pos) {
        this.type = type;
        this.blockPos = pos;
    }

    public PacketUpdateBlockstate(PacketBuffer buf) {
        type = DimensionType.getById(buf.readInt());
        blockPos = BlockPos.fromLong(buf.readLong());
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeInt(type.getId());
        buf.writeBlockPos(blockPos);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerWorld spawnWorld = ctx.get().getSender().world.getServer().getWorld(type);
            spawnWorld.getChunkProvider().markBlockChanged(blockPos);
        });
    }

}
