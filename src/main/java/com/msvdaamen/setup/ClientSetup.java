package com.msvdaamen.setup;

import com.msvdaamen.Drawbridges;
import com.msvdaamen.client.blocks.drawbridge.DrawbridgeBakedModel;
import com.msvdaamen.client.gui.drawbridge.DrawbridgeScreen;
import com.msvdaamen.client.gui.drawbridge_advanced.DrawbridgeAdvancedScreen;
import com.msvdaamen.client.gui.drawbridge_extended.DrawbridgeExtendedScreen;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Drawbridges.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {


    public static void init(final FMLClientSetupEvent event) {
        ScreenManager.registerFactory(Registration.DRAWBRIDGE_CONTAINER.get(), DrawbridgeScreen::new);
        ScreenManager.registerFactory(Registration.DRAWBRIDGE_EXTENDED_CONTAINER.get(), DrawbridgeExtendedScreen::new);
        ScreenManager.registerFactory(Registration.DRAWBRIDGE_ADVANCED_CONTAINER.get(), DrawbridgeAdvancedScreen::new);
//        RenderingRegistry.registerEntityRenderingHandler(Registration.WEIRDMOB.get(), WeirdMobRenderer::new);
//        ModelLoaderRegistry.registerLoader(new ResourceLocation(Drawbridges.MODID, "drawbridge"), new DrawbridgeModelLoader());
//        MagicRenderer.register();
//        MinecraftForge.EVENT_BUS.addListener(InWorldRenderer::render);
//        MinecraftForge.EVENT_BUS.addListener(AfterLivingRenderer::render);

        ModelLoader.addSpecialModel(new ResourceLocation(Drawbridges.MODID, "block/drawbridge"));
        ModelLoader.addSpecialModel(new ResourceLocation(Drawbridges.MODID, "block/drawbridge_extended"));
        ModelLoader.addSpecialModel(new ResourceLocation(Drawbridges.MODID, "block/drawbridge_advanced"));

        RenderTypeLookup.setRenderLayer(Registration.DRAWBRIDGE.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(Registration.DRAWBRIDGE_EXTENDED.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(Registration.DRAWBRIDGE_ADVANCED.get(), RenderType.getTranslucent());
    }

    @SubscribeEvent
    public static void onModelBake(ModelBakeEvent e) {
        for (ResourceLocation id : e.getModelRegistry().keySet()) {
            if (isDrawbridge(id, Registration.DRAWBRIDGE.getId())) {
                e.getModelRegistry().put(id, new DrawbridgeBakedModel(
                        e.getModelRegistry().get(new ResourceLocation(Drawbridges.MODID,"block/drawbridge"))
                ));
            }
            if (isDrawbridge(id, Registration.DRAWBRIDGE_EXTENDED.getId())) {
                e.getModelRegistry().put(id, new DrawbridgeBakedModel(
                        e.getModelRegistry().get(new ResourceLocation(Drawbridges.MODID,"block/drawbridge_extended"))
                ));
            }
            if (isDrawbridge(id, Registration.DRAWBRIDGE_ADVANCED.getId())) {
                e.getModelRegistry().put(id, new DrawbridgeBakedModel(
                        e.getModelRegistry().get(new ResourceLocation(Drawbridges.MODID,"block/drawbridge_advanced"))
                ));
            }
        }
    }

    private static boolean isDrawbridge(ResourceLocation id, ResourceLocation drawbridgeId) {
        return id instanceof ModelResourceLocation
                && id.getNamespace().equals(Drawbridges.MODID)
                && id.getPath().equals(drawbridgeId.getPath())
                && !((ModelResourceLocation) id).getVariant().equals("inventory");
    }
}
