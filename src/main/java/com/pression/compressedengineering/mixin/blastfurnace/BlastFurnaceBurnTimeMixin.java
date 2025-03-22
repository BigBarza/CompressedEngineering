package com.pression.compressedengineering.mixin.blastfurnace;

import blusunrize.immersiveengineering.common.blocks.metal.BlastFurnacePreheaterBlockEntity;
import blusunrize.immersiveengineering.common.blocks.stone.BlastFurnaceAdvancedBlockEntity;
import blusunrize.immersiveengineering.common.blocks.stone.FurnaceLikeBlockEntity;
import com.pression.compressedengineering.CommonConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//This mixin intercepts the burn time of a blast furnace fuel and applies the multiplier to it if it's in an improved blast furnace.
//Unfortunately this does not prevent just turning on the preheaters briefly before adding the fuel, since modifying the burn time reduction
//per tick instead will not work if we attempt to make fuel last longer at base speed.
@Mixin(FurnaceLikeBlockEntity.class)
public class BlastFurnaceBurnTimeMixin {
    @Shadow(remap = false) private int burnTime;
    @Shadow(remap = false) private int lastBurnTime;
    @Unique private int lastPreheaters = -1;

    @Inject(method = "tickServer", at = @At("TAIL"), remap = false)
    private void onTick(CallbackInfo ci){
        FurnaceLikeBlockEntity<?, ?> self = (FurnaceLikeBlockEntity<?, ?>) (Object) this;
        //"FurnaceLikeBlockEntity" includes both blast furnaces AND the alloy kiln
        if(self instanceof BlastFurnaceAdvancedBlockEntity ibf){
            //We can't use the processing speed, since that can be changed by configs
            int curPreheaters = ibf.getFromPreheater(true, BlastFurnacePreheaterBlockEntity::doSpeedup, 0)
                    + ibf.getFromPreheater(false, BlastFurnacePreheaterBlockEntity::doSpeedup, 0);
            //We don't need to check if there's new fuel, as that's taken care of by FuelBurnTimeMixin
            if(lastPreheaters == -1){
                lastPreheaters = curPreheaters;
            }
            //This is where we check for changes in preheaters
            if(lastPreheaters != curPreheaters){
                double oldMult = CommonConfig.IMPROVED_FUEL_MULT.get().get(lastPreheaters);
                double newMult = CommonConfig.IMPROVED_FUEL_MULT.get().get(curPreheaters);
                burnTime = (int) (burnTime / oldMult * newMult);
                lastBurnTime = (int) (lastBurnTime / oldMult * newMult);
                lastPreheaters = curPreheaters;
            }
        }
    }

}

