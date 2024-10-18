package com.pression.compressedengineering.jei;

import blusunrize.immersiveengineering.common.register.IEBlocks;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.pression.compressedengineering.CompressedEngineering;
import com.pression.compressedengineering.recipe.ShapedAssemblerRecipe;
import com.pression.compressedengineering.recipe.ShapelessAssemblerRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AssemblerRecipeCategory implements IRecipeCategory<CraftingRecipe> {
    public static final RecipeType<CraftingRecipe> TYPE = RecipeType.create(CompressedEngineering.MODID, "assembler", CraftingRecipe.class);

    private final Component title;
    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable shapeless;
    private final ResourceLocation texture = new ResourceLocation(CompressedEngineering.MODID, "textures/gui/jei_assembler.png");

    public AssemblerRecipeCategory(IGuiHelper guiHelper){
        this.title = Component.translatable("compressedengineering.jei.assembler_title");
        this.background = guiHelper.createDrawable(texture, 0,0,125,68);
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(IEBlocks.Multiblocks.ASSEMBLER));
        this.shapeless = guiHelper.createDrawable(texture, 126,1,14,14);
    }

    @Override
    public RecipeType<CraftingRecipe> getRecipeType(){
        return TYPE;
    }
    @Override
    public Component getTitle(){
        return title;
    }
    @Override
    public IDrawable getBackground(){
        return background;
    }
    @Override
    public IDrawable getIcon(){
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CraftingRecipe recipe, IFocusGroup focuses) {
        int xOffset = 9;
        int yOffset = 8;
        List<Ingredient> ingredientList = new ArrayList<>();

        if(recipe instanceof ShapedAssemblerRecipe r){

            Ingredient[] grid = new Ingredient[9];
            Arrays.fill(grid, Ingredient.EMPTY);

            for(int y=0; y<r.getHeight(); y++){
                for(int x=0; x<r.getWidth(); x++){
                    grid[(y*3)+x] = r.getIngredients().get((y*r.getWidth())+x);
                }
            }
            ingredientList.addAll(List.of(grid));
        }
        else ingredientList.addAll(recipe.getIngredients());

        for (Ingredient ingredient : ingredientList){
            if(ingredient.isEmpty()) builder.addSlot(RecipeIngredientRole.INPUT, xOffset, yOffset);
            else builder.addSlot(RecipeIngredientRole.INPUT, xOffset, yOffset).addItemStacks(List.of(ingredient.getItems()));
            xOffset += 18;
            if(xOffset >= 50){ //I HOPE the vanilla recipe serializer disallows more than 9 ingredients.
                xOffset = 9;
                yOffset += 18;
            }
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, 99, 26).addItemStack(recipe.getResultItem());

    }

    @Override
    public void draw(CraftingRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        RenderSystem.enableBlend();
        if(recipe instanceof ShapelessAssemblerRecipe r){
            shapeless.draw(stack, 69, 12); //nice.
        }
        RenderSystem.disableBlend();
    }

}
