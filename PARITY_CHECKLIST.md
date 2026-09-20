# Wayfinder 2.3 parity checklist

Target: Minecraft 1.21.1 / NeoForge 21.1.249+

## Runtime behavior

- [x] Signal Array registration, editing, owner UUID, colors, type and redstone privacy
- [x] Signal Beacon registration, type 4 behavior, random-tick recovery and `ownerUUID = "beacon"`
- [x] world signal persistence and synchronization
- [x] player factor0..factor4 tracking
- [x] Signalscope volume fade-in/fade-out and persistence
- [x] CCA `RespawnCopyStrategy.NEVER_COPY` behavior reproduced on NeoForge
- [x] Concave Lens range: 3000
- [x] Privacy Lens filtering
- [x] Vantage Lens filtering
- [x] Twisted Lens filtering and range: 500
- [x] base Signalscope range: 1000
- [x] look-factor math and 0.95 -> 1.0 snap
- [x] 15.65 degree signal identification
- [x] all five looping signal sound channels
- [x] `Let The Choir Sing` timing and criteria
- [x] `A+ Hearing`, `Personal VPN`, and compass advancements
- [x] Signalscope used statistic
- [x] Wayfinder Compass open/close lifecycle and angle predicate
- [x] Lifeform Analyzer upstream 2.3 item-shell behavior

## Client parity

- [x] upstream Signalscope HUD textures, positioning, colors, fade ranges and signal text
- [x] vignette and zoom interpolation
- [x] upstream mouse-sensitivity caching and 0.4 multiplier
- [x] Signal Array screen geometry, fields, RGB parsing and transparent type button
- [x] custom colored Signal Array text fields
- [x] Signalscope custom tooltip lens slot
- [x] Nitrogen-equivalent tooltip background/border colors
- [x] context-sensitive GUI/GROUND/hand models
- [x] folded/open compass rendering
- [x] Signalscope arm pose
- [x] Signal Array cutout render layer

## Data and assets

- [x] original `assets/wayfinder/**`
- [x] original `data/wayfinder/**`
- [x] original recipes
- [x] original advancements
- [x] original Signal Tower worldgen and NBT
- [x] original Signal Array loot table
- [x] `minecraft:mineable/pickaxe` tag
- [x] `minecraft:needs_iron_tool` tag
- [x] upstream JAR SHA-256 pinned in Gradle

## API replacements

Fabric/CCA/Nitrogen infrastructure is replaced with NeoForge-native APIs where that does not change observable behavior. Two client mixins remain because their loader-native substitutes are not behaviorally identical:

- `HumanoidModelMixin`
- `ItemRendererMixin`

No third-party mod/runtime library is required.

## Validation boundary

CI validates compilation, dependency policy, upstream asset identity and dedicated-server startup. The remaining validation that cannot be done headlessly is an in-game client visual/interaction pass on NeoForge 1.21.1.
