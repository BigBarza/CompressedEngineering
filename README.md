## Compressed Engineering

This mod contains tweaks to Immersive Engineering for the Compression modpack.

### Features
- Allows the coke oven to have custom fluid outputs instead of just the hardcoded creosote oil. It adds no recipes on its own.  
- Prevents a crash when more than 13 items are assigned to an engineering blueprint. Specifically, it would crash when removing said bp.
- Adds two new recipe types (compressedengineering:assembler_shaped and compressedengineering:assembler_shapeless)
  - They're identical to crafting recipes but can only be done in the IE Assembler
- New JEI integration for excavator veins, complete with custom graphs and more info visible to the player 
- Allows electrodes to be input and output from the arc furnace. I/O point is the electrode holder on top.
- Fixes an issue where the secondary output chance for the arc furnace is inverted.

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

- *Q: What happens to the default recipes?*
  - A: Nothing. If there's no custom fluid but a creosote value, the coke oven will output creosote oil as usual.

- *Q: What happens if there's a different fluid in the output?*
  - A: The recipe simply won't start.

- *Q: Is there a new cap for the blueprint crash fix?*
  - A: Yes, 33 recipes. Other things usually break before it gets there.

- *Q: These mixins look cursed.*
  - A: Mixins are like dark and forbidden magic. Nothing to be concerned about
