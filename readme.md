<div align="center">

# Lazuli

</div>

Lazuli is a personal library mod made to make modding a little less tedious.

This library is the culmination of all the foundational and API code that I
wrote for my mods. I started to dislike having to rewrite it all every time, so
I decided that it's time to make Yet Another Library Mod.

You're welcome modpackers, now you'll have one extra mod to pad your mod counts
with to make your modpacks look larger than they are :3

## "Features"

Lazuli ultimately boils down to tons of helper functions, and there are *quite a lot* of
them at that. Here are the "highlights" or "interesting" things (I guess).

- Blocks
  - Generic "block with entity" helpers (`AbstractBlockWithEntity`)
  - Energy-capable BE helpers (`IEnergyBE`, `AbstractEnergyBE`, `AbstractCapacitorBE`, `AbstractGeneratorBE`)
  - Inventory-capable BE helpers (`IInventoryBE`, `AbstractInventoryBE`)
  - Fluid-capable BE helpers (`IFluidBE`, `AbstractSingleTankBE`)
  - "Zoned" block entities, ripped from Minefactorial (`IZonedBE`)
- Client Things
  - Particle helpers
  - A 2D particle engine for GUIs or wherever else you want to use them (`IParticleEngine2D`, `ScreenParticleEngine2D`)
  - Screen Helpers
- Crafting
  - Ingredients/outputs for in-world blocks, entities, item entities, and fluids
- Collections (a more flexible `WeightedRandomList` implementation, `HistoryList`)
- Colours (`UnpackedColour`, `ColourPalette`)
- Energy (`LazuliEnergyStorage`, `ProtectiveEnergyStorage`)
- Fluids
- Inventories (`ContainerUtils`, `RoundRobinInventory`, `GhostStackHandler`)
- Items (`GhostItemStack`)
- Mathematics (`CubicBezier`, `QuadraticBezier`)
- Menus (`AbstractBlockEntityContainer`, `GhostSlot`, `SlotOutputOnly`)
- Misc helpers (`TickScheduler`)
- World helpers (`AABBHelpers`, `FakePlayerHelpers`, `Raycasting`)

## Mods Using Lazuli

<!-- Todo: s/cygnus' foundation/lazuli/ - [Cygnus](https://github.com/nebula-modding/cygnus) -->
<!-- Todo: s/minefactorial's foundation/lazuli/ - [Minefactorial](https://github.com/emmathemartian/minefactorial) -->
<!-- I'll add Ala Sona to this when ("if" may be a better term) I publish it on Git - Ala Sona -->

## Usage

Lazuli is extensively documented using Javadocs. I also publish source and
Javadoc JARs for you to include via Gradle.

Feel free to JiJ Lazuli if you'd like, just make sure you remember indicate
Lazuli's [license](license.txt) (MIT) in your license. Just add a section under
your license saying something like "Lazuli License" and copy+paste
[license.txt](license.txt) into it (**this is not legal advice**).

To add Lazuli to your project, just add it as a `modImplementation` in Gradle:

```kts
// Applies to all Gradle plugins
repositories {
	// Modrinth maven is the easiest and most reliable place for me to publish at the moment.
	// I publish sources and JavaDocs here too.
	exclusiveContent {
		forRepository {
			maven("https://api.modrinth.com/maven")
		}
		filter {
			includeGroup("maven.modrinth")
		}
	}
}

dependencies {
	// Loom and Architectury Loom
	modImplementation("maven.modrinth:lazuli:VERSION")
	// NeoGradle
	implementation(fg.deobf("maven.modrinth:lazuli:VERSION"))
	// ModDevGradle
	implementation("maven.modrinth:lazuli:VERSION") // I think?
	// TODO: Add Unimined and whatever other Gradle plugins are out there in the wild.
}
```

## Contributors

Notes to contributors:

- This is a personal API first-and-foremost. It's going to be opinionated, and
  it's going to be catered by my likings.
- Use a `package-info.java` in every package and add
  `@ParametersAreNonnullByDefault` and `@MethodsReturnNonnullByDefault` to them.
  If the package is client-only, use `@OnlyIn(Dist.CLIENT)` too.
- Before I publish, I will check for warnings from the Java compiler or from
  Javadoc. There should be *zero* warnings for a published JAR if at all
  possible.
- Please follow my code style. You can import [Project.xml](Project.xml) into
  your IDE to use it automatically. Otherwise, the main points are:
  - Allman indentation (braces on new lines)
  - Spaces in between for-loop semicolons (`for (int i = 0 ; i < 10 ; i++)`)
  - Tabs > spaces. I don't care what your tab width is, so don't try to align
    based on that.
- Keep a [changelog](changelog.md) of everything you add/remove/change.
- Use `@Deprecate`, don't remove things randomly. Breaking changes are beyond
  infuriating in library mods.
- **If you use another person's code, CREDIT THEM AND MAKE SURE YOU'RE ALLOWED
  TO USE THEIR CODE.** If you steal, I will ban you from the repository.
  [Don't be a jerk](https://github.com/CoFH/ThermalExpansion?tab=readme-ov-file#cofh-dont-be-a-jerk-license-v2).
- Format your code! You can either use `Shift+Shift` and find `Format Code`, or
  if you have a keybind for it then you can use that. Mine is `Alt+Shift+F`.
  Just run this on your uncommitted files before pushing. You should also run
  an `Optimize Imports` on your uncommitted files before pushing.
- That's about it! TL;DR, follow my general style in the codebase and be kind!

## Todo

- [ ] Add tests for as much of the API as I can
- [ ] Move quaternion transforms from Cygnus to Lazuli?
- [ ] Helpers for common mod integrations?
  - Perhaps a recipe abstraction which also handles EMI/JEI/REI integration?
  - If I ever get into developing addon mods for Create/Botania/Ars Nouveau/other major mods, integration helpers could be useful.
  - Helpers/wrappers for KubeJS+CraftTweaker
- [ ] Fabric port???
  - This will be difficult since I have a plethora of NeoForge-specific helpers (fluid, energy, inventories, etc.).
    I'd basically be recreating Arch API.
- [ ] Multiversion
  - I primarily mod on 1.21.1, however I plan to update to the next "LTS" modding version when available.
    I have a feeling that the LTS version will be either 1.21.6 or 1.22. Both seem pretty likely.
