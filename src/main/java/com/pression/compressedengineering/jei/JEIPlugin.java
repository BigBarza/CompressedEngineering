package com.pression.compressedengineering.jei;

import blusunrize.immersiveengineering.api.crafting.IERecipeTypes;
import blusunrize.immersiveengineering.api.excavator.MineralMix;
import blusunrize.immersiveengineering.common.register.IEBlocks;
import blusunrize.immersiveengineering.common.util.compat.jei.AssemblerRecipeTransferHandler;
import com.pression.compressedengineering.CompressedEngineering;
import com.pression.compressedengineering.recipe.CERecipeTypes;
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
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

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
        registration.addRecipeCategories(new MineralMixRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration){
        Level mcLevel = Minecraft.getInstance().level;
        if(mcLevel == null) return;
        //Assembler Recipes
        List<ShapedAssemblerRecipe> shapedRecipes = mcLevel.getRecipeManager().getAllRecipesFor(CERecipeTypes.SHAPED_ASSEMBLY_RECIPE_TYPE.get());
        List<ShapelessAssemblerRecipe> shapelessRecipes = mcLevel.getRecipeManager().getAllRecipesFor(CERecipeTypes.SHAPELESS_ASSEMBLY_RECIPE_TYPE.get());
        List<CraftingRecipe> recipes = new ArrayList<>();
        recipes.addAll(shapedRecipes);
        recipes.addAll(shapelessRecipes);
        registration.addRecipes(AssemblerRecipeCategory.TYPE, recipes);
        //Excavator Veins
        List<MineralMix> mineralMixRecipes = mcLevel.getRecipeManager().getAllRecipesFor(IERecipeTypes.MINERAL_MIX.get());
        List<MineralMix> enabledVeins = new ArrayList<>();
        for(MineralMix vein : mineralMixRecipes){
            if(vein.weight > 0) enabledVeins.add(vein); //Only add enabled veins with a non-zero weight
        }
        registration.addRecipes(MineralMixRecipeCategory.TYPE, enabledVeins);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration){
        registration.addRecipeCatalyst(new ItemStack(IEBlocks.Multiblocks.ASSEMBLER), AssemblerRecipeCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(IEBlocks.Multiblocks.EXCAVATOR), MineralMixRecipeCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(IEBlocks.Multiblocks.BUCKET_WHEEL), MineralMixRecipeCategory.TYPE);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration){
        registration.addRecipeTransferHandler(new AssemblerRecipeTransferHandler(registration.getTransferHelper()), AssemblerRecipeCategory.TYPE);
    }

}
