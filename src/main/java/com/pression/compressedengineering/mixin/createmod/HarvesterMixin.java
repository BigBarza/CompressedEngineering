package com.pression.compressedengineering.mixin.createmod;

import blusunrize.immersiveengineering.common.blocks.plant.HempBlock;
import com.simibubi.create.content.contraptions.actors.harvester.HarvesterMovementBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//This mixin fixes the behavioud of hemp crops with Create harvesters.
@Mixin(value = HarvesterMovementBehaviour.class, remap = false)
@Pseudo
public class HarvesterMixin {
    //Fixes harvesters replacing the top block of hemp crops with...something. A ghost block? Whatever it is, it jams the crop and farm.
    @Inject(method = "cutCrop", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getBlock()Lnet/minecraft/world/level/block/Block;", ordinal = 0, remap = true), cancellable = true)
    private void handleCutCrop(Level world, BlockPos pos, BlockState state, CallbackInfoReturnable<BlockState> cir) {
        if(state.getBlock() instanceof HempBlock && state.getValue(HempBlock.TOP)) {
            cir.setReturnValue(Blocks.AIR.defaultBlockState());
        }
    }

    //These two make the hemp recognized as "harvestable" but not strictly a "crop"
    //Doing so, prevents create from subtracting a seed, making the crop yield no seeds at all.
    @Inject(method = "isValidCrop", at = @At(value = "HEAD",remap = true), cancellable = true)
    private void hempValidCrop(Level world, BlockPos pos, BlockState state, CallbackInfoReturnable<Boolean> cir){
        if(state.getBlock() instanceof HempBlock && state.getValue(HempBlock.TOP)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "isValidOther", at = @At(value = "HEAD",remap = true), cancellable = true)
    private void hempValidOther(Level world, BlockPos pos, BlockState state, CallbackInfoReturnable<Boolean> cir){
        if(state.getBlock() instanceof HempBlock && state.getValue(HempBlock.TOP)) {
            cir.setReturnValue(true);
        }
    }
}
