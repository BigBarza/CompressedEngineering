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

@Mixin(value = HarvesterMovementBehaviour.class, remap = false)
@Pseudo
public class HarvesterMixin {
    @Inject(method = "cutCrop", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getBlock()Lnet/minecraft/world/level/block/Block;", ordinal = 0, remap = true), cancellable = true)
    private void handleHemp(Level world, BlockPos pos, BlockState state, CallbackInfoReturnable<BlockState> cir) {
        if(state.getBlock() instanceof HempBlock) {
            cir.setReturnValue(Blocks.AIR.defaultBlockState());
        }
    }
}
