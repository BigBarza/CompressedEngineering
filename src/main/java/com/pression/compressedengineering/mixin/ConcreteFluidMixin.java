package com.pression.compressedengineering.mixin;

import blusunrize.immersiveengineering.common.fluids.ConcreteFluid;
import blusunrize.immersiveengineering.common.register.IEBlocks;
import blusunrize.immersiveengineering.common.register.IEPotions;
import com.pression.compressedengineering.CommonConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

//This mixin adds the alternate implementation of liquid concrete hardening.
@Mixin(ConcreteFluid.class)
public class ConcreteFluidMixin {

    @Unique //This just saves the config value for faster reference.
    private static final boolean ENABLE_ALT_IMPL = CommonConfig.ALT_CONCRETE.get();

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lblusunrize/immersiveengineering/common/fluids/ConcreteFluid;isSource(Lnet/minecraft/world/level/material/FluidState;)Z"),
            cancellable = true, locals = LocalCapture.CAPTURE_FAILSOFT)
    private void onTick(Level world, BlockPos pos, FluidState state, CallbackInfo ci, int timer, int level, int quantaRemaining, boolean mayDry){
        if(!ENABLE_ALT_IMPL) return;
        mayDry = false; //Disable normal drying.
        if(!state.isSource()) ci.cancel(); //Don't do anything else on non-source blocks.
        else if (timer >= 31){ //Solidification time
            hardenRecursive(world, pos, state);
        }
    }
    //This method hardens the liquid concrete block then recursively goes through all connected fluid blocks until the entire flow is hardened.
    private void hardenRecursive(Level world, BlockPos pos, FluidState state){
        hardenBlock(world, pos, state);
        Direction.stream().forEach(direction -> {
            FluidState newState = world.getBlockState(pos.relative(direction)).getFluidState();
            if(newState.getType().isSame(state.getType())){
                hardenRecursive(world, pos.relative(direction), newState);
            }
        });
    }

    //This method implements the actual solidification. Mostly spliced from the original.
    private void hardenBlock(Level world, BlockPos pos, FluidState state){
        IEBlocks.BlockEntry<? extends Block> concreteBlock;
        int fluidLevel = getFluidLevel(state);
        if(fluidLevel >= 5 && fluidLevel < 8) concreteBlock = IEBlocks.TO_SLAB.get(IEBlocks.StoneDecoration.CONCRETE.getId());
        else concreteBlock = IEBlocks.StoneDecoration.CONCRETE;
        world.setBlockAndUpdate(pos, concreteBlock.get().defaultBlockState());
        for(LivingEntity living : world.getEntitiesOfClass(LivingEntity.class, new AABB(pos, pos.offset(1, 1, 1))))
            living.addEffect(new MobEffectInstance(IEPotions.CONCRETE_FEET.get(), Integer.MAX_VALUE));
    }

    //The original one is package-protected so we just replicate the logic here. I don't quite get it but hey, if it works...
    private int getFluidLevel(FluidState state){
        return state.isSource() ? 0 : 8 - Math.min(state.getAmount(), 8) + (state.getValue(FlowingFluid.FALLING) ? 8 : 0);
    }

}