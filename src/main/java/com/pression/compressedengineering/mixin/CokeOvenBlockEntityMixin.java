package com.pression.compressedengineering.mixin;

import blusunrize.immersiveengineering.api.crafting.CokeOvenRecipe;
import com.pression.compressedengineering.interfaces.CokeOvenFluidOutput;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import blusunrize.immersiveengineering.common.blocks.stone.CokeOvenBlockEntity;
import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.function.Supplier;

//This mixin lets us output whichever FluidStack we want, instead of just creosote.
@Mixin(CokeOvenBlockEntity.class)
public class CokeOvenBlockEntityMixin {
        @Final
        @Shadow(remap = false) private Supplier<CokeOvenRecipe> cachedRecipe;
        private CokeOvenRecipe currentRecipe;

        @Inject(method = "getRecipe()Lblusunrize/immersiveengineering/api/crafting/CokeOvenRecipe;", remap = false, at = @At(value = "RETURN"), locals = LocalCapture.CAPTURE_FAILSOFT)
        private void captureCurRecipe(CallbackInfoReturnable<CokeOvenRecipe> cir, CokeOvenRecipe recipe){
            currentRecipe = recipe;
        }

        /*@ModifyVariable(method = "tickServer()V", at = @At(value = "INVOKE", target = "Lblusunrize/immersiveengineering/common/blocks/stone/CokeOvenBlockEntity;getRecipe()Lblusunrize/immersiveengineering/api/crafting/CokeOvenRecipe;"))
        private CokeOvenRecipe capture(CokeOvenRecipe recipe){
            currentRecipe = recipe;
            return recipe;
        }*/

        @Redirect(method = "tickServer()V", remap = false, at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fluids/capability/templates/FluidTank;fill(Lnet/minecraftforge/fluids/FluidStack;Lnet/minecraftforge/fluids/capability/IFluidHandler$FluidAction;)I"))
        private int fillTank(FluidTank tank, FluidStack resource, IFluidHandler.FluidAction action){
            CokeOvenFluidOutput fluid = (CokeOvenFluidOutput) currentRecipe;
            if(fluid.getFluidOutput() != FluidStack.EMPTY) resource = fluid.getFluidOutput();
            return tank.fill(resource, action);
        }

        /*@ModifyArg(method = "tickServer()V", remap = false, at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fluids/capability/templates/FluidTank;fill(Lnet/minecraftforge/fluids/FluidStack;Lnet/minecraftforge/fluids/capability/IFluidHandler$FluidAction;)I"), index = 0)
        private @NotNull FluidStack switchFluid(FluidStack f){
            return new FluidStack(Fluids.LAVA, 10);
        }*/
}
