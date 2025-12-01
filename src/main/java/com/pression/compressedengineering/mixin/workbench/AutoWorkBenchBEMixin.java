package com.pression.compressedengineering.mixin.workbench;


import blusunrize.immersiveengineering.api.crafting.BlueprintCraftingRecipe;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockContext;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.AutoWorkbenchLogic;
import net.minecraft.core.NonNullList;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(AutoWorkbenchLogic.class)
public abstract class AutoWorkBenchBEMixin {
    //@Shadow(remap = false) @Final public NonNullList<ItemStack> inventory;

    @Shadow(remap = false) @Final private static int FIRST_INPUT_SLOT;

    //@Shadow(remap = false) public abstract void doProcessOutput(ItemStack output);

    @Unique NonNullList<ItemStack> leftovers;
    @Unique
    NonNullList<ItemStack> consumed;

    @Inject(method = "tickServer", at = @At(value = "INVOKE", target = "Lblusunrize/immersiveengineering/api/crafting/BlueprintCraftingRecipe;consumeInputs(Lnet/minecraft/core/NonNullList;I)Lnet/minecraft/core/NonNullList;"), remap = false, locals = LocalCapture.CAPTURE_FAILHARD)
    private void captureLeftovers(IMultiblockContext<AutoWorkbenchLogic.State> context, CallbackInfo ci, AutoWorkbenchLogic.State state, boolean isRSEnabled, boolean active, BlueprintCraftingRecipe[] recipes, BlueprintCraftingRecipe recipe, NonNullList<ItemStack> query, int crafted){
        SimpleContainer container = new SimpleContainer(query.size());
        NonNullList<ItemStack> copy = NonNullList.withSize(query.size(), ItemStack.EMPTY);
        for(int i = 0;i<query.size();i++) copy.set(i, query.get(i).copy());
        consumed = recipe.consumeInputs(copy, 1);
        consumed.forEach(container::addItem);
        leftovers = recipe.getRemainingItems(container);
        readdLeftovers(context);
    }

    //@Inject(method = "tickServer", at = @At(value = "INVOKE", target = "Lblusunrize/immersiveengineering/common/blocks/metal/AutoWorkbenchBlockEntity;setChanged()V"))
    private void readdLeftovers(IMultiblockContext<AutoWorkbenchLogic.State> context){
        SimpleContainer container = new SimpleContainer(context.getState().inventory.getSlots());
        for(int i = 0; i<container.getContainerSize(); i++) container.setItem(i, context.getState().inventory.getStackInSlot(i).copy());
        for(int i = 0; i<leftovers.size(); i++){
            if(leftovers.get(i).isEmpty()) continue;
            if(leftovers.get(i).is(consumed.get(i).getItem()) && container.canAddItem(leftovers.get(i))){
                container.addItem(leftovers.get(i));
            }else{
                context.getState().doProcessOutput(leftovers.get(i), context.getLevel());
            }

        }
        for(int i = 0; i< container.getContainerSize(); i++){
            context.getState().inventory.setStackInSlot(i, container.getItem(i).copy());
        }

    }


}
