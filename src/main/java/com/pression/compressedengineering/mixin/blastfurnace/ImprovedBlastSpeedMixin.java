package com.pression.compressedengineering.mixin.blastfurnace;

import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.AdvBlastFurnaceLogic;
import com.pression.compressedengineering.CommonConfig;
import com.pression.compressedengineering.CompressedEngineering;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//This mixin takes the processing speed of the improved blast furnace and sets it to a new value based on the original.
//In base IE, the processing values are 1,2 and 3 for no preheaters, one preheater and both preheaters respectively.
@Mixin(AdvBlastFurnaceLogic.State.class)
public class ImprovedBlastSpeedMixin {
    @Unique
    private static boolean missingEntryFlag = false;

    @Inject(method = "getProcessSpeed", at = @At("RETURN"), cancellable = true, remap = false)
    private void getNewProcessingSpeed(CallbackInfoReturnable<Integer> cir){
        int speed = cir.getReturnValue();
        //If another mod comes in and adds better preheaters we need to be able to at least not die.
        if(CommonConfig.PREHEATER_BOOST.get().size() >= speed){
            cir.setReturnValue(CommonConfig.PREHEATER_BOOST.get().get(speed-1));
        }
        else{
            if(!missingEntryFlag){
                missingEntryFlag = true; //Only complain about this once.
                CompressedEngineering.LOGGER.error("Missing entry in the list of blast furnace preheater speeds! Letting the original value of {} through.", speed);
            }
        }
    }
}