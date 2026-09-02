# Agent guide for BlueMap Powah Add-on

This is an independent public BlueMap add-on repository in the BlueMap ATMons
portfolio. Read this file and `README.md` before changing it.

## Exact baseline

- All the Mons `1.2.0`
- Minecraft `1.21.1`
- NeoForge `21.1.248`
- Java `21`
- BlueMap feature backport
  `5.22-feature.backport-5.23-stateless-java-web-server-46`, commit
  `7e07f4e74ec1e92a6ead9aa1e66054af3e133aac`, API commit
  `285c9a60eff3ac2b0cab308ce1058d1565be0971`
- Adapter API `0.1.0-alpha.2`, commit
  `e81f08bc4bfbf02d810ec8949a019130e2e61634`, source tree
  `2f974c9bb2ba13888d69682f86f30f58922d30eb`
- Render Core `0.1.0-alpha.2`, commit
  `24b84efdc8235f3f1323e1a8e9fd033080e3a79e`, source tree
  `424040931680fb82d37693f893ca887c0ed48eae`
- Add-on ID `bluemap-powah`, candidate version `0.1.0-alpha.3`

This is a standalone BlueMap add-on, not a NeoForge mod. Do not add client
classes, candidate binaries or assets, nested JARs, Minecraft classes, Mixins,
or world state.

## Development contract

- Preserve stock rendering when the exact runtime profile is absent,
  unsupported, malformed, duplicated, or disabled.
- Keep BlueMap internals isolated below the versioned adapter package.
- Keep exact candidate identities and resource contracts in the profile.
- Keep state decoding, normalized data, and mesh emission separate.
- Use bounded diagnostics and stock fallback for unknown family data.
- Keep gallery cases, renderer behavior, licensing, and provenance owned by
  this repository.
- Follow the shared `addon-v1` source style maintained by `bluemap-atmons`.

## Commands

Initialize all pinned support modules and run the documented exact-input gate:

```bash
git submodule update --init --recursive -- \
  tooling/bluemap-addon-toolkit modules/bluemap-addon-render-core \
  modules/bluemap-addon-adapter-api
gradle --no-daemon -PpowahJar=/path/to/Powah-6.2.10.jar \
  clean prototypeCheck build generatePomFileForAddonPublication \
  generateMetadataFileForAddonPublication
```

Follow `docs/RELEASING.md` and the repository workflow for publication. Never
stage generated build output, candidate JARs, worlds, credentials, logs, or
local research evidence.
