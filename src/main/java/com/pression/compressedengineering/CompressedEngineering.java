package com.pression.compressedengineering;

import com.mojang.logging.LogUtils;
import com.pression.compressedengineering.recipe.IERecipeTypes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CompressedEngineering.MODID)
public class CompressedEngineering
{
    public static final String MODID = "compressedengineering";
    private static final Logger LOGGER = LogUtils.getLogger();
    public CompressedEngineering(){
        LOGGER.info("Hexagons are the bestagons!");
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IERecipeTypes.RECIPE_TYPES.register(modEventBus);
        IERecipeTypes.RECIPE_SERIALIZERS.register(modEventBus);
    }
}