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
        if(oil > 0){
            fluid = new FluidStack(IEFluids.CREOSOTE.getStill(), oil);
        }
        if(json.has("fluid")){
            oil = 1;
            fluid = ApiUtils.jsonDeserializeFluidStack(GsonHelper.getAsJsonObject(json, "fluid"));
        }
        CokeOvenRecipe newRecipe = new CokeOvenRecipe(recipeId, recipe.output, recipe.input, recipe.time, oil);
        CokeOvenFluidOutput recipeFluid = (CokeOvenFluidOutput) newRecipe;
        recipeFluid.setFluidOutput(fluid);
        cir.setReturnValue(newRecipe);
    }

    @Inject(
            method = "readFromJson(Lnet/minecraft/resources/ResourceLocation;Lcom/google/gson/JsonObject;Lnet/minecraftforge/common/crafting/conditions/ICondition$IContext;)Lblusunrize/immersiveengineering/api/crafting/CokeOvenRecipe;",
            remap = false,
            at = @At(value = "HEAD")
    )
    private void recipeSafeguard(ResourceLocation recipeId, JsonObject json, ICondition.IContext context, CallbackInfoReturnable<CokeOvenRecipe> cir){ //This is just a small safeguard because with this, the creosote field might be missing. IT just sets it to 0.
        if(!json.has("creosote")){
            json.addProperty("creosote", 0);
        }
    }

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

/* Original mixins, here as a backup.
    @Overwrite(remap = false)
    public CokeOvenRecipe readFromJson(ResourceLocation recipeId, JsonObject json, IContext context){
        Lazy<ItemStack> output = readOutput(json.get("result"));
        IngredientWithSize input = IngredientWithSize.deserialize(json.get("input"));
        int time = GsonHelper.getAsInt(json, "time");
        int oil = 0; //If there's no creosote nor other fluid, this must be 0 to ensure fluids don't get drawn in jei.
        FluidStack fluidOutput = FluidStack.EMPTY;
        if(json.has("creosote")){
            oil = GsonHelper.getAsInt(json, "creosote");
            fluidOutput = new FluidStack(IEFluids.CREOSOTE.getStill(), oil);
        }
        if(json.has("fluid")){
            fluidOutput = ApiUtils.jsonDeserializeFluidStack(GsonHelper.getAsJsonObject(json, "fluid"));
            oil = 1; //Dummy value to ensure fluids get drawn anyway
        }
        CokeOvenRecipe recipe = new CokeOvenRecipe(recipeId, output, input, time, oil);
        CokeOvenFluidOutput fluidAdder = (CokeOvenFluidOutput) recipe;
        fluidAdder.setFluidOutput(fluidOutput);
        return recipe;
    }

    @Overwrite(remap = false)
    public CokeOvenRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer)
    {
        Lazy<ItemStack> output = readLazyStack(buffer);
        IngredientWithSize input = IngredientWithSize.read(buffer);
        int time = buffer.readInt();
        int oil = buffer.readInt();
        FluidStack fluid = buffer.readFluidStack();
        CokeOvenRecipe recipe = new CokeOvenRecipe(recipeId, output, input, time, oil);
        CokeOvenFluidOutput fluidAdder = (CokeOvenFluidOutput) recipe;
        fluidAdder.setFluidOutput(fluid);
        return recipe;
    }

    @Overwrite(remap = false)
    public void toNetwork(FriendlyByteBuf buffer, CokeOvenRecipe recipe)
    {
        writeLazyStack(buffer, recipe.output);
        recipe.input.write(buffer);
        buffer.writeInt(recipe.time);
        buffer.writeInt(recipe.creosoteOutput);
        CokeOvenFluidOutput fluidReader = (CokeOvenFluidOutput) recipe;
        buffer.writeFluidStack(fluidReader.getFluidOutput());
    }
*/