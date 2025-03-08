package com.pression.compressedengineering.interfaces;

import blusunrize.immersiveengineering.api.multiblocks.blocks.util.StoredCapability;
import net.minecraftforge.items.IItemHandler;

public interface IStateProxy {
    StoredCapability<IItemHandler> getElectrodeHandler();
}
