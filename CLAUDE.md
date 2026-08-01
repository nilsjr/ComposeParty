# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this project is

ComposeParty is a single-module Android playground for experimenting with Jetpack Compose — animations, custom layouts, shaders, canvas drawing, physics, text effects. It is a scratchpad app, not a product: samples are self-contained, some are half-finished, and a few are commented out in `MainActivity` with `// TODO: error` markers.

## Commands

```bash
./gradlew :app:assembleDebug         # debug build
./gradlew :app:assembleRelease       # what CI runs (R8 full mode, debug signing config)
./gradlew installDebug               # build + install on connected device/emulator
./gradlew dependencyUpdates          # ben-manes plugin: report outdated deps
./gradlew clean
```

There are no unit or instrumentation tests — `app/src` contains only `main`. Verification means building and running the app on a device.

Requires JDK 21 (CI uses Zulu 21). Toolchains are resolved via the foojay resolver.

## Architecture

Everything lives in `app/src/main/java/de/nilsdruyen/composeparty/`, one package per topic (`animations`, `layouts`, `text`, `paths`, `freestyle`, `cards`, `buttons`, `material`, `camera`, `math`, `isles`, `swipe`, `modifiers`, `nyancat`, `design`, `effects`).

`MainActivity` is the whole navigation system: a `Map<String, @Composable () -> Unit>` named `demoItems` maps display label → sample composable. A `Crossfade` over a `String` state swaps between `SampleList` and the selected sample; `BackHandler` resets to the list. There is no navigation library, no ViewModels, no DI, no repository layer.

**To add a sample:** write a `@Composable` (typically zero-arg, often annotated `@Preview`) in the appropriate topic package, then add one entry to the `demoItems` map in `MainActivity`. That's the only wiring.

Shared helpers live in `utils/`: `Centered`/`Center` (full-size centering wrappers used by most samples), `ItemList` (the sample list UI), `AnimUtils`, `MathUtils`, `SensorEffect`/`SensorManager`. Theme is `ui/theme/` (`ComposePartyTheme`). `data/Images.kt` holds sample image URLs.

`ComposePartyApp` is the `Application`: plants Timber in debug and provides the Coil `ImageLoader`.

## Conventions and constraints

- Compose BOM is the **alpha** BOM (`compose-bom-alpha`), Material3 is on an alpha version, and AGP is an alpha (`9.x-alpha`). APIs used here may not exist in stable releases; expect churn on dependency bumps (Renovate opens these PRs).
- Kotlin compiles with `progressiveMode`, `-Xcontext-parameters`, and `languageVersion = KOTLIN_2_0`, JVM target 21.
- `minSdk 26`, `compileSdk`/`targetSdk` 37. Samples using newer APIs (RuntimeShader → API 33, etc.) must guard with `Build.VERSION.SDK_INT` checks plus `@RequiresApi` and render a fallback message, as `ShaderSample` does.
- Edge-to-edge is enabled app-wide in `MainActivity`; samples that need it handle insets themselves (`utils.Center` applies `systemBarsPadding`).
- Camera permission is requested unconditionally at startup for the QR sample.
- Gradle configuration cache is on (problems set to `warn`); avoid build-script patterns that break it.
- `kscript/*.kts` are standalone experiment scripts, unrelated to the Gradle build. `compare.scenarios` is a gradle-profiler config.
