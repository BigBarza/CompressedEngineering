## Compressed Engineering

This mod contains assorted tweaks to Immersive Engineering made primarily for the Compression modpack.

### Features
- Allows the coke oven to have custom fluid outputs instead of just the hardcoded creosote oil. It adds no recipes on its own.  
- Prevents a crash when more than 13 items are assigned to an engineering blueprint. Specifically, it would crash when removing said blueprint.
- Adds two new recipe types (compressedengineering:assembler_shaped and compressedengineering:assembler_shapeless).
  - They're identical to crafting recipes but can only be done in the IE Assembler. Supports JEI's "Move to grid" feature.
- New JEI integration for excavator veins, complete with custom graphs and more info visible to the player.
- Allows electrodes to be input and output from the arc furnace. I/O point is the electrode holder on top. Toggleable in configs.
- Fixes an issue where the secondary output chance for the arc furnace is inverted.
- Configurable multiplier for fuel duration in the improved blast furnace. Modifies the JEI category for fuel burn time if set to anything other than 1.
- Customizable values for the improved blast furnace's base speed and speed with preheaters.
- Alternative implementation for liquid concrete hardening where it hardens all at once starting from the source. Toggleable in configs, if disabled, the old system will be used instead.

Curseforge link: https://legacy.curseforge.com/minecraft/mc-mods/compressed-engineering

Compression modpack: https://www.curseforge.com/minecraft/modpacks/compression

Compression Discord: https://discord.gg/dz7UJ7sDmU

### Changing the fluid of a coke oven recipe
```
{
 "type": "immersiveengineering:coke_oven",
 "fluid": {
    "amount":1000,
    "fluid":"immersiveengineering:ethanol"
  },
 "input": {
    "item":"minecraft:gold_ingot"
  },
 "result": {
    "item":"minecraft:diamond"
  },
 "time": 100
}
```
### FAQ: (Not really, but i feel like these might come up.)

- *Q: Will you support other Minecraft versions or loaders?*
  - A: No, this mod is made with the needs of the Compression modpack first. Feel free to open a pull request though.

- *Q: What happens to the default coke oven recipes?*
  - A: Nothing. If there's no custom fluid but a creosote value, the coke oven will output creosote oil as usual.

- *Q: What happens if there's a different fluid in the coke oven output?*
  - A: The recipe simply won't start.

- *Q: Is there a new cap for the blueprint crash fix?*
  - A: Yes, 33 recipes. Other things usually break before it gets there.

- *Q: These mixins look cursed.*
  - A: Mixins are like dark and forbidden magic. Nothing to be concerned about

*I hereby acknowledge that given certain features, this mod goes against Immersive Engineering's design philosophy and is not officially endorsed by its development team.
Do not ask the IE team for help with features of this mod, use the Compression discord instead (linked above).*