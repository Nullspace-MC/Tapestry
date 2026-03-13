# Tapestry

A Fabric-Carpet-like mod for old Minecraft versions

## Features

### Commands
| Command Name | Description                                    |
| ------------ | ---------------------------------------------- |
| `clone`      | Backport of `clone` from 1.8                   |
| `counter`    | Controls hopper counters                       |
| `fill`       | Backport of `fill` from 1.8                    |
| `info`       | Provides information about blocks and entities |
| `log`        | Subscribes the player to loggers               |
| `tap`        | Used for configuring rules                     |
| `tick`       | Controls tick rate/tick warp                   |

### Rules

`alwaysEatCake`
* Allows player to always eat cake
* Type: `boolean`
* Default: `false`

`arrowsPersist`
* Disables arrows despawning after 1 minute in the ground
* Type: `boolean`
* Default: `false`

`betterCompletions`
* Adds improved tab completions to vanilla commands
* Type: `booelan`
* Default: `false`

`bottleFillCauldron`
* Enables filling cauldrons with water bottles
* Type: `boolean`
* Default: `false`

`chunkPattern`
* Generates superflat worlds with a chunk-aligned checkerboard pattern
* For superflat presets with 2 layers, the layers will be exchanged for the alternate-colored chunks
* Type: `boolean`
* Default: `false`

`commandClone`
* Enables the `clone` command
* Recommended to also enable `fillOrientationFix`
* Type: `boolean`
* Default: `true`

`commandCounter`
* Enables the `counter` command
* Type: `boolean`
* Default: `true`

`commandFill`
* Enables the `fill` command
* Recommended to also enable `fillOrientationFix`
* Type: `boolean`
* Default: `true`

`commandInfo`
* Enables the `info` command
* Type: `boolean`
* Default: `true`

`commandLog`
* Enables the `log` command
* Type: `boolean`
* Default: `true`

`commandTick`
* Enables the `tick` command
* Type: `boolean`
* Default: `true`

`creativeNoClip`
* Enables creative player noclip with a compatible client-side mod
* Type: `boolean`
* Default: `false`

`explosionBlockBreaking`
* Enables/Disables explosion block breaking
* Type: `boolean`
* Default: `true`

`fasterItemFrameMaps`
* Speeds up the time it takes to send map data packets
* Only affects item frame maps
* Type: `boolean`
* Default: `false`

`fillLimit`
* Volume limit of the fill/clone commands
* Type: `int`
* Default: `32768`

`fillOrientationFix`
* Fixes the orientation of chests, furnaces, etc. placed via command
* Specifically, this fixes [MC-31365](https://bugs.mojang.com/browse/MC-31365)
* Type: `boolean`
* Default: `false`

`fillUpdates`
* Determines whether fill/clone/setblock send block updates
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

`instantCommandBlock`
* Enables intant command blocks
* A command block will run instantly if it is on top of a redstone ore block
* Type: `boolean`
* Default: `false`

`instantFall`
* Causes gravity-affected blocks to fall instantly
* Type: `boolean`
* Default: `false`

`instantScheduling`
* Instantly executes scheduled ticks
* Type: `boolean`
* Defualt: `false`

`kaboVillageMarker`
* Enables interoperability with [KaboPC's Village Marker Mod](https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/1288327-village-marker-mod)
* Players must relog for changes to take effect
* Type: `boolean`
* Default: `false`

`liquidDamageDisabled`
* Disables fluid flowing breaking blocks
* Type: `boolean`
* Default: `false`

`loggerRefreshRate`
* Sets the rate at which loggers refresh
* Type: `int`
* Default: `20`

`placeFilledCauldron`
* Enables placing already filled cauldrons
* Placing a cauldron named with a number will cause it to get that number as fill level
* Type: `boolean`
* Default: `false`

`randomRedstoneDust`
* Makes redstone dust update order random
* Type: `boolean`
* Default: `false`

`randomTickSpeed`
* Sets the random tick speed
* Type: `int`
* Default: `false`

`repeaterHalfDelay`
* Allows repeaters to have half of their usual delay
* A repeater's delay is halved if it is on top of a redstone ore block
* Type: `boolean`
* Default: `false`

`repeatingCommandBlock`
* Enables repeating command blocks
* A command block will run every tick if it is on top of a diamond ore block
* Type: `boolean`
* Default: `false`

`saveUnloadChunks`
* Enables chunk unloading when the world is saved
* Behaves like 1.7.5+
* Type: `boolean`
* Default: `false`

`tcpNoDelay`
* Forces connections to send small packets immediately instead of buffering them
* Reduces latency in some cases
* Type: `boolean`
* Default: `false`

## License

This mod is available under the MIT License
