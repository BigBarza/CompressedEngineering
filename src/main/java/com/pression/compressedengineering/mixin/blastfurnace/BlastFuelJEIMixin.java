package com.pression.compressedengineering.mixin.blastfurnace;

import blusunrize.immersiveengineering.api.crafting.BlastFurnaceFuel;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.register.IEBlocks;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.compat.jei.blastfurnace.BlastFurnaceFuelCategory;
import com.mojang.blaze3d.vertex.PoseStack;
import com.pression.compressedengineering.CommonConfig;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.gui.Font;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//This mixin alters the jei category for blast furnace fuel.
@Mixin(BlastFurnaceFuelCategory.class)
public class BlastFuelJEIMixin {

    //This flag serves to enable the changes to the category only if the IBF fuel multiplier is set to anything other than 1.
    //Afterall, it wouldn't make sense to change it if the two burn times are going to be the same.
    private static final boolean multFlag = CommonConfig.IMPROVED_FUEL_MULT.get() != 1;

    //Widen the background a bit, as we need to fit the two blast furnaces.
    @ModifyArg(method = "<init>", at =
    @At(value = "INVOKE", target = "Lmezz/jei/api/gui/drawable/IDrawableBuilder;addPadding(IIII)Lmezz/jei/api/gui/drawable/IDrawableBuilder;"),
    remap = false, index = 3)
    private int addMorePadding(int paddingRight){
        return multFlag ? paddingRight + 24 : paddingRight;
    }

    //Show the two blast furnaces next to their respective burn time text.
    @Inject(method = "setRecipe(Lmezz/jei/api/gui/builder/IRecipeLayoutBuilder;Lblusunrize/immersiveengineering/api/crafting/BlastFurnaceFuel;Lmezz/jei/api/recipe/IFocusGroup;)V",
    at = @At("TAIL"), remap = false)
    private void addFurnaces(IRecipeLayoutBuilder builder, BlastFurnaceFuel recipe, IFocusGroup focuses, CallbackInfo ci){
        if(!multFlag) return;
        builder.addSlot(RecipeIngredientRole.CATALYST, 20, 0)
                .addItemStack(new ItemStack(IEBlocks.Multiblocks.BLAST_FURNACE.asItem()));
        builder.addSlot(RecipeIngredientRole.CATALYST, 20, 20)
                .addItemStack(new ItemStack(IEBlocks.Multiblocks.ADVANCED_BLAST_FURNACE.asItem()));
    }

    //Move the original burn time upwards and to the right, to accommodate the second text and the blast furnace "items".
    @Redirect(method = "draw(Lblusunrize/immersiveengineering/api/crafting/BlastFurnaceFuel;Lmezz/jei/api/gui/ingredient/IRecipeSlotsView;Lcom/mojang/blaze3d/vertex/PoseStack;DD)V",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Font;draw(Lcom/mojang/blaze3d/vertex/PoseStack;Ljava/lang/String;FFI)I"))
    private int moveFirstText(Font instance, PoseStack ms, String text, float x, float y, int col){
        if(multFlag) return instance.draw(ms, text, 40, 4, col);
        else return instance.draw(ms, text, 24, 12, col);
    }

    //Add the second burn time text, modified to reflect the multiplier.
    @Inject(method = "draw(Lblusunrize/immersiveengineering/api/crafting/BlastFurnaceFuel;Lmezz/jei/api/gui/ingredient/IRecipeSlotsView;Lcom/mojang/blaze3d/vertex/PoseStack;DD)V",
    remap = false, at = @At("TAIL"))
    private void addSecondText(BlastFurnaceFuel recipe, IRecipeSlotsView recipeSlotsView, PoseStack transform, double mouseX, double mouseY, CallbackInfo ci){
        if(!multFlag) return;
        String improvedBurnTime = I18n.get("desc.immersiveengineering.info.seconds", Utils.formatDouble((recipe.burnTime)*CommonConfig.IMPROVED_FUEL_MULT.get()/20, "#.##"));
        ClientUtils.font().draw(transform, improvedBurnTime, 40, 23, 0x777777);
    }



}
