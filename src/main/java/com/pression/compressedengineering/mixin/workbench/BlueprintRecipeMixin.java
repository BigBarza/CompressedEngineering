package com.pression.compressedengineering.mixin.workbench;

import blusunrize.immersiveengineering.api.crafting.BlueprintCraftingRecipe;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlueprintCraftingRecipe.class)
public class BlueprintRecipeMixin {
    @Redirect(method = "consumeInputs", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item;hasCraftingRemainingItem(Lnet/minecraft/world/item/ItemStack;)Z"), remap = false)
    private boolean cancelLeftovers(Item instance, ItemStack stack){
        return false;
    }
}