package com.pression.compressedengineering.recipe;

import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapelessRecipe;

import javax.annotation.Nullable;

public class ShapelessAssemblerRecipe extends ShapelessRecipe {
    public ShapelessAssemblerRecipe(ResourceLocation p_44246_, String p_44247_, ItemStack p_44248_, NonNullList<Ingredient> p_44249_) {
        super(p_44246_, p_44247_, p_44248_, p_44249_);
    }
    public ShapelessAssemblerRecipe(ShapelessRecipe r){
        super(r.getId(), r.getGroup(), r.getResultItem(), r.getIngredients());
    }

    @Override
    public RecipeType<?> getType(){
        return IERecipeTypes.SHAPELESS_ASSEMBLY_RECIPE_TYPE.get();
    }

    @Override
    public RecipeSerializer<?> getSerializer(){
        return IERecipeTypes.SHAPELESS_ASSEMBLY_SERIALIZER.get();
    }

    public static class Serializer implements RecipeSerializer<ShapelessAssemblerRecipe> {
        public static ShapelessRecipe.Serializer serializer = new ShapelessRecipe.Serializer();
        public ShapelessAssemblerRecipe fromJson(ResourceLocation id, JsonObject json){
            ShapelessRecipe r = serializer.fromJson(id, json);
            //No, you can't just cast it.
            return new ShapelessAssemblerRecipe(r);
        }
        public @Nullable ShapelessAssemblerRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf){
            return new ShapelessAssemblerRecipe(serializer.fromNetwork(id, buf));
        }
        public void toNetwork(FriendlyByteBuf buf, ShapelessAssemblerRecipe recipe){
            serializer.toNetwork(buf, recipe);
        }
    }
}
