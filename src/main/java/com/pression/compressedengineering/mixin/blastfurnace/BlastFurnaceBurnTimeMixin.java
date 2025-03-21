package com.pression.compressedengineering.mixin.blastfurnace;

import blusunrize.immersiveengineering.common.blocks.stone.BlastFurnaceAdvancedBlockEntity;
import blusunrize.immersiveengineering.common.blocks.stone.BlastFurnaceBlockEntity;
import com.pression.compressedengineering.CommonConfig;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//This mixin intercepts the burn time of a blast furnace fuel and applies the multiplier to it if it's in an improved blast furnace.
@Mixin(BlastFurnaceBlockEntity.class)
public class BlastFurnaceBurnTimeMixin {

    @Inject(method = "getBurnTimeOf", at = @At("RETURN"), remap = false, cancellable = true)
    private void recalcBurnTime(ItemStack fuel, CallbackInfoReturnable<Integer> cir){
        int burnTime = cir.getReturnValue();
        BlastFurnaceBlockEntity<?> self = (BlastFurnaceBlockEntity<?>) (Object) this;
        if(self instanceof BlastFurnaceAdvancedBlockEntity ibf){
            cir.setReturnValue((int) (burnTime * CommonConfig.IMPROVED_FUEL_MULT.get()));
        }
    }
}
