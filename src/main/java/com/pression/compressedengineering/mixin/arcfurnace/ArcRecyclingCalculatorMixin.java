package com.pression.compressedengineering.mixin.arcfurnace;

import blusunrize.immersiveengineering.api.crafting.ArcFurnaceRecipe;
import blusunrize.immersiveengineering.common.crafting.ArcRecyclingCalculator;
import com.pression.compressedengineering.CommonConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

//This mixin uses a config to prevent the list of arc furnace recycling recipes from being populated at all.
@Mixin(ArcRecyclingCalculator.class)
public class ArcRecyclingCalculatorMixin {
    @Inject(method = "run", at = @At("HEAD"), remap = false, cancellable = true)
    private void nukeRecyclingRecipes(CallbackInfoReturnable<List<ArcFurnaceRecipe>> cir){
        if(CommonConfig.NUKE_RECYCLING.get()) cir.setReturnValue(new ArrayList<>());
    }
}
