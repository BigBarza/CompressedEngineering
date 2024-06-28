package com.pression.compressedengineering.mixin;

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

//Previous mixin, this is here as a backup

/*
    @Final
    @Shadow(remap = false) private Supplier<CokeOvenRecipe> cachedRecipe;
    @Final @Shadow(remap = false) private NonNullList<ItemStack> inventory;
    @Final @Shadow(remap = false) public static int OUTPUT_SLOT;
    @Shadow(remap = false) public abstract int getSlotLimit(int slot);


    @Nullable
    @Overwrite(remap = false)
    public CokeOvenRecipe getRecipe()
    {
        CokeOvenRecipe recipe = cachedRecipe.get();
        if(recipe==null)
            return null;
        CokeOvenFluidOutput fluidOutput = (CokeOvenFluidOutput) recipe;
        if(inventory.get(OUTPUT_SLOT).isEmpty()||(ItemStack.isSame(inventory.get(OUTPUT_SLOT), recipe.output.get())&&
                inventory.get(OUTPUT_SLOT).getCount()+recipe.output.get().getCount() <= getSlotLimit(OUTPUT_SLOT)))
            //if(tank.getFluidAmount()+recipe.creosoteOutput <= tank.getCapacity())
            if((tank.getFluid().isFluidEqual(fluidOutput.getFluidOutput()) || tank.isEmpty()) && (tank.getFluidAmount() + fluidOutput.getFluidOutput().getAmount() <= tank.getCapacity()))
                return recipe;
        return null;
    }
    */

