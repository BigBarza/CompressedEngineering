package com.pression.compressedengineering;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class CommonConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Double> IMPROVED_FUEL_MULT;
    public static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> PREHEATER_BOOST;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ALT_CONCRETE;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ELECTRODE_AUTOMATION;

    static {
        BUILDER.push("Compressed Engineering Config");
        IMPROVED_FUEL_MULT = BUILDER.comment("A multiplier on the fuel duration in the Improved Blast Furnace. Higher values means the fuel lasts longer. The final value will be an integer.")
                .defineInRange("IBF Fuel Efficiency", 1, Double.MIN_VALUE, Double.MAX_VALUE);
        PREHEATER_BOOST = BUILDER.comment("List of multipliers for the speed of the Improved Blast Furnace, in order of 'No preheater', 'One preheater' and 'Two preheaters'")
                .defineList("IBF Speed Multipliers", List.of(1,2,3), mult -> mult instanceof Integer);
        ALT_CONCRETE = BUILDER.comment("Enable an alternative implementation of liquid concrete drying, where it dries all at once, starting from the source. Far from perfect, but should prevent permanent infinite concrete setups.")
                .define("Liquid Concrete Rework", true);
        ELECTRODE_AUTOMATION = BUILDER.comment("Allows automatic insertion of graphite electrodes in the arc furnace through the holder on top.")
                .define("Arc Furnace Electrode Automation", true);
        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
