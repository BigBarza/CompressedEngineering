package com.pression.compressedengineering.interfaces;

import net.minecraftforge.fluids.FluidStack;

public interface CokeOvenFluidOutput {
    // This is just a small interface to access the FluidStack field added to CokeOvenRecipe.
    // To access it, the recipe must be first cast to the interface
    // CokeOvenFluidOutput recipeFluid = (CokeOvenFluidOutput) recipe;
    public void setFluidOutput(FluidStack fluid);
    public FluidStack getFluidOutput();
}
