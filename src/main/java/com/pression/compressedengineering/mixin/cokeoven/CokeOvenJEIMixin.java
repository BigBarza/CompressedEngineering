package com.pression.compressedengineering.mixin.cokeoven;

import blusunrize.immersiveengineering.api.crafting.CokeOvenRecipe;
import blusunrize.immersiveengineering.common.util.compat.jei.cokeoven.CokeOvenRecipeCategory;
import com.pression.compressedengineering.interfaces.CokeOvenFluidOutput;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//This mixin handles showing the correct fluid for custom recipes in JEI.
//NOTE: The gauge in JEI is sized to ONE bucket, not 12, do not let that throw you off.
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

    @ModifyArg( //I tried modifyargs before and uhh...that did NOT end well.
            method = "setRecipe(Lmezz/jei/api/gui/builder/IRecipeLayoutBuilder;Lblusunrize/immersiveengineering/api/crafting/CokeOvenRecipe;Lmezz/jei/api/recipe/IFocusGroup;)V",
            remap = false,
            at = @At(value = "INVOKE", target = "Lmezz/jei/api/gui/builder/IRecipeSlotBuilder;addIngredient(Lmezz/jei/api/ingredients/IIngredientType;Ljava/lang/Object;)Lmezz/jei/api/gui/builder/IIngredientAcceptor;"),
            index = 1
    )
    private Object setFluid(Object fluid){
        CokeOvenFluidOutput recipe = (CokeOvenFluidOutput) currentRecipe;
        return recipe.getFluidOutput(); //As simple as it gets. Instead of a FluidStack of creosote, it's a FluidStack of whatever we want
    }
}
