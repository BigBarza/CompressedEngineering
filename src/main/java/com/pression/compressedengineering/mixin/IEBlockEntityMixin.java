package com.pression.compressedengineering.mixin;

import blusunrize.immersiveengineering.common.blocks.IEBaseBlockEntity;
import blusunrize.immersiveengineering.common.util.ResettableCapability;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(IEBaseBlockEntity.class)
public interface IEBlockEntityMixin {
    @Accessor(value = "caps", remap = false)
    List<ResettableCapability<?>> getCaps();
}
