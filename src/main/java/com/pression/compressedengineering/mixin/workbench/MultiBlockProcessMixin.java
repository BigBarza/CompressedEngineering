package com.pression.compressedengineering.mixin.workbench;

import blusunrize.immersiveengineering.common.blocks.multiblocks.process.MultiblockProcessInWorld;
import com.pression.compressedengineering.CommonConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiblockProcessInWorld.class)
public class MultiBlockProcessMixin {
    @Inject(method = "processFinish(Lblusunrize/immersiveengineering/common/blocks/multiblocks/process/ProcessContext$ProcessContextInWorld;Lblusunrize/immersiveengineering/api/multiblocks/blocks/env/IMultiblockLevel;)V"
    , at = @At(value = "INVOKE", target = "Lblusunrize/immersiveengineering/api/multiblocks/blocks/env/IMultiblockLevel;getRawLevel()Lnet/minecraft/world/level/Level;"), remap = false, cancellable = true)
    private void yeetPointlessCheck(CallbackInfo ci){
        //The check in processFinish tries to find "oversized" stacks, and restarts the process if it finds any. Note that the inputs were already validated.
        //This creates more of the output than intended. So off it goes.
        if(CommonConfig.ALT_BP_RECIPE_COUNT_CHECK.get()) ci.cancel();
    }
}
