package net.blay09.mods.cookingforblockheads.client;

import com.google.common.collect.ImmutableMap;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.block.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.item.DyeColor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.SimpleModelTransform;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.function.Predicate;

@Mod.EventBusSubscriber(modid = CookingForBlockheads.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModModels {
    public static IBakedModel milkJarLiquid;
    public static IBakedModel sinkLiquid;
    public static IBakedModel ovenDoor;
    public static IBakedModel ovenDoorActive;
    public static IBakedModel fridgeDoor;
    public static IBakedModel fridgeDoorFlipped;
    public static IBakedModel fridgeDoorLarge;
    public static IBakedModel fridgeDoorLargeFlipped;
    public static IBakedModel[] counterDoors;
    public static IBakedModel[] counterDoorsFlipped;
    public static IBakedModel[] cabinetDoors;
    public static IBakedModel[] cabinetDoorsFlipped;

    @SubscribeEvent
    public static void onModelBake(ModelBakeEvent event) {
        try {
            milkJarLiquid = loadAndBakeModel(event, new ResourceLocation(CookingForBlockheads.MOD_ID, "block/milk_jar_liquid"));
            sinkLiquid = loadAndBakeModel(event, new ResourceLocation(CookingForBlockheads.MOD_ID, "block/sink_liquid"));
            ovenDoor = loadAndBakeModel(event, new ResourceLocation(CookingForBlockheads.MOD_ID, "block/oven_door"));
            ovenDoorActive = loadAndBakeModel(event, new ResourceLocation(CookingForBlockheads.MOD_ID, "block/oven_door_active"));
            fridgeDoor = loadAndBakeModel(event, new ResourceLocation(CookingForBlockheads.MOD_ID, "block/fridge_door"));
            fridgeDoorFlipped = loadAndBakeModel(event, new ResourceLocation(CookingForBlockheads.MOD_ID, "block/fridge_door_flipped"));
            fridgeDoorLarge = loadAndBakeModel(event, new ResourceLocation(CookingForBlockheads.MOD_ID, "block/fridge_large_door"));
            fridgeDoorLargeFlipped = loadAndBakeModel(event, new ResourceLocation(CookingForBlockheads.MOD_ID, "block/fridge_large_door_flipped"));

            DyeColor[] colors = DyeColor.values();
            counterDoors = new IBakedModel[colors.length + 1];
            counterDoors[0] = loadAndBakeModel(event, new ResourceLocation(CookingForBlockheads.MOD_ID, "block/counter_door"));
            counterDoorsFlipped = new IBakedModel[colors.length + 1];
            counterDoorsFlipped[0] = loadAndBakeModel(event, new ResourceLocation(CookingForBlockheads.MOD_ID, "block/counter_door_flipped"));
            for (DyeColor color : colors) {
                counterDoors[color.getId() + 1] = loadAndBakeModel(event, new ResourceLocation(CookingForBlockheads.MOD_ID, "block/counter_door"),
                        it -> retexture(it, replaceTexture(getColoredTerracottaTexture(color))));
                counterDoorsFlipped[color.getId() + 1] = loadAndBakeModel(event, new ResourceLocation(CookingForBlockheads.MOD_ID, "block/counter_door_flipped"),
                        it -> retexture(it, replaceTexture(getColoredTerracottaTexture(color))));
            }

            cabinetDoors = new IBakedModel[colors.length + 1];
            cabinetDoors[0] = loadAndBakeModel(event, new ResourceLocation(CookingForBlockheads.MOD_ID, "block/cabinet_door"));
            cabinetDoorsFlipped = new IBakedModel[colors.length + 1];
            cabinetDoorsFlipped[0] = loadAndBakeModel(event, new ResourceLocation(CookingForBlockheads.MOD_ID, "block/cabinet_door_flipped"));
            for (DyeColor color : colors) {
                cabinetDoors[color.getId() + 1] = loadAndBakeModel(event, new ResourceLocation(CookingForBlockheads.MOD_ID, "block/cabinet_door"),
                        it -> retexture(it, replaceTexture(getColoredTerracottaTexture(color))));
                cabinetDoorsFlipped[color.getId() + 1] = loadAndBakeModel(event, new ResourceLocation(CookingForBlockheads.MOD_ID, "block/cabinet_door_flipped"),
                        it -> retexture(it, replaceTexture(getColoredTerracottaTexture(color))));
            }

            registerColoredKitchenBlock(event, "block/cooking_table", ModBlocks.cookingTable);

            IUnbakedModel sinkModel = event.getModelLoader().getModelOrMissing(new ResourceLocation(CookingForBlockheads.MOD_ID, "block/sink"));
            IUnbakedModel sinkFlippedModel = event.getModelLoader().getModelOrMissing(new ResourceLocation(CookingForBlockheads.MOD_ID, "block/sink_flipped"));
            overrideWithDynamicModel(event, ModBlocks.sink, "block/sink", it -> {
                IUnbakedModel result = it.get(SinkBlock.FLIPPED) ? sinkFlippedModel : sinkModel;
                if (it.get(SinkBlock.HAS_COLOR)) {
                    result = retexture(result, replaceTexture(getColoredTerracottaTexture(it.get(SinkBlock.COLOR))));
                }
                return result;
            });

            overrideWithDynamicModel(event, ModBlocks.cuttingBoard, "block/cutting_board");

            IUnbakedModel toasterModel = event.getModelLoader().getModelOrMissing(new ResourceLocation(CookingForBlockheads.MOD_ID, "block/toaster"));
            IUnbakedModel toasterActiveModel = event.getModelLoader().getModelOrMissing(new ResourceLocation(CookingForBlockheads.MOD_ID, "block/toaster_active"));
            overrideWithDynamicModel(event, ModBlocks.toaster, "block/toaster", it -> it.get(ToasterBlock.ACTIVE) ? toasterActiveModel : toasterModel);

            overrideWithDynamicModel(event, ModBlocks.fruitBasket, "block/fruit_basket");
            overrideWithDynamicModel(event, ModBlocks.milkJar, "block/milk_jar");
            overrideWithDynamicModel(event, ModBlocks.cowJar, "block/milk_jar");
            IUnbakedModel fridgeSmallModel = event.getModelLoader().getModelOrMissing(new ResourceLocation(CookingForBlockheads.MOD_ID, "block/fridge"));
            IUnbakedModel fridgeLargeModel = event.getModelLoader().getModelOrMissing(new ResourceLocation(CookingForBlockheads.MOD_ID, "block/fridge_large"));
            IUnbakedModel fridgeInvisibleModel = event.getModelLoader().getModelOrMissing(new ResourceLocation(CookingForBlockheads.MOD_ID, "block/fridge_invisible"));
            overrideWithDynamicModel(event, ModBlocks.fridge, "block/fridge", it -> {
                FridgeBlock.FridgeModelType fridgeModelType = it.get(FridgeBlock.MODEL_TYPE);
                switch (fridgeModelType) {
                    case LARGE:
                        return fridgeLargeModel;
                    case INVISIBLE:
                        return fridgeInvisibleModel;
                    default:
                        return fridgeSmallModel;
                }
            });

            registerColoredKitchenBlock(event, "block/counter", ModBlocks.counter);
            registerColoredKitchenBlock(event, "block/corner", ModBlocks.corner);
            registerColoredKitchenBlock(event, "block/cabinet", ModBlocks.cabinet);

            overrideWithDynamicModel(event, ModBlocks.oven, "block/oven", null, state -> {
                ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
                String normalTexture = "cookingforblockheads:block/oven_front";
                String activeTexture = "cookingforblockheads:block/oven_front_active";
                if (state.get(OvenBlock.POWERED)) {
                    normalTexture = "cookingforblockheads:block/oven_front_powered";
                    activeTexture = "cookingforblockheads:block/oven_front_powered_active";
                }

                builder.put("ovenfront", state.get(OvenBlock.ACTIVE) ? activeTexture : normalTexture);
                return builder.build();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static IUnbakedModel retexture(IUnbakedModel it, ImmutableMap<String, String> replaceTexture) {
        // TODO new blockmodel instance
        return it;
    }

    private static void registerColoredKitchenBlock(ModelBakeEvent event, String modelPath, Block block) {
        IUnbakedModel model = event.getModelLoader().getModelOrMissing(new ResourceLocation(CookingForBlockheads.MOD_ID, modelPath));
        overrideWithDynamicModel(event, block, modelPath, it -> {
            if (it.get(BlockKitchen.HAS_COLOR)) {
                return retexture(model, replaceTexture(getColoredTerracottaTexture(it.get(BlockKitchen.COLOR))));
            }

            return model;
        });
    }

    private static ImmutableMap<String, String> replaceTexture(String texturePath) {
        return ImmutableMap.<String, String>builder().put("texture", texturePath).put("particle", texturePath).build();
    }

    private static String getColoredTerracottaTexture(DyeColor color) {
        return "minecraft:block/" + color.getName().toLowerCase(Locale.ENGLISH) + "_terracotta";
    }

    private static void overrideWithDynamicModel(ModelBakeEvent event, Block block, String modelPath) throws Exception {
        overrideWithDynamicModel(event, block, modelPath, null, null);
    }

    private static void overrideWithDynamicModel(ModelBakeEvent event, Block block, String modelPath, @Nullable List<Pair<Predicate<BlockState>, IBakedModel>> parts, @Nullable Function<BlockState, ImmutableMap<String, String>> textureMapFunction) {
        ResourceLocation location = new ResourceLocation(CookingForBlockheads.MOD_ID, modelPath);
        IUnbakedModel model = event.getModelLoader().getModelOrMissing(location);
        CachedDynamicModel dynamicModel = new CachedDynamicModel(event.getModelLoader(), it -> model, parts, textureMapFunction, location);
        overrideModelIgnoreState(block, dynamicModel, event);
    }

    private static void overrideWithDynamicModel(ModelBakeEvent event, Block block, String modelPath, Function<BlockState, IUnbakedModel> modelFunction) {
        ResourceLocation location = new ResourceLocation(CookingForBlockheads.MOD_ID, modelPath);
        CachedDynamicModel dynamicModel = new CachedDynamicModel(event.getModelLoader(), modelFunction, null, null, location);
        overrideModelIgnoreState(block, dynamicModel, event);
    }

    private static void overrideWithDynamicModel(ModelBakeEvent event, Block block, String modelPath, Function<BlockState, IUnbakedModel> modelFunction, @Nullable List<Pair<Predicate<BlockState>, IBakedModel>> parts, @Nullable Function<BlockState, ImmutableMap<String, String>> textureMapFunction) {
        ResourceLocation location = new ResourceLocation(CookingForBlockheads.MOD_ID, modelPath);
        CachedDynamicModel dynamicModel = new CachedDynamicModel(event.getModelLoader(), modelFunction, parts, textureMapFunction, location);
        overrideModelIgnoreState(block, dynamicModel, event);
    }

    @Nullable
    private static IBakedModel loadAndBakeModel(ModelBakeEvent event, ResourceLocation resourceLocation) {
        return loadAndBakeModel(event, resourceLocation, null);
    }

    @Nullable
    private static IBakedModel loadAndBakeModel(ModelBakeEvent event, ResourceLocation resourceLocation, @Nullable Function<IUnbakedModel, IUnbakedModel> preprocessor) {
        IUnbakedModel model = event.getModelLoader().getModelOrMissing(resourceLocation);
        if (preprocessor != null) {
            model = preprocessor.apply(model);
        }

        return model.bakeModel(event.getModelLoader(), ModelLoader.defaultTextureGetter(), SimpleModelTransform.IDENTITY, resourceLocation);
    }

    private static void overrideModelIgnoreState(Block block, IBakedModel model, ModelBakeEvent event) {
        block.getStateContainer().getValidStates().forEach((state) -> {
            ModelResourceLocation modelLocation = BlockModelShapes.getModelLocation(state);
            event.getModelRegistry().put(modelLocation, model);
        });
    }
}