package com.pression.compressedengineering;

import com.mojang.logging.LogUtils;
import com.pression.compressedengineering.recipe.CERecipeTypes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CompressedEngineering.MODID)
public class CompressedEngineering
{
    public static final String MODID = "compressedengineering";
    public static final Logger LOGGER = LogUtils.getLogger();
    public CompressedEngineering(){
        LOGGER.info("Hexagons are the bestagons!");
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        CERecipeTypes.RECIPE_TYPES.register(modEventBus);
        CERecipeTypes.RECIPE_SERIALIZERS.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CommonConfig.SPEC);
    }

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent e){
        if(CommonConfig.IMPROVED_FUEL_MULT.get().size() < 2){
            LOGGER.error("Not enough elements in IBF fuel multipliers list, reverting to default.");
            CommonConfig.IMPROVED_FUEL_MULT.set(CommonConfig.IMPROVED_FUEL_MULT.getDefault());
        }
        if(CommonConfig.PREHEATER_BOOST.get().size() < 2){
            LOGGER.error("Not enough elements in IBF preheater speeds list, reverting to default.");
            CommonConfig.PREHEATER_BOOST.set(CommonConfig.PREHEATER_BOOST.getDefault());
        }
    }

}