package com.pression.compressedengineering.mixin.blastfurnace;

import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockContext;
import blusunrize.immersiveengineering.common.blocks.metal.BlastFurnacePreheaterBlockEntity;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.AdvBlastFurnaceLogic;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.FurnaceHandler;
import blusunrize.immersiveengineering.common.config.IEServerConfig;
import com.pression.compressedengineering.CommonConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//This mixin intercepts the burn time of a blast furnace fuel and applies the multiplier to it if it's in an improved blast furnace.
@Mixin(FurnaceHandler.class)
public abstract class BlastFurnaceBurnTimeMixin {

    @Unique
    private static final BlockPos[] PREHEATERS = {
            new BlockPos(-1, 0, 1), new BlockPos(3, 0, 1)
    };
    @Unique private int lastPreheaters = -1;
    @Unique private IMultiblockContext<? extends FurnaceHandler.IFurnaceEnvironment<?>> capturedContext;
    @Shadow(remap = false) private int burnTime;
    @Shadow(remap = false) private int lastBurnTime;
    @Shadow(remap = false) private int process;

    @Inject(method = "tickServer", at = @At("HEAD"), remap = false)
    private void captureCtx(IMultiblockContext<? extends FurnaceHandler.IFurnaceEnvironment<?>> ctx, CallbackInfoReturnable<Boolean> cir){
        capturedContext = ctx;
    }

    @Inject(method = "tickServer", at = @At("TAIL"), remap = false)
    private void onTick(IMultiblockContext<? extends FurnaceHandler.IFurnaceEnvironment<?>> ctx, CallbackInfoReturnable<Boolean> cir){
        if(ctx.getState() instanceof AdvBlastFurnaceLogic.State state){
            BlastFurnacePreheaterBlockEntity preheaterA = state.getPreheater(ctx.getLevel(), PREHEATERS[0]);
            BlastFurnacePreheaterBlockEntity preheaterB = state.getPreheater(ctx.getLevel(), PREHEATERS[1]);
            int curPreheaters = fakePreheaterSpeedup(preheaterA)
                + fakePreheaterSpeedup(preheaterB);
            if(lastPreheaters == -1) lastPreheaters = curPreheaters;
            if(lastPreheaters != curPreheaters){
                double oldMult = CommonConfig.IMPROVED_FUEL_MULT.get().get(lastPreheaters);
                double newMult = CommonConfig.IMPROVED_FUEL_MULT.get().get(curPreheaters);
                burnTime = (int) (burnTime / oldMult * newMult);
                lastBurnTime = (int) (lastBurnTime / oldMult * newMult);
                lastPreheaters = curPreheaters;
            }

        }
    }

    @Redirect(method = "tickServer", remap = false, at = @At(value = "INVOKE", target = "Lblusunrize/immersiveengineering/common/blocks/multiblocks/logic/FurnaceHandler$IFurnaceEnvironment;getBurnTimeOf(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;)I"))
    private int modifyBurnTime(FurnaceHandler.IFurnaceEnvironment<?> instance, Level level, ItemStack itemStack){
        int time = instance.getBurnTimeOf(level, itemStack);
        if(capturedContext.getState() instanceof AdvBlastFurnaceLogic.State state && time > 0){
            BlastFurnacePreheaterBlockEntity preheaterA = state.getPreheater(capturedContext.getLevel(), PREHEATERS[0]);
            BlastFurnacePreheaterBlockEntity preheaterB = state.getPreheater(capturedContext.getLevel(), PREHEATERS[1]);
            int index = 0;
            if(preheaterA != null) index += preheaterA.doSpeedup();
            if(preheaterB != null) index += preheaterB.doSpeedup();
            time = (int) (time * CommonConfig.IMPROVED_FUEL_MULT.get().get(index));
            lastPreheaters = index;
        }
        return time;
    }

    @Unique
    private int fakePreheaterSpeedup(BlastFurnacePreheaterBlockEntity preheater){
        if(preheater == null) return 0;
        int consumed = IEServerConfig.MACHINES.preheater_consumption.get();
        if(preheater.energyStorage.extractEnergy(consumed, true) == consumed){
            return 1;
        }
        else return 0;
    }


}