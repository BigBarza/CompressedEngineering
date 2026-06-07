package com.pression.compressedengineering.mixin.workbench;

import blusunrize.immersiveengineering.api.crafting.BlueprintCraftingRecipe;
import blusunrize.immersiveengineering.api.crafting.IngredientWithSize;
import com.pression.compressedengineering.CommonConfig;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.OptionalInt;

@Mixin(BlueprintCraftingRecipe.class)
public class BlueprintRecipeMixin {
    @Shadow(remap = false)
    @Final
    public IngredientWithSize[] inputs;

    @Redirect(method = "consumeInputs", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item;hasCraftingRemainingItem(Lnet/minecraft/world/item/ItemStack;)Z"), remap = false)
    private boolean cancelLeftovers(Item instance, ItemStack stack){
        return false;
    }

    @Inject(method = "getMaxCrafted", at = @At(value = "INVOKE", target = "Ljava/util/Map$Entry;getValue()Ljava/lang/Object;", ordinal = 1), remap = false, locals = LocalCapture.CAPTURE_FAILHARD)
    private void fixTaken(NonNullList<ItemStack> query, CallbackInfoReturnable<Integer> cir, HashMap queryAmount, OptionalInt maxCrafted, IngredientWithSize[] var4, int var5, int var6, IngredientWithSize ingr, int maxCraftedWithIngredient, int req, Iterator<Map.Entry<ItemStack, Integer>>  queryIt, Map.Entry<ItemStack, Integer> e, ItemStack compStack, int taken){
        if(CommonConfig.ALT_BP_RECIPE_COUNT_CHECK.get()) {
            //Yes. This is a ugly hack. If you're reading this and are willing to replace this with something better...go right ahead!
            //I can't get taken to be 1 at the right point. So i set the value of the entry so that the end result is as if taken was 1.
            e.setValue(e.getValue() + (taken-1) * req);
        }
    }
}