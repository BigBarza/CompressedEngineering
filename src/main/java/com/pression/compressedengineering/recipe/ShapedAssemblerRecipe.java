package com.pression.compressedengineering.recipe;

import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import javax.annotation.Nullable;

public class ShapedAssemblerRecipe extends ShapedRecipe{

    public ShapedAssemblerRecipe(ResourceLocation p_44153_, String p_44154_, int p_44155_, int p_44156_, NonNullList<Ingredient> p_44157_, ItemStack p_44158_) {
        super(p_44153_, p_44154_, p_44155_, p_44156_, p_44157_, p_44158_);
    }
    public ShapedAssemblerRecipe(ShapedRecipe r){
        super(r.getId(), r.getGroup(), r.getRecipeWidth(), r.getRecipeHeight(), r.getIngredients(), r.getResultItem());
    }

    @Override
    public RecipeType<?> getType(){
        return CERecipeTypes.SHAPED_ASSEMBLY_RECIPE_TYPE.get();
    }

    @Override
    public RecipeSerializer<?> getSerializer(){
        return CERecipeTypes.SHAPED_ASSEMBLY_SERIALIZER.get();
    }

    public static class Serializer implements RecipeSerializer<ShapedAssemblerRecipe> {
        public static ShapedRecipe.Serializer serializer = new ShapedRecipe.Serializer();
        public ShapedAssemblerRecipe fromJson(ResourceLocation id, JsonObject json){
            ShapedRecipe r = serializer.fromJson(id, json);
            //No, you can't just cast it, for SOME reason.
            return new ShapedAssemblerRecipe(r);
        }
        public @Nullable ShapedAssemblerRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf){
            return new ShapedAssemblerRecipe(serializer.fromNetwork(id, buf));
        }
        public void toNetwork(FriendlyByteBuf buf, ShapedAssemblerRecipe recipe){
            serializer.toNetwork(buf, recipe);
        }
    }

}