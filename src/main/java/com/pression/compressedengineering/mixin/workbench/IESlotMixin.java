package com.pression.compressedengineering.mixin.workbench;

import blusunrize.immersiveengineering.api.crafting.BlueprintCraftingRecipe;
import blusunrize.immersiveengineering.common.gui.BlueprintInventory;
import blusunrize.immersiveengineering.common.gui.IESlot;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

//This mixin changes the leftover items behaviour for the Engineer's Workbench.
//The purpose is to make them friendlier to use with other mechanics in Compression.
@Mixin(IESlot.BlueprintOutput.class)
public class IESlotMixin {
    @Shadow(remap = false) @Final public BlueprintCraftingRecipe recipe;
    @Shadow(remap = false) @Final private Container inputInventory;

    @Unique NonNullList<ItemStack> leftovers;

    @Inject(method = "onTake", at = @At(value = "INVOKE", target = "Lblusunrize/immersiveengineering/common/gui/BlueprintInventory;reduceIputs(Lnet/minecraft/world/Container;Lblusunrize/immersiveengineering/api/crafting/BlueprintCraftingRecipe;Lnet/minecraft/world/item/ItemStack;)V"), remap = false)
    private void captureLeftovers(Player player, ItemStack stack, CallbackInfo ci){
        NonNullList<ItemStack> copy = NonNullList.withSize(inputInventory.getContainerSize(), ItemStack.EMPTY);
        for(int i = 0;i<copy.size();i++) copy.set(i, inputInventory.getItem(i).copy());
        NonNullList<ItemStack> consumed = recipe.consumeInputs(copy, 1);
        SimpleContainer container = new SimpleContainer(consumed.size());
        consumed.forEach(container::addItem);
        leftovers = recipe.getRemainingItems(container);
    }

    @Inject(method = "onTake", at = @At(value = "INVOKE", target = "Lblusunrize/immersiveengineering/common/gui/IESlot;onTake(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;)V"))
    private void readdLeftovers(Player player, ItemStack stack, CallbackInfo ci){
        //This holds itemstacks that could not be fit in place.
        List<ItemStack> unclaimed = new ArrayList<>();
        for(int i = 0; i<leftovers.size();i++){ //The NonNullList is always the same size as the container.
            if(leftovers.get(i).isEmpty()) continue;
            if(inputInventory.getItem(i).isEmpty()){ //If the matching slot is empty, add it here.
                inputInventory.setItem(i, leftovers.get(i));
                continue;
            }
            if(ItemStack.isSameItemSameTags(inputInventory.getItem(i), leftovers.get(i)) && //If the matching slot has that same item and can fit that much more, add it.
                    inputInventory.getItem(i).getCount() + leftovers.get(i).getCount() <= leftovers.get(i).getMaxStackSize()){
                inputInventory.getItem(i).grow(leftovers.get(i).getCount());
                continue;
            }
            //If we get here, we couldn't place the item in its slot. Place it in the queue for later.
            unclaimed.add(leftovers.get(i));
        }

        for(ItemStack item : unclaimed){ //Try to find a place for other leftover items.
            boolean deposited = false;
            for(int i = 0; i<inputInventory.getContainerSize();i++){
                if(inputInventory.getItem(i).isEmpty()) {
                    inputInventory.setItem(i, item);
                    deposited = true;
                    break;
                }
                else{
                    if(ItemStack.isSameItemSameTags(inputInventory.getItem(i), item) && inputInventory.getItem(i).getCount()+item.getCount() <= item.getMaxStackSize()){
                        inputInventory.getItem(i).grow(item.getCount());
                        deposited = true;
                        break;
                    }
                }
            }
            if(!deposited){ //If we couldn't deposit it, drop it for the player to pick it up.
                ItemEntity entity = new ItemEntity(player.level(), player.getX(), player.getY(), player.getZ(), item);
                player.level().addFreshEntity(entity);
            }
        }
        //Make it update the outputs again, since we meddled with the input inventory.
        ((BlueprintInventory) ((IESlot.BlueprintOutput)(Object) this).container).updateOutputs(inputInventory);
    }

}