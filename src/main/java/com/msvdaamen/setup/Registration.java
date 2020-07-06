package com.msvdaamen.setup;

import com.msvdaamen.Drawbridges;
import com.msvdaamen.blocks.Drawbridge;
import com.msvdaamen.blocks.DrawbridgeAdvanced;
import com.msvdaamen.blocks.DrawbridgeExtended;
import com.msvdaamen.client.gui.drawbridge.DrawbridgeContainer;
import com.msvdaamen.client.gui.drawbridge_advanced.DrawbridgeAdvancedContainer;
import com.msvdaamen.client.gui.drawbridge_extended.DrawbridgeExtendedContainer;
import com.msvdaamen.tileentities.DrawbridgeAdvancedTileEntity;
import com.msvdaamen.tileentities.DrawbridgeExtendedTileEntity;
import com.msvdaamen.tileentities.DrawbridgeTileEntity;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.msvdaamen.Drawbridges.MODID;

public class Registration {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    private static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MODID);
    private static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, MODID);
    private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, MODID);
    private static final DeferredRegister<ModDimension> DIMENSIONS = DeferredRegister.create(ForgeRegistries.MOD_DIMENSIONS, MODID);

    public static void init() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TILE_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        DIMENSIONS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<Drawbridge> DRAWBRIDGE = BLOCKS.register("drawbridge", Drawbridge::new);
    public static final RegistryObject<Item> DRAWBRIDGE_ITEM = ITEMS.register("drawbridge", () -> new BlockItem(DRAWBRIDGE.get(), new Item.Properties().group(ModSetup.ITEM_GROUP)));
    public static final RegistryObject<TileEntityType<DrawbridgeTileEntity>> DRAWBRIDGE_TILE = TILE_ENTITIES.register("drawbridge", () -> TileEntityType.Builder.create(DrawbridgeTileEntity::new, DRAWBRIDGE.get()).build(null));
    public static final RegistryObject<ContainerType<DrawbridgeContainer>> DRAWBRIDGE_CONTAINER = CONTAINERS.register("drawbridge", () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        return new DrawbridgeContainer(windowId, Drawbridges.proxy.getClientWorld(), pos, inv, Drawbridges.proxy.getClientPlayer());
    }));

    public static final RegistryObject<DrawbridgeExtended> DRAWBRIDGE_EXTENDED = BLOCKS.register("drawbridge_extended", DrawbridgeExtended::new);
    public static final RegistryObject<TileEntityType<DrawbridgeExtendedTileEntity>> DRAWBRIDGE_EXTENDED_TILE = TILE_ENTITIES.register("drawbridge_extended", () -> TileEntityType.Builder.create(DrawbridgeExtendedTileEntity::new, DRAWBRIDGE_EXTENDED.get()).build(null));
    public static final RegistryObject<Item> DRAWBRIDGE_EXTENDED_ITEM = ITEMS.register("drawbridge_extended", () -> new BlockItem(DRAWBRIDGE_EXTENDED.get(), new Item.Properties().group(ModSetup.ITEM_GROUP)));
    public static final RegistryObject<ContainerType<DrawbridgeExtendedContainer>> DRAWBRIDGE_EXTENDED_CONTAINER = CONTAINERS.register("drawbridge_extended", () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        return new DrawbridgeExtendedContainer(windowId, Drawbridges.proxy.getClientWorld(), pos, inv, Drawbridges.proxy.getClientPlayer());
    }));

    public static final RegistryObject<DrawbridgeAdvanced> DRAWBRIDGE_ADVANCED = BLOCKS.register("drawbridge_advanced", DrawbridgeAdvanced::new);
    public static final RegistryObject<TileEntityType<DrawbridgeAdvancedTileEntity>> DRAWBRIDGE_ADVANCED_TILE = TILE_ENTITIES.register("drawbridge_advanced", () -> TileEntityType.Builder.create(DrawbridgeAdvancedTileEntity::new, DRAWBRIDGE_ADVANCED.get()).build(null));
    public static final RegistryObject<Item> DRAWBRIDGE_ADVANCED_ITEM = ITEMS.register("drawbridge_advanced", () -> new BlockItem(DRAWBRIDGE_ADVANCED.get(), new Item.Properties().group(ModSetup.ITEM_GROUP)));
    public static final RegistryObject<ContainerType<DrawbridgeAdvancedContainer>> DRAWBRIDGE_ADVANCED_CONTAINER = CONTAINERS.register("drawbridge_advanced", () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        return new DrawbridgeAdvancedContainer(windowId, Drawbridges.proxy.getClientWorld(), pos, inv, Drawbridges.proxy.getClientPlayer());
    }));

}
