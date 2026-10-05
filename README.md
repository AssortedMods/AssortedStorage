# Assorted Storage

Assorted blocks and items useful for storage. Each group is also its own mod if you only want some of them.

- [Assorted Storage](mods/storage) has all of them in one download
- [Assorted Chests](mods/chests) adds material chests
- [Assorted Barrels](mods/barrels) adds material barrels
- [Assorted Shulkers](mods/shulkers) adds material shulker boxes
- [Assorted Hoppers](mods/hoppers) adds material hoppers
- [Assorted Crates](mods/crates) adds crates, crate controllers, crate upgrades and the rotator majig
- [Assorted Bags](mods/bags) adds bags and the ender bag
- [Assorted Containers](mods/containers) adds cabinets, safes, lockers, item towers and warehouse crates
- [Assorted Locks](mods/locks) adds padlocks and keys, an add-on that locks any of the others and doors
- [Assorted Level Upgrades](mods/levelupgrades) adds level upgrades, an add-on that upgrades the material containers in place

Worlds made with Assorted Storage 11.x work with any of these.

Requires [Assorted Lib](https://github.com/AssortedMods/AssortedLib). Branches are per Minecraft version and `26.2` is the current one.

## Issue Reporting

Please include the following

* Minecraft version
* NeoForge version, or Fabric Loader and Fabric API versions
* Which of these mods you have and their versions
* Assorted Lib version
* The full `latest.log`, and the crash report if the game crashed

## Building

You need JDK 25. Each mod is its own folder under `mods`. The build setup comes from
[AssortedBuild](https://github.com/AssortedMods/AssortedBuild) and `assortedbuild_version` in `gradle.properties`
picks the version.

To build against a local copy of Assorted Lib, publish it first.

```bash
cd ../AssortedLib && ./gradlew publishToMavenLocal
```

Some useful commands

```bash
./gradlew build                                  # build every mod
./gradlew :chests:neoforge:runClient             # run one mod
./gradlew :all:neoforge:runClient                # run every mod together
./gradlew runGameTestServer runGameTest          # gametests on NeoForge and Fabric
./gradlew runClientData runServerData            # datagen
```

Generated resources are committed. The NeoForge datagen writes them for both loaders.

## License

[GPL-3.0-only](LICENSE).
