# Changelog

**English** | [简体中文](CHANGELOG.zh-CN.md)

All notable changes to this project are documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/).

## [1.0.2] - 2026-11-20

### Added

- **Finplayer TV branding**: the display name is now `Finplayer` / `飞影视界` / `飛影視界`, following the system language. `applicationId` changed to `com.finplayer.androidtv`.
- **New brand identity**: Mint `#00E0A4` + Ink `#0C1417` palette with a fin-shaped play mark. Launcher icon, landscape banner, in-app logo, splash, settings menu icons and monochrome icons are all replaced, with Simplified Chinese / Traditional Chinese / English vector wordmarks.
- **"Underwater reflection" logo style**: launcher icon, settings menu entries, login page title and splash now share the same treatment — a solid upper half, a fading reflection below, and a waterline highlight. VectorDrawable does not support gradient fills, so the fade is simulated with 20 horizontal slices of decreasing alpha.
- **Build-time injection of the default server address**: the address is no longer hard-coded. It is injected into `BuildConfig` at build time from `local.properties`, an environment variable, or a Gradle property, and a `local.properties.example` template without the real address is provided. When unset the value is an empty string and the app falls back to the native "Add server" flow, so a clean clone still compiles.
- **First launch goes straight to login**: when signed out, the default server is registered automatically and the username/password login page opens directly, skipping QuickConnect. Registration no longer depends on network discovery scoring; network access is only required for the actual authentication.
- **Default playback preferences**: ASS / PGS subtitles direct-play by default; AC3 / EAC3 / DTS / TrueHD bitstream passthrough is enabled by default.

### Fixed

- Invisible wordmark on dark backgrounds: the app uses a fixed dark theme, so wordmarks are now always white instead of following the system light/dark setting.
- English leftovers on Chinese-locale devices: the welcome text, login subtitle, the "Manage servers" settings entry, and the About page version title and caption are all localized. Server records already saved with the old English name are masked at the display layer, so clearing app data is not required.
- Wordmark still in English on Chinese-locale devices: added `drawable-zh-rCN` / `drawable-zh-rTW` Chinese vector wordmarks; the landscape logo, banner and splash switch automatically by locale.
- Playback icons in Settings clipped by the adaptive-icon circular mask: added a plain vector `app_icon_list` instead of `R.mipmap.app_icon` and dropped the extra clip. The external player entry still shows the real app icon.
- Banner / landscape logo wordmark not vertically centered: `translateY` is recalculated from the wordmark bounding-box center, and the density-qualified Chinese and English assets are now complete.
- Full release build failed with `Expecting a top level declaration`: removed a stray character at the end of `ServerConstants.kt`.

### Changed

- The version is managed by `jellyfin.version` in `gradle.properties` (`1.0.2`).
- Brand strings replaced across 50+ language `strings.xml` files; all remaining Jellyfin wording cleaned up.
- About and Settings icons now reference the project's own `ic_finplayer`; the caption changed to `finplayer-androidtv`.
- `.gitignore` gained rules for local work data and build outputs, so files containing private information are never committed to the public repository.
