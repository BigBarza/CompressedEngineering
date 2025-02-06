package com.pression.compressedengineering.mixin;

import blusunrize.immersiveengineering.common.blocks.metal.ArcFurnaceBlockEntity;
import blusunrize.immersiveengineering.common.register.IEItems;
import blusunrize.immersiveengineering.common.util.MultiblockCapability;
import blusunrize.immersiveengineering.common.util.ResettableCapability;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import blusunrize.immersiveengineering.common.util.inventory.IIEInventory;
import com.pression.compressedengineering.interfaces.IElectrodeHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ArcFurnaceBlockEntity.class)
public abstract class ArcFurnaceMixin implements IElectrodeHandler {
    @Shadow(remap = false)
    public abstract ArcFurnaceBlockEntity getGuiMaster();

    @Unique
    public final MultiblockCapability<IItemHandler> electrodeHandler = MultiblockCapability.make(
            (ArcFurnaceBlockEntity) (Object) this,
            be -> ((IElectrodeHandler) be).getElectrodeCap(),
            ArcFurnaceBlockEntity::master,
            makeCapability(new IEInventoryHandler(3, (IIEInventory) this, 23, true, true)
                           {
                               @Override
                               public ItemStack insertItem(int slot, ItemStack stack, boolean simulate){
                                   if(!IEItems.Misc.GRAPHITE_ELECTRODE.asItem().equals(stack.getItem())){
                                       //If it's not an electrode, send it back.
                                       return stack;
                                   }
                                   ItemStack returnedStack = super.insertItem(slot, stack, simulate);
                                   //This is just to update the electrodes visually.
                                   getGuiMaster().updateMasterBlock(getGuiMaster().getBlockState(), true);
                                   return returnedStack;
                               }
                               @Override
                               public ItemStack extractItem(int slot, int amount, boolean simulate){
                                   ItemStack extractedStack = super.extractItem(slot, amount, simulate);
                                   //This is just to update the electrodes visually.
                                   getGuiMaster().updateMasterBlock(getGuiMaster().getBlockState(), true);
                                   return extractedStack;
                               }
                           }
            ));

    @Override
    public MultiblockCapability<IItemHandler> getElectrodeCap(){
        return electrodeHandler;
    }

    @Unique
    private <T> ResettableCapability<T> makeCapability(T val){
        List<ResettableCapability<?>> caps = ((IEBlockEntityMixin) this).getCaps();
        ResettableCapability<T> cap = new ResettableCapability<>(val);
        caps.add(cap);
        return cap;
    }

    @Inject(method = "getCapability", at = @At("HEAD"), remap = false, cancellable = true)
    private <T> void injectElectrodeCapability(Capability<T> capability, Direction facing, CallbackInfoReturnable<LazyOptional<T>> cir){
        if(capability == ForgeCapabilities.ITEM_HANDLER){
            if(new BlockPos(2,4,2).equals(((ArcFurnaceBlockEntity)(Object) this).posInMultiblock)){
                cir.setReturnValue(electrodeHandler.getAndCast());
            }
        }
    }

}
