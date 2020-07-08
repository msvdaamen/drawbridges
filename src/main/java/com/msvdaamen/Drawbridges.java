package com.msvdaamen;

import com.msvdaamen.proxy.ClientProxy;
import com.msvdaamen.proxy.IProxy;
import com.msvdaamen.proxy.ServerProxy;
import com.msvdaamen.setup.ClientSetup;
import com.msvdaamen.setup.ModSetup;
import com.msvdaamen.setup.Registration;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Callable;

@Mod("drawbridges")
public class Drawbridges {

    public static final String MODID = "drawbridges";

    public static final IProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());

    public static final Logger LOGGER = LogManager.getLogger();


    public Drawbridges() {
//        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONFIG);
//        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);

        Registration.init();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(ModSetup::init);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::init);
    }

}