package com.pression.compressedengineering.mixin;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.render.tile.BlueprintRenderer;
import com.pression.compressedengineering.CompressedEngineering;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;

//This mixin overrides the texture used to draw the blueprint if one is present in the correct path.
//If it does not exist, let the original stuff through.
@Mixin(BlueprintRenderer.class)
public class BlueprintRenderMixin {

    @Nullable
    private static TextureAtlasSprite getTexture(ResourceLocation rl){
        try {
            final Function<ResourceLocation, TextureAtlasSprite> blockAtlas = ClientUtils.mc().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS);
            //This returns a MissingTextureAtlasSprite if the texture isn't actually there.
            return blockAtlas.apply(new ResourceLocation(CompressedEngineering.MODID, "blueprint/"+rl.getNamespace()+"/"+rl.getPath()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Inject(method = "getBlueprintDrawable(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;)Lblusunrize/immersiveengineering/client/render/tile/BlueprintRenderer$BlueprintLines;",
    at = @At(value = "INVOKE", target = "Ljava/util/List;isEmpty()Z"), remap = false, locals = LocalCapture.CAPTURE_FAILHARD)
    private static void rewriteTexture(ItemStack stack, Level world, CallbackInfoReturnable<BlueprintRenderer.BlueprintLines> cir, Player player, List<TextureAtlasSprite> images){
        TextureAtlasSprite texture = getTexture(ForgeRegistries.ITEMS.getKey(stack.getItem()));
        if(texture != null && !(texture instanceof MissingTextureAtlasSprite)){
            images.clear();
            images.add(texture);
        }
    }
}
