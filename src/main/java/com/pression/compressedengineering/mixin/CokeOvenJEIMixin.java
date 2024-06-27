package com.pression.compressedengineering.mixin;

import blusunrize.immersiveengineering.api.crafting.CokeOvenRecipe;
import blusunrize.immersiveengineering.common.register.IEFluids;
import blusunrize.immersiveengineering.common.util.compat.jei.cokeoven.CokeOvenRecipeCategory;
import com.pression.compressedengineering.interfaces.CokeOvenFluidOutput;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CokeOvenRecipeCategory.class)
public class CokeOvenJEIMixin {
    private CokeOvenRecipe currentRecipe;

    @Inject(
            method = "setRecipe(Lmezz/jei/api/gui/builder/IRecipeLayoutBuilder;Lblusunrize/immersiveengineering/api/crafting/CokeOvenRecipe;Lmezz/jei/api/recipe/IFocusGroup;)V",
            remap = false,
            at = @At("HEAD")
    )
    private void captureRecipe(IRecipeLayoutBuilder builder, CokeOvenRecipe recipe, IFocusGroup focuses, CallbackInfo ci){
        currentRecipe = recipe;
    }

    @ModifyArg( //I tried modifyargs before and uhh...shit broke
            method = "setRecipe(Lmezz/jei/api/gui/builder/IRecipeLayoutBuilder;Lblusunrize/immersiveengineering/api/crafting/CokeOvenRecipe;Lmezz/jei/api/recipe/IFocusGroup;)V",
            remap = false,
            at = @At(value = "INVOKE", target = "Lmezz/jei/api/gui/builder/IRecipeSlotBuilder;addIngredient(Lmezz/jei/api/ingredients/IIngredientType;Ljava/lang/Object;)Lmezz/jei/api/gui/builder/IIngredientAcceptor;"),
            index = 1
    )
    private Object setFluid(Object fluid){
        fluid = (FluidStack) fluid;
        CokeOvenFluidOutput recipe = (CokeOvenFluidOutput) currentRecipe;
        if(recipe.getFluidOutput() == FluidStack.EMPTY){
            //If there's no custom fluid set, add a fluidstack of creosote.
            return new FluidStack(IEFluids.CREOSOTE.getStill(), currentRecipe.creosoteOutput);
        }
        else return recipe.getFluidOutput(); //Add the custom fluidstack
    }
}
