package com.pression.compressedengineering.mixin;

import blusunrize.immersiveengineering.common.gui.ModWorkbenchContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

//For the record, i don't like this.
//This mixin changes the value of MAX_NUM_DYNAMIC_SLOTS from 20 to 40.
//It increases the amount of items that can be added to an IE blueprint without crashing upon removing it.
//Raising to 40 makes it so theoretically, up to 33 items can go in a bp, but other things break before it gets there.
@Mixin(ModWorkbenchContainer.class)
public class ModWorkbenchSlotsMixin {

    //This is sketchy, but there's nothing else that uses 20 as a constant in there so...
    @ModifyConstant(method = "rebindSlots", remap = false, constant = @Constant(intValue = 20))
    private int modifyMaxSlot(int original){
        return 40;
    }
}
