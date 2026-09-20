# Wayfinder NeoForge 1.21.1 compatibility port

Private, unofficial compatibility port of **Wayfinder 2.3** by **Nitron / n1tr0n__** from Fabric 1.21.1 to NeoForge 1.21.1.

Upstream: https://github.com/n1tr0nR/Wayfinder
Published upstream build: https://modrinth.com/mod/wayfinder_rbn/version/2.3-fabric.1.21.1

## Status

Strict functional-parity candidate for Wayfinder 2.3 on NeoForge 1.21.1.

Ported without third-party runtime libraries:

- `wayfinder:signal_array` and redstone-powered private networks
- `wayfinder:signal_beacon`
- `wayfinder:signalscope`
- Concave, Privacy, Vantage and Twisted lenses
- Wayfinder's Compass
- Lifeform Analyzer item shell, matching upstream 2.3 behavior
- persistent signal positions using vanilla `SavedData`
- upstream-equivalent server-side player signal factor/volume tracking without Cardinal Components
- native NeoForge server -> client synchronization
- Signal Array editing and ownership
- upstream-style Signalscope HUD, range, identification and distance display
- smooth zoom and reduced mouse sensitivity
- looping directional signal audio
- original advancements
- original recipes, structure/worldgen data and assets
- upstream-compatible Signalscope / compass component ids and codecs

The original Fabric libraries have deliberately **not** been ported as dependencies.

See [PORTING_NOTES.md](PORTING_NOTES.md) for the implementation mapping and remaining cosmetic differences.

## Runtime dependencies

Exactly:

- Minecraft 1.21.1
- NeoForge 21.1.249+

No Fabric API, Sinytra Connector, Forgified Fabric API, Cardinal Components API, Nitrogen, MidnightLib, Architectury, Cloth Config or other library mod is required.

## Upstream assets

Wayfinder is **All Rights Reserved**. This repository is intended for private compatibility work. It does not commit the upstream binary textures/sounds/models. During the build, Gradle downloads the author's official Wayfinder `2.3-fabric.1.21.1` JAR from Modrinth, verifies its pinned SHA-256, and extracts the original `assets/wayfinder` and `data/wayfinder` content plus the relevant vanilla block tags.

Do not publish or redistribute this port without permission from the upstream author.

## Building

Java 21 is required.

```bash
gradle build
```

The resulting JAR is written to `build/libs/`.

## Implementation notes

The Fabric/Cardinal world component is replaced by per-dimension vanilla `SavedData`. The Fabric/Cardinal player component is reproduced with server-side factor/volume tracking, persistent player NBT and a native NeoForge S2C payload, including upstream `NEVER_COPY` respawn behavior. This preserves the gameplay state machine without adding Cardinal Components.

Client rendering uses NeoForge events only where the observable result is equivalent to upstream. Two client mixins are retained for strict 1:1 parity where a loader-native replacement is not equivalent:

- Humanoid arm pose while using the Signalscope
- context-sensitive item model substitution for Signalscope, Compass and Lifeform Analyzer

The port also preserves upstream string `ownerUUID` semantics, including the special `"beacon"` owner marker.

Mixin support is supplied by NeoForge itself and adds no external mod dependency.
