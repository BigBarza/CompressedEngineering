package com.pression.compressedengineering.mixin.arcfurnace;

import blusunrize.immersiveengineering.api.crafting.ArcFurnaceRecipe;
import blusunrize.immersiveengineering.api.crafting.StackWithChance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

//This mixin fixes a bug in the 1.19 and 1.20 versions of IE where the arc furnace secondary chance is inverted.
//For example: a 90% chance would show as 90% in jei but actually only trigger 10% of the time.
@Mixin(ArcFurnaceRecipe.class)
public class ArcFurnaceRecipeMixin {

    @Redirect(method = "generateActualOutput", at = @At(value = "INVOKE", target = "Lblusunrize/immersiveengineering/api/crafting/StackWithChance;chance()F"), remap = false)
    private float reInvertChance(StackWithChance secondary){
        return 1 - secondary.chance();
    }

}
