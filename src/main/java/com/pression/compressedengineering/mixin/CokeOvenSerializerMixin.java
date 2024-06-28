package com.pression.compressedengineering.mixin;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.crafting.CokeOvenRecipe;
import blusunrize.immersiveengineering.api.crafting.IERecipeSerializer;
import blusunrize.immersiveengineering.common.crafting.serializers.CokeOvenRecipeSerializer;
import blusunrize.immersiveengineering.common.register.IEFluids;
import com.google.gson.JsonObject;
import com.pression.compressedengineering.interfaces.CokeOvenFluidOutput;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//This mixin handles the loading of recipes from json, by loading the fluid field as well.
@Mixin(CokeOvenRecipeSerializer.class)
abstract public class CokeOvenSerializerMixin extends IERecipeSerializer<CokeOvenRecipe> {

    @Inject(
            method = "readFromJson(Lnet/minecraft/resources/ResourceLocation;Lcom/google/gson/JsonObject;Lnet/minecraftforge/common/crafting/conditions/ICondition$IContext;)Lnet/minecraft/world/item/crafting/Recipe;",
            remap = false, cancellable = true,
            at = @At("RETURN")
    )
    public void addFluidToRecipe(ResourceLocation recipeId, JsonObject json, ICondition.IContext context, CallbackInfoReturnable<CokeOvenRecipe> cir){
        CokeOvenRecipe recipe = cir.getReturnValue();
        int oil = recipe.creosoteOutput;
        FluidStack fluid = FluidStack.EMPTY;
        if(oil > 0){ //Means a creosote value was set
            fluid = new FluidStack(IEFluids.CREOSOTE.getStill(), oil);
        }
        if(json.has("fluid")){ //We let custom fluid outputs have priority
            oil = 1; //This is a dummy value to ensure JEI draws the fluid when viewing the recipe.
            fluid = ApiUtils.jsonDeserializeFluidStack(GsonHelper.getAsJsonObject(json, "fluid")); //This is 100% from the fermenter recipe serializer.
        }
        CokeOvenRecipe newRecipe = new CokeOvenRecipe(recipeId, recipe.output, recipe.input, recipe.time, oil); //we rebuild the recipe since we cannot modify the oil value.
        CokeOvenFluidOutput recipeFluid = (CokeOvenFluidOutput) newRecipe;
        recipeFluid.setFluidOutput(fluid);
        cir.setReturnValue(newRecipe);
    }

    //If the serializer cannot find a creosote field, it won't load the recipe. This injector springs in action before that and adds the field if it's not present.
    @Inject(
            method = "readFromJson(Lnet/minecraft/resources/ResourceLocation;Lcom/google/gson/JsonObject;Lnet/minecraftforge/common/crafting/conditions/ICondition$IContext;)Lblusunrize/immersiveengineering/api/crafting/CokeOvenRecipe;",
            remap = false,
            at = @At(value = "HEAD")
    )
    private void recipeSafeguard(ResourceLocation recipeId, JsonObject json, ICondition.IContext context, CallbackInfoReturnable<CokeOvenRecipe> cir){
        if(!json.has("creosote")){
            json.addProperty("creosote", 0);
        }
    }

    //I'm going to be honest, I don't know what these two do. I just modified them to accommodate the fluid.
    @Inject(method = "fromNetwork(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/network/FriendlyByteBuf;)Lblusunrize/immersiveengineering/api/crafting/CokeOvenRecipe;",
    remap = false, at = @At("RETURN"), cancellable = true)
    private void onFromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer, CallbackInfoReturnable<CokeOvenRecipe> cir){
        CokeOvenRecipe recipe = cir.getReturnValue();
        FluidStack fluid = buffer.readFluidStack();
        CokeOvenFluidOutput recipeFluid = (CokeOvenFluidOutput) recipe;
        recipeFluid.setFluidOutput(fluid);
        cir.setReturnValue(recipe);
    }

    @Inject(method = "toNetwork(Lnet/minecraft/network/FriendlyByteBuf;Lblusunrize/immersiveengineering/api/crafting/CokeOvenRecipe;)V",
            at = @At("TAIL"), remap = false)
    private void onToNetwork(FriendlyByteBuf buffer, CokeOvenRecipe recipe, CallbackInfo ci){
        CokeOvenFluidOutput recipeFluid = (CokeOvenFluidOutput) recipe;
        buffer.writeFluidStack(recipeFluid.getFluidOutput());
    }

}