# Tapestry

A Fabric-Carpet-like mod for old Minecraft versions

## Features

### Rules

`arrowsPersist`
* Disables arrows despawning after 1 minute in the ground
* Type: `boolean`
* Default: `false`

`chunkPattern`
* Generates superflat worlds with a chunk-aligned checkerboard pattern
* For superflat presets with 2 layers, the layers will be exchanged for the alternate-colored chunks
* Type: `boolean`
* Default: `false`

`creativeNoClip`
* Enables creative player noclip with a compatible client-side mod
* Type: `boolean`
* Default: `false`

`explosionBlockBreaking`
* Enables/Disables explosion block breaking
* Type: `boolean`
* Default: `true`

`fortressSpawningFix`
* Fix that allows nether brick spawning in all fortresses
* Type: `boolean`
* Default: `false`

`hopperCounter`
* Enables hopper counters
* Type: `boolean`
* Default: `false`

`kaboVillageMarker`
* Enables interoperability with [KaboPC's Village Marker Mod](https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/1288327-village-marker-mod)
* Players must relog for changes to take effect
* Type: `boolean`
* Default: `false`

`loggerRefreshRate`
* Sets the rate at which loggers refresh
* Type: `int`
* Default: `20`

`repeatingCommandBlock`
* Allows a command block with a diamond ore block underneath to execute it's command every tick
* Tyoe: `boolean`
* Default: `false`

### Commands
| Command Name | Description                      |
| ------------ | -------------------------------- |
| `log`        | Subscribes the player to loggers |
| `tap`        | Used for configuring rules       |

## License

This mod is available under the MIT License
