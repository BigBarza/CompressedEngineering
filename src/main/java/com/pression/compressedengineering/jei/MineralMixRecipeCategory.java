package com.pression.compressedengineering.jei;

import blusunrize.immersiveengineering.api.crafting.StackWithChance;
import blusunrize.immersiveengineering.api.excavator.MineralMix;
import blusunrize.immersiveengineering.common.register.IEItems;
import blusunrize.immersiveengineering.common.register.IEMultiblockLogic;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.pression.compressedengineering.CompressedEngineering;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

public class MineralMixRecipeCategory implements IRecipeCategory<MineralMix> {
    public static final RecipeType<MineralMix> TYPE = RecipeType.create(CompressedEngineering.MODID, "excavator_veins", MineralMix.class);

    private final Component title;
    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable overlay;
    private final ResourceLocation texPath = new ResourceLocation(CompressedEngineering.MODID, "textures/gui/jei_excavator.png");
    private final List<Integer> colours = List.of(0x88AA0000, 0x88FFAA00, 0x8800AA00, 0x880000AA, 0x88AA00AA, 0x8800AAAA);

    public MineralMixRecipeCategory(IGuiHelper guiHelper){
        this.title = Component.translatable("compressedengineering.jei.excavator_title");
        this.background = guiHelper.createBlankDrawable(180, 105);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, IEMultiblockLogic.EXCAVATOR.iconStack());
        this.overlay = guiHelper.createDrawable(texPath, 0,0, 180 ,105);
    }

    @Override
    public RecipeType<MineralMix> getRecipeType() {
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
    public void draw(MineralMix recipe, IRecipeSlotsView slotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        RenderSystem.enableBlend();
        overlay.draw(graphics, 0, 0);
        Font font = Minecraft.getInstance().font;
        Component veinNameText = Component.translatable(recipe.getTranslationKey());
        Component failChanceText = recipe.spoils.length > 0 ? Component.translatable("compressedengineering.jei.basefailchance", String.format("%.1f%%", recipe.failChance * 100))
                : Component.translatable("compressedengineering.jei.basefailchance_nospoils", String.format("%.1f%%", recipe.failChance * 100));
        //This should place the text in the right spots regardless of the box's final size.
        graphics.drawString(font, veinNameText.getVisualOrderText(), ((float) background.getWidth() / 2)-((float) font.width(veinNameText.getString()) /2), 0, 0x888888, false);
        graphics.drawString(font, failChanceText.getVisualOrderText(), ((float) background.getWidth() / 2)-((float) font.width(failChanceText.getString()) /2), 52, 0x888888, false);

        float barFilled = ((float) background.getWidth() /2)- 75;
        int area = (background.getWidth()-16-(16*recipe.outputs.length))/2;
        int xOffset = 16;
        int col = 0;
        for(int i = 0; i<recipe.outputs.length; i++){

            StackWithChance output = recipe.outputs[i];
            xOffset += (int) (area*output.chance()+8);
            int x2 = Math.round(barFilled+(output.chance()*75));
            drawLine(graphics.pose(), xOffset, 28, xOffset, 32, 0xFF666666);
            graphics.fill(xOffset-1, 31, xOffset+1,33, 0xFF666666);
            drawLine(graphics.pose(), xOffset, 32, x2, 36, 0xFF666666);
            graphics.fill(x2-1, 35, x2+1, 37, 0xFF666666);
            drawLine(graphics.pose(), x2, 36, x2, 40, 0xFF666666);

            graphics.fill((int) barFilled, 40, (int) (barFilled+output.chance()*150), 47, colours.get(col));
            col++;
            if(col >= colours.size()) col = 0;
            barFilled += output.chance()*150;
            xOffset += (int) (area*output.chance()+8);
        }


        barFilled = ((float) background.getWidth() /2)- 75;
        area = (background.getWidth()-16-(16*recipe.spoils.length))/2;
        xOffset = 16;
        col = 0;
        for(int i = 0; i<recipe.spoils.length; i++){

            StackWithChance output = recipe.spoils[i];
            xOffset += (int) (area*output.chance()+8);
            int x2 = Math.round(barFilled+(output.chance()*75));
            drawLine(graphics.pose(), xOffset, 80, xOffset, 84, 0xFF666666);
            graphics.fill(xOffset-1, 83, xOffset+1,85, 0xFF666666);
            drawLine(graphics.pose(), xOffset, 84, x2, 88, 0xFF666666);
            graphics.fill(x2-1, 87, x2+1, 89, 0xFF666666);
            drawLine(graphics.pose(), x2, 88, x2, 92, 0xFF666666);

            graphics.fill((int) barFilled, 92, (int) (barFilled+output.chance()*150), 99, colours.get(col));
            col++;
            if(col >= colours.size()) col = 0;
            barFilled += output.chance()*150;
            xOffset += (int) (area*output.chance()+8);
        }

        RenderSystem.disableBlend();
    }


    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, MineralMix recipe, IFocusGroup focuses) {
        int area = (background.getWidth()-16-(16*recipe.outputs.length))/2;
        int xOffset = 16;
        for(int i = 0; i<recipe.outputs.length; i++){
            StackWithChance output = recipe.outputs[i];
            xOffset += (int) (area*output.chance()+8);
            builder.addSlot(RecipeIngredientRole.OUTPUT, xOffset-8, 12).addItemStack(output.stack().get())
                    .addTooltipCallback((recipeSlotView, tooltip) -> tooltip.add(1, Component.translatable("compressedengineering.jei.outputchance", String.format("%.1f%%", output.chance() * 100))));
            xOffset += (int) (area*output.chance()+8);
        }

        area = (background.getWidth()-16-(16*recipe.spoils.length))/2;
        xOffset = 16;
        for(int i = 0; i<recipe.spoils.length; i++){
            StackWithChance output = recipe.spoils[i];
            xOffset += (int) (area*output.chance()+8);
            builder.addSlot(RecipeIngredientRole.OUTPUT, xOffset-8, 64).addItemStack(output.stack().get())
                    .addTooltipCallback((recipeSlotView, tooltip) -> tooltip.add(1, Component.translatable("compressedengineering.jei.outputchance", String.format("%.1f%%", output.chance() * 100))));
            xOffset += (int) (area*output.chance()+8);
        }

        ItemStack coreSample = new ItemStack(IEItems.Misc.CORESAMPLE);
        try {
            coreSample.setTag(TagParser.parseTag("{dimension:\"minecraft:overworld\",mineralInfo:[{depletion:0,mineral:\"immersiveengineering:mineral/"+recipe.getPlainName()+"\",percentage:1.0d,saturation:1.0d}],timestamp:0L,x:0,z:0}"));
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
        builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 0, 0)
                .addItemStack(coreSample)
                .addTooltipCallback((recipeSlotView, tooltip) -> tooltip.add(Component.translatable("compressedengineering.jei.core_sample_disclaimer")));

    }

    @NotNull
    @Override
    public List<Component> getTooltipStrings(MineralMix recipe, IRecipeSlotsView slotsView, double mouseX, double mouseY){
        List<Component> tooltips = new ArrayList<>();
        Component veinName = Component.translatable(recipe.getTranslationKey());
        Component failChance = recipe.spoils.length > 0 ? Component.translatable("compressedengineering.jei.basefailchance", String.format("%.1f%%", recipe.failChance * 100))
                : Component.translatable("compressedengineering.jei.basefailchance_nospoils", String.format("%.1f%%", recipe.failChance * 100));

        Font font = Minecraft.getInstance().font;
        //All this to ensure we only cover the text's area.
        if(mouseY < 12 && mouseX > (double) background.getWidth() /2 - (double) font.width(veinName.getString()) /2 && mouseX < (double) background.getWidth()/2 + (double) font.width(veinName.getString()) /2){
            tooltips.add(Component.translatable("compressedengineering.jei.vein_weight_1", recipe.weight));
            tooltips.add(Component.translatable("compressedengineering.jei.vein_weight_2"));
            tooltips.add(Component.translatable("compressedengineering.jei.dimensions_list"));
            recipe.dimensions.forEach(dim -> {
                tooltips.add(Component.literal(dim.location().getNamespace()+":"+dim.location().getPath()));
            });
        }

        if(mouseY > 52 && mouseY < 64 && mouseX > (double) background.getWidth() /2 - (double) font.width(failChance.getString()) /2 && mouseX < (double) background.getWidth()/2 + (double) font.width(failChance.getString()) /2){
            tooltips.add(Component.translatable("compressedengineering.jei.spoils_guide"));
        }

        return tooltips;
    }


    private static void drawLine(PoseStack ms, float x1, float y1, float x2, float y2, int col){
        float deltaX = x1-x2;
        float deltaY = y1-y2;
        float magnitude = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        if(magnitude == 0) return;
        deltaX =  ((deltaX / magnitude));
        deltaY =  ((deltaY / magnitude));

        //Forgive me for what i am about to do
        //BEWARE: Rendering fuckery ahead.
        //RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        Matrix4f matrix = ms.last().pose();
        BufferBuilder buffer = Tesselator.getInstance().getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        buffer.vertex(matrix, x2+deltaY, y2-deltaX, 0).color(col).endVertex();
        buffer.vertex(matrix, x2-deltaY, y2+deltaX, 0).color(col).endVertex();
        buffer.vertex(matrix, x1-deltaY, y1+deltaX, 0).color(col).endVertex();
        buffer.vertex(matrix, x1+deltaY, y1-deltaX, 0).color(col).endVertex();

        BufferUploader.drawWithShader(buffer.end());
        //RenderSystem.enableTexture();
    }

    }
