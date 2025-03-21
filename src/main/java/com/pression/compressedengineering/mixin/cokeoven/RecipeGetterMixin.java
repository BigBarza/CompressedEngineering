package com.pression.compressedengineering.mixin.cokeoven;

import blusunrize.immersiveengineering.api.crafting.CokeOvenRecipe;
import blusunrize.immersiveengineering.common.blocks.stone.CokeOvenBlockEntity;
import com.pression.compressedengineering.interfaces.CokeOvenFluidOutput;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(CokeOvenBlockEntity.class)
public abstract class RecipeGetterMixin {
    @Shadow(remap = false) public FluidTank tank;

    //getRecipe() is responsible for fetching the matching recipe for the input and checking if it can be processed.
    @Inject(remap = false, method = "getRecipe()Lblusunrize/immersiveengineering/api/crafting/CokeOvenRecipe;", cancellable = true, at = @At(value = "RETURN"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void fluidRecipeCheck(CallbackInfoReturnable<CokeOvenRecipe> cir, CokeOvenRecipe recipe){
        if(cir.getReturnValue() == null) return; //We want to leave the other returns alone.
        CokeOvenFluidOutput recipeFluid = (CokeOvenFluidOutput) recipe;
        if(recipe.creosoteOutput == 0) return; //No fluid output, nothing to handle.
        if(tank.isEmpty() || tank.getFluid().isFluidEqual(recipeFluid.getFluidOutput())){ //If the tank is empty or the fluid matches
            if(tank.getFluidAmount() + recipeFluid.getFluidOutput().getAmount() <= tank.getCapacity()) return; //And there's enough space, let the recipe trough.
        }
        cir.setReturnValue(null); //If we got here, the check has failed and no recipe should be actually returned.
    }
}