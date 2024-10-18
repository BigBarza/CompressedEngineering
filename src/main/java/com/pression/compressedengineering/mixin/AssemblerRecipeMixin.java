package com.pression.compressedengineering.mixin;

import blusunrize.immersiveengineering.common.util.Utils;
import com.pression.compressedengineering.recipe.IERecipeTypes;
import com.pression.compressedengineering.recipe.ShapedAssemblerRecipe;
import com.pression.compressedengineering.recipe.ShapelessAssemblerRecipe;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(Utils.class)
public class AssemblerRecipeMixin {
    @Inject(method = "findCraftingRecipe", at = @At("RETURN"), cancellable = true, remap = false)
    private static void checkAssemblerRecipes(CraftingContainer crafting, Level world, CallbackInfoReturnable<Optional<CraftingRecipe>> cir){
        Optional<ShapedAssemblerRecipe> newShapedRecipe = world.getRecipeManager().getRecipeFor(IERecipeTypes.SHAPED_ASSEMBLY_RECIPE_TYPE.get(), crafting, world);
        Optional<ShapelessAssemblerRecipe> newShapelessRecipe = world.getRecipeManager().getRecipeFor(IERecipeTypes.SHAPELESS_ASSEMBLY_RECIPE_TYPE.get(), crafting, world);
        //If an assembler shaped recipe is present, override the result with it. Then check for shapeless. Then let the original result through.
        newShapedRecipe.ifPresent(assemblerRecipe -> cir.setReturnValue(Optional.of(assemblerRecipe)));
        newShapelessRecipe.ifPresent(assemblerRecipe -> cir.setReturnValue(Optional.of(assemblerRecipe)));
    }
}
