package com.pression.compressedengineering.mixin.cokeoven;

import com.pression.compressedengineering.interfaces.CokeOvenFluidOutput;
import org.spongepowered.asm.mixin.Mixin;
import blusunrize.immersiveengineering.api.crafting.CokeOvenRecipe;
import net.minecraftforge.fluids.FluidStack;

//This mixin adds a new field to CokeOvenRecipe, along with a getter and setter. (See the CokeOvenFluidInput interface)
@Mixin(CokeOvenRecipe.class)
public class CokeOvenRecipeMixin implements CokeOvenFluidOutput {
    public FluidStack fluidOutput;

    public void setFluidOutput(FluidStack fluid){
        fluidOutput = fluid;
    }
    public FluidStack getFluidOutput(){
        return fluidOutput;
    }
}

