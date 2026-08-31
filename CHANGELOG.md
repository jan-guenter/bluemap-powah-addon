# Changelog

## 0.1.0-alpha.2 - 2026-08-31

- Target only BlueMap feature-backport commit
  `7e07f4e74ec1e92a6ead9aa1e66054af3e133aac` and API commit
  `285c9a60eff3ac2b0cab308ce1058d1565be0971`.
- Move the local adapter boundary from `bluemap522` to `bluemap523`.
- Compile the four pinned Adapter API sources and remove duplicate local helpers.
- Preserve reactor, energy-cable endpoint, and Energizing Orb behavior.

## 0.1.0-alpha.1 - 2026-08-18

- Render formed and unformed Powah reactors from their persisted assembly
  state while keeping the stable tier appearance.
- Render persistent machine endpoints on energy cables.
- Render the Energizing Orb charge cube with a translucent approximation of
  Powah's additive glow.
- Leave fast-changing energy, transfer, and machine activity out of BlueMap's
  static view.
- Pass disposable full-pack staging and owner visual acceptance.
