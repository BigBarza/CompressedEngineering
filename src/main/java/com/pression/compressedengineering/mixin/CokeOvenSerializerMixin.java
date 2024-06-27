package com.pression.compressedengineering.mixin;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.crafting.CokeOvenRecipe;
import blusunrize.immersiveengineering.api.crafting.IERecipeSerializer;
import blusunrize.immersiveengineering.api.crafting.IngredientWithSize;
import blusunrize.immersiveengineering.common.crafting.serializers.CokeOvenRecipeSerializer;
import com.pression.compressedengineering.interfaces.CokeOvenFluidOutput;
import com.google.gson.JsonObject;
import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.crafting.conditions.ICondition.IContext;
import net.minecraftforge.common.util.Lazy;
import net.minecraft.network.FriendlyByteBuf;


@Mixin(CokeOvenRecipeSerializer.class)
abstract public class CokeOvenSerializerMixin extends IERecipeSerializer<CokeOvenRecipe> {
    /**
     * @author Big Barza
     * @reason Intellij made me do this.
     */
    @Overwrite(remap = false)
    public CokeOvenRecipe readFromJson(ResourceLocation recipeId, JsonObject json, IContext context){
        Lazy<ItemStack> output = readOutput(json.get("result"));
        IngredientWithSize input = IngredientWithSize.deserialize(json.get("input"));
        int time = GsonHelper.getAsInt(json, "time");
        int oil = 0; //If there's no creosote nor other fluid, this must be 0 to ensure fluids don't get drawn in jei.
        FluidStack fluidOutput = FluidStack.EMPTY;
        if(json.has("creosote")) oil = GsonHelper.getAsInt(json, "creosote");
        if(json.has("fluid")){
            fluidOutput = ApiUtils.jsonDeserializeFluidStack(GsonHelper.getAsJsonObject(json, "fluid"));
            oil = 10; //Dummy value to ensure fluids get drawn anyway
        }
        CokeOvenRecipe recipe = new CokeOvenRecipe(recipeId, output, input, time, oil);
        CokeOvenFluidOutput fluidAdder = (CokeOvenFluidOutput) recipe;
        fluidAdder.setFluidOutput(fluidOutput);
        return recipe;
    }

    /**
     * @author Big Barza
     * @reason Intellij made me do this.
     */
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

    /**
     * @author Big Barza
     * @reason Intellij made me do this.
     */
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
}




/*private CallbackInfo c;
    @Inject(locals = LocalCapture.CAPTURE_FAILSOFT, method = "readFromJson(Lnet/minecraft/resources/ResourceLocation;Lcom/google/gson/JsonObject;Lnet/minecraftforge/common/crafting/conditions/ICondition$IContext;)Lnet/minecraft/world/item/crafting/Recipe;", at = @At("TAIL"))
    private void readFromJsonFluid(ResourceLocation id, JsonObject json, IContext context, CallbackInfoReturnable<Recipe> cir){
        FluidStack fluidOutput = FluidStack.EMPTY;
        if(json.has("fluid")) fluidOutput = ApiUtils.jsonDeserializeFluidStack(GsonHelper.getAsJsonObject(json, "fluid"));
        CokeOvenRecipe = time;

    }*/