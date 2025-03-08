package com.pression.compressedengineering.mixin.arcfurnace;

import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockContext;
import blusunrize.immersiveengineering.api.multiblocks.blocks.util.CapabilityPosition;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.arcfurnace.ArcFurnaceLogic;
import com.pression.compressedengineering.interfaces.IStateProxy;
import net.minecraft.core.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArcFurnaceLogic.class)
public class ArcFurnaceLogicMixin {

    @Unique
    private BlockPos ELECTRODE_POS = new BlockPos(2,4,2);

    @Inject(method = "getCapability", at = @At("HEAD"), remap = false, cancellable = true)
    private <T> void injectElectrodeCapability(IMultiblockContext<ArcFurnaceLogic.State> ctx, CapabilityPosition position, Capability<T> cap, CallbackInfoReturnable<LazyOptional<T>> cir){
        ArcFurnaceLogic.State state = ctx.getState();
        if(cap == ForgeCapabilities.ITEM_HANDLER && ELECTRODE_POS.equals(position.posInMultiblock())){
            cir.setReturnValue(((IStateProxy) state).getElectrodeHandler().cast(ctx));
        }
    }

}
