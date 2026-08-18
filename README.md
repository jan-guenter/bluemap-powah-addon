# BlueMap Powah Add-on

[![CI](https://github.com/jan-guenter/bluemap-powah-addon/actions/workflows/ci.yml/badge.svg?branch=main)](https://github.com/jan-guenter/bluemap-powah-addon/actions/workflows/ci.yml)

A small exact-profile BlueMap 5.22 add-on for the stable appearance missing
from Powah 6.2.10 in All the Mons 1.2.0.

## Status and compatibility

Version `0.1.0-alpha.1` is the owner-accepted prerelease for this exact
environment. Its production JAR is 36,572 bytes with SHA-256
`0b370dfcd5d8c0a5844dd920d60a2cdb74ed441d87ff038382d8292c374686c8`.
Compatibility outside these inputs is not asserted.

## Visual scope

The add-on targets only:

- All the Mons `1.2.0`, Minecraft `1.21.1`, NeoForge `21.1.248`, Java 21;
- BlueMap backport `5.22-agent.backport-5.22-mc1.21.1-2` at commit
  `9be321df995a1103808621d529eb72773e719d4d`;
- Powah `6.2.10`, exact 2,737,991-byte JAR with SHA-256
  `0e604a7356111c1dd44a00ea42fc1aa960d9faeb978261349df1138fcee4d0b4`.

It adds formed and unformed reactor geometry, persistent cable machine
endpoints, and the Energizing Orb charge cube while leaving stock JSON models
intact. The orb material approximates Powah's additive glow with a translucent
BlueMap texture. Fast-changing energy, transfer, and machine activity are
intentionally outside the static map view.

Missing Powah, a different artifact, or unsupported data leaves stock BlueMap
rendering unchanged. The add-on writes nothing to the world.

## Build and verification

```bash
gradle --no-daemon clean check build \
  generatePomFileForAddonPublication \
  generateMetadataFileForAddonPublication
```

`check` rejects any production JAR that differs from the owner-accepted size
or SHA-256. Tagged releases publish production/source JARs, POM, Gradle module
metadata, and checksums on GitHub Releases and Maven coordinates
`io.github.jan-guenter:bluemap-powah-addon:<version>` on GitHub Packages.

## Installation

Place `build/libs/bluemap-powah-addon-0.1.0-alpha.1.jar` in
`config/bluemap/packs`, keep the exact Powah JAR available to BlueMap's
resource scan, restart, and rerender the affected area. Do not place this
add-on in `mods`.

The included gallery datapack is only a disposable visual-review fixture.

## License and provenance

This project is released under [LGPL-3.0-only](LICENSE). No Powah resources or
binaries are bundled. See [NOTICE.md](NOTICE.md),
[THIRD_PARTY.md](THIRD_PARTY.md), and
[provenance/upstreams.json](provenance/upstreams.json).
