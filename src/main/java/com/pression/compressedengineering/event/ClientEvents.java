package com.pression.compressedengineering.event;

import com.pression.compressedengineering.CompressedEngineering;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = CompressedEngineering.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents {
    @SubscribeEvent
    //The blueprint textures don't get picked up automatically, and we need to tell the atlas to grab them before we can make use of them.
    public static void onTextureStitch(TextureStitchEvent.Pre e){
        if(!e.getAtlas().location().equals(TextureAtlas.LOCATION_BLOCKS)) return;
        ResourceManager manager = Minecraft.getInstance().getResourceManager();
        for(ResourceLocation rl : manager.listResources("textures/blueprint/", s -> s.getPath().endsWith(".png")).keySet()){
            //Some fixing is needed to get the correct format.
            e.addSprite(new ResourceLocation(rl.getNamespace(), rl.getPath().substring("textures/".length(), rl.getPath().length() - ".png".length())));
        }
    }
}
