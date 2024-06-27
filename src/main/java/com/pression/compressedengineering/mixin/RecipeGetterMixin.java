package com.pression.compressedengineering.mixin;

import blusunrize.immersiveengineering.api.crafting.CokeOvenRecipe;
import blusunrize.immersiveengineering.common.blocks.stone.CokeOvenBlockEntity;
import com.pression.compressedengineering.interfaces.CokeOvenFluidOutput;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;
import java.util.function.Supplier;

@Mixin(CokeOvenBlockEntity.class)
public abstract class RecipeGetterMixin {
    @Final @Shadow(remap = false) private Supplier<CokeOvenRecipe> cachedRecipe;
    @Final @Shadow(remap = false) private NonNullList<ItemStack> inventory;
    @Final @Shadow(remap = false) public static int OUTPUT_SLOT;
    @Shadow(remap = false) public FluidTank tank;
    @Shadow(remap = false) public abstract int getSlotLimit(int slot);

    /**
     * @author Big Barza
     * @reason Intellij made me do this.
     */
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
}
