package com.pression.compressedengineering.mixin.arcfurnace;

import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IInitialMultiblockContext;
import blusunrize.immersiveengineering.api.multiblocks.blocks.util.StoredCapability;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.arcfurnace.ArcFurnaceLogic;
import blusunrize.immersiveengineering.common.register.IEItems;
import blusunrize.immersiveengineering.common.util.inventory.WrappingItemHandler;
import com.pression.compressedengineering.interfaces.IStateProxy;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArcFurnaceLogic.State.class)
public class ArcFurnaceStateMixin implements IStateProxy {
    @Shadow(remap = false)
    public ItemStackHandler inventory;
    private StoredCapability<IItemHandler> electrodeHandler;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onInit(IInitialMultiblockContext ctx, CallbackInfo ci){
        //ElectrodeItemStackHandler electrode = (ElectrodeItemStackHandler) inventory;
        inventory = new ItemStackHandler(26){
            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack){
                if(slot >= 23 && slot < 26){
                    return IEItems.Misc.GRAPHITE_ELECTRODE.asItem().equals(stack.getItem());
                }
                else return true;
            }
            @Override
            public int getStackLimit(int slot, @NotNull ItemStack stack){
                if(slot >= 23 && slot < 26) return 1;
                else return super.getStackLimit(slot, stack);
            }
        };
        electrodeHandler = new StoredCapability<>(
                new WrappingItemHandler(inventory, true, true, new WrappingItemHandler.IntRange(23, 26)));
    }

    public StoredCapability<IItemHandler> getElectrodeHandler() {
        return electrodeHandler;
    }
}
