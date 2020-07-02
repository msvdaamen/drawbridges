package com.msvdaamen.network;

import com.msvdaamen.Drawbridges;
import com.msvdaamen.network.packages.PacketUpdateBlockstate;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class Networking {

    public static SimpleChannel INSTANCE;
    private static int ID = 0;

    public static int nextID() {
        return ID++;
    }

    public static void registerMessages() {
        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(Drawbridges.MODID, "drawbridges"), () -> "1.0", s -> true, s -> true);


        INSTANCE.registerMessage(
                nextID(),
                PacketUpdateBlockstate.class,
                PacketUpdateBlockstate::toBytes,
                PacketUpdateBlockstate::new,
                PacketUpdateBlockstate::handle
        );
//        INSTANCE.registerMessage(nextID(),
//                PacketSpawn.class,
//                PacketSpawn::toBytes,
//                PacketSpawn::new,
//
    }
}