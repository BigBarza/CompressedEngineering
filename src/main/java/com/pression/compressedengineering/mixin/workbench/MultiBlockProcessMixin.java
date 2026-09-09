package com.pression.compressedengineering.mixin.workbench;

import blusunrize.immersiveengineering.api.crafting.BlueprintCraftingRecipe;
import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockLevel;
import blusunrize.immersiveengineering.common.blocks.multiblocks.process.MultiblockProcessInWorld;
import blusunrize.immersiveengineering.common.blocks.multiblocks.process.ProcessContext;
import com.pression.compressedengineering.CommonConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//This mixin disables some logic at the end of multiblock processes for the automated workbench which break recipes with oversized inputs
@Mixin(MultiblockProcessInWorld.class)
public class MultiBlockProcessMixin {
    @Inject(method = "processFinish(Lblusunrize/immersiveengineering/common/blocks/multiblocks/process/ProcessContext$ProcessContextInWorld;Lblusunrize/immersiveengineering/api/multiblocks/blocks/env/IMultiblockLevel;)V"
    , at = @At(value = "INVOKE", target = "Lblusunrize/immersiveengineering/api/multiblocks/blocks/env/IMultiblockLevel;getRawLevel()Lnet/minecraft/world/level/Level;"), remap = false, cancellable = true)
    private <R extends MultiblockRecipe> void yeetPointlessCheck(ProcessContext.ProcessContextInWorld<R> context, IMultiblockLevel level, CallbackInfo ci){
        //The check in processFinish tries to find "oversized" stacks, and restarts the process if it finds any. Note that the inputs were already validated.
        //This creates more of the output than intended. So off it goes.
        MultiblockRecipe recipe = ((MultiblockProcessInWorld<?>)(Object)this).getRecipe(level.getRawLevel());
        if(recipe == null) return; // The recipe check is needec cause apparently it can mess up other multis like crushers.
        if(CommonConfig.ALT_BP_RECIPE_COUNT_CHECK.get() && recipe instanceof BlueprintCraftingRecipe bp) ci.cancel();
    }
}
