package com.pression.compressedengineering.mixin.blastfurnace;

import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.AdvBlastFurnaceLogic;
import com.pression.compressedengineering.CommonConfig;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//This mixin intercepts the burn time of a blast furnace fuel and applies the multiplier to it if it's in an improved blast furnace.
@Mixin(AdvBlastFurnaceLogic.State.class)
public class BlastFurnaceBurnTimeMixin {
    //Hard to believe, huh? A 1.20 change that simplifies things, for once.
    @Inject(method = "getBurnTimeOf", at = @At("RETURN"), remap = false, cancellable = true)
    private void recalcBurnTime(Level level, ItemStack fuel, CallbackInfoReturnable<Integer> cir){
        int burnTime = cir.getReturnValue();
        cir.setReturnValue((int) (burnTime * CommonConfig.IMPROVED_FUEL_MULT.get()));
    }
}