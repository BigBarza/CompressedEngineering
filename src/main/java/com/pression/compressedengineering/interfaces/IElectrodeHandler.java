package com.pression.compressedengineering.interfaces;

import blusunrize.immersiveengineering.common.util.MultiblockCapability;
import net.minecraftforge.items.IItemHandler;

public interface IElectrodeHandler {
    MultiblockCapability<IItemHandler> getElectrodeCap();
}
