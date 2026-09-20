# Wayfinder 2.3 -> NeoForge 1.21.1 porting notes

This port targets Minecraft 1.21.1 / NeoForge 21.1.249+ and intentionally has no third-party runtime dependencies.

## Loader/API replacements

| Upstream Fabric implementation | NeoForge port |
| --- | --- |
| Fabric Loader entrypoints | `@Mod` constructors |
| Fabric registries | NeoForge `DeferredRegister` |
| Fabric networking | NeoForge custom payload API |
| Cardinal Components world component | vanilla `SavedData` per dimension |
| Cardinal Components player component | native server-side transient/persistent player signal state + NeoForge S2C payload |
| Fabric HUD callback | NeoForge `RenderGuiEvent.Post` |
| Fabric client tick callback | NeoForge `ClientTickEvent.Post` |
| Fabric compass angle predicate | vanilla item property registered from NeoForge client setup |
| `GameRendererMixin` | `ViewportEvent.ComputeFov` (numerically identical) |
| `WorldRendererMixin` sound driver | vanilla `AbstractTickableSoundInstance` managed from client tick, fed by server-synced factor state |
| direct mouse sensitivity mutation | same cached sensitivity mutation in client tick, matching upstream lifecycle |
| Nitrogen tooltip styling | `RenderTooltipEvent.Color` |
| Nitrogen `ColorableItem` | native item names + NeoForge tooltip color event |
| Fabric datagen output | official upstream 2.3 JAR assets/data copied at build time with pinned SHA-256 |

## Preserved compatibility

- Mod id remains `wayfinder`.
- Main registry ids remain the upstream ids.
- Signalscope and compass data-component ids/codecs mirror upstream 2.3.
- Signal Array block-entity NBT reads the upstream `owner_uuid` string and also writes it for interoperability.
- Signal names, RGB values, signal type semantics and string `ownerUUID` semantics (including literal `"beacon"`) are preserved.
- Redstone-powered Signal Arrays remain private to their owner.
- Public Signal Arrays remain publicly editable.
- Signal Beacon behavior and type 4 semantics are preserved.
- Original recipes, advancements, structure/worldgen data, models, textures and sounds are taken from the official upstream JAR.

## Runtime dependency set

Only:

- Minecraft 1.21.1
- NeoForge 21.1.249+

No Fabric API, Sinytra Connector, Forgified Fabric API, Cardinal Components API, Nitrogen, MidnightLib, Architectury or Cloth Config is required. The port uses NeoForge's bundled Mixin runtime for the two client mixins needed for exact rendering parity.

## Validation

CI performs:

1. zero-external-mod-dependency guard,
2. Java 21 NeoForge build,
3. pinned upstream asset SHA-256 verification,
4. dedicated-server startup smoke test,
5. JAR artifact upload.

## Parity strategy

The port keeps upstream behavior rather than avoiding mixins categorically.

Client mixins retained:
- `HumanoidModelMixin` for the original Signalscope two-arm pose.
- `ItemRendererMixin` for context-sensitive GUI/GROUND/hand models exactly like upstream.

Loader-native replacements are used only where their externally observable behavior is equivalent:
- FOV/zoom through `ViewportEvent.ComputeFov`.
- Mouse sensitivity caching/mutation through the same tick lifecycle as upstream `ZoomHandler`.
- Tooltip colors and custom tooltip component registration through NeoForge client events.
- World state through vanilla `SavedData`, and player factor/volume state through native server tracking + NeoForge payloads.

No third-party runtime library is required.
