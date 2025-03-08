package com.pression.compressedengineering.mixin.arcfurnace;

import blusunrize.immersiveengineering.api.crafting.ArcFurnaceRecipe;
import blusunrize.immersiveengineering.api.crafting.StackWithChance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ArcFurnaceRecipe.class)
public class ArcFurnaceRecipeMixin {

    @Redirect(method = "generateActualOutput", at = @At(value = "INVOKE", target = "Lblusunrize/immersiveengineering/api/crafting/StackWithChance;chance()F"), remap = false)
    private float reInvertChance(StackWithChance secondary){
        return 1 - secondary.chance();
    }

}
