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
    private ItemStackHandler electrodeInventory;
    private StoredCapability<IItemHandler> electrodeHandler;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onInit(IInitialMultiblockContext ctx, CallbackInfo ci){
        //ElectrodeItemStackHandler electrode = (ElectrodeItemStackHandler) inventory;
        electrodeInventory = new ItemStackHandler(26){
            //The previous method isn't working so the checking has to be done here.
            @Override
            public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate){
                if(!IEItems.Misc.GRAPHITE_ELECTRODE.asItem().equals(stack.getItem())) return stack;
                ItemStack stackCopy = stack.copy();
                ItemStack returnCopy = stack.copy();
                returnCopy.shrink(1);
                stackCopy.setCount(1);
                if(inventory.getStackInSlot(slot) == ItemStack.EMPTY){
                    inventory.insertItem(slot, stackCopy, simulate);
                    return returnCopy;
                }
                else return stack;
            }
            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate){
                return inventory.extractItem(slot, amount, simulate);
            }
        };
        electrodeHandler = new StoredCapability<>(
                new WrappingItemHandler(electrodeInventory, true, true, new WrappingItemHandler.IntRange(23, 26)));
    }

    public StoredCapability<IItemHandler> getElectrodeHandler() {
        return electrodeHandler;
    }
}
