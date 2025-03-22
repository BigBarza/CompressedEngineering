package com.pression.compressedengineering.mixin.blastfurnace;

import blusunrize.immersiveengineering.api.crafting.BlastFurnaceFuel;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.register.IEMultiblockLogic;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.compat.jei.blastfurnace.BlastFurnaceFuelCategory;
import com.pression.compressedengineering.CommonConfig;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//This mixin alters the jei category for blast furnace fuel.
@Mixin(BlastFurnaceFuelCategory.class)
public class BlastFuelJEIMixin {

    //Widen the background a bit, as we need to fit the two blast furnaces.
    @ModifyArg(method = "<init>", at =
    @At(value = "INVOKE", target = "Lmezz/jei/api/gui/drawable/IDrawableBuilder;addPadding(IIII)Lmezz/jei/api/gui/drawable/IDrawableBuilder;"),
            remap = false, index = 3)
    private int addMorePadding(int paddingRight){
        return paddingRight + 24;
    }

    //Show the two blast furnaces next to their respective burn time text.
    @Inject(method = "setRecipe(Lmezz/jei/api/gui/builder/IRecipeLayoutBuilder;Lblusunrize/immersiveengineering/api/crafting/BlastFurnaceFuel;Lmezz/jei/api/recipe/IFocusGroup;)V",
            at = @At("TAIL"), remap = false)
    private void addFurnaces(IRecipeLayoutBuilder builder, BlastFurnaceFuel recipe, IFocusGroup focuses, CallbackInfo ci){
        builder.addSlot(RecipeIngredientRole.CATALYST, 20, 0)
                .addItemStack(IEMultiblockLogic.BLAST_FURNACE.iconStack());
        builder.addSlot(RecipeIngredientRole.CATALYST, 20, 20)
                .addItemStack(IEMultiblockLogic.ADV_BLAST_FURNACE.iconStack())
                .addTooltipCallback((recipeSlotView, tooltip) -> {
                    tooltip.add(Component.translatable("compressedengineering.jei.blastfuel"));
                    tooltip.add(Component.translatable("compressedengineering.jei.no_preheaters",
                            Utils.formatDouble(recipe.burnTime * CommonConfig.IMPROVED_FUEL_MULT.get().get(0) / CommonConfig.PREHEATER_BOOST.get().get(0)/20, "#.##"),
                            CommonConfig.PREHEATER_BOOST.get().get(0)));
                    tooltip.add(Component.translatable("compressedengineering.jei.one_preheater",
                            Utils.formatDouble(recipe.burnTime * CommonConfig.IMPROVED_FUEL_MULT.get().get(1) / CommonConfig.PREHEATER_BOOST.get().get(1)/20, "#.##"),
                            CommonConfig.PREHEATER_BOOST.get().get(1)));
                    tooltip.add(Component.translatable("compressedengineering.jei.both_preheaters",
                            Utils.formatDouble(recipe.burnTime * CommonConfig.IMPROVED_FUEL_MULT.get().get(2) / CommonConfig.PREHEATER_BOOST.get().get(2)/20, "#.##"),
                            CommonConfig.PREHEATER_BOOST.get().get(2)));
                });
    }

    //Move the original burn time upwards and to the right, to accommodate the second text and the blast furnace "items".
    @Redirect(method = "draw(Lblusunrize/immersiveengineering/api/crafting/BlastFurnaceFuel;Lmezz/jei/api/gui/ingredient/IRecipeSlotsView;Lnet/minecraft/client/gui/GuiGraphics;DD)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;IIIZ)I"))
    private int moveFirstText(GuiGraphics instance, Font font, String text, int x, int y, int col, boolean shadow){
        return instance.drawString(font, text, 40, 4, col, shadow);
    }

    //Add the second burn time text, hinting to hover over the IBF.
    @Inject(method = "draw(Lblusunrize/immersiveengineering/api/crafting/BlastFurnaceFuel;Lmezz/jei/api/gui/ingredient/IRecipeSlotsView;Lnet/minecraft/client/gui/GuiGraphics;DD)V",
            remap = false, at = @At("TAIL"))
    private void addSecondText(BlastFurnaceFuel recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY, CallbackInfo ci){
        Component hoverHint = Component.translatable("compressedengineering.jei.blastfuel.hover");
        graphics.drawString(ClientUtils.font(), hoverHint.getString(), 40, 23, 0x777777, false);
    }
}