package com.pression.compressedengineering.jei;

import blusunrize.immersiveengineering.common.util.compat.jei.AssemblerRecipeTransferHandler;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;
import net.minecraft.world.item.crafting.CraftingRecipe;
import org.jetbrains.annotations.NotNull;

public class AssemblerOnlyRecipeTransferHandler extends AssemblerRecipeTransferHandler {

    public AssemblerOnlyRecipeTransferHandler(IRecipeTransferHandlerHelper transferHandlerHelper) {
        super(transferHandlerHelper);
    }

    @Override
    public @NotNull RecipeType<CraftingRecipe> getRecipeType() {
        return AssemblerRecipeCategory.TYPE;
    }
}
