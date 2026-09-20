# Wayfinder 3.5 backport checklist

Target: Minecraft 1.21.1 / NeoForge 21.1.249+  
Reference: Wayfinder 3.5 Fabric 1.21.10

## Runtime behavior

- [x] Signal Array registration, persistence, ownership and privacy
- [x] world signal synchronization
- [x] Signal Tweaker component and editing workflow
- [x] Signal Tweaker inventory selection through Alt + mouse wheel
- [x] Signalscope frequency control
- [x] Signalscope gain control
- [x] lens insertion/removal
- [x] upgrade insertion/removal
- [x] standard lens behavior
- [x] Privacy Lens filtering
- [x] Vantage Lens filtering
- [x] Signal Booster range scaling
- [x] Signal Expander range scaling
- [x] directional signal audio
- [x] updated 3.5 HUD signal tracking and labels

## Client behavior

- [x] context-sensitive GUI/GROUND/FIXED vs hand Signalscope models
- [x] installed lens/upgrade model variants
- [x] GUI model injection bound to the exact 1.21.1 ItemRenderer method
- [x] inventory scroll uses ScreenEvent.MouseScrolled.Pre
- [x] held Alt + scroll frequency/gain input remains outside screens
- [x] high-gain positional error is stable across rendered frames
- [x] tooltip background/frame integration
- [x] Signal Array cutout render layer

## Data and assets

- [x] official 3.5 `assets/wayfinder/**` pulled from pinned upstream JAR
- [x] official 3.5 `data/wayfinder/**` pulled from pinned upstream JAR
- [x] NeoForge 1.21.1 recipes supplied where upstream 1.21.10 recipe format is not directly reusable
- [x] upstream JAR SHA-256 pinned in Gradle

## Removed legacy compatibility targets

These belonged to the old 2.3 compatibility baseline and are not part of the 3.5 target:

- Wayfinder's Compass
- Signal Beacon
- Lifeform Analyzer
- Concave/Twisted lens workflow

## Validation boundary

CI validates compilation, dependency policy, asset identity, client startup/mixin loading and dedicated-server startup. Exact visual appearance, mouse feel and inventory interaction still require an in-game client pass on NeoForge 1.21.1.
