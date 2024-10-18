package com.pression.compressedengineering.recipe;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import com.pression.compressedengineering.CompressedEngineering;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class IERecipeTypes {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, CompressedEngineering.MODID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registry.RECIPE_TYPE_REGISTRY, CompressedEngineering.MODID);

    public static final RegistryObject<RecipeType<ShapedAssemblerRecipe>> SHAPED_ASSEMBLY_RECIPE_TYPE = RECIPE_TYPES.register("assembler_shaped",
            () -> new RecipeType<ShapedAssemblerRecipe>() {
                @Override
                public String toString() {
                    return new ResourceLocation(ImmersiveEngineering.MODID, "assembler_shaped").toString();
                }
            });
    public static final RegistryObject<RecipeSerializer<ShapedAssemblerRecipe>> SHAPED_ASSEMBLY_SERIALIZER = RECIPE_SERIALIZERS.register("assembler_shaped", ShapedAssemblerRecipe.Serializer::new);



    public static final RegistryObject<RecipeType<ShapelessAssemblerRecipe>> SHAPELESS_ASSEMBLY_RECIPE_TYPE = RECIPE_TYPES.register("assembler_shapeless",
            () -> new RecipeType<ShapelessAssemblerRecipe>() {
                @Override
                public String toString() {
                    return new ResourceLocation(ImmersiveEngineering.MODID, "assembler_shapeless").toString();
                }
            });
    public static final RegistryObject<RecipeSerializer<ShapelessAssemblerRecipe>> SHAPELESS_ASSEMBLY_SERIALIZER = RECIPE_SERIALIZERS.register("assembler_shapeless", ShapelessAssemblerRecipe.Serializer::new);


}
