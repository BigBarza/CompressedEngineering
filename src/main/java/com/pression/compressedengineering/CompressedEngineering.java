package com.pression.compressedengineering;

import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CompressedEngineering.MODID)
public class CompressedEngineering
{
    public static final String MODID = "compressedengineering";
    private static final Logger LOGGER = LogUtils.getLogger();
    public CompressedEngineering(){
        LOGGER.info("Hexagons are the bestagons!");
    }
}