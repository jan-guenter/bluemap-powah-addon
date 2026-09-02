# BlueMap Powah Add-on

[![CI](https://github.com/jan-guenter/bluemap-powah-addon/actions/workflows/ci.yml/badge.svg?branch=main)](https://github.com/jan-guenter/bluemap-powah-addon/actions/workflows/ci.yml)

A small exact-profile BlueMap 5.23 feature-backport add-on for the stable appearance missing
from Powah 6.2.10 in All the Mons 1.2.0.

## Status and compatibility

Version `0.1.0-alpha.3` is the render-core migration candidate. It preserves
the owner-accepted alpha.2 renderer while replacing Powah's private
`FaceLighting` copy with the exact released shared source. Compatibility
outside these inputs is not asserted. Candidate artifact identities remain
unsealed until the final local gate passes.

## Visual scope

The add-on targets only:

- All the Mons `1.2.0`, Minecraft `1.21.1`, NeoForge `21.1.248`, Java 21;
- BlueMap feature backport
  `5.22-feature.backport-5.23-stateless-java-web-server-46` at commit
  `7e07f4e74ec1e92a6ead9aa1e66054af3e133aac`, API commit
  `285c9a60eff3ac2b0cab308ce1058d1565be0971`;
- Powah `6.2.10`, exact 2,737,991-byte JAR with SHA-256
  `0e604a7356111c1dd44a00ea42fc1aa960d9faeb978261349df1138fcee4d0b4`.

It adds formed and unformed reactor geometry, persistent cable machine
endpoints, and the Energizing Orb charge cube while leaving stock JSON models
intact. The orb material approximates Powah's additive glow with a translucent
BlueMap texture. Fast-changing energy, transfer, and machine activity are
intentionally outside the static map view.

Missing Powah, a different artifact, or unsupported data leaves stock BlueMap
rendering unchanged. The add-on writes nothing to the world.

The pinned `modules/bluemap-addon-render-core` gitlink contributes only the
MIT-licensed BlueMap 5.23 `FaceLighting` source. The consumer compiles that
source into this add-on. It neither installs nor nests the standalone module
JAR. The shared source is package- and visibility-normalized equivalent to the
removed local helper; emitters, gallery data, profiles, and fallback policy
remain local and unchanged.

## Build and verification

Clone with submodules so the exact reviewed build convention is available:

```bash
git clone --recurse-submodules \
  https://github.com/jan-guenter/bluemap-powah-addon.git
```

For an existing checkout, initialize all exact support modules:

```bash
git submodule update --init --recursive -- \
  tooling/bluemap-addon-toolkit modules/bluemap-addon-render-core \
  modules/bluemap-addon-adapter-api
```

The build rejects an uninitialized, dirty, incorrectly pinned, or
source-tree-mismatched support module. Archive gates require exactly one
shared `FaceLighting` class and source, reject the removed local helper and
unexpected render-core classes, and reject nested JARs.

```bash
gradle --no-daemon \
  -PbluemapSourcePath=/path/to/exact/bluemap-backport \
  -PpowahJar=/path/to/Powah-6.2.10.jar \
  clean prototypeCheck build \
  generatePomFileForAddonPublication \
  generateMetadataFileForAddonPublication
```

`check` enforces production and sources archive boundaries. Owner acceptance
seals the exact release bytes before tagging. Tagged releases publish production/source JARs, POM, Gradle module
metadata, and checksums on GitHub Releases and Maven coordinates
`io.github.jan-guenter:bluemap-powah-addon:<version>` on GitHub Packages.

## Installation

Place `build/libs/bluemap-powah-addon-0.1.0-alpha.3.jar` in
`config/bluemap/packs`, keep the exact Powah JAR available to BlueMap's
resource scan, restart, and rerender the affected area. Do not place this
add-on in `mods`.

The included gallery datapack is only a disposable visual-review fixture.

## License and provenance

This project is released under [LGPL-3.0-only](LICENSE). No Powah resources or
binaries are bundled. See [NOTICE.md](NOTICE.md),
[THIRD_PARTY.md](THIRD_PARTY.md), and
[provenance/upstreams.json](provenance/upstreams.json).
