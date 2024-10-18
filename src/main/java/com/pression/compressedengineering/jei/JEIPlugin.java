package com.pression.compressedengineering.jei;

import blusunrize.immersiveengineering.common.register.IEBlocks;
import blusunrize.immersiveengineering.common.util.compat.jei.AssemblerRecipeTransferHandler;
import com.pression.compressedengineering.CompressedEngineering;
import com.pression.compressedengineering.recipe.IERecipeTypes;
import com.pression.compressedengineering.recipe.ShapedAssemblerRecipe;
import com.pression.compressedengineering.recipe.ShapelessAssemblerRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid(){
        return new ResourceLocation(CompressedEngineering.MODID, "jei_plugin");
    }
    @Override
    public void registerCategories(IRecipeCategoryRegistration registration){
        registration.addRecipeCategories(new AssemblerRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }
    @Override
    public void registerRecipes(IRecipeRegistration registration){
        List<ShapedAssemblerRecipe> shapedRecipes = Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(IERecipeTypes.SHAPED_ASSEMBLY_RECIPE_TYPE.get());
        List<ShapelessAssemblerRecipe> shapelessRecipes = Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(IERecipeTypes.SHAPELESS_ASSEMBLY_RECIPE_TYPE.get());
        List<CraftingRecipe> recipes = new ArrayList<>();
        recipes.addAll(shapedRecipes);
        recipes.addAll(shapelessRecipes);

        registration.addRecipes(AssemblerRecipeCategory.TYPE, recipes);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration){
        registration.addRecipeCatalyst(new ItemStack(IEBlocks.Multiblocks.ASSEMBLER), AssemblerRecipeCategory.TYPE);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration){
        registration.addRecipeTransferHandler(new AssemblerRecipeTransferHandler(registration.getTransferHelper()), AssemblerRecipeCategory.TYPE);
    }

}
